package osgood;

import java.io.IOException;

public class SystemOutRelay implements MessageRelay {

	@Override
	public void relay(String message) throws IOException {
		System.out.println(message);
	}

}
