package zamn.board;

import java.io.IOException;
import java.net.URI;

public interface IBoardUriResolver {
	URI resolve(String boardId) throws IOException;
}
