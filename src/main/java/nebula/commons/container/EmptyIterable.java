package nebula.commons.container;

import java.util.Iterator;

public class EmptyIterable {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Iterable<T> getInstance() {
		return (Iterable<T>) new Iterable() {
			@Override
			public Iterator iterator() {
				return EmptyIterator.getInstance();
			}
		};
	}

}
