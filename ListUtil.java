import java.util.*;
import java.util.stream.Collectors;

/**
 * The methods are implemented referring to the methods of Array in ECMAScript.
 */
public enum ListUtil {;

    public interface Predicate<T> {
        /**
         * @param element The current element being processed in the list.
         * @param index The index of the current element being processed in the list.
         * @param list The list the outer method was called upon.
         * @return <b>true</b> if the input arguments matches the predicate, otherwise <b>false</b>
         */
        boolean test(T element, int index, List<? extends T> list);
    }

    public interface Consumer<T> {
        /**
         * @param element The current element being processed in the list.
         * @param index The index of the current element being processed in the list.
         * @param list The list the outer method was called upon.
         */
        void accept(T element, int index, List<? extends T> list);
    }

    public interface Mapper<T, R> {
        /**
         * @param element The current element being processed in the list.
         * @param index The index of the current element being processed in the list.
         * @param list The list the outer method was called upon.
         * @return The new object.
         */
        R apply(T element, int index, List<? extends T> list);
    }

    public interface Reducer<T, R> {
        /**
         * @param accumulator The accumulator accumulates callback's return values. It is the accumulated value previously returned in the last invocation of the callback—or <b>initialValue</b>, if it was supplied.
         * @param element The current element being processed in the list.
         * @param index The index of the current element being processed in the list. Starts from index <b>0</b> if an <b>initialValue</b> is provided. Otherwise, it starts from index <b>1</b>.
         * @param list The list the outer method was called upon.
         * @return The new accumulator value.
         */
        R apply(R accumulator, T element, int index, List<? extends T> list);
    }

    /**
     * Used to merge two or more lists. This method does not change the existing lists, but instead returns a new list.
     * @param list List to be concatenated
     * @param lists Lists to concatenate into a new list. If this parameter is omitted, concat returns a shallow copy of the existing list on which it is called.
     * @return A new <b>List</b> instance.
     */
    @SafeVarargs
    public static <T> List<T> concat(List<T> list, Collection<? extends T>... lists) {
        List<T> result = new ArrayList<>(list);
        for (Collection<? extends T> l : lists)
            result.addAll(l);
        return result;
    }

    /**
     * Used to append two or more elements into a list. This method does not change the existing lists, but instead returns a new list.
     * @param list List to be concatenated
     * @param items Values to concatenate into a new list. If this parameter is omitted, concat returns a shallow copy of the existing list on which it is called.
     * @return A new <b>List</b> instance.
     */
    @SafeVarargs
    public static <T> List<T> concat(List<T> list, T... items) {
        List<T> result = new ArrayList<>(list);
        result.addAll(Arrays.asList(items));
        return result;
    }

    /**
     * Shallow copies part of a list, from a start index (default <b>0</b>) to an end index (default <b>list.size()</b>), to another location in the same list and returns it without modifying its size.
     * @param list List to be modified.
     * @param target Zero-based index at which to copy the sequence to. If negative, <b>target</b> will be counted from the end.
     * @return The modified list.
     */
    public static <T> List<T> copyWithin(List<T> list, int target) {
        Objects.requireNonNull(list);
        if (target < 0)
            target %= list.size();
        else if (target >= list.size())
            return list;
        if (target == 0)
            return list;
        int start = 0;
        for (int i = list.size() - target + start - 1; i >= start; i--)
            list.set(target + i - start, list.get(i));
        return list;
    }

    /**
     * Shallow copies part of a list, from a start index (default <b>0</b>) to an end index (default <b>list.size()</b>), to another location in the same list and returns it without modifying its size.
     * @param list List to be modified.
     * @param target Zero-based index at which to copy the sequence to. If negative, <b>target</b> will be counted from the end.
     * @param start Zero-based index at which to start copying elements from. If negative, <b>start</b> will be counted from the end.
     * @return The modified list.
     */
    public static <T> List<T> copyWithin(List<T> list, int target, int start) {
        Objects.requireNonNull(list);
        if (target < 0)
            target %= list.size();
        else if (target >= list.size())
            return list;
        if (start < 0) {
            start += list.size();
            if (start < 0)
                start = 0;
        } else if (start >= list.size())
            return list;
        if (target == start)
            return list;
        if (start > target)
            for (int i = start; i < list.size(); i++)
                list.set(target++, list.get(i));
        else
            for (int i = list.size() - target + start - 1; i >= start; i--)
                list.set(target + i - start, list.get(i));
        return list;
    }

    /**
     * Shallow copies part of a list, from a start index (default <b>0</b>) to an end index (default <b>list.size()</b>), to another location in the same list and returns it without modifying its size.
     * @param list List to be modified.
     * @param target Zero-based index at which to copy the sequence to. If negative, <b>target</b> will be counted from the end.
     * @param start Zero-based index at which to start copying elements from. If negative, <b>start</b> will be counted from the end.
     * @param end Zero-based index at which to end copying elements from. copyWithin copies up to but not including <b>end</b>. If negative, <b>end</b> will be counted from the end.
     * @return The modified list.
     */
    public static <T> List<T> copyWithin(List<T> list, int target, int start, int end) {
        Objects.requireNonNull(list);
        if (target < 0)
            target %= list.size();
        else if (target >= list.size())
            return list;
        if (start < 0) {
            start += list.size();
            if (start < 0)
                start = 0;
        }
        if (end < 0)
            end += list.size();
        else if (end > list.size())
            end = list.size();
        if (end <= start)
            return list;
        if (target == start)
            return list;
        if (start > target)
            for (int i = start; i < end; i++)
                list.set(target++, list.get(i));
        else {
            if (target + end - start > list.size())
                end = list.size() - target + start;
            for (int i = end - 1; i >= start; i--)
                list.set(target + i - start, list.get(i));
        }
        return list;
    }

    /**
     * Tests whether all elements in the list pass the test implemented by the provided function. It returns a <b>Boolean</b> value.
     * @param list List under test.
     * @param predicate A function to test for each element, taking three arguments:
     * <ul>
     *     <li>element - The current element being processed in the list.</li>
     *     <li>index - The index of the current element being processed in the list.</li>
     *     <li>list - The list <b>every()</b> was called upon.</li>
     * </ul>
     * @return <b>true</b> if the predicate function returns a truthy value for every list element. Otherwise, <b>false</b>.
     */
    public static <T> boolean every(List<T> list, Predicate<? super T> predicate) {
        Objects.requireNonNull(list);
        for (int i = 0; i < list.size(); i++)
            if (!predicate.test(list.get(i), i, list))
                return false;
        return true;
    }

    /**
     * Changes all elements in a list to a static value, from a start index (default <b>0</b>) to an end index (default <b>list.size()</b>). It returns the modified list.
     * @param list List to be modified.
     * @param item Item to fill the list with. (Note all elements in the list will be this exact item.)
     * @return The modified list, filled with item.
     */
    public static <T> List<T> fill(List<T> list, T item) {
        int size = list.size();
        list.clear();
        for (int i = 0; i < size; i++)
            list.add(item);
        return list;
    }

    /**
     * Changes all elements in a list to a static value, from a start index (default <b>0</b>) to an end index (default <b>list.size()</b>). It returns the modified list.
     * @param list List to be modified.
     * @param item Item to fill the list with. (Note all elements in the list will be this exact item.)
     * @param start Start index, default <b>0</b>. If start is negative, it is treated as <b>list.size() + start</b>.
     * @return The modified list, filled with item.
     */
    public static <T> List<T> fill(List<T> list, T item, int start) {
        Objects.requireNonNull(list);
        if (start < 0) {
            start += list.size();
            if (start < 0)
                start = 0;
        } else if (start >= list.size())
            return list;
        for (int i = start; i < list.size(); i++)
            list.set(i, item);
        return list;
    }

    /**
     * Changes all elements in a list to a static value, from a start index (default <b>0</b>) to an end index (default <b>list.size()</b>). It returns the modified list.
     * @param list List to be modified.
     * @param item Item to fill the list with. (Note all elements in the list will be this exact item.)
     * @param start Start index, default <b>0</b>. If start is negative, it is treated as <b>list.size() + start</b>
     * @param end End index, default <b>list.size()</b>. If end is negative, it is treated as <b>list.size() + end</b>.
     * @return The modified list, filled with <b>item</b>.
     */
    public static <T> List<T> fill(List<T> list, T item, int start, int end) {
        Objects.requireNonNull(list);
        if (start < 0) {
            start += list.size();
            if (start < 0)
                start = 0;
        }
        if (end < 0)
            end += list.size();
        else if (end > list.size())
            end = list.size();
        if (end <= start)
            return list;
        for (int i = start; i < end; i++)
            list.set(i, item);
        return list;
    }

    /**
     * Creates a new list with all elements that pass the test implemented by the provided function.
     * @param list List to be filtered.
     * @param predicate Predicate to test each element of the array. Return a value that coerces to <b>true</b> to keep the element, or to <b>false</b> otherwise. It accepts three arguments:
     * <ul>
     *     <li>element - The current element being processed in the list.</li>
     *     <li>index - The index of the current element being processed in the list.</li>
     *     <li>list - The list <b>filter()</b> was called upon.</li>
     * </ul>
     * @return A new <b>List</b> instance with the elements that pass the test. If no elements pass the test, an empty array will be returned.
     */
    public static <T> List<T> filter(List<T> list, Predicate<? super T> predicate) {
        Objects.requireNonNull(list);
        List<T> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
            if (predicate.test(list.get(i), i, list))
                result.add(list.get(i));
        return result;
    }

    /**
     * Returns the value of the first element in the provided list that satisfies the provided testing function. If no values satisfy the testing function, <b>null</b> is returned.
     * @param list List under search.
     * @param predicate Function to execute on each value in the array, taking 3 arguments:
     * <ul>
     *     <li>element - The current element being processed in the list.</li>
     *     <li>index - The index of the current element being processed in the list.</li>
     *     <li>list - The list <b>find()</b> was called upon.</li>
     * </ul>
     * @return The value of the first element in the list that satisfies the provided testing function. Otherwise, <b>null</b> is returned.
     */
    public static <T> T find(List<T> list, Predicate<? super T> predicate) {
        Objects.requireNonNull(list);
        for (int i = 0; i < list.size(); i++)
            if (predicate.test(list.get(i), i, list))
                return list.get(i);
        return null;
    }

    /**
     * Returns the index of the first element in the list that satisfies the provided testing function. Otherwise, it returns <b>-1</b>, indicating that no element passed the test.
     * @param list List under search.
     * @param predicate A function to execute on each value in the array until the function returns true, indicating that the satisfying element was found. It takes three arguments:
     * <ul>
     *     <li>element - The current element being processed in the list.</li>
     *     <li>index - The index of the current element being processed in the list.</li>
     *     <li>list - The list <b>findIndex()</b> was called upon.</li>
     * </ul>
     * @return The index of the first element in the list that passes the test. Otherwise, <b>-1</b>.
     */
    public static <T> int findIndex(List<T> list, Predicate<? super T> predicate) {
        Objects.requireNonNull(list);
        for (int i = 0; i < list.size(); i++)
            if (predicate.test(list.get(i), i, list))
                return i;
        return -1;
    }

    /**
     * Executes a provided function once for each list element.
     * @param list List to be iterated.
     * @param consumer Function to execute on each element. It accepts three arguments:
     * <ul>
     *     <li>element - The current element being processed in the list.</li>
     *     <li>index - The index of the current element being processed in the list.</li>
     *     <li>list - The list <b>forEach()</b> was called upon.</li>
     * </ul>
     */
    public static <T> void forEach(List<T> list, Consumer<? super T> consumer) {
        Objects.requireNonNull(list);
        for (int i = 0; i < list.size(); i++)
            consumer.accept(list.get(i), i, list);
    }

    /**
     * Creates a new, shallow-copied <b>List</b> instance from a collection.
     * @deprecated Please use <b>new ArrayList<>(collection)</b> directly.
     */
    @Deprecated
    public static <T> List<T> from(Collection<? extends T> collection) {
        return new ArrayList<>(collection);
    }

    /**
     * Equivalent to <b>map(new ArrayList<T>(collection), mapper)</b>.
     * @param collection A collection to convert to a list.
     * @param mapper Map function to call on every element of the list.
     * @return A new <b>List</b> instance.
     */
    public static <T,R> List<R> from(Collection<? extends T> collection, Mapper<? super T, ? extends R> mapper) {
        List<R> result = new ArrayList<>(collection.size());
        List<T> cList = new ArrayList<>(collection);
        forEach(cList, (element, index, list) -> result.set(index, mapper.apply(element, index, list)));
        return result;
    }

    /**
     * Determines whether a list includes a certain value among its entries, returning true or false as appropriate.
     * @deprecated Please use <b>list.contains(item)</b> directly.
     */
    @Deprecated
    public static <T> boolean includes(List<T> list, T item) {
        return list.contains(item);
    }

    /**
     * Determines whether a list includes a certain value among its entries, returning true or false as appropriate.
     * @param list List under search.
     * @param item The value to search for.
     * @param fromIndex The position in this list at which to begin searching for <b>list</b>. The first element to be searched is found at <b>fromIndex</b> for positive values of <b>fromIndex</b>, or at <b>list.size() + fromIndex</b> for negative values of <b>fromIndex</b>.
     * @return A <b>Boolean</b> which is true if the value valueToFind is found within the part of the list indicated by the index <b>fromIndex</b>.
     */
    public static <T> boolean includes(List<T> list, T item, int fromIndex) {
        if (fromIndex >= list.size())
            return false;
        if (fromIndex < 0) {
            fromIndex += list.size();
            if (fromIndex < 0)
                fromIndex = 0;
        }
        return list.indexOf(item) >= fromIndex;
    }

    /**
     * Returns the first index at which a given element can be found in the list, or -1 if it is not present.
     * @deprecated Please use <b>list.indexOf(item)</b> directly.
     */
    @Deprecated
    public static <T> int indexOf(List<T> list, T item) {
        return list.indexOf(item);
    }

    /**
     * Returns the first index at which a given element can be found in the list, or -1 if it is not present.
     * @param list List under search.
     * @param item Element to locate in the list.
     * @param fromIndex The index to start the search at. If the index is greater than or equal to <b>list.size()</b>, -1 is returned, which means the list will not be searched. If the provided index value is a negative number, it is taken as the offset from the end of the list. Note: if the provided index is negative, the list is still searched from front to back. If the provided index is 0, then the whole list will be searched.
     * @return The first index of the element in the list; <b>-1</b> if not found.
     */
    public static <T> int indexOf(List<T> list, T item, int fromIndex) {
        if (fromIndex >= list.size())
            return -1;
        if (fromIndex < 0) {
            fromIndex += list.size();
            if (fromIndex < 0)
                fromIndex = 0;
        }
        for (int i = fromIndex; i < list.size(); i++)
            if (list.get(i).equals(item))
                return i;
        return -1;
    }

    /**
     * Creates and returns a new string by concatenating all of the elements in a list, separated by commas or a specified separator string. If the list has only one item, then that item will be returned without using the separator.
     * @param list List of elements to be joined.
     * @return A string with all list elements joined. If <b>list.size()</b> is 0, the empty string is returned.
     */
    public static <T> String join(List<T> list) {
        return join(list, ",");
    }

    /**
     * Creates and returns a new string by concatenating all of the elements in a list, separated by commas or a specified separator string. If the list has only one item, then that item will be returned without using the separator.
     * @param list List of elements to be joined.
     * @param separator Specifies a string to separate each pair of adjacent elements of the list.
     * @return A string with all list elements joined. If <b>list.size()</b> is 0, the empty string is returned.
     */
    public static <T> String join(List<T> list, String separator) {
        if (list.isEmpty())
            return "";
        StringBuilder builder;
        if (list.get(0) != null)
            builder = new StringBuilder(list.get(0).toString());
        else
            builder = new StringBuilder();
        for (int i = 1; i < list.size(); i++) {
            builder.append(separator);
            if (list.get(i) != null)
                builder.append(list.get(i).toString());
        }
        return builder.toString();
    }

    /**
     * Returns the last index at which a given element can be found in the list, or -1 if it is not present.
     * @deprecated Please use <b>list.lastIndexOf(item)</b> directly.
     */
    @Deprecated
    public static <T> int lastIndexOf(List<T> list, T item) {
        return list.lastIndexOf(item);
    }

    /**
     * Returns the last index at which a given element can be found in the list, or -1 if it is not present. The list is searched backwards, starting at <b>fromIndex</b>.
     * @param list List under search.
     * @param item Element to locate in the list.
     * @param fromIndex The index at which to start searching backwards. If the index is greater than or equal to the size of the list, the whole list will be searched. If negative, it is taken as the offset from the end of the list. Note that even when the index is negative, the list is still searched from back to front. If the calculated index is less than 0, -1 is returned, i.e. the list will not be searched.
     * @return The last index of the element in the list; -1 if not found.
     */
    public static <T> int lastIndexOf(List<T> list, T item, int fromIndex) {
        if (fromIndex >= list.size())
            fromIndex = list.size() - 1;
        if (fromIndex < 0) {
            fromIndex += list.size();
            if (fromIndex < 0)
                return -1;
        }
        for (int i = fromIndex; i >= 0; i--)
            if (list.get(i).equals(item))
                return i;
        return -1;
    }

    /**
     * Creates a new list populated with the results of calling a provided function on every element in the calling list.
     * @param list List to be mapped.
     * @param mapper Function that is called for every element of <b>list</b>. Each time callback executes, the returned value is added to the new list. The <b>mapper</b> function accepts the following arguments:
     * <ul>
     *     <li>element - The current element being processed in the list.</li>
     *     <li>index - The index of the current element being processed in the list.</li>
     *     <li>list - The list <b>map()</b> was called upon.</li>
     * </ul>
     * @return A new list with each element being the result of the callback function.
     */
    public static <T,R> List<R> map(List<T> list, Mapper<? super T, ? extends R> mapper) {
        List<R> result = new ArrayList<>(list.size());
        forEach(list, (element, index, list1) -> result.set(index, mapper.apply(element, index, list1)));
        return result;
    }

    /**
     * Creates a new <b>List</b> instance from a variable number of arguments, regardless of number or type of the arguments.
     * @param items Elements used to create the list.
     * @return A new <b>List</b> instance.
     */
    @SafeVarargs
    public static <T> List<T> of(T... items) {
        return Arrays.asList(items);
    }

    /**
     * Removes the last element from a list and returns that element. This method changes the size of the list.
     * @param list List to be modified.
     * @return The removed element from the list; <b>null</b> if the list is empty.
     */
    public static <T> T pop(List<T> list) {
        if (list.isEmpty())
            return null;
        return list.remove(list.size() - 1);
    }

    /**
     * Adds one or more elements to the end of a list and returns the new size of the list.
     * @param list List to be modified.
     * @param items The element(s) to add to the end of the list.
     * @return The new size of the list.
     */
    @SafeVarargs
    public static <T> int push(List<T> list, T... items) {
        list.addAll(Arrays.asList(items));
        return list.size();
    }

    /**
     * Executes a reducer function (that you provide) on each element of the list, resulting in single output value.
     * @param list Source list.
     * @param reducer A function to execute on each element in the list except for the first. It takes four arguments:
     * <ul>
     *     <li>accumulator - The accumulator accumulates callback's return values. It is the accumulated value previously returned in the last invocation of the callback.</li>
     *     <li>element - The current element being processed in the list.</li>
     *     <li>index - The index of the current element being processed in the list. Starts from index <b>1</b>.</li>
     *     <li>list - The list <b>reduce()</b> was called upon.</li>
     * </ul>
     * @return The single value that results from the reduction.
     * @throws UnsupportedOperationException If <b>list.isEmpty()</b>.
     */
    public static <T> T reduce(List<T> list, Reducer<? super T, T> reducer) {
        if (list.isEmpty())
            throw new UnsupportedOperationException("reduce() could not be performed with an empty list without initial value.");
        T accumulator = list.get(0);
        for (int i = 1; i < list.size(); i++)
            accumulator = reducer.apply(accumulator, list.get(i), i, list);
        return accumulator;
    }

    /**
     * Executes a reducer function (that you provide) on each element of the list, resulting in single output value.
     * @param list Source list.
     * @param reducer A function to execute on each element in the list. It takes four arguments:
     * <ul>
     *     <li>accumulator - The accumulator accumulates callback's return values. It is the accumulated value previously returned in the last invocation of the callback or <b>initialValue</b>.</li>
     *     <li>element - The current element being processed in the list.</li>
     *     <li>index - The index of the current element being processed in the list. Starts from index <b>0</b>.</li>
     *     <li>list - The list <b>reduce()</b> was called upon.</li>
     * </ul>
     * @param initialValue A value to use as the first argument to the first call of the <b>reducer</b>.
     * @return The single value that results from the reduction.
     */
    public static <T, R> R reduce(List<T> list, Reducer<? super T, R> reducer, R initialValue) {
        Objects.requireNonNull(list);
        R accumulator = initialValue;
        for (int i = 0; i < list.size(); i++)
            accumulator = reducer.apply(accumulator, list.get(i), i, list);
        return accumulator;
    }

    /**
     * Applies a function against an accumulator and each value of the list (from right-to-left) to reduce it to a single value.
     * @param list Source list.
     * @param reducer A function to execute on each element in the list. It takes four arguments:
     * <ul>
     *     <li>accumulator - The accumulator accumulates callback's return values. It is the accumulated value previously returned in the last invocation of the callback.</li>
     *     <li>element - The current element being processed in the list.</li>
     *     <li>index - The index of the current element being processed in the list.</li>
     *     <li>list - The list <b>reduceRight()</b> was called upon.</li>
     * </ul>
     * @return The value that results from the reduction.
     * @throws UnsupportedOperationException If <b>list.isEmpty()</b>.
     */
    public static <T> T reduceRight(List<T> list, Reducer<? super T, T> reducer) {
        if (list.isEmpty())
            throw new UnsupportedOperationException("reduceRight() could not be performed with an empty list without initial value.");
        T accumulator = list.get(list.size() - 1);
        for (int i = list.size() - 2; i >= 0; i--)
            if (i < list.size())
                accumulator = reducer.apply(accumulator, list.get(i), i, list);
        return accumulator;
    }

    /**
     * Applies a function against an accumulator and each value of the list (from right-to-left) to reduce it to a single value.
     * @param list Source list.
     * @param reducer A function to execute on each element in the list. It takes four arguments:
     * <ul>
     *     <li>accumulator - The accumulator accumulates callback's return values. It is the accumulated value previously returned in the last invocation of the callback or <b>initialValue</b>.</li>
     *     <li>element - The current element being processed in the list.</li>
     *     <li>index - The index of the current element being processed in the list.</li>
     *     <li>list - The list <b>reduceRight()</b> was called upon.</li>
     * </ul>
     * @return The value that results from the reduction.
     */
    public static <T, R> R reduceRight(List<T> list, Reducer<? super T, R> reducer, R initialValue) {
        Objects.requireNonNull(list);
        R accumulator = initialValue;
        for (int i = list.size() - 1; i >= 0; i--)
            if (i < list.size())
                accumulator = reducer.apply(accumulator, list.get(i), i, list);
        return accumulator;
    }

    /**
     * Reverses a list in place. The first list element becomes the last, and the last list element becomes the first.
     * @param list List to be modified.
     * @return The reversed list.
     */
    public static <T> List<T> reverse(List<T> list) {
        Objects.requireNonNull(list);
        for (int i = 0; i < list.size() / 2; i++) {
            T temp = list.get(i);
            int target = list.size() - i - 1;
            list.set(i, list.get(target));
            list.set(target, temp);
        }
        return list;
    }

    /**
     * Removes the first element from an array and returns that removed element. This method changes the length of the array.
     * @param list List to be modified.
     * @return The removed element from the list; <b>null</b> if the list is empty.
     */
    public static <T> T shift(List<T> list) {
        if (list.isEmpty())
            return null;
        return list.remove(0);
    }

    /**
     * Returns a shallow copy of a list.
     * @deprecated Please use <b>new ArrayList<>(list)</b> directly.
     */
    @Deprecated
    public static <T> List<T> slice(List<T> list) {
        return new ArrayList<>(list);
    }

    /**
     * Returns a shallow copy of a portion of a list into a new list object selected from <b>start</b> to <b>end</b> (<b>end</b> not included) where <b>start</b> and <b>end</b> (default <b>list.size()</b>) represent the index of items in that list. The original list will not be modified.
     * @param list Source list.
     * @param start Zero-based index at which to start extraction. A negative index can be used, indicating an offset from the end of the sequence.
     * @return A new list containing the extracted elements.
     */
    public static <T> List<T> slice(List<T> list, int start) {
        Objects.requireNonNull(list);
        if (start < 0) {
            start += list.size();
            if (start < 0)
                start = 0;
        } else if (start >= list.size())
            return new ArrayList<>();
        return list.subList(start, list.size());
    }

    /**
     * Returns a shallow copy of a portion of a list into a new list object selected from <b>start</b> to <b>end</b> (<b>end</b> not included) where <b>start</b> and <b>end</b> (default <b>list.size()</b>) represent the index of items in that list. The original list will not be modified.
     * @param list Source list.
     * @param start Zero-based index at which to start extraction. A negative index can be used, indicating an offset from the end of the sequence.
     * @param end Zero-based index before which to end extraction. <b>slice</b> extracts up to but not including <b>end</b>. A negative index can be used, indicating an offset from the end of the sequence.
     * @return A new list containing the extracted elements.
     */
    public static <T> List<T> slice(List<T> list, int start, int end) {
        Objects.requireNonNull(list);
        if (start < 0) {
            start += list.size();
            if (start < 0)
                start = 0;
        }
        if (end < 0)
            end += list.size();
        else if (end > list.size())
            end = list.size();
        if (end <= start)
            return new ArrayList<>();
        return list.subList(start, end);
    }

    /**
     * Tests whether at least one element in the list passes the test implemented by the provided function. It returns true if, in the list, it finds an element for which the provided function returns true; otherwise it returns false. It doesn't modify the list.
     * @param list List under test.
     * @param predicate A function to test for each element, taking three arguments:
     * <ul>
     *     <li>element - The current element being processed in the list.</li>
     *     <li>index - The index of the current element being processed in the list.</li>
     *     <li>list - The list <b>some()</b> was called upon.</li>
     * </ul>
     * @return <b>true</b> if the callback function returns a truthy value for at least one element in the list. Otherwise, <b>false</b>.
     */
    public static <T> boolean some(List<T> list, Predicate<? super T> predicate) {
        Objects.requireNonNull(list);
        for (int i = 0; i < list.size(); i++)
            if (predicate.test(list.get(i), i, list))
                return true;
        return false;
    }

    /**
     * Sorts the elements of a list in place and returns the sorted list.
     * @param list List to be modified.
     * @return The sorted list. Note that the list is sorted in place, and no copy is made.
     */
    public static <T> List<T> sort(List<T> list) {
        return sort(list, Comparator.comparing(Object::toString));
    }

    /**
     * Sorts the elements of a list in place and returns the sorted list.
     * @param list List to be modified.
     * @param comparator Specifies a function that defines the sort order, taking two arguments:
     * <ul>
     *     <li>o1 - The first element for comparison.</li>
     *     <li>o2 - The second element for comparison.</li>
     * </ul>
     * @return The sorted list. Note that the list is sorted in place, and no copy is made.
     */
    public static <T> List<T> sort(List<T> list, Comparator<? super T> comparator) {
        List<T> newList = list.stream().sorted(comparator).collect(Collectors.toList());
        ListIterator<T> iterator = newList.listIterator();
        for (int i = 0; i < newList.size(); i++)
            list.set(i, iterator.next());
        return list;
    }

    /**
     * Changes the contents of a list by removing or replacing existing elements and/or adding new elements in place.
     * @param list List to be modified.
     * @param start The index at which to start changing the list.
     * @return A list containing the deleted elements.
     */
    public static <T> List<T> splice(List<T> list, int start) {
        Objects.requireNonNull(list);
        if (start < 0) {
            start += list.size();
            if (start < 0)
                start = 0;
        } else if (start >= list.size())
            return new ArrayList<>();
        List<T> removed;
        if (start == 0) {
            removed = new ArrayList<>(list);
            list.clear();
        } else {
            removed = new ArrayList<>();
            while (list.size() > start)
                removed.add(list.remove(start));
        }
        return removed;
    }

    /**
     * Changes the contents of a list by removing or replacing existing elements and/or adding new elements in place.
     * @param list List to be modified.
     * @param start The index at which to start changing the list.
     * @param deleteCount An integer indicating the number of elements in the list to remove from <b>start</b>.
     * @param items The elements to add to the array, beginning from <b>start</b>.
     * @return A list containing the deleted elements.
     */
    @SafeVarargs
    public static <T> List<T> splice(List<T> list, int start, int deleteCount, T... items) {
        Objects.requireNonNull(list);
        if (start < 0) {
            start += list.size();
            if (start < 0)
                start = 0;
        }
        List<T> removed = new ArrayList<>();
        while (list.size() > start && deleteCount > 0) {
            removed.add(list.remove(start));
            deleteCount--;
        }
        if (start >= list.size())
            list.addAll(Arrays.asList(items));
        else
            list.addAll(start, Arrays.asList(items));
        return removed;
    }

    /**
     * Equivalent to <b>join(list)</b>.
     * Consider using list.toString() depends on your purpose.
     */
    public static <T> String toString(List<T> list) {
        return join(list);
    }

    /**
     * Adds one or more elements to the beginning of a list and returns the new size of the list.
     * @param list List to be modified.
     * @param items The elements to add to the front of the list.
     * @return The new size of the list.
     */
    @SafeVarargs
    public static <T> int unshift(List<T> list, T... items) {
        list.addAll(0, Arrays.asList(items));
        return list.size();
    }
}
