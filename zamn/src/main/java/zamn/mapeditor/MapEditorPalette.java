package zamn.mapeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import zamn.creation.SpriteMapDefinition;

public class MapEditorPalette extends JScrollPane implements MouseListener {

	private static final long serialVersionUID = 5684413780256691006L;

	private int cursorX;
	private int cursorY;
	private Map<String, String> reverseLookupSpriteMap;
	private Map<String, Integer[]> spriteMap;
	private BufferedImage spriteSheet;
	private Dimension spriteSize;

	public MapEditorPalette(ObjectMapper objectMapper, Resource spriteResource,
			Dimension spriteSize) throws IOException {
		this.spriteSize = spriteSize;
		SpriteMapDefinition spriteMapDefinition = objectMapper.readValue(
				spriteResource.getURL(), SpriteMapDefinition.class);
		spriteMap = spriteMapDefinition.getSpriteMap();
		createReverseLookupSpriteMap();
		spriteSheet = ImageIO.read((new ClassPathResource(spriteMapDefinition
				.getSpriteSheetClassPath()).getURL()));
		JLabel spriteSheetLabel = new JLabel(new ImageIcon(spriteSheet));
		setViewportView(spriteSheetLabel);
		addMouseListener(this);
		setBorder(BorderFactory.createEmptyBorder());
	}

	protected void createReverseLookupSpriteMap() {
		reverseLookupSpriteMap = new HashMap<String, String>();
		Set<String> spriteKeys = spriteMap.keySet();
		for (String key : spriteKeys) {
			reverseLookupSpriteMap.put(getReverseLookupKey(spriteMap.get(key)),
					key);
		}
	}

	protected String getReverseLookupKey(Integer[] xy) {
		return xy[0] + "," + xy[1];
	}

	public String getSelectedSpriteId() {
		return reverseLookupSpriteMap.get(getReverseLookupKey(new Integer[] {
				cursorX, cursorY }));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		cursorX = e.getX() / spriteSize.width;
		cursorY = e.getY() / spriteSize.height;
		repaint();
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
		g2d.drawImage(spriteSheet, 0, 0, spriteSheet.getWidth(),
				spriteSheet.getHeight(), 0, 0, spriteSheet.getWidth(),
				spriteSheet.getHeight(), null);

		// draw the cursor
		g2d.setColor(Color.red);
		g2d.drawRect(cursorX * spriteSize.width, cursorY * spriteSize.height,
				spriteSize.width - 1, spriteSize.height - 1);
	}

}
