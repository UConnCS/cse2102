package co.m1ke.project.storage;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * This class is an adapter for Storage shards, and dynamically creates them based on the class of the object being stored.
 *
 * This allows for a single StorageCluster to store multiple different types of objects, and allows for better dev-UX
 * when storing different types of Watchables for example.
 */
public class StorageCluster {

    // The private HashMap that stores the key-value pairs.
    private Map<Class<?>, Storage<?>> shards;

    // The public constructor for the Storage class.
    public StorageCluster() {
        this.shards = new HashMap<>();
    }

    /**
     * Register a new Storage shard for a given class.
     * @param clazz The class to register a new Storage shard for.
     * @param <T> The type of object to store.
     */
    public <T> void register(Class<?> clazz) {
        this.shards.put(clazz, new Storage<T>());
    }

    /**
     * Register a new Storage shard for a given class.
     * @param classes The classes to register a new Storage shard for.
     */
    public void register(Class<?>... classes) {
        Arrays
                .stream(classes)
                .forEach(this::register);
    }

    /**
     * Adds a single object to the StorageCluster by first finding the appropriate
     * registered Shard for the class of the object, and then by using reflection,
     * serializing the object using the given key field string.
     *
     * @param object The object to add to the StorageCluster.
     * @param keyField The field to serialize the object by.
     * @param <T> The type of object to store.
     */
    public <T> void add(T object, String keyField) {
        List<T> arr = Arrays.asList(object);
        this.addMany(arr, keyField);
    }

    /**
     * Adds a list of objects to the StorageCluster by first
     * finding the appropriate registered Shard for the class of the object,
     * and then by using reflection, serializing the object(s) using the
     * given key field string.
     *
     * @param objects The list of objects to add to the StorageCluster.
     * @param keyField The field to serialize the object(s) by.
     * @param <T> The type of object to store.
     */
    public <T> void addMany(List<T> objects, String keyField) {
        if (objects.size() == 0) {
            throw new IllegalArgumentException("Cannot add empty list of objects to storage cluster.");
        }

        Storage<T> storage = this.getCluster((Class<T>) objects.get(0).getClass());
        if (storage == null) {
            throw new IllegalArgumentException("No storage cluster found for class " + objects.get(0).getClass().getName());
        }

        try {
            Class<T> clazz = (Class<T>) objects.get(0).getClass();
            Field field = clazz.getSuperclass().getDeclaredField(keyField);
            field.setAccessible(true);

            if (!field.getType().equals(String.class)) {
                throw new IllegalArgumentException("Field " + keyField + " is not a string, and thus cannot be used to serialize Storage children.");
            }

            for (T object : objects) {
                storage.add((String) field.get(object), object);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Removes a key from all instances of the cluster.
     * @param key The key to remove.
     */
    public void remove(String key) {
        this.shards.forEach((clazz, shard) -> shard.remove(key));
    }

    /**
     * Get an object from the StorageCluster by first finding the appropriate
     * registered Shard for the class of the object, and then by using reflection,
     * serializing the object using the given key field string.
     *
     * @param target The class of the object to get.
     * @param predicate The predicate to use to find the object.
     * @param <T> The type of object to store.
     *
     * @return The object that was found, or null if none was found.
     */
    public <T> List<T> search(Class<T> target, Predicate<T> predicate) {
        Storage<T> cluster = this.getCluster(target);
        if (cluster == null) {
            return null;
        }

        return cluster.search(predicate);
    }

    /**
     * Get a Storage shard for a given class.
     * @param clazz The class to get the Storage shard for.
     * @param <T> The type of object to store.
     * @return The Storage shard for the given class.
     */
    public <T> Storage<T> getCluster(Class<T> clazz) {
        return (Storage<T>) this.shards.get(clazz);
    }

    /**
     * Downlevels an abstractive layer and recombines all shards of a given type into a single Storage.
     * @param clazzes The classes to de-abstractify.
     * @return The Storage containing all objects of the given classes.
     * @param <M> The type of object to store.
     */
    public <M> Storage<M> abstractify(Class<?> ...clazzes) {
        Storage<M> storage = new Storage<>();
        for (Class<?> clazz : clazzes) {
            Storage<?> shard = this.getCluster(clazz);
            if (shard == null) {
                continue;
            }

            shard.stream().forEach(ent -> storage.add(ent.getKey(), (M) ent.getValue()));
        }

        return storage;
    }

}
