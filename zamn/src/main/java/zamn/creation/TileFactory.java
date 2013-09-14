package zamn.creation;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.io.Resource;

import zamn.board.Sprite;
import zamn.board.Tile;

public class TileFactory extends BoardPieceFactory {

	public TileFactory(ObjectMapper objectMapper, Resource spriteMapResource)
			throws IOException {
		super(objectMapper, spriteMapResource);
	}

	@Override
	protected Sprite getNewSprite() {
		return new Tile();
	}

}
