package org.aksw.simba.owl2nl.qr.tools;

import java.util.Collection;

public interface IApplier<E, T> {

    /**
     * Applies the collection element to the value. Other can be used for any meta-information and is passed by parameter other of mapList(...).
     * @param value Applied value to know.
     * @param collectionElement Collection element to apply.
     * @param other Any other value.
     * @return Applied value.
     */
    T apply(T value, E collectionElement, T other);

    /**
     * Maps a collection via invoke of apply.
     * @param collection Collection to map.
     * @param initValue Init value value to apply.
     * @param other Any other value.
     * @return Applied value.
     */
    default public T mapCollection(Collection<E> collection, T initValue, T other) {
        T val = initValue;
        for (E e: collection) {
            val = apply(val, e, other);
        }
        return val;
    }
}
