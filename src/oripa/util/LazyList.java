package oripa.util;

import java.util.List;
import java.util.RandomAccess;

/**
 * Lazy methods which do not keep the order.
 * @author koji
 *
 */
public interface LazyList<E> extends List<E>, RandomAccess{
	
	/**
	 * The item at the index will be removed.
	 * This method does not keep the order of the list.
	 * @param index
	 * @return next index
	 */
	public int lazyRemove(int index);
}
