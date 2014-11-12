package be.bendem.bendembot.utils;

import java.util.*;
import java.util.function.BiConsumer;

public class MultiMap<K, V> {

    private final Map<K, List<V>> map;

    public MultiMap() {
        this.map = new HashMap<>();
    }

    public void put(K key, V value) {
        map.computeIfAbsent(key, k -> new LinkedList<>()).add(value);
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public boolean containsValue(V value) {
        return map.values().stream().anyMatch(values -> values.contains(value));
    }

    public void removeKey(K key) {
        map.remove(key);
    }

    public void removeValue(V value) {
        Iterator<List<V>> iterator = map.values().iterator();
        while(iterator.hasNext()) {
            Collection<V> cur = iterator.next();
            if(cur.contains(value)) {
                cur.remove(value);
            }
            if(cur.isEmpty()) {
                iterator.remove();
            }
        }
    }

    public List<V> get(K key) {
        return Collections.unmodifiableList(new ArrayList<>(map.get(key)));
    }

    public V getFirst(K key) {
        if(!map.containsKey(key)) {
            return null;
        }
        return map.get(key).get(0);
    }

    public void forEach(BiConsumer<K, Collection<V>> consumer) {
        map.forEach(consumer);
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public void clear() {
        map.clear();
    }

}
