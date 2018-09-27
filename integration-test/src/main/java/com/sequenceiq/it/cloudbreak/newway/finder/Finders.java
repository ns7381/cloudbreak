package com.sequenceiq.it.cloudbreak.newway.finder;

import java.util.Collection;

public class Finders {

    public static <T> Finder<T> first() {
        return list -> ((Collection<T>) list).stream().findFirst().orElse(null);
    }

    public static <T> Finder<T> same() {
        return element -> element;
    }
}
