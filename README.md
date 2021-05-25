# Java List Util
An ECMAScript [Array](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array) styled Java utility class for java.util.List.

## Example
#### In JavaScript
```js
let numbers = [1, 2, 3, 4];
console.log(numbers.map((element, index, array) => element * 2 + array[(index + 1) % array.length] - array.pop()));
// Array [0, 4, undefined, undefined]
```
#### Equivalent Here
```java
List<Integer> numbers = ListUtil.of(1, 2, 3, 4);
System.out.println(ListUtil.map(numbers, (element, index, list) -> element * 2 + list.get((index + 1) % list.size()) - ListUtil.pop(list)));
// [0, 4, null, null]
```
