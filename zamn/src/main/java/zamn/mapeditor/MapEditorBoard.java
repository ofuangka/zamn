package zamn.mapeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.beans.Transient;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import zamn.board.AbstractBoard;
import zamn.board.AbstractBoardPiece;
import zamn.board.Tile;
import zamn.board.controlmode.Action;

public class MapEditorBoard extends AbstractBoard implements MouseListener {

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

	private static final long serialVersionUID = 8159431301832393440L;

	public MapEditorBoard() {
		addMouseListener(this);
	}

	@Override
	public void backspace() {
		// remove a piece from the currently selected tile
		Tile currentTile = getCurrentTile();
		List<AbstractBoardPiece> pieces = currentTile.getPieces();
		if (!pieces.isEmpty()) {
			pieces.remove(pieces.size());
		}
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

	protected Tile getCurrentTile() {
		return getTile(CURSOR.getX(), CURSOR.getY());
	}

	@Override
	@Transient
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	@Override
	@Transient
	public Dimension getPreferredSize() {
		Dimension spriteSize = getSpriteSize();
		return (tiles == null) ? new Dimension() : new Dimension(tiles.length
				* spriteSize.width, tiles[0].length * spriteSize.height);
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
	public void mouseClicked(MouseEvent e) {
		Dimension spriteSize = getSpriteSize();
		int tileX = e.getX() / spriteSize.width;
		int tileY = e.getY() / spriteSize.height;

		if (isInBounds(tileX, tileY)) {
			placePiece(CURSOR, tileX, tileY);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

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
					if (tile.isSolid()) {
						g2d.setColor(Color.red);
						g2d.drawLine(dx1, dy1, dx2 - 1, dy2 - 1);
						g2d.drawLine(dx2 - 1, dy1, dx1, dy2 - 1);
					}

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
		// toggle the solidity of the tile
		Tile currentTile = getCurrentTile();
		currentTile.setSolid(!currentTile.isSolid());
		repaint();
	}
}
