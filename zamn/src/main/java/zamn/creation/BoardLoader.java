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

public class BoardLoader {

	private final ObjectMapper objectMapper;
	private Dimension spriteSize;
	private Map<String, Integer[]> tileSpriteMap;
	private BufferedImage tileSpriteSheet;

	public BoardLoader(ObjectMapper objectMapper, Resource tileSpriteMapResource)
			throws IOException {
		this.objectMapper = objectMapper;
		SpriteMapDefinition spriteMapDefinition = parseSpriteMapDefinition(tileSpriteMapResource);
		tileSpriteSheet = parseSpriteSheet(new ClassPathResource(
				spriteMapDefinition.getSpriteSheetClassPath()));
		tileSpriteMap = spriteMapDefinition.getSpriteMap();
	}

	protected void doAfterLoad(BoardDefinition boardDefinition,
			AbstractBoard board) {
		// hook for subclasses
	}

	public void load(URI boardId, AbstractBoard board)
			throws JsonParseException, JsonMappingException,
			MalformedURLException, IOException {

		BoardDefinition boardDefinition = parseBoardDefinition(boardId);
		TileDefinition[][] tileDefinitions = boardDefinition.getTiles();
		Tile[][] ret = new Tile[tileDefinitions.length][];
		for (int x = 0; x < tileDefinitions.length; x++) {
			ret[x] = new Tile[tileDefinitions[x].length];
			for (int y = 0; y < tileDefinitions[x].length; y++) {
				ret[x][y] = new Tile(x, y);
				ret[x][y].setSolid(!tileDefinitions[x][y].is_());
				if (x != 0) {
					ret[x - 1][y].setRight(ret[x][y]);
					ret[x][y].setLeft(ret[x - 1][y]);
				}
				if (y != 0) {
					ret[x][y - 1].setBottom(ret[x][y]);
					ret[x][y].setTop(ret[x][y - 1]);
				}

				String tileId = tileDefinitions[x][y].getSpriteId();
				Integer[] spriteSheetXY = tileSpriteMap.get(tileId);
				if (spriteSheetXY == null) {
					throw new IllegalArgumentException(
							"Could not retrieve tile with ID: '" + tileId + "'");
				}
				ret[x][y].applySprite(tileSpriteSheet, spriteSheetXY[0],
						spriteSheetXY[1], spriteSize);
			}
		}

		board.setTiles(ret);

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
	public void setSpriteSize(Dimension spriteSize) {
		this.spriteSize = spriteSize;
	}
}
