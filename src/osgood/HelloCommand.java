package osgood;

public class HelloCommand extends SimpleResponder {

	@Override
	protected String response() {
		return "250 localhost-127.0.0.1 Hello from the postal hoard";
	}

}
