package zamn.ui;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;

import org.apache.log4j.Logger;

/**
 * This class acts as a container for the game Board and any layers that should
 * be shown as part of the game screen. It manages the bounds/size for each
 * layer and routes the key presses to the appropriate layer--the one with the
 * highest z-order that is currently showing (is not empty)
 * 
 * @author ofuangka
 * 
 */
public class GameLayeredPane extends JLayeredPane implements IDelegatingKeySink {
	private static final Logger LOG = Logger.getLogger(GameLayeredPane.class);

	private static final long serialVersionUID = -205137708801737354L;

	private List<ILayer> layers;

	/**
	 * Adds the ILayer objects with an increasing z-order derived from ILayer's
	 * position in the list
	 * 
	 * @param board
	 * @param layers
	 */
	public GameLayeredPane(List<ILayer> layers) {
		LOG.debug("Initializing GameScreen layers...");
		if (layers != null) {

			for (int i = 0; i < layers.size(); i++) {
				ILayer layer = layers.get(i);
				if (JComponent.class.isAssignableFrom(layer.getClass())) {
					add((JComponent) layer, Integer.valueOf(i + 1));
				} else {
					throw new IllegalArgumentException(
							"Expected ILayer to be assignable to JComponent");
				}
			}

			this.layers = layers;

		} else {
			throw new IllegalArgumentException(
					"Required constructor arg cannot be null");
		}
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

	/**
	 * Returns the ILayer with the highest z-order that is not empty
	 */
	public IKeySink getCurrentKeySink() {
		for (int i = 0; i < layers.size(); i++) {
			ILayer layer = layers.get(layers.size() - i - 1);
			if (layer.isVisible() && !layer.isEmpty()) {
				return layer;
			}
		}
		throw new IllegalStateException("Must have at least one layer KeySink");
	}

	@Override
	public boolean isListening() {
		return getCurrentKeySink().isListening();
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
		super.setMinimumSize(preferredSize);
		super.setMaximumSize(preferredSize);

		// set all the layers' bounds
		for (ILayer layer : layers) {
			layer.setBounds(0, 0, preferredSize.width, preferredSize.height);
			layer.setPreferredSize(preferredSize);
		}
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
