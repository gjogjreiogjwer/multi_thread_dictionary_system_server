package saul.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import lombok.extern.log4j.Log4j2;
import saul.utils.MyThreadPool;
import saul.utils.ParseMainParameters;
import saul.utils.UtilDictionary;

@Log4j2
public class DictionaryServer {
	public static String dictionaryPath;
	
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		ParseMainParameters args4j = new ParseMainParameters();
		CmdLineParser parser = new CmdLineParser(args4j);
		try {
			parser.parseArgument(args);
			int port = args4j.getPort();
			int poolSize = args4j.getThreadSize();
			dictionaryPath = args4j.getDictionaryPath();
			if (!UtilDictionary.checkFilePath(dictionaryPath)) {
				log.error("wrong format of dictionary path");
				System.exit(0);			
			}
			serverSocket = new ServerSocket(port);
			MyThreadPool executors = new MyThreadPool(poolSize);
			log.info("Dictionary server has been started, waiting for connection...");
			while(true) {
				Socket socket = serverSocket.accept();
				UserThread userThread = new UserThread(socket);
				log.info("connected with client:" + socket.getInetAddress().getHostName());
				executors.execute(userThread);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (CmdLineException e) {
			log.error(e.getMessage());
		}
	}
	
	
}
