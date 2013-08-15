package zamn.ui;

import java.awt.Dimension;

import zamn.framework.event.IEventContext;

public class GameScreen extends DistributedRoundedSplitPane implements
		IDelegatingKeySink {

	private static final long serialVersionUID = 1535542436102660670L;

	private GameInterface gameInterface;
	private GameLayeredPane gameLayeredPane;

	public GameScreen(IEventContext eventContext,
			GameLayeredPane gameLayeredPane, GameInterface gameInterface,
			Dimension spriteSize, double distribution) {
		super(gameLayeredPane, gameInterface, spriteSize, distribution);
		this.gameLayeredPane = gameLayeredPane;
		this.gameInterface = gameInterface;
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

	@Override
	public boolean isListening() {
		return getCurrentKeySink().isListening();
	}
}
