package osgood;

public class ResetCommand extends SimpleResponder {

	@Override
	protected String response() {
		return "250 Reset OK";
	}

}
