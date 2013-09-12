package zamn.creation;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Map;

import javax.imageio.ImageIO;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import zamn.board.AbstractBoard;
import zamn.board.Tile;
import zamn.board.controlmode.Action;
import zamn.board.piece.Critter;

public class BoardLoader {

	private CritterFactory critterFactory;
	private final ObjectMapper objectMapper;
	private Dimension spriteSize;
	private Map<String, int[]> tileSpriteMap;

	private BufferedImage tileSpriteSheet;

	public BoardLoader(ObjectMapper objectMapper, Resource tileSpriteMapResource)
			throws IOException {
		this.objectMapper = objectMapper;
		SpriteMapDefinition spriteMapDefinition = parseSpriteMapDefinition(tileSpriteMapResource);
		tileSpriteSheet = parseSpriteSheet(new ClassPathResource(
				spriteMapDefinition.getSpriteSheetClassPath()));
		tileSpriteMap = spriteMapDefinition.getSpriteMap();
	}

	public void applyTerrainToTile(String spriteId, Tile tile) {
		tile.setSpriteId(spriteId);
		int[] spriteSheetXY = tileSpriteMap.get(spriteId);
		if (spriteSheetXY == null) {
			throw new IllegalArgumentException(
					"Could not retrieve sprite with ID: '" + spriteId + "'");
		}
		tile.drawSprite(tileSpriteSheet, spriteSheetXY[0], spriteSheetXY[1],
				spriteSize);
	}

	protected void doAfterLoad(BoardDefinition boardDefinition,
			AbstractBoard board) {
		// hook for subclasses
	}

	public void load(URI boardId, AbstractBoard board)
			throws JsonParseException, JsonMappingException,
			MalformedURLException, IOException {

		BoardDefinition boardDefinition = parseBoardDefinition(boardId);

		// create the tiles 2d array
		TileDefinition[][] tileDefinitions = boardDefinition.getTiles();
		Tile[][] tiles = new Tile[tileDefinitions.length][];
		for (int x = 0; x < tileDefinitions.length; x++) {
			tiles[x] = new Tile[tileDefinitions[x].length];
			for (int y = 0; y < tileDefinitions[x].length; y++) {
				Tile tile = new Tile(x, y);
				tile.setSolid(!tileDefinitions[x][y].is_());
				if (x != 0) {
					tiles[x - 1][y].setRight(tile);
					tile.setLeft(tiles[x - 1][y]);
				}
				if (y != 0) {
					tiles[x][y - 1].setBottom(tile);
					tile.setTop(tiles[x][y - 1]);
				}

				String spriteId = tileDefinitions[x][y].getSpriteId();
				applyTerrainToTile(spriteId, tile);
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

		// add a hook for subclasses
		doAfterLoad(boardDefinition, board);
	}

	protected BoardDefinition parseBoardDefinition(URI boardId)
			throws JsonParseException, JsonMappingException,
			MalformedURLException, IOException {
		return objectMapper.readValue(boardId.toURL(), BoardDefinition.class);
	}

	protected SpriteMapDefinition parseSpriteMapDefinition(
			Resource tileSpriteMapResource) throws JsonParseException,
			JsonMappingException, MalformedURLException, IOException {
		return objectMapper.readValue(tileSpriteMapResource.getURI().toURL(),
				SpriteMapDefinition.class);
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
	public void setSpriteSize(Dimension spriteSize) {
		this.spriteSize = spriteSize;
	}
}
