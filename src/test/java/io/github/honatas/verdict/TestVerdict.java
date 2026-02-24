package io.github.honatas.verdict;

import java.io.Serializable;

public class TestVerdict extends Verdict {

    public TestVerdict() {
    }

    public TestVerdict(Serializable data) {
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
