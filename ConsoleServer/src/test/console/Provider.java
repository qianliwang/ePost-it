package test.console;

import java.io.*;
import java.net.*;

public class Provider {
	
	private static Provider instance = null;
	
	private static ServerSocket providerSocket;
	private static Socket connection = null;
	private static BufferedWriter out;
	private static BufferedReader in;
	private static String message;

	public static synchronized Provider getInstance(){
		
		if(instance == null){
			instance = new Provider();
			initial();
		}
		return instance;
	}
	protected Provider() {
	}

	private static void initial() {
		try {
			// 1. creating a server socket
			providerSocket = new ServerSocket(2004, 10);
			// 2. Wait for connection
			System.out.println("Waiting for connection");
			connection = providerSocket.accept();
			System.out.println("Connection received from "
					+ connection.getInetAddress().getHostName());
			// 3. get Input and Output streams
			out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(),"latin1"));
			out.flush();
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"latin1"));
			sendMessage("Connection successful\n");
			// 4. The two parts communicate via the input and output streams
//			do {
//				message = in.readLine();
//				System.out.println("client>" + message);
//				if (message.equals("bye"))
//					sendMessage("bye");
//			} while (!message.equals("bye"));
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} 
//		finally {
//			// 4: Closing connection
//			try {
//				in.close();
//				out.close();
//				providerSocket.close();
//			} catch (IOException ioException) {
//				ioException.printStackTrace();
//			}
//		}
	}

	public static void sendMessage(String msg) {
		
		try {
			out.write(msg);
			out.flush();
			System.out.println("server>" + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

//	public static void main(String args[]) {
//		Provider server = new Provider();
//		while (true) {
//			server.initial();
//		}
//	}
}