package osgood;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class Conversation implements Runnable {
	Socket socket;
	MessageRelay relay;
	
	public Conversation(Socket socket, MessageRelay relay) {
		this.socket = socket;
		this.relay = relay;
	}

	public void run() {
		try {
			this.socket.setSoTimeout(30000);
			
			new WelcomeCommand().execute(socket);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String message = null;
			while (socket.isConnected() && !socket.isClosed()) {
				Command command = CommandFactory.get(in.readLine());
				command.execute(socket);
				if (command instanceof DataCommand) {
					message = ((DataCommand) command).getData();
				}
			}
			
			relay.relay(message);
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
