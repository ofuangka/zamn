package zamn.framework.event;

import java.util.List;
import java.util.Random;

public interface IEventContext {

	void fire(IEventType type);

	void fire(IEventType type, Object arg);

	List<IEventType> getAllEventTypes();

	Random getRandom();

	void on(IEventType type, IEventHandler handler);

	void onAll(IEventHandler handler);
}
