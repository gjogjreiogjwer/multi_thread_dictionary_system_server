package saul.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Message {
	private Status status;
	private int type;
	private String info;
	private String data;
	
	public void clear() {
		status = Status.DEFAULT;
		info = null;
		data = null;
	}
	
}