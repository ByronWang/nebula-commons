package nebula.commons.name;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import nebula.commons.name.JavaNameStrategy;

public class JavaNameStrategyTest {

	JavaNameStrategy javaNameStrategy = new JavaNameStrategy();

	@Test
	public void testConvertName() {
		assertEquals("height", javaNameStrategy.convertName("Height"));
		assertEquals("firstName", javaNameStrategy.convertName("FirstName"));
		assertEquals("ID", javaNameStrategy.convertName("ID"));
		assertEquals("id", javaNameStrategy.convertName("Id"));
		assertEquals("ISDN", javaNameStrategy.convertName("ISDN"));
		assertEquals("firstID", javaNameStrategy.convertName("FirstID"));
	}

	@Test
	public void testSplitIntoWords() {
		assertEquals("height", javaNameStrategy.splitIntoWords("Height"));
		assertEquals("first name", javaNameStrategy.splitIntoWords("FirstName"));
		assertEquals("ID", javaNameStrategy.splitIntoWords("ID"));
		assertEquals("id", javaNameStrategy.splitIntoWords("Id"));
		assertEquals("ISDN", javaNameStrategy.splitIntoWords("ISDN"));
		assertEquals("first ID", javaNameStrategy.splitIntoWords("FirstID"));
	}
}
