package osgood;

import java.io.IOException;
import java.net.Socket;

public interface Command {

	void execute(Socket socket) throws IOException;

}
