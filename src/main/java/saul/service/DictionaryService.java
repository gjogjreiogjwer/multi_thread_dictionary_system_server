package saul.service;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import saul.dao.DictionaryDao;
import saul.utils.MessageType;
import saul.utils.Status;
import saul.utils.Message;

@Log4j2
public class DictionaryService {
	@Getter
	private DictionaryDao dao;
	
	public DictionaryService() {
		dao = new DictionaryDao();
		log.info("dictionary service has been initialized.");
	}
	
	public Message search(String word) {
		Message message = new Message();
		message.setType(MessageType.TYPE_SEARCH);
		String data = dao.search(word);
		if (data == null) {
			message.setStatus(Status.ERROR);
			message.setInfo("the word \"" + word +"\" was not found!");
		}else {
			message.setStatus(Status.SUCCESS);
			message.setData(data);
		}
		return message;
	}
	
	public Message add(String word, String meanings) {
		Message message = new Message();
		String data = dao.add(word, meanings);
		if ("success".equals(data)) {
			message.setStatus(Status.SUCCESS);
			message.setData(word + " has been added to dictionary successfully!");
		}else {
			message.setStatus(Status.FAIL);
			message.setInfo(word + " is duplicated, add failed!");
		}
		return message;
	}
	
	public Message remove(String word) {
		Message message = new Message();
		String data = dao.remove(word);
		if ("success".equals(data)) {
			message.setStatus(Status.SUCCESS);
			message.setData(word + " has been removed from dictionary.");
		}else {
			message.setStatus(Status.FAIL);
			message.setInfo(word + " is not found in the dictionary.");
		}
		return message;
	}

}
