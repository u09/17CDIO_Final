package SceneBuild_JavaFX;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import QuickConnect.FunctionUser;
import QuickConnect.Threads;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class chatWindow implements EventHandler<ActionEvent> {

	private Stage myStage;
	private Scene myScene;
	private BorderPane chatFrame;
	static Thread th;
	@FXML private MenuBar menuBar;
	@FXML private MenuItem mAbout, mClose, mSettings, mSignOut, mFullScreen, mExitFullScreen;
	@FXML private ListView<String> friendsOnlineList, friendsOfflineList, groupsList;
	@FXML private TitledPane titledPane, onlinePane, offlinePane;
	@FXML private TextArea textArea;
	@FXML private TextField inMessage;
	@FXML private Button bEmojis, bSearchRecent, bSearchFriends, bSearchGroups, bAddFriend, bAddGroup;
	@FXML private HBox hBoxMessage;
	@FXML private ColorPicker colorPick;
	private FunctionUser fu;
	private int[] offlineFriends;
	private int[] onlineFriends;
	private int[] groups;
	private int activeUser;
	private ArrayList<ArrayList<String>> messages=new ArrayList<ArrayList<String>>();
	private ArrayList<Integer> users=new ArrayList<Integer>();
	@FXML private ContextMenu contextMenu;
	@FXML private MenuItem mDeleteOn, mDeleteOff, mBlockOn, mBlockOff, mInfoOn, mInfoOff, mLeave, mInfoGroup, mDeleteGroup, mThrow;
	private long loginTime;
	private boolean checkType;

	public void start(Stage stage, FunctionUser fu) throws SQLException, IOException {
		this.fu = fu;
		this.myStage = stage;
		this.myStage.setTitle("QuickConnect - user: " + fu.user().getUsername());
		this.loginTime=fu.f.timestamp();
		fu.activateUser();
		fu.setOnlineUser();

		showChatFrame();
		myScene = new Scene(chatFrame);
		

		this.myStage.setScene(myScene);
		this.myStage.show();

		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() throws Exception {
				while(true) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							try {
								getListsContents();
								if(checkType==false){
									ArrayList<ArrayList<String>> msg=fu.getGroupMessages(activeUser);
									for(int i=1;i<=msg.get(0).size();i++){
										textArea.appendText(msg.get(1).get(i-1)+":\n"+msg.get(0).get(i-1)+"\n\n");
									}
								}
								else{
									fu.getMessages(messages,users);
									for(int i=1;i<=messages.size();i++){
										for(int t=1;t<=messages.get(i-1).size();t++){
											if(users.get(i-1)==activeUser) textArea.appendText(fu.id2nick(users.get(i-1))+":\n"+messages.get(i-1).get(t-1)+"\n\n");
										}
									}
								}

								messages.clear();
								users.clear();
								fu.con().update("UPDATE users SET last_on='"+fu.f.timestamp()+"' WHERE user_ID='"+fu.user().getUserID()+"'");
							} catch(SQLException | IOException e) {
								e.printStackTrace();
							}
						}
					});
					Thread.sleep(5000);
				}
			}
		};
		th = new Thread(task);
		th.setDaemon(true);
		th.start();

		Runnable r = new Threads(fu);
		new Thread(r).start();
	}

	public void showChatFrame() throws SQLException, IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(chatWindow.class.getResource("ChatFrame.fxml"));
		loader.setController(this);
		try {
			chatFrame = (BorderPane) loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}

		titledPane.setText("Chat system");
		HBox.setHgrow(inMessage, Priority.ALWAYS);
		bSearchFriends.setId("bSearch");
		bSearchGroups.setId("bSearch");
		bEmojis.setId("bEmoji");
		bAddFriend.setId("bAddPlus");
		bAddGroup.setId("bAddPlus");
		addTextLimiter(inMessage, 900);
		inMessage.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode().equals(KeyCode.ENTER)) {
					String msg = inMessage.getText();
					// FunctionUser.sendMessage(msg, user.UserID,
					// titledPane.getText());
					try {
						textArea.appendText(fu.getNickName() + ":\n" + msg + "\n\n");
					} catch(SQLException e) {
						e.printStackTrace();
					}

					try {
						if(checkType==true) fu.sendMessage(msg,activeUser);
						else if(checkType==false) fu.sendGroupMessage(msg,activeUser);
					} catch (SQLException | IOException e) {
						e.printStackTrace();
					}
					inMessage.clear();
				}
			}
		});
	    
		setMenuBarFunctions();
		getListsContents();
		setListsFunctions();
	}
	
	public static void addTextLimiter(final TextField tf, final int maxLength) {
		tf.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
				if (tf.getText().length() > maxLength) {
					String s = tf.getText().substring(0, maxLength);
					tf.appendText(s);
				}
			}
		});
	}

	private void setMenuBarFunctions() {
		menuBar.setUseSystemMenuBar(true);
		mAbout.setOnAction(this);
		mClose.setOnAction(this);
		mClose.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
		mSettings.setOnAction(this);
		mSettings.setAccelerator(KeyCombination.keyCombination("Ctrl+I"));
		mSignOut.setOnAction(this);
		mSignOut.setAccelerator(KeyCombination.keyCombination("Ctrl+L"));
		mFullScreen.setOnAction(this);
		mFullScreen.setAccelerator(KeyCombination.keyCombination("Ctrl+F"));
		mExitFullScreen.setOnAction(this);
		mExitFullScreen.setAccelerator(KeyCombination.keyCombination("Esc"));
		colorPick.setOnAction(this);
		bAddFriend.setOnAction(this);
		bAddGroup.setOnAction(this);
		mDeleteOn.setOnAction(this);
		mDeleteOff.setOnAction(this);
		mBlockOn.setOnAction(this);
		mBlockOff.setOnAction(this);
		mInfoOn.setOnAction(this);
		mInfoOff.setOnAction(this);
		mLeave.setOnAction(this);
		mInfoGroup.setOnAction(this);
		mDeleteGroup.setOnAction(this);
		mThrow.setOnAction(this);
	}

	private void getListsContents() throws SQLException, IOException {
		this.onlineFriends=fu.onlineUsersId();
		String[] on=fu.onlineUsersNickname();
		ObservableList<String> onlineItems = FXCollections.observableArrayList(on);
		friendsOnlineList.setItems(onlineItems);
		onlinePane.setText("Online (" + onlineItems.size() + " venner)");
		
		this.offlineFriends=fu.offlineUsersId();
		String[] off = fu.offlineUsersNickname();
		ObservableList<String> offlineItems = FXCollections.observableArrayList(off);
		friendsOfflineList.setItems(offlineItems);
		offlinePane.setText("Offline (" + offlineItems.size() + " venner)");
		
		this.groups=fu.groupsId();
		String[] gro = fu.showGroupsName();
		ObservableList<String> groupsItems = FXCollections.observableArrayList(gro);
		groupsList.setItems(groupsItems);
	}

	private void setListsFunctions() {
		friendsOnlineList.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				checkType=true;
				int id = friendsOnlineList.getSelectionModel().getSelectedIndex();
				if(id==-1) return;
				String name = friendsOnlineList.getSelectionModel().getSelectedItem();
				activeUser=onlineFriends[id];
				System.out.println("clicked on "+activeUser);
				textArea.clear();
				ArrayList<ArrayList<String>> msgs=new ArrayList<ArrayList<String>>();
				try {
					msgs=fu.getMessages(activeUser,loginTime);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				for(int i=1;i<=msgs.get(0).size();i++) textArea.appendText(msgs.get(1).get(i-1)+":\n"+msgs.get(0).get(i-1)+"\n\n");
				textArea.setScrollTop(Double.MAX_VALUE);
				if(name != null && !name.isEmpty()) titledPane.setText(name);
			}
		});

		friendsOfflineList.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				checkType=true;
				int id = friendsOfflineList.getSelectionModel().getSelectedIndex();
				if(id==-1) return;
				String name = friendsOfflineList.getSelectionModel().getSelectedItem();
				activeUser=offlineFriends[id];
				System.out.println("clicked on "+activeUser);
				textArea.clear();
				ArrayList<ArrayList<String>> msgs=new ArrayList<ArrayList<String>>();
				try {
					msgs=fu.getMessages(activeUser,loginTime);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				for(int i=1;i<=msgs.get(0).size();i++) textArea.appendText(msgs.get(1).get(i-1)+":\n"+msgs.get(0).get(i-1)+"\n\n");
				textArea.setScrollTop(Double.MAX_VALUE);
				if(name != null && !name.isEmpty()) titledPane.setText(name);	
			}
		});

		groupsList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				checkType=false;
				int id = groupsList.getSelectionModel().getSelectedIndex();
				if(id==-1) return;
				String name = groupsList.getSelectionModel().getSelectedItem();
				activeUser=groups[id];
				System.out.println("clicked on "+activeUser);
				textArea.clear();
				ArrayList<ArrayList<String>> msgs=new ArrayList<ArrayList<String>>();
				try {
					msgs=fu.getGroupMessages(activeUser,loginTime);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				for(int i=1;i<=msgs.get(0).size();i++) textArea.appendText(msgs.get(1).get(i-1)+":\n"+msgs.get(0).get(i-1)+"\n\n");
				textArea.setScrollTop(Double.MAX_VALUE);
				if(name!=null && !name.isEmpty()) titledPane.setText(name);
			}
		});
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == mAbout) {
			Stage stage = new Stage();
			stage.setResizable(false);
			AnchorPane AboutFrame = null;
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(friendsWindow.class.getResource("AboutFrame.fxml"));
			loader.setController(this);
			try {
				AboutFrame = (AnchorPane) loader.load();
			} catch(IOException e) {
				e.printStackTrace();
			}
			myScene = new Scene(AboutFrame);
			stage.setScene(myScene);
			stage.show();
		}
		if(event.getSource() == mClose) {
			ButtonType bClose = new ButtonType("Luk", ButtonData.OK_DONE);
			ButtonType bCancel = new ButtonType("Annullér", ButtonData.NO);
			Alert confirmClose = new Alert(AlertType.CONFIRMATION, null, bClose, bCancel);
			confirmClose.initOwner(myStage);
			confirmClose.setTitle("Luk QuickConnect");
			confirmClose.setHeaderText("Du er ved at logge ud og lukke QuickConnect");
			confirmClose.setContentText("Er du sikker på at du vil fortsætte?");

			Optional<ButtonType> result = confirmClose.showAndWait();

			if(result.get() == bClose) {
				closeChatWindow();
			} else {
				confirmClose.close();
			}
		}
		if(event.getSource() == mSettings) {
			Stage stage = new Stage();
			stage.initOwner(menuBar.getScene().getWindow());
			settingsWindow sW = new settingsWindow();
			try {
				sW.start(stage, fu);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		if(event.getSource() == mSignOut) {

			ButtonType bSignOut = new ButtonType("Log ud", ButtonData.OK_DONE);
			ButtonType bCancel = new ButtonType("Annullér", ButtonData.NO);
			Alert confirmSignOut = new Alert(AlertType.CONFIRMATION, null, bSignOut, bCancel);
			confirmSignOut.initOwner(myStage);
			confirmSignOut.setTitle("Log ud");
			confirmSignOut.setHeaderText("Du er ved at logge ud af QuickConnect");
			confirmSignOut.setContentText("Er du sikker på at du vil logge ud?");

			Optional<ButtonType> result = confirmSignOut.showAndWait();

			if(result.get() == bSignOut) {
				th.interrupt();// Threaden stopper ikke når denne kodes køres, resten virker
				try {
					fu.setOfflineUser();
				} catch(SQLException e1) {
					e1.printStackTrace();
				}
				myStage.close();
				Stage stage = new Stage();
				loginWindow lW = new loginWindow();

				try {
					lW.start(stage);
				} catch(Exception e) {
					e.printStackTrace();
				}

			} else {
				confirmSignOut.close();
			}
		}
		if(event.getSource() == mFullScreen) {
			myStage.setFullScreen(true);
			mExitFullScreen.setDisable(false);
		}
		if(event.getSource() == mExitFullScreen) {
			myStage.setFullScreen(false);
			mExitFullScreen.setDisable(true);
		}
		if(event.getSource() == colorPick) {
			System.out.println(colorPick.getValue().toString());
		}
		if(event.getSource() == bAddFriend) {
			Stage stage = new Stage();
			stage.initOwner(bAddFriend.getScene().getWindow());
			friendsWindow fW = new friendsWindow();
			try {
				fW.start(stage, fu);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		if(event.getSource() == bAddGroup) {
			Stage stage = new Stage();
			stage.initOwner(bAddGroup.getScene().getWindow());
			groupWindow gW = new groupWindow();
			try {
				gW.start(stage, fu);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		if(event.getSource() == mDeleteOn || event.getSource() == mDeleteOff) {
			try {
				fu.deleteFriend(activeUser);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(event.getSource() == mBlockOn || event.getSource() == mBlockOff) {

			try {
				fu.blockContact(activeUser);
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(event.getSource() == mInfoOn || event.getSource() == mInfoOff) {
			Stage stage = new Stage();
			infoWindow iW = new infoWindow();
			try {
				iW.start(stage,activeUser);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		if(event.getSource() == mLeave) {
			try {
				fu.leaveGroup(activeUser);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		if(event.getSource() == mInfoGroup) {
			System.out.println("Trykket på infogroup");
		}
		
		if(event.getSource() == mDeleteGroup){
			int check=0;
			try {
				check = fu.deleteGroup(activeUser);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if(check == 0){
				Alert registerFail = new Alert(AlertType.WARNING);
				registerFail.setTitle(this.myStage.getTitle());
				registerFail.setHeaderText("Sletning af gruppe mislykkedes!");
				registerFail.setContentText("Du kan ikke slette gruppen, da du ikke er admin!");
				registerFail.showAndWait();
			}
			else{
				Alert registerSuccess = new Alert(AlertType.INFORMATION);
				registerSuccess.setTitle(this.myStage.getTitle());
				registerSuccess.setHeaderText("Gruppen er slettet!");
				registerSuccess.setContentText("Du er admin af gruppen, og den er nu slettet.");
				registerSuccess.showAndWait();
			}
		}
		
		if(event.getSource() == mThrow){
			try {
				fu.throwOut(activeUser);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public Stage getPrimaryStage() {
		return myStage;
	}

	public void closeChatWindow() {
		try {
			fu.setOfflineUser();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}
