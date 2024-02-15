# Collections.singletonList vs List.of

Both Collections.singletonList(comment) and List.of(comment) are valid ways to create an immutable list containing a single element in Java. However, they have some differences in behavior and usage contexts:

Collections.singletonList(comment)
Immutability: Returns an immutable list, which means you cannot add, remove, or replace elements in the list.
Since: It has been available since Java 1.3.
Null Handling: Allows null elements. If comment is null, the list will contain one null element.
Use Case: Ideal when you need a non-modifiable list with exactly one element, and your code needs to be compatible with older Java versions.
List.of(comment)
Immutability: Also returns an immutable list. You cannot add, remove, or replace elements.
Since: Introduced in Java 9 as part of the Java Collections Framework enhancements.
Null Handling: Does not allow null elements. If comment is null, it will throw a NullPointerException.
Use Case: Preferred in modern Java applications where null elements are not desired or expected. It provides a more concise and readable way to create immutable lists.
Which to Use?
Compatibility: If you are working in an environment that uses Java 8 or earlier, you should use Collections.singletonList.
Null Safety: If you are using Java 9 or later and want to avoid null elements in your lists, List.of is the better choice.
Readability: List.of is often favored in newer codebases for its readability and conciseness, especially when working with Java 9 and above.
In the context of your test code, if you are sure that comment is not null and you're using Java 9 or later, List.of(comment) would be a more modern and concise choice. However, if you need backward compatibility with older Java versions or if there's a possibility that comment could be null, then Collections.singletonList(comment) would be appropriate.