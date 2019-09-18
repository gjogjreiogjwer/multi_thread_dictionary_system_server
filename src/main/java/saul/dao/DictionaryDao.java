package saul.dao;

import java.util.Properties;
import lombok.extern.log4j.Log4j2;
import saul.controller.UserThread;

@Log4j2
public class DictionaryDao {
	private  Properties prop;
	
	public DictionaryDao() {
		prop = UserThread.properties;
		log.info("the dictionary file loaded in server successfully.");
	}
	
	public synchronized String add(String word, String meanings) {
		if (prop.getProperty(word) == null) {
			prop.put(word, meanings);
			return "success";
		}
		return "duplicate";
	}
	
	public synchronized String remove(String word) {
		if (prop.getProperty(word) != null) {
			prop.remove(word);
			return "success";
		}
		return "not found";
	}
	
	public synchronized String search(String word) {
		return prop.getProperty(word);
	}
	
}
