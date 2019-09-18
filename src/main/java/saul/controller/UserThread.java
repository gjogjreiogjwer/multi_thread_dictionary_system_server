package saul.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import saul.service.DictionaryService;
import saul.utils.MessageType;
import saul.utils.Status;
import saul.utils.UtilDictionary;
import saul.utils.Message;

@Log4j2
public class UserThread implements Runnable{
	private  PrintStream sender;
	private  Scanner receiver;
	public static DictionaryService service;
	private Message reply;
	public static Properties properties;
	
	static {
		
		File file = new File(DictionaryServer.dictionaryPath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				log.error(e.getMessage());
				System.exit(0);
			}
		}
		properties = new Properties();
		InputStream in = null;
		//InputStream in = DictionaryDao.class.getClassLoader().getResourceAsStream("dictionary.properties");
		try {
			in = new BufferedInputStream(new FileInputStream(DictionaryServer.dictionaryPath));
			properties.load(in);
		} catch (IOException e) {
			log.error(e.getMessage());
		}finally {
			UtilDictionary.closeInputStream(in);
		}
		service = new DictionaryService();
	}
	
	public UserThread(Socket socket) throws IOException {
		super();
		sender = new PrintStream(new BufferedOutputStream(socket.getOutputStream()));
		receiver = new Scanner(socket.getInputStream());
		reply = new Message();
	}


	public void run() {
		boolean flag = false;
		while(receiver.hasNext()) {
			flag = false;
			String messageString = receiver.nextLine();
			Gson gson = new Gson();
			Message command = gson.fromJson(messageString, Message.class);
			String word = command.getData();
			
			if (command.getType() == MessageType.TYPE_ADD) {
				reply = service.add(word, command.getInfo());
				if (reply.getStatus() == Status.SUCCESS) flag = true;
			}else if (command.getType() == MessageType.TYPE_SEARCH) {
				reply = service.search(word);
			}else if (command.getType() == MessageType.TYPE_REMOVE) {
				reply = service.remove(word);
				if (reply.getStatus() == Status.SUCCESS) flag = true;
			}else {
				reply.setStatus(Status.ERROR);
				reply.setInfo("Wrong command format, please enter the command again.");
			}
			if (flag) store();
			String replyString = gson.toJson(reply);
			sender.println(replyString);
			sender.flush();
			
		}
	}
	

	public void store() {
		OutputStream outputStream = null;
		try {
//			URL url = Thread.currentThread().getContextClassLoader().getResource("dictionary.properties");
//			outputStream = new FileOutputStream(new File(url.toURI()));
			outputStream = new BufferedOutputStream(new FileOutputStream(DictionaryServer.dictionaryPath));
			UserThread.properties.store(outputStream,"");
			log.info("the dictionary has been updated to disk.");
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}finally {
			if(outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		}
	}
	
}
