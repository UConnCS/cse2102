package co.m1ke.project.util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SteppingIterator<T> {

    private List<T> iterating;
    private BiPredicate<Integer, T> step;

    public SteppingIterator(BiPredicate<Integer, T> step) {
        this.iterating = new ArrayList<>();
        this.step = step;
    }

    @SafeVarargs
    public static <T> SteppingIterator<T> with(BiPredicate<Integer, T> step, T... items) {
        return new SteppingIterator<>(step)
                .iterate(items);
    }

    public static <T> SteppingIterator<T> with(BiPredicate<Integer, T> step, List<T> items) {
        return new SteppingIterator<>(step)
                .iterate(items);
    }

    @SafeVarargs
    public final SteppingIterator<T> iterate(T... items) {
        this.iterating = Arrays.asList(items);
        return this;
    }

    public final SteppingIterator<T> iterate(List<T> items) {
        this.iterating = items;
        return this;
    }

    public SteppingIterator<T> map(Function<T, T> converter) {
        this.iterating = iterating
                .stream()
                .map(converter)
                .collect(Collectors.toList());
        return this;
    }

    public SteppingIterator<T> filter(Predicate<T> filter) {
        this.iterating = iterating
                .stream()
                .filter(filter)
                .collect(Collectors.toList());
        return this;
    }

    public void forEach(BiConsumer<Integer, T> and) {
        for (int i = 0; i < iterating.size(); i++) {
            T item = iterating.get(i);
            if (step.test(i, item)) {
                and.accept(i, item);
            }
        }
    }

}