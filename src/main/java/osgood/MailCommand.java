package osgood;

public class MailCommand extends SimpleResponder {

	@Override
	protected String response() {
		return "250 OK";
	}

}
