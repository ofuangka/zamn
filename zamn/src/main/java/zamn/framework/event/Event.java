package zamn.framework.event;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * This implements the Observer pattern in which Event subscribers are notified
 * when this Event is fired
 * 
 * @author ofuangka
 * 
 */
public class Event {

	private static final Logger LOG = Logger.getLogger(Event.class);

	private List<IEventHandler> handlers = new ArrayList<IEventHandler>();
	private final IEventType type;

	public Event(IEventType type) {
		this.type = type;
	}

	public void fire() {
		fire(null);
	}

	public void fire(Object arg) {

		LOG.info("Firing event " + type);

		// TODO: figure out a way to apply a hierarchy to event handlers in
		// order to have a deterministic order of handler execution
		for (int i = handlers.size(); i > 0; i--) {
			if (!handlers.get(i - 1).handleEvent(this, arg)) {
				break;
			}
		}
	}

	public IEventType getType() {
		return type;
	}

	public void subscribe(IEventHandler handler) {
		handlers.add(handler);
	}
}
