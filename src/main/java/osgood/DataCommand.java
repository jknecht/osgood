package osgood;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class DataCommand implements Command {
	String data;
	
	public void execute(Socket socket) throws IOException {
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		out.write("354 Enter mail, end with \".\" on a line by itself\r\n");
		out.flush();
		
		String input = null;
		StringBuilder messageData = new StringBuilder();
		while (!(input = in.readLine()).equals(".")) {
			if (input.startsWith("..")) {
				input = new String(input.substring(1));
			}
			messageData.append(input).append('\n');
		}
	
		this.data = messageData.toString();
		
		out.write("250 Message queued for delivery\r\n");
		out.flush();
		
		socket.close();
		
	}

	public String getData() {
		return data;
	}
	
}
