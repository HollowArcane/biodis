package com.toolkit.spring.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;

public class Groups<E, T>
{
    public Map<E, T> items;
    private int nestedSize; // unused, just to trigger json serialization of getNestedSize()

    public Groups()
    { items = new HashMap<>(); }

    public Set<Entry<E, T>> getEntries()
    { return items.entrySet(); }

    public T get(E item)
    { return items.get(item); }

    private void put(E key, T value)
    { items.put(key, value); }

    /**
     * @param <P> the type of the elements to group
     * @param items the elements to group
     * @param by: a getter which return the data by which the given items will be grouped
     * 
     * Note that this method sort items in the list before grouping them
     * so no need to manually presort them
    * @return an object which groups those elements
    */
    public static <P, Q extends Comparable<Q>> Groups<Q, List<P>> group(List<P> items, Function<P, Q> by)
    {
        Objects.requireNonNull(items);
        Objects.requireNonNull(by);
        items = new ArrayList<>(items);

        items.sort((a, b) -> by.apply(a).compareTo(by.apply(b)));
        Q currentGrouper = null;
        List<P> currentGroup = new ArrayList<>();

        Groups<Q, List<P>> group = new Groups<>();
        for(P item: items)
        {
            Q groupData = by.apply(item);
            if(currentGrouper == null)
            { currentGrouper = groupData; }

            if(!currentGrouper.equals(groupData))
            {
                // pack current group
                group.put(currentGrouper, currentGroup);
                // reset for next group
                currentGrouper = groupData;
                currentGroup = new ArrayList<>();
            }
            currentGroup.add(item);
        }
        // pack last group
        group.put(currentGrouper, currentGroup);
        return group;
    }
    
    
    /**
     * @param <R>
     * @param mapper
     * 
     * Returns new groups by applying given mapper on each of the items
     * inside these groups
    * @return 
    */
    public <R> Groups<E, R> then(Function<T, R> mapper)
    {
        Groups<E, R> newGroups = new Groups<>();
        for(Entry<E, T> entry: items.entrySet())
        { newGroups.put(entry.getKey(), mapper.apply(entry.getValue())); }
        
        return newGroups;
    }


    /**
     * @return the numbers of groups contained by this object
     */
    public int getSize()
    { return items.size(); }

    /**
     * @return the sum of the size of the deepest ungrouped elements
     * 
     * This method runs recursively while the contained items
     * are still groups, else 1
     */
    @SuppressWarnings("rawtypes")
    public int getNestedSize()
    {
        int sum = 0;
        for(Entry<E, T> item: items.entrySet())
        {
            if(item.getValue() instanceof Groups)
            { sum += ((Groups)item.getValue()).getNestedSize(); }
            else
            { sum++; }
        }
        return sum;
    }
}
