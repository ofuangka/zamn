package zamn.ui;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JSplitPane;

public class GameScreen extends JSplitPane implements IDelegatingKeySink {

	private static final long serialVersionUID = 1535542436102660670L;

	private double distribution;
	private GameInterface gameInterface;
	private GameLayeredPane gameLayeredPane;
	private Dimension spriteSize;

	public GameScreen(GameLayeredPane gameLayeredPane,
			GameInterface gameInterface, Dimension spriteSize,
			double distribution) {
		super(JSplitPane.HORIZONTAL_SPLIT, gameLayeredPane, gameInterface);
		this.gameLayeredPane = gameLayeredPane;
		this.gameInterface = gameInterface;
		this.spriteSize = spriteSize;
		this.distribution = distribution;
		setOneTouchExpandable(false);
		setDividerSize(0);
		setBorder(BorderFactory.createEmptyBorder());
	}

	@Override
	public void backspace() {
		getCurrentKeySink().backspace();
	}

	@Override
	public void down() {
		getCurrentKeySink().down();

	}

	@Override
	public void enter() {
		getCurrentKeySink().enter();

	}

	@Override
	public void esc() {
		getCurrentKeySink().esc();

	}

	public IKeySink getCurrentKeySink() {
		return gameLayeredPane;
	}

	@Override
	public void left() {
		getCurrentKeySink().left();

	}

	@Override
	public void right() {
		getCurrentKeySink().right();

	}

	@Override
	public void setPreferredSize(Dimension preferredSize) {
		super.setPreferredSize(preferredSize);
		gameLayeredPane.setPreferredSize(new Dimension(
				(int) (preferredSize.width * distribution / spriteSize.width)
						* spriteSize.width, preferredSize.height));
		gameInterface.setPreferredSize(new Dimension(preferredSize.width
				- gameLayeredPane.getPreferredSize().width,
				preferredSize.height));
	}

	@Override
	public void space() {
		getCurrentKeySink().space();

	}

	@Override
	public void up() {
		getCurrentKeySink().up();

	}

	@Override
	public void x() {
		getCurrentKeySink().x();

	}
}
