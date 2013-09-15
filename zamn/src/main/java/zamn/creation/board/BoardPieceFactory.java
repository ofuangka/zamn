package zamn.creation.board;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.io.Resource;

import zamn.board.BoardPiece;
import zamn.board.Sprite;

public class BoardPieceFactory extends SpriteFactory {

	public BoardPieceFactory(ObjectMapper objectMapper,
			Resource spriteMapResource) throws IOException {
		super(objectMapper, spriteMapResource);
	}

	@Override
	public Sprite get(SpriteDefinition spriteDefinition) {
		BoardPiece ret = (BoardPiece) super.get(spriteDefinition);

		BoardPieceDefinition boardPieceDefinition = (BoardPieceDefinition) spriteDefinition;

		int[] coords = boardPieceDefinition.getCoords();

		ret.setXY(coords[0], coords[1]);

		if (coords.length > 2) {
			ret.setZ(coords[2]);
		}

		// we only want to overwrite the default when the definition
		// specifically tells us to
		if (boardPieceDefinition.isSolid()) {
			ret.setSolid(boardPieceDefinition.isSolid());
		}

		return ret;
	}

	@Override
	protected Sprite getNewSprite() {
		return new BoardPiece();
	}

}
