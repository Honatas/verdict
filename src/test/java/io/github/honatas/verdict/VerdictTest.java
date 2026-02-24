package io.github.honatas.verdict;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class VerdictTest {

	@Test
	void testGetErrors_returnsEmptyMapInitially() {
		TestVerdict v = new TestVerdict();
		assertTrue(v.getErrors().isEmpty());
	}
	
	@Test
	void testValidate_dataInline_passesRequired() {
		TestVerdict v = new TestVerdict();
		v.validate("John", "name", v.required);
		assertTrue(v.getErrors().isEmpty());
	}

	@Test
	void testValidate_dataInline_failsRequired() {
		TestVerdict v = new TestVerdict();
		v.validate(null, "name", v.required);
		assertTrue(v.getErrors().containsKey("name"));
	}

	@Test
	void testValidate_pojoInline_passesRequired() {
		TestPojo pojo = new TestPojo("John");
		TestVerdict v = new TestVerdict();
		v.validate(pojo.getName(), "name", v.required);
		assertTrue(v.getErrors().isEmpty());
	}

	@Test
	void testValidate_pojoInline_failsRequired() {
		TestPojo pojo = new TestPojo(null);
		TestVerdict v = new TestVerdict();
		v.validate(pojo.getName(), "name", v.required);
		assertTrue(v.getErrors().containsKey("name"));
	}

	@Test
	void testValidate_recordInline_passesRequired() {
		TestRecord rec = new TestRecord("John");
		TestVerdict v = new TestVerdict();
		v.validate(rec.name(), "name", v.required);
		assertTrue(v.getErrors().isEmpty());
	}

	@Test
	void testValidate_recordInline_failsRequired() {
		TestRecord rec = new TestRecord(null);
		TestVerdict v = new TestVerdict();
		v.validate(rec.name(), "name", v.required);
		assertTrue(v.getErrors().containsKey("name"));
	}

	@Test
	void testValidate_pojo_passesRequired() {
		TestPojo pojo = new TestPojo("John");
		TestVerdict v = new TestVerdict(pojo);
		v.validate("name", v.required);
		assertTrue(v.getErrors().isEmpty());
	}

	@Test
	void testValidate_pojo_failsRequired() {
		TestPojo pojo = new TestPojo(null);
		TestVerdict v = new TestVerdict(pojo);
		v.validate("name", v.required);
		assertTrue(v.getErrors().containsKey("name"));
	}

	@Test
	void testValidate_record_passesRequired() {
		TestRecord rec = new TestRecord("John");
		TestVerdict v = new TestVerdict(rec);
		v.validate("name", v.required);
		assertTrue(v.getErrors().isEmpty());
	}

	@Test
	void testValidate_record_failsRequired() {
		TestRecord rec = new TestRecord(null);
		TestVerdict v = new TestVerdict(rec);
		v.validate("name", v.required);
		assertTrue(v.getErrors().containsKey("name"));
	}

	@Test
	void documentationTest() {
		TestRecord data = new TestRecord(null, 2, -3);
		TestVerdict v = new TestVerdict(data);
		v.validate("name", v.required);
		v.validate("age", v.required, v.positive);
		v.validate("height", v.required, v.positive);
		assertEquals("{name=name is required, height=height must be greater than zero}", v.getErrors().toString());
	}

	@Test
	void edgeCases() {
		TestVerdict v = new TestVerdict();
		// No validators
		assertThrows(IllegalArgumentException.class, () -> {
			v.validate("abc", "name");
		});
		// Empty field name
		assertThrows(IllegalArgumentException.class, () -> {
			v.validate("abc", "", v.required);
		});
		// No data
		assertThrows(IllegalStateException.class, () -> {
			v.validate("name", v.required);
		});
	}
}
