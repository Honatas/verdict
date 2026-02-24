# Verdict

[![GitHub](https://img.shields.io/github/license/honatas/verdict?style=plastic)](https://github.com/Honatas/verdict "View this project on GitHub")
[![Maven Central](https://img.shields.io/maven-central/v/io.github.honatas/verdict.svg?style=plastic)](https://search.maven.org/search?q=g:%22io.github.honatas%22%20AND%20a:%22verdict%22)
[![coffee](https://img.shields.io/badge/buy%20me%20a-coffee-brown?style=plastic)](https://ko-fi.com/honatas "Buy me a coffee")  


An imperative, zero-dependency data validator.  

## Requirements

Verdict requires Java 16 or higher.  

## Download

If you are using Maven, add this to your pom.xml in the dependencies section:

```xml
    <dependency>
        <groupId>io.github.honatas</groupId>
        <artifactId>verdict</artifactId>
        <version>1.0.0</version>
    </dependency>
```

## Usage

First, create your own verdict class by extending Verdict and define your Validator functions. Don't forget to implement both constructors:

```java
import io.github.honatas.verdict.Verdict;

public class MyVerdict extends Verdict {

    public MyVerdict() {
    }

    public MyVerdict(Serializable data) {
        super(data);
    }

    public Validator required = (Object value, String name) -> {
        if (value == null || value instanceof String string && string.isEmpty() || value instanceof Number number && number.intValue() == 0) {
            return name + " is required";
        }
        return null;
    };

    public Validator positive = (Object value, String name) -> {
        if (value == null || !(value instanceof Number number) || number.intValue() < 0) {
            return name + " must be greater than zero";
        }
        return null;
    };
}
```

Suppose you have the following data structure to validate:

```java
public record MyData(String name, Integer age, Integer height) {
}
```

You can then create a new instance of MyVerdict to validate your data:

```java
    MyData data = new MyData(null, 2, -3);
    MyVerdict v = new MyVerdict(data);
    v.validate("name", v.required);
    v.validate("age", v.required, v.positive);
    v.validate("height", v.required, v.positive);

    System.out.println(v.getErrors());
```

This will produce the following output:

```
{name=name is required, height=height must be greater than zero}
```

## How it works

Validator functions must be written to return null if the data is valid, or a string containing an error message if the validation fails.  

You can pass as many Validators as you need to the validate method. Verdict will execute each of them in the order they were passed. It will halt on the first Validator that fails (i.e., does not return null), and it will add the error message to the errors Map using the field name as the key.  

## Error checking

There are two ways to check for errors after running your validators. You can use the **Verdict::hasErrors** method, which returns a boolean indicating whether any validation errors occurred. Alternatively, you can use the **Verdict::checkHasErrors** method, which throws a **VerdictException** if there are any validation errors. This exception class provides a **getErrors()** method that returns the errors Map, and a **getData()** method that returns the validated data if it was passed to the constructor.  

## Inline Validations

You are not required to pass a data object to the constructor. You can validate any single value by passing it directly to the **Verdict::validate** method, along with the field name and the validators.

```java
    MyVerdict v = new MyVerdict();
    v.validate("Joseph Climber", "name", v.required);
```

## Parameterized Validations

You can also create parameterized validators. Here is an example:

```java
public class MyVerdict extends Verdict {

	public Validator maxLength(int size) {
    	return (value, name) -> {
    		if (value == null) {
    			return null;
    		}
    		if (value.toString().length() > size) {
    			return name + " must have maximum " + size + " characters";
    		}
    		return null;
    	};
    }
}
```

You can then call the validator like this:

```java
    MyVerdict v = new MyVerdict();
    v.validate("abcde", "myField", v.maxLength(2));
```


## Regex Validation

You can create regex Validators using the static methods **Verdict::getValidatorRegexMatch** and **Verdict::getValidatorRegexFind**:  

```java
public class MyVerdict extends Verdict {

    public Validator number = Verdict.getValidatorRegexMatch("\\d+", "Value has to be a number");
}
```

## Contributions

Feel free to open an issue or submit a pull request at any time. Contributions are always welcome!  

If this project has helped you in any way, please consider buying me a [coffee](https://ko-fi.com/honatas).
