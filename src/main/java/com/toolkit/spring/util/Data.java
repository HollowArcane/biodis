package com.toolkit.spring.util;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Data
{
    @SuppressWarnings("unchecked")
    public static <E, T, L> Map<E, T> asMap(List<L> data, Function<L, E> key, Function<L, T> value)
    {
        return Map.ofEntries(data.stream().map(
            item -> Map.entry(key.apply(item), value.apply(item))
        ).toArray(Map.Entry[]::new));
    }
}
