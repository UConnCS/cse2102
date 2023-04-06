package co.m1ke.project.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A simple key-value storage wrapper around a HashMap. To be used in conjunction with a StorageCluster, which stores
 * multiple different types of these Storage shards. This class is generic, and can be used to store any type of object.
 *
 * @param <T> The type of object to store.
 */
public class Storage<T> {

    // The private HashMap that stores the key-value pairs.
    private Map<String, T> stash;

    // The public constructor for the Storage class.
    public Storage() {
        this.stash = new HashMap<>();
    }

    /**
     * Add a key-value pair to the Storage.
     * @param key The key to store the value under.
     * @param value The value to store.
     */
    public void add(String key, T value) {
        this.stash.put(key, value);
    }

    /**
     * Get a value from the Storage.
     * @param key The key to get the value from.
     * @return The value stored under the given key.
     */
    public T get(String key) {
        return this.stash.get(key);
    }

    /**
     * Remove a key-value pair from the Storage.
     * @param key The key to remove the value from.
     */
    public void remove(String key) {
        this.stash.remove(key);
    }

    /**
     * Check if the Storage contains a key.
     * @param key The key to check for.
     * @return Whether or not the Storage contains the given key.
     */
    public boolean contains(String key) {
        return this.stash.containsKey(key);
    }

    /**
     * Get the size of the Storage.
     * @return The size of the Storage.
     */
    public int size() {
        return this.stash.size();
    }

    // Flushes the storage.
    public void clear() {
        this.stash.clear();
    }

    /**
     * Get a stream of the key-value pairs in the Storage.
     * @return A stream of the key-value pairs in the Storage.
     */
    public Stream<Map.Entry<String, T>> stream() {
        return this.stash.entrySet().stream();
    }

    /**
     * Search the Storage for a value that matches the given predicate.
     * @param predicate The predicate to search for.
     * @return A list of values that match the given predicate.
     */
    public List<T> search(Predicate<T> predicate) {
        return this
                .streamValues()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * Get a stream of the keys in the Storage.
     * @return A stream of the keys in the Storage.
     */
    public Stream<String> streamKeys() {
        return this.stash.keySet().stream();
    }

    /**
     * Get a stream of the values in the Storage.
     * @return A stream of the values in the Storage.
     */
    public Stream<T> streamValues() {
        return this.stash.values().stream();
    }

}
