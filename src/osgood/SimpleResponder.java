package osgood;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public abstract class SimpleResponder implements Command {

	@Override
	public void execute(Socket socket) throws IOException {
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		out.write(response() + "\r\n");
		out.flush();
	}

	protected abstract String response();
}
