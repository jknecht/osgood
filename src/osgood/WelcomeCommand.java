package osgood;

public class WelcomeCommand extends SimpleResponder {

	@Override
	protected String response() {
		return "220 Osgood ESMTP SMTP-Server; (Version 1.0/" + System.getProperty("os.name") + ") ready.";
	}

}
