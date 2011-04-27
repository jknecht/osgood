package osgood;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileRelay implements MessageRelay {
	File file;
	
	public FileRelay(File file) {
		this.file = file;
	}

	@Override
	public void relay(String message) throws IOException {
		FileWriter writer = new FileWriter(file);
		writer.write(message);
		writer.write("\n");
		writer.flush();
		writer.close();
	}
	
}
