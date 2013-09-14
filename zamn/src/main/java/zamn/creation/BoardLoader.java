package zamn.creation;

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
import zamn.board.Tile;
import zamn.board.controlmode.Action;
import zamn.board.piece.Critter;

public class BoardLoader {

	private ObjectMapper objectMapper;

	private TileFactory tileFactory;
	private CritterFactory critterFactory;

	public Class<? extends BoardDefinition> getBoardDefinitionClass() {
		return BoardDefinition.class;
	}

	public void load(URI boardId, AbstractBoard board)
			throws JsonParseException, JsonMappingException,
			MalformedURLException, IOException {

		BoardDefinition boardDefinition = objectMapper.readValue(
				boardId.toURL(), getBoardDefinitionClass());

		// create the tiles 2d array
		BoardPieceDefinition[][] tileDefinitions = boardDefinition.getTiles();
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

		// add the critters
		CritterDefinition[] critterPositionDefinitions = boardDefinition
				.getCritterDefinitions();
		for (int i = 0; i < critterPositionDefinitions.length; i++) {
			CritterDefinition critterPositionDefinition = critterPositionDefinitions[i];
			Critter critter = (Critter) critterFactory
					.get(critterPositionDefinition);
			int[] xy = critterPositionDefinition.getCoords();
			int seedX = xy[0];
			int seedY = xy[1];
			if (tiles[seedX] != null && tiles[seedX][seedY] != null
					&& !tiles[seedX][seedY].isOccupied()) {
				tiles[seedX][seedY].add(critter);
				critter.setXY(seedX, seedY);
			}
			board.addCritter(critter);
		}

		// add the exit definitions
		ExitDefinition[] exitDefinitions = boardDefinition.getExits();
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
		board.setEntrances(boardDefinition.getEntrances());

		board.setTiles(tiles);
	}

	protected BufferedImage parseSpriteSheet(Resource spriteSheetResource)
			throws MalformedURLException, IOException {
		return ImageIO.read(spriteSheetResource.getURI().toURL());
	}

	@Required
	public void setTileFactory(TileFactory tileFactory) {
		this.tileFactory = tileFactory;
	}

	@Required
	public void setCritterFactory(CritterFactory critterFactory) {
		this.critterFactory = critterFactory;
	}

	@Required
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
}
