package osgood;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The server's job is to open a port and
 * listen on it.  When it accepts a connection,
 * it will spawn a thread to manage the communication.
 * 
 * @author jeff knecht
 *
 */
public class Server implements Runnable {

	private final ServerSocket serverSocket;
	private final Properties config;
	
	public Server(Properties config) throws NumberFormatException, IOException {
		this.config = config;
		this.serverSocket = new ServerSocket(Integer.parseInt(config.getProperty("smtp.port")));
	}
	
	public void run() {
		File mailbagDir = new File(config.getProperty("mailbag.location"));
		ExecutorService threadPool = Executors.newCachedThreadPool(); 
		while (!serverSocket.isClosed()) {
			try {
				Socket socket = serverSocket.accept();
				if (!mailbagDir.exists()) {
					if (!mailbagDir.mkdirs()) {
						throw new RuntimeException("Unable to create mailbag directory.");
					}
				}
				if (!mailbagDir.isDirectory()) {
					throw new RuntimeException("mailbag is not a directory.");
				}
				threadPool.execute(new Conversation(socket, new FileRelay(new File(mailbagDir, String.valueOf(System.currentTimeMillis()) + ".eml"))));
			} catch (IOException e) {
				break;
			}
		}
	}
	
	public void stop() {
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			Properties config = new Properties();
			String configFileLocation = System.getProperty("config", "config/osgood.properties");
			File configFile = new File(configFileLocation);
			if (!configFile.exists() || !configFile.isFile() || !configFile.canRead()) {
				System.err.println("Unable to read config file at " + configFile.getAbsolutePath() + ". Using defaults.");
				config.setProperty("smtp.port", "1025");
				config.setProperty("control.port", "1026");
				config.setProperty("mailbag.location", "mailbag");
				System.err.println(config);
			} else {
				FileReader reader = new FileReader(configFile);
				config.load(reader);
				reader.close();
			}
			
			Server server = new Server(config);
			ExecutorService executor = Executors.newSingleThreadExecutor(new DaemonThreadFactory());
			executor.execute(server);
			
			//control the app lifecycle by spawning a control socket
			ServerSocket serverSocket = new ServerSocket(Integer.parseInt(config.getProperty("control.port")));
			boolean running = true;
			while (running) {
				Socket client = serverSocket.accept();
				client.getOutputStream().write("osgood control server.  Send 'kill' to stop the server.\r\n".getBytes());
				client.getOutputStream().flush();
				
				BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
				String in = null;
				while (running && !client.isClosed() && ((in = br.readLine()) != null)) {
					in = in.trim();
					if (in.equals("kill")) {
						client.close();
						running = false;
					} else if (in.equals("bye")) {
							client.close();
					} else {
						client.getOutputStream().write(("Unrecognized command: [" + in + "]\r\n").getBytes());
						client.getOutputStream().flush();
					}
				}
			}
			serverSocket.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
