package zamn.creation.board;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

import javax.imageio.ImageIO;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;

import zamn.board.AbstractBoard;
import zamn.board.Critter;
import zamn.board.Decoration;
import zamn.board.Tile;
import zamn.board.controlmode.Action;

public class BoardLoader {

	private CritterFactory critterFactory;

	private DecorationFactory decorationFactory;
	private ObjectMapper objectMapper;
	private TileFactory tileFactory;

	public Class<? extends BoardDefinition> getBoardDefinitionClass() {
		return BoardDefinition.class;
	}

	public void load(URI boardId, AbstractBoard board)
			throws JsonParseException, JsonMappingException,
			MalformedURLException, IOException {

		BoardDefinition boardDefinition = objectMapper.readValue(
				boardId.toURL(), getBoardDefinitionClass());

		// create the tiles 2d array
		BoardPieceDefinition[][] tileDefinitions = boardDefinition
				.getTileDefinitions();
		Tile[][] tiles = new Tile[tileDefinitions.length][];
		for (int x = 0; x < tileDefinitions.length; x++) {
			tiles[x] = new Tile[tileDefinitions[x].length];
			for (int y = 0; y < tileDefinitions[x].length; y++) {
				BoardPieceDefinition tileDefinition = tileDefinitions[x][y];
				tileDefinition.setCoords(new int[] { x, y });
				Tile tile = (Tile) tileFactory.get(tileDefinition);
				if (x != 0) {
					tiles[x - 1][y].setRight(tile);
					tile.setLeft(tiles[x - 1][y]);
				}
				if (y != 0) {
					tiles[x][y - 1].setBottom(tile);
					tile.setTop(tiles[x][y - 1]);
				}
				tiles[x][y] = tile;
			}
		}

		board.setTiles(tiles);

		// add the decorations
		DecorationDefinition[] decorationDefinitions = boardDefinition
				.getDecorationDefinitions();
		for (int i = 0; i < decorationDefinitions.length; i++) {
			DecorationDefinition decorationDefinition = decorationDefinitions[i];
			Decoration decoration = (Decoration) decorationFactory
					.get(decorationDefinition);
			int[] xy = decorationDefinition.getCoords();
			int x = xy[0];
			int y = xy[1];
			board.addBoardPiece(decoration, x, y);
		}

		// add the critters
		CritterDefinition[] critterDefinitions = boardDefinition
				.getCritterDefinitions();
		for (int i = 0; i < critterDefinitions.length; i++) {
			CritterDefinition critterDefinition = critterDefinitions[i];
			Critter critter = (Critter) critterFactory.get(critterDefinition);
			int[] xy = critterDefinition.getCoords();
			int seedX = xy[0];
			int seedY = xy[1];
			if (tiles[seedX] != null && tiles[seedX][seedY] != null
					&& !tiles[seedX][seedY].isOccupied()) {
				board.addBoardPiece(critter, seedX, seedY);
			}
		}

		// add the exit definitions
		ExitDefinition[] exitDefinitions = boardDefinition.getExitDefinitions();
		for (int i = 0; i < exitDefinitions.length; i++) {
			ExitDefinition exitDefinition = exitDefinitions[i];
			board.addExit(
					exitDefinition.getX(),
					exitDefinition.getY(),
					Action.valueOf(exitDefinition.getDir().toUpperCase()),
					new String[] { exitDefinition.getBoardId(),
							String.valueOf(exitDefinition.getEntryPoint()) });
		}

		// add the entrances
		board.setEntrances(boardDefinition.getEntranceDefinitions());
	}

	protected BufferedImage parseSpriteSheet(Resource spriteSheetResource)
			throws MalformedURLException, IOException {
		return ImageIO.read(spriteSheetResource.getURI().toURL());
	}

	@Required
	public void setCritterFactory(CritterFactory critterFactory) {
		this.critterFactory = critterFactory;
	}

	@Required
	public void setDecorationFactory(DecorationFactory decorationFactory) {
		this.decorationFactory = decorationFactory;
	}

	@Required
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Required
	public void setTileFactory(TileFactory tileFactory) {
		this.tileFactory = tileFactory;
	}
}
