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
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
	@FXML private TextArea messagesArea, inMessageArea;
	@FXML private TextField inSearchFriends, inSearchGroups;
	@FXML private Button bAddFriend, bAddGroup;
	@FXML private ColorPicker colorPick;
	private ArrayList<Integer> notification = new ArrayList<Integer>();
	private FunctionUser fu;
	private int[] offlineFriends;
	private int[] onlineFriends;
	private int[] groups;
	private int activeUser;
	private ArrayList<ArrayList<String>> messages = new ArrayList<ArrayList<String>>();
	private ArrayList<Integer> users = new ArrayList<Integer>();
	@FXML private ContextMenu contextMenu;
	@FXML private MenuItem mDeleteOn, mDeleteOff, mBlockOn, mBlockOff, mInfoOn, mInfoOff, mLeave, mInfoGroup,
	        mDeleteGroup, mThrow;
	private boolean checkType;

	public void start(Stage stage, FunctionUser fu) throws SQLException, IOException {
		this.fu = fu;
		this.myStage = stage;
		this.myStage.setTitle("QuickConnect - user: " + fu.user().getUsername());
		this.myStage.setMinHeight(450);
		this.myStage.setMinWidth(425);
		fu.activateUser();
		fu.setUserOnline();

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
					            if(checkType == false) {
						            ArrayList<ArrayList<String>> msg = fu.getGroupMessages(activeUser);
						            for(int i = 1; i <= msg.get(0).size(); i++) {
							            messagesArea.appendText(
		                                        msg.get(1).get(i - 1) + ":\n" + msg.get(0).get(i - 1) + "\n\n");
						            }
					            } else {
						            fu.getMessages(messages, users);
						            for(int i = 1; i <= messages.size(); i++) {
							            for(int t = 1; t <= messages.get(i - 1).size(); t++) {
								            if(users.get(i - 1) == activeUser)
									            messagesArea.appendText(fu.idToNickname(users.get(i - 1)) + ":\n"
		                                                + messages.get(i - 1).get(t - 1) + "\n\n");
							            }

							            if(users.get(i - 1) != activeUser) {
								            notification.add(users.get(i - 1));
							            }
						            }
					            }
					            messages.clear();
					            users.clear();
					            getListsContents();
					            fu.con().update("UPDATE users SET last_on='" + fu.f.timestamp()
		                                + "', online=1 WHERE user_ID='" + fu.user().getUserID() + "'");
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

		titledPane.setText("QuickConnect chat");
		bAddFriend.setId("bAddPlus");
		bAddGroup.setId("bAddPlus");
		if(titledPane.getText().equals("QuickConnect chat"))
			inMessageArea.setEditable(false);
		else inMessageArea.setEditable(true);

		setInMessageFunctions();
		setButtonFunctions();
		getListsContents();
		setListsFunctions();

	}

	private void setInMessageFunctions() {
		addTextLimiter(inMessageArea, 900);
		inMessageArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode().equals(KeyCode.ENTER)) {
					String msg = inMessageArea.getText();
					// FunctionUser.sendMessage(msg, user.UserID,
		            // titledPane.getText());
					try {
						messagesArea.appendText(fu.getNickName() + ":\n" + msg + "\n\n");
					} catch(SQLException e) {
						e.printStackTrace();
					}

					try {
						if(checkType == true)
							fu.sendMessage(msg, activeUser);
						else if(checkType == false)
							fu.sendGroupMessage(msg, activeUser);
					} catch(SQLException | IOException e) {
						e.printStackTrace();
					}
					inMessageArea.clear();
				}
			}
		});
	}

	public static void addTextLimiter(final TextArea tf, final int maxLength) {
		tf.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue,
		            final String newValue) {
				if(tf.getText().length() > maxLength) {
					String s = tf.getText().substring(0, maxLength);
					tf.appendText(s);
				}
			}
		});
	}

	private void setButtonFunctions() {

		menuBar.setUseSystemMenuBar(true);
		bAddFriend.setOnAction(this);
		bAddGroup.setOnAction(this);
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
		if(inSearchFriends.getText().length() == 0) {
			this.onlineFriends = fu.getOnlineUsersId();
			String[] on = fu.getOnlineUsersNickname();
			if(notification.size() > 0 && checkType == true) {
				for(int j = 0; j < notification.size(); j++) {
					System.out.println("blabla " + "notifation" + notification.get(j));
					on[j] = "(!)" + on[j];
				}
			}
			ObservableList<String> onlineItems = FXCollections.observableArrayList(on);
			FilteredList<String> filteredonlineData = new FilteredList<>(onlineItems, s -> true);
			inSearchFriends.textProperty().addListener(obs -> {
				String filter = inSearchFriends.getText();
				if(filter == null || filter.length() == 0) {
					filteredonlineData.setPredicate(s -> true);
				} else {
					filteredonlineData.setPredicate(s -> s.toLowerCase().contains(filter.toLowerCase()));
				}
			});
			SortedList<String> sortedonlineData = new SortedList<>(filteredonlineData);
			friendsOnlineList.setItems(sortedonlineData);
			onlinePane.setText("Online (" + onlineItems.size() + " venner)");
		}

		if(inSearchFriends.getText().length() == 0) {
			this.offlineFriends = fu.getOfflineUsersId();
			String[] off = fu.getOfflineUsersNickname();
			ObservableList<String> offlineItems = FXCollections.observableArrayList(off);
			FilteredList<String> filteredofflineData = new FilteredList<>(offlineItems, s -> true);
			friendsOfflineList.setItems(offlineItems);
			inSearchFriends.textProperty().addListener(obs -> {
				String filter = inSearchFriends.getText();
				if(filter == null || filter.length() == 0) {
					filteredofflineData.setPredicate(s -> true);
				} else {
					filteredofflineData.setPredicate(s -> s.toLowerCase().contains(filter.toLowerCase()));
				}
			});
			SortedList<String> sortedofflineData = new SortedList<>(filteredofflineData);
			friendsOfflineList.setItems(sortedofflineData);
			offlinePane.setText("Offline (" + offlineItems.size() + " venner)");
		}
		if(inSearchGroups.getText().length() == 0) {
			this.groups = fu.getGroupsId();
			String[] gro = fu.getGroupsNames();
			ObservableList<String> groupsItems = FXCollections.observableArrayList(gro);
			FilteredList<String> filteredgroupsData = new FilteredList<>(groupsItems, s -> true);
			inSearchGroups.textProperty().addListener(obs -> {
				String filter = inSearchGroups.getText();
				if(filter == null || filter.length() == 0) {
					filteredgroupsData.setPredicate(s -> true);
				} else {
					filteredgroupsData.setPredicate(s -> s.toLowerCase().contains(filter.toLowerCase()));
				}
			});
			SortedList<String> sortedgroupsData = new SortedList<>(filteredgroupsData);
			groupsList.setItems(sortedgroupsData);
		}
	}

	private void setListsFunctions() {
		setListOnMouseClicked(friendsOnlineList, onlineFriends);
		setListOnMouseClicked(friendsOfflineList, offlineFriends);
		setListOnMouseClicked(groupsList, groups);
	}

	private void setListOnMouseClicked(ListView<String> list, int[] intArray) {
		list.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				int id = list.getSelectionModel().getSelectedIndex();
				if(id == -1)
					return;

				String name = list.getSelectionModel().getSelectedItem();
				if(list == friendsOnlineList) {
					checkType = true;
					activeUser = onlineFriends[id];
					if(notification.contains(activeUser))
						notification.remove(notification.indexOf(activeUser));
				} else if(list == friendsOfflineList) {
					checkType = true;
					activeUser = offlineFriends[id];
				} else {
					checkType = false;
					activeUser = groups[id];
				}

				System.out.println("clicked on " + activeUser);
				messagesArea.clear();
				ArrayList<ArrayList<String>> msgs = new ArrayList<ArrayList<String>>();
				try {
					if(list == groupsList)
						msgs = fu.getGroupMessages(activeUser, 0);
					else msgs = fu.getMessages(activeUser, 0);
				} catch(SQLException e1) {
					e1.printStackTrace();
				}
				if(msgs.get(0).size() > 0) {
					for(int i = 1; i <= msgs.get(0).size(); i++)
						messagesArea.appendText(msgs.get(1).get(i - 1) + ":\n" + msgs.get(0).get(i - 1) + "\n\n");
				}
				messagesArea.setScrollTop(Double.MAX_VALUE);
				if(name != null && !name.isEmpty())
					titledPane.setText(name);
				inMessageArea.setEditable(true);
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
				try {
					fu.setUserOffline();
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
			String hexColor = "#" + Integer.toHexString(colorPick.getValue().hashCode());
			inMessageArea.setStyle("-fx-text-inner-color: " + hexColor + "; -fx-focus-color: " + hexColor + ";");
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
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		if(event.getSource() == mBlockOn || event.getSource() == mBlockOff) {

			try {
				fu.blockContact(activeUser);
			} catch(SQLException | IOException e) {
				e.printStackTrace();
			}
		}
		if(event.getSource() == mInfoOn || event.getSource() == mInfoOff) {
			Stage stage = new Stage();
			infoWindow iW = new infoWindow();
			try {
				iW.start(stage, activeUser);
			} catch(Exception e) {
				e.printStackTrace();
			}

		}
		if(event.getSource() == mLeave) {
			try {
				fu.leaveGroup(activeUser);
			} catch(SQLException e) {
				e.printStackTrace();
			}

		}
		if(event.getSource() == mInfoGroup) {
			System.out.println("Trykket på infogroup");
		}

		if(event.getSource() == mDeleteGroup) {
			int check = 0;
			try {
				check = fu.deleteGroup(activeUser);
			} catch(SQLException e) {
				e.printStackTrace();
			}
			if(check == 0) {
				Alert deleteFail = new Alert(AlertType.WARNING);
				deleteFail.setTitle(this.myStage.getTitle());
				deleteFail.setHeaderText("Sletning af gruppe mislykkedes!");
				deleteFail.setContentText("Du kan ikke slette gruppen, da du ikke er admin!");
				deleteFail.showAndWait();
			} else {
				Alert deleteSuccess = new Alert(AlertType.INFORMATION);
				deleteSuccess.setTitle(this.myStage.getTitle());
				deleteSuccess.setHeaderText("Gruppen er slettet!");
				deleteSuccess.setContentText("Du er admin af gruppen, og den er nu slettet.");
				deleteSuccess.showAndWait();
			}
		}

		if(event.getSource() == mThrow) {
			try {
				if(fu.isOwner(activeUser)) {
					Stage stage = new Stage();
					throwWindow tW = new throwWindow();
					try {
						tW.start(stage, activeUser);
					} catch(Exception e) {
						e.printStackTrace();
					}
				} else {
					Alert deleteFail = new Alert(AlertType.WARNING);
					deleteFail.setTitle(this.myStage.getTitle());
					deleteFail.setHeaderText("Du kan ikke smide medlemmer ud af gruppen!");
					deleteFail.setContentText(
					        "Du har desv�rre ikke administrator tilladelse til denne gruppe, og kan derfor ikke smide nogen ud.");
					deleteFail.showAndWait();
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public Stage getPrimaryStage() {
		return myStage;
	}

	public void closeChatWindow() {
		try {
			fu.setUserOffline();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}
