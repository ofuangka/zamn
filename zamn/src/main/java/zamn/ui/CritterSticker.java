package zamn.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import zamn.board.Critter;

public class CritterSticker extends JLabel {

	private static final Color FRIENDLY_COLOR = new Color(0, 0, 255, 50);

	private static final Color HOSTILE_COLOR = new Color(255, 0, 0, 50);
	private static final long serialVersionUID = -8559163820702667647L;

	private Critter critter;

	public CritterSticker(Critter critter) {
		this.critter = critter;
		setIcon(new ImageIcon(critter.getImage()));
		Dimension spriteSize = critter.getSpriteSize();
		setPreferredSize(new Dimension(spriteSize.width, spriteSize.height + 7));
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		Dimension preferredSize = getPreferredSize();

		g2d.setColor((critter.isHostile()) ? HOSTILE_COLOR : FRIENDLY_COLOR);

		g2d.fillRect(1, 1, preferredSize.width - 2, preferredSize.height - 2);

		super.paintComponent(g);

		int currHp = critter.getStat(Critter.Stat.HP);
		int maxHp = critter.getStat(Critter.Stat.MAXHP);
		int hpBarSize = ((preferredSize.width - 4) * currHp) / maxHp;
		int currMp = critter.getStat(Critter.Stat.MP);
		int maxMp = critter.getStat(Critter.Stat.MAXMP);
		int mpBarSize = ((preferredSize.width - 4) * currMp) / maxMp;

		g2d.setColor(Color.red);
		g2d.fillRect(2, preferredSize.height - 6, hpBarSize, 2);

		g2d.setColor(Color.blue);
		g2d.fillRect(2, preferredSize.height - 3, mpBarSize, 2);

	}
}
