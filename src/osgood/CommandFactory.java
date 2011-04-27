package osgood;

import java.util.HashMap;

public class CommandFactory {

	private static HashMap<String, Class<? extends Command>> commandMap = new HashMap<String, Class<? extends Command>>();
	
	static {
		commandMap.put("helo", HelloCommand.class);
		commandMap.put("ehlo", HelloCommand.class);
		commandMap.put("data", DataCommand.class);
		commandMap.put("mail", MailCommand.class);
		commandMap.put("rcpt", RecipientCommand.class);
		commandMap.put("rset", ResetCommand.class);
		commandMap.put("quit", QuitCommand.class);
	}
	
	public static Command get(String readLine) {
		String commandLine = readLine.toLowerCase();
		for (String key : commandMap.keySet()) {
			if (commandLine.startsWith(key)) {
				try {
					return commandMap.get(key).newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return new UnrecognizedCommand();
	}

}
