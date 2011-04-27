package osgood;

public class RecipientCommand extends SimpleResponder {

	@Override
	protected String response() {
		return "250 Accepted";
	}

}
