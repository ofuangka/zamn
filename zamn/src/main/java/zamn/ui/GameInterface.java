package zamn.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import zamn.board.piece.Critter;
import zamn.framework.event.Event;
import zamn.framework.event.GameEventContext;
import zamn.framework.event.IEventContext;
import zamn.framework.event.IEventHandler;

public class GameInterface extends JScrollPane implements IEventHandler {

	private static final long serialVersionUID = 4716145778679751281L;

	private Map<Critter, CritterSticker> items = new HashMap<Critter, CritterSticker>();

	private JLabel spacer = new JLabel();

	public GameInterface(IEventContext eventContext) {
		super(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setViewportView(new JPanel(new GridBagLayout()));
		eventContext.onAll(this);
	}

	public void addCritter(Critter critter) {
		CritterSticker critterItem = new CritterSticker(critter);
		items.put(critter, critterItem);
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = items.size();
		getView().add(critterItem, gc);
		refreshSpacer();
	}

	public void removeCritter(Critter critter) {
		getView().remove(items.remove(critter));
	}

	public void clearCritters() {
		Set<Critter> critters = items.keySet();
		for (Critter critter : critters) {
			getView().remove(items.get(critter));
		}
		items.clear();
	}

	protected JPanel getView() {
		return ((JPanel) getViewport().getView());
	}

	protected void refreshSpacer() {
		getView().remove(spacer);
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = items.size() + 1;
		gc.weighty = 1;
		getView().add(spacer, gc);
	}

	@Override
	public boolean handleEvent(Event event, Object arg) {
		switch ((GameEventContext.GameEventType) event.getType()) {
		case CRITTER_ADDED_TO_BOARD: {
			addCritter((Critter) arg);
			break;
		}
		case CRITTER_DEATH: {
			removeCritter((Critter) arg);
			break;
		}
		case LOCATION_CHANGE: {
			clearCritters();
			break;
		}
		case END_OF_TURN: {
			repaint();
			break;
		}
		default: {
			break;
		}
		}
		return true;
	}

}
