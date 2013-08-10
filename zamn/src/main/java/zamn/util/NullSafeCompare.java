package zamn.util;

public class NullSafeCompare {
	public static final int compare(Integer i1, Integer i2) {
		return (i1 == null && i2 == null) ? 0 : i1.compareTo(i2);
	}
}
