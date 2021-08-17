# Unit Testing Steps
This are the basic steps for doing unit tests. This assumes that the mock objects are set up and that @BeforeAll and @BeforeEach are already set up.

## 1. Set up parameters that are not already setup
Any parameters that are needed for the test should be setup. For instance, if you have a specific input you need to test for, or a specific value is being passed in to another method that you need to check.

## 2. Make sure mocks return the correct objects
Mock objects by default return null, as they are not the real object, they are just a stand in for testing. To have the Mock objects actually return values or objects, we need to use `when()`.

```Java
Mockito.when(mock.method(arguments)).thenReturn(retValue);
```
Here's what each part of the example code is doing:
1. `when(...)`: Is used to specify that, when a method from a mock object is called, do something (in most cases, return something).
2. `mock`: The mock object that was created.
3. `method(...)`: The method from the mock object that is being called. Note that the method needs a return type, as void methods don't return anything.
4. `arguments`: Specifies what arguments will actually trigger the return. Useful for making sure the correct object is called. Note that if the method doesn't take any arguments, then the method call should contain no arguments.
5. `thenReturn(...)`: This specifies that, when the mocked objects method is called with the provided arguments, what should be returned.
6. `retValue`: The value that will be returned when the mock method is called.

## 3. Instantiate ArgumentCaptors
ArgumentCaptors are used to verify that the correct arguments are being passed in methods that are called inside the class. This is really useful for void methods that do not return any data.

```Java
ArgumentCaptor<Type> captor = ArgumentCaptor.forClass(Type.class);
```
Note that each argument needs its own ArgumentCaptor to verify it.

## 4. Call the method
Actually call the method. If the method is not void, then make sure to instantiate an object to get the return value.

## 5. Use assert to make sure the method is sending back the correct data
To make sure the method did what it was supposed to do, we need to use `assert` statements to make sure that the values are what we need them to be. There are many types of assert statements:
* `assertEquals()`: Checks to make sure that two elements are the same.

```Java
assertEquals(value1, value2, "Assert that these two values are equal");
```
* `assertTrue()` and `assertFalse()`: Makes sure that a value is either true or false.

```Java
assertTrue(booleanValue1, "Assert that this value is true");
assertFalse(booleanValue2, "Assert that this value is false");
```
* `assertNull`: Check to make sure the value is null.

```Java
assertNull("Assert that this value is null", nullValue);
```
* `assertThrows`: Checks to see if a method throws an exception.

```Java
assertThrows(Exception.class, () -> object.method(arguments), "Assert that the method throws an exception.");
```
Note that there are many other types of assertions, these are just a few of the more common ones.

## 6. Verify inside methods were called with Mockito.verify and capture arguments
To make sure method calls are being correctly made, we need to use `Mockito.verify()` to ensure that each method call inside is being called correctly. We also want to get the arguments passed in to make sure they are correct.

```Java
Mockito.verify(mock).method(captor.capture());
```
Here's what each part of the example is doing:
1. `Mockito.verify(...)`: Checks to make sure that a method was called once. Will fail if the method isn't called. Note: if a method was called more than once and verify is called like in the example, Mockito will fail the test as you need to specify how many times the method should be called if more than once. 
2. `(mock).method(...)`: The mock object and the method being called from that object. Unlike when(), the method call is made outside of the verify argument parenthetical, not inside.
3. `captor.capture()`: Used to get the argument from the method call. Note that if the method has more than one argument, will need to use more than one captor to capture them. If the method has no arguments, then no arguments are needed to be passed in.

## 7. Verify the arguments passed in are the correct arguments
ArgumentCaptors have a method called `getValue()` that will return the captured value.

```Java
captor.getValue();
```
When used in conjunction with `assert` statements, it can be used to make sure that the value is correct.

```Java
assertEquals(value, captor.getValue(), "Assert that the value passed in is correct");
```

## Conclusion
This is a very basic overview about how unit tests should be written. One thing to stress about tests is to make sure you are not writing over-complicated tests. If you have to call the method more than once, it might be a better idea to write separate tests. Note that it is generally fine to call the method more than once when checking for bad inputs i.e. inputs that should return nulls or throw exceptions.

If there are any questions, please let me know as soon as possible.