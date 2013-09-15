package zamn.creation.board;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.io.Resource;

import zamn.board.Decoration;
import zamn.board.Sprite;

public class DecorationFactory extends BoardPieceFactory {

	public DecorationFactory(ObjectMapper objectMapper,
			Resource spriteMapResource) throws IOException {
		super(objectMapper, spriteMapResource);
	}

	@Override
	protected Sprite getNewSprite() {
		return new Decoration();
	}

}
