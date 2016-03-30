package Livechat;

import java.awt.FontFormatException;
import java.io.IOException;
import java.sql.Connection;
import Livechat_GUI.Start;

public class Main {
	public static void main(String[] args) throws FontFormatException, IOException {
		Start.start();
		Connection con=Function.mysql();
	}
}
