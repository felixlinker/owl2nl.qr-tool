package org.aksw.simba.owl2nl.qr.data;

import java.util.LinkedList;
import java.util.List;

public interface ListConverter<S, T> {
    abstract T apply(S s);

    default List<T> map(List<S> list) {
        List<T> mapped = new LinkedList<>();
        for (S element: list) {
            mapped.add(apply(element));
        }

        return mapped;
    }
}
