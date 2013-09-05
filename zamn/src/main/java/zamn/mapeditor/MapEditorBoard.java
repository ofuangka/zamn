package zamn.mapeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.beans.Transient;
import java.io.IOException;
import java.net.URI;

import org.springframework.beans.factory.annotation.Required;

import zamn.board.AbstractBoard;
import zamn.board.AbstractBoardPiece;
import zamn.board.Tile;
import zamn.board.controlmode.Action;

public class MapEditorBoard extends AbstractBoard {

	private static final long serialVersionUID = 8159431301832393440L;

	private static final AbstractBoardPiece CURSOR = new AbstractBoardPiece() {
		@Override
		public BufferedImage getImage() {
			Dimension spriteSize = getSpriteSize();
			BufferedImage ret = new BufferedImage(spriteSize.width,
					spriteSize.height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = (Graphics2D) ret.getGraphics();
			g2d.setColor(Color.red);
			g2d.drawRect(0, 0, spriteSize.width - 1, spriteSize.height - 1);
			return ret;
		}
	};

	@Override
	public void backspace() {
		// remove a piece from the currently selected tile
	}

	@Override
	public void down() {
		tryMove(CURSOR, Action.DOWN);
	}

	@Override
	public void enter() {
		// add the piece to the currently selected tile
	}

	@Override
	public void esc() {
		// do nothing
	}

	@Override
	@Transient
	public Dimension getPreferredSize() {
		Dimension spriteSize = getSpriteSize();
		return (tiles == null) ? new Dimension() : new Dimension(tiles.length
				* spriteSize.width, tiles[0].length * spriteSize.height);
	}

	@Override
	@Transient
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	@Override
	public boolean isListening() {
		return true;
	}

	@Override
	public void left() {
		tryMove(CURSOR, Action.LEFT);
	}

	@Override
	public void load(URI id) throws IOException {
		super.load(id);
		placePiece(CURSOR, 0, 0);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		if (tiles != null) {
			for (int x = 0; x < tiles.length; x++) {
				for (int y = 0; y < tiles[x].length; y++) {
					Tile tile = tiles[x][y];
					Dimension tileSize = tile.getSpriteSize();
					int dx1 = x * tileSize.width;
					int dx2 = dx1 + tileSize.width;
					int dy1 = y * tileSize.height;
					int dy2 = dy1 + tileSize.height;
					g2d.drawImage(tile.getImage(), dx1, dy1, dx2, dy2, 0, 0,
							tileSize.width, tileSize.height, null);
				}
			}
		}
	}

	@Override
	public void right() {
		tryMove(CURSOR, Action.RIGHT);
	}

	@Override
	@Required
	public void setSpriteSize(Dimension spriteSize) {
		super.setSpriteSize(spriteSize);
		CURSOR.setSpriteSize(spriteSize);
	}

	@Override
	public void space() {
		// add the piece to the currently selected tile
	}

	@Override
	public void up() {
		tryMove(CURSOR, Action.UP);
	}

	@Override
	public void x() {
		// toggle the tile's walkability
	}
}
