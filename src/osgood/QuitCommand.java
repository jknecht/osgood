package osgood;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class QuitCommand implements Command {

	@Override
	public void execute(Socket socket) throws IOException {
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		out.write("221 Closing connection. Goodbye.\r\n");
		out.flush();
		socket.close();
	}

}
