package co.m1ke.project.parser;

import java.util.function.Function;

/**
 * Maps a string to a target type - for use in CsvParser.
 * @param <T> The type to map to.
 */
public class Mapper<T> {

    // The index of the target value in the CSV line
    private final int target;

    // The function to map the string to the target type
    private final Function<String, T> mapper;

    // Whether or not this value should be used when assembling objects in CsvParser
    private final boolean constructorArg;

    // The public constructor for the Mapper class.
    public Mapper(int target, Function<String, T> mapper, boolean constructorArg) {
        this.target = target;
        this.mapper = mapper;
        this.constructorArg = constructorArg;
    }

    // Maps a String -> T
    public T map(String value) {
        return mapper.apply(value);
    }

    // Returns the target index
    public int getTarget() {
        return target;
    }

    // Returns whether or not this value should be used when assembling objects in CsvParser
    public boolean isConstructorArg() {
        return constructorArg;
    }

    // Singleton method for space-saving for use in TYPES arrays in CsvParser
    public static <Q> Mapper<Q> of(int target, Function<String, Q> mapper, boolean constructorArg) {
        return new Mapper<>(target, mapper, constructorArg);
    }

    // Singleton method to link a String to itself. (String -> String)
    public static Function<String, String> link(String value) {
        return val -> value;
    }
}