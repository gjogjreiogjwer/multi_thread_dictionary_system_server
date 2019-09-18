package saul.utils;

import org.kohsuke.args4j.Option;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ParseMainParameters {
	@Option(name="-port", required = false, usage = "tcp port")
	private int port = 8888;
	
	@Option(name="-filePath", required = false, usage = "dictionary file path")
	private String dictionaryPath = "./dictionary.properties";
	
	@Option(name="-size", required = false, usage = "the size of thread pool")
	private int threadSize = 20;
}
