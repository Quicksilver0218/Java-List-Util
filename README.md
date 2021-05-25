# Java List Util
An ECMAScript Array styled Java utility class for java.util.List.

## Example
#### In JavaScript
```js
let numbers = [1, 2, 3, 4];
console.log(numbers.map((element, index, array) => element * 2 + array[(index + 1) % array.length])); // Array [4, 7, 10, 9]
```
#### Equivalent Here
```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
System.out.println(ListUtil.map(numbers, (element, index, list) -> element * 2 + list.get((index + 1) % list.size())));
// [4, 7, 10, 9]
```
