package osgood;

public class UnrecognizedCommand extends SimpleResponder {

	@Override
	protected String response() {
		return "500 Command unrecognized";
	}

}
