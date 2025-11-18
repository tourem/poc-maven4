package com.larbotech.maven4.common.util;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Utilitaires pour les collections
 */
public class CollectionUtil {

    public static <T> boolean isEmpty(Collection<T> collection) {
        return CollectionUtils.isEmpty(collection);
    }

    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return CollectionUtils.isNotEmpty(collection);
    }

    public static <T> List<T> emptyIfNull(List<T> list) {
        return list == null ? Collections.emptyList() : list;
    }

    public static <T> Set<T> emptyIfNull(Set<T> set) {
        return set == null ? Collections.emptySet() : set;
    }

    public static <T> List<List<T>> partition(List<T> list, int size) {
        return Lists.partition(list, size);
    }

    public static <T> List<T> union(Collection<T> col1, Collection<T> col2) {
        return CollectionUtils.union(col1, col2).stream().collect(Collectors.toList());
    }

    public static <T> List<T> intersection(Collection<T> col1, Collection<T> col2) {
        return CollectionUtils.intersection(col1, col2).stream().collect(Collectors.toList());
    }

    public static <T> List<T> subtract(Collection<T> col1, Collection<T> col2) {
        return CollectionUtils.subtract(col1, col2).stream().collect(Collectors.toList());
    }

    public static <T> T getFirst(List<T> list) {
        return isEmpty(list) ? null : list.get(0);
    }

    public static <T> T getLast(List<T> list) {
        return isEmpty(list) ? null : list.get(list.size() - 1);
    }

    public static <T> List<T> reverse(List<T> list) {
        List<T> reversed = new ArrayList<>(list);
        Collections.reverse(reversed);
        return reversed;
    }

    public static <T> List<T> removeDuplicates(List<T> list) {
        return new ArrayList<>(new LinkedHashSet<>(list));
    }
}
