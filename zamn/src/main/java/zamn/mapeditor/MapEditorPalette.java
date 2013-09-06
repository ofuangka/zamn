package zamn.mapeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.springframework.core.io.Resource;

public class MapEditorPalette extends JPanel {

	private static final long serialVersionUID = 5684413780256691006L;

	private BufferedImage spriteSheet;
	private Dimension spriteSize;
	private int cursorX;
	private int cursorY;

	public MapEditorPalette(Resource spriteSheetResource,
			Resource spriteResource, Dimension spriteSize) throws IOException {
		spriteSheet = ImageIO.read(spriteSheetResource.getURL());
		setPreferredSize(new Dimension(spriteSheet.getWidth(),
				spriteSheet.getHeight()));
		this.spriteSize = spriteSize;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(spriteSheet, 0, 0, spriteSheet.getWidth(),
				spriteSheet.getHeight(), 0, 0, spriteSheet.getWidth(),
				spriteSheet.getHeight(), null);

		// draw the cursor
		g2d.setColor(Color.red);
		g2d.drawRect(cursorX * spriteSize.width, cursorY * spriteSize.height,
				spriteSize.width - 1, spriteSize.height - 1);
	}

}
