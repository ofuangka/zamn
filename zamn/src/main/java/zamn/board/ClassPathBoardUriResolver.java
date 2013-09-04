package zamn.board;

import java.io.IOException;
import java.net.URI;

import org.springframework.core.io.ClassPathResource;

public class ClassPathBoardUriResolver implements IBoardUriResolver {

	private static final String DEFAULT_PREFIX = "boards/";
	private static final String DEFAULT_SUFFIX = ".js";

	private String prefix = DEFAULT_PREFIX;
	private String suffix = DEFAULT_SUFFIX;

	public URI resolve(String boardId) throws IOException {
		return (new ClassPathResource(prefix + boardId + suffix)).getURI();
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
}
