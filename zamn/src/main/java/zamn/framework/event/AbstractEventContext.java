package zamn.framework.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

public abstract class AbstractEventContext implements IEventContext {

	private static final Random random = new Random(System.currentTimeMillis());

	private Map<IEventType, Event> events = new HashMap<IEventType, Event>();

	@Override
	public void fire(IEventType type) {
		fire(type, null);
	}

	@Override
	public void fire(IEventType type, Object arg) {
		getEvent(type).fire(arg);
	}

	protected Event getEvent(IEventType type) {
		if (!events.containsKey(type)) {
			events.put(type, new Event(type));
		}
		return events.get(type);
	}

	public Random getRandom() {
		return random;
	}

	public void on(IEventType type, IEventHandler handler) {
		getEvent(type).subscribe(handler);
		Logger.getLogger(getClass()).debug(handler + " subscribed to " + type);
	}
	
	public void onAll(IEventHandler handler) {
		List<IEventType> types = getAllEventTypes();
		if (types != null) {
			for (IEventType type : types) {
				on(type, handler);
			}
		}
	}

}
