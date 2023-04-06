package co.m1ke.project.util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A simple iterator that allows you to iterate over a list of items, and perform actions on each item.
 * @param <T> The type of object to iterate over.
 */
public class SteppingIterator<T> {

    private List<T> iterating;
    private BiPredicate<Integer, T> step;

    /**
     * Create a new SteppingIterator.
     * @param step A BiPredicate that takes the index of the item, and the item itself. If the predicate returns true, the item will be iterated over.
     */
    public SteppingIterator(BiPredicate<Integer, T> step) {
        this.iterating = new ArrayList<>();
        this.step = step;
    }

    @SafeVarargs
    /**
     * Create a new SteppingIterator, and iterate over the given items.
     */
    public static <T> SteppingIterator<T> with(BiPredicate<Integer, T> step, T... items) {
        return new SteppingIterator<>(step)
                .iterate(items);
    }

    /**
     * Create a new SteppingIterator, and iterate over the given items.
     * @param step A BiPredicate that takes the index of the item, and the item itself. If the predicate returns true, the item will be iterated over.
     * @param items The items to iterate over.
     * @param <T> The type of object to iterate over.
     *
     * @return A new SteppingIterator.
     */
    public static <T> SteppingIterator<T> with(BiPredicate<Integer, T> step, List<T> items) {
        return new SteppingIterator<>(step)
                .iterate(items);
    }

    @SafeVarargs
    /**
     * Iterate over the given items.
     * @param items The items to iterate over.
     * @return The SteppingIterator.
     */
    public final SteppingIterator<T> iterate(T... items) {
        this.iterating = Arrays.asList(items);
        return this;
    }

    /**
     * Iterate over the given items.
     * @param items The items to iterate over.
     * @return The SteppingIterator.
     */
    public final SteppingIterator<T> iterate(List<T> items) {
        this.iterating = items;
        return this;
    }

    /**
     * Map the items in the iterator.
     * @param converter The function to convert the items.
     * @return The SteppingIterator.
     */
    public SteppingIterator<T> map(Function<T, T> converter) {
        this.iterating = iterating
                .stream()
                .map(converter)
                .collect(Collectors.toList());
        return this;
    }

    /**
     * Filter the items in the iterator.
     * @param filter The predicate to filter the items.
     * @return The SteppingIterator.
     */
    public SteppingIterator<T> filter(Predicate<T> filter) {
        this.iterating = iterating
                .stream()
                .filter(filter)
                .collect(Collectors.toList());
        return this;
    }

    /**
     * Iterate over the items in the iterator.
     * @param and The BiConsumer to perform on each item.
     */
    public void forEach(BiConsumer<Integer, T> and) {
        for (int i = 0; i < iterating.size(); i++) {
            T item = iterating.get(i);
            if (step.test(i, item)) {
                and.accept(i, item);
            }
        }
    }

}