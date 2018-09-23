package nebula.commons.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import nebula.commons.Comparing;
import nebula.commons.annotations.NotNull;
import nebula.commons.container.ContainerUtil;
import nebula.commons.text.LineColumn;
import nebula.commons.text.LineSeparator;
import nebula.commons.text.NaturalComparator;
import nebula.commons.text.StringUtil;
import nebula.commons.text.TextRange;

public class StringUtilTest {
  @Test
  public void testTrimLeadingChar() {
    doTestTrimLeading("", "");
    doTestTrimLeading("", " ");
    doTestTrimLeading("", "    ");
    doTestTrimLeading("a  ", "a  ");
    doTestTrimLeading("a  ", "  a  ");
  }

  @Test
  public void testTrimTrailingChar() {
    doTestTrimTrailing("", "");
    doTestTrimTrailing("", " ");
    doTestTrimTrailing("", "    ");
    doTestTrimTrailing("  a", "  a");
    doTestTrimTrailing("  a", "  a  ");
  }

  private static void doTestTrimLeading(@NotNull String expected, @NotNull String string) {
    assertEquals(expected, StringUtil.trimLeading(string));
    assertEquals(expected, StringUtil.trimLeading(string, ' '));
    assertEquals(expected, StringUtil.trimLeading(new StringBuilder(string), ' ').toString());
  }

  private static void doTestTrimTrailing(@NotNull String expected, @NotNull String string) {
    assertEquals(expected, StringUtil.trimTrailing(string));
    assertEquals(expected, StringUtil.trimTrailing(string, ' '));
    assertEquals(expected, StringUtil.trimTrailing(new StringBuilder(string), ' ').toString());
  }

  @Test
  public void testToUpperCase() {
    assertEquals('/', StringUtil.toUpperCase('/'));
    assertEquals(':', StringUtil.toUpperCase(':'));
    assertEquals('A', StringUtil.toUpperCase('a'));
    assertEquals('A', StringUtil.toUpperCase('A'));
    assertEquals('K', StringUtil.toUpperCase('k'));
    assertEquals('K', StringUtil.toUpperCase('K'));

    assertEquals('\u2567', StringUtil.toUpperCase(Character.toLowerCase('\u2567')));
  }

  @Test
  public void testToLowerCase() {
    assertEquals('/', StringUtil.toLowerCase('/'));
    assertEquals(':', StringUtil.toLowerCase(':'));
    assertEquals('a', StringUtil.toLowerCase('a'));
    assertEquals('a', StringUtil.toLowerCase('A'));
    assertEquals('k', StringUtil.toLowerCase('k'));
    assertEquals('k', StringUtil.toLowerCase('K'));

    assertEquals('\u2567', StringUtil.toUpperCase(Character.toLowerCase('\u2567')));
  }

  @Test
  public void testIsEmptyOrSpaces() {
    assertTrue(StringUtil.isEmptyOrSpaces(null));
    assertTrue(StringUtil.isEmptyOrSpaces(""));
    assertTrue(StringUtil.isEmptyOrSpaces("                   "));

    assertFalse(StringUtil.isEmptyOrSpaces("1"));
    assertFalse(StringUtil.isEmptyOrSpaces("         12345          "));
    assertFalse(StringUtil.isEmptyOrSpaces("test"));
  }

  @Test
  public void testSplitWithQuotes() {
    final List<String> strings = StringUtil.splitHonorQuotes("aaa bbb   ccc \"ddd\" \"e\\\"e\\\"e\"  ", ' ');
    assertEquals(5, strings.size());
    assertEquals("aaa", strings.get(0));
    assertEquals("bbb", strings.get(1));
    assertEquals("ccc", strings.get(2));
    assertEquals("\"ddd\"", strings.get(3));
    assertEquals("\"e\\\"e\\\"e\"", strings.get(4));
  }

  @Test
  public void testUnPluralize() {
    // synthetic
    assertEquals("plurals", StringUtil.unpluralize("pluralses"));
    assertEquals("Inherits", StringUtil.unpluralize("Inheritses"));
    assertEquals("s", StringUtil.unpluralize("ss"));
    assertEquals("I", StringUtil.unpluralize("Is"));
    assertNull(StringUtil.unpluralize("s"));
    assertEquals("z", StringUtil.unpluralize("zs"));
    // normal
    assertEquals("case", StringUtil.unpluralize("cases"));
    assertEquals("Index", StringUtil.unpluralize("Indices"));
    assertEquals("fix", StringUtil.unpluralize("fixes"));
    assertEquals("man", StringUtil.unpluralize("men"));
    assertEquals("leaf", StringUtil.unpluralize("leaves"));
    assertEquals("cookie", StringUtil.unpluralize("cookies"));
    assertEquals("search", StringUtil.unpluralize("searches"));
    assertEquals("process", StringUtil.unpluralize("process"));
    assertEquals("PROPERTY", StringUtil.unpluralize("PROPERTIES"));
    assertEquals("THIS", StringUtil.unpluralize("THESE"));
    assertEquals("database", StringUtil.unpluralize("databases"));
    assertEquals("basis", StringUtil.unpluralize("bases"));
  }

  @Test
  public void testPluralize() {
    assertEquals("values", StringUtil.pluralize("value"));
    assertEquals("values", StringUtil.pluralize("values"));
    assertEquals("indices", StringUtil.pluralize("index"));
    assertEquals("matrices", StringUtil.pluralize("matrix"));
    assertEquals("fixes", StringUtil.pluralize("fix"));
    assertEquals("men", StringUtil.pluralize("man"));
    assertEquals("media", StringUtil.pluralize("medium"));
    assertEquals("stashes", StringUtil.pluralize("stash"));
    assertEquals("children", StringUtil.pluralize("child"));
    assertEquals("leaves", StringUtil.pluralize("leaf"));
    assertEquals("stomata", StringUtil.pluralize("stoma"));
    assertEquals("tornadoes", StringUtil.pluralize("tornado"));
    assertEquals("feet", StringUtil.pluralize("foot"));
    assertEquals("these", StringUtil.pluralize("this"));
    assertEquals("cookies", StringUtil.pluralize("cookie"));
    assertEquals("VaLuES", StringUtil.pluralize("VaLuE"));
    assertEquals("PLANS", StringUtil.pluralize("PLAN"));
    assertEquals("stackTraceLineExes", StringUtil.pluralize("stackTraceLineEx"));
    assertEquals("schemas", StringUtil.pluralize("schema")); // anglicized version
    assertEquals("PROPERTIES", StringUtil.pluralize("PROPERTY"));
    assertEquals("THESE", StringUtil.pluralize("THIS"));
    assertEquals("databases", StringUtil.pluralize("database"));
    assertEquals("bases", StringUtil.pluralize("base"));
    assertEquals("bases", StringUtil.pluralize("basis"));
  }

  @Test
  public void testStartsWithConcatenation() {
    assertTrue(StringUtil.startsWithConcatenation("something.with.dot", "something", "."));
    assertTrue(StringUtil.startsWithConcatenation("something.with.dot", "", "something."));
    assertTrue(StringUtil.startsWithConcatenation("something.", "something", "."));
    assertTrue(StringUtil.startsWithConcatenation("something", "something", "", "", ""));
    assertFalse(StringUtil.startsWithConcatenation("something", "something", "", "", "."));
    assertFalse(StringUtil.startsWithConcatenation("some", "something", ""));
  }

  @Test
  public void testNaturalCompareTransitivity() {
    String s1 = "#";
    String s2 = "0b";
    String s3 = " 0b";
    assertTrue(StringUtil.naturalCompare(s1, s2) < 0);
    assertTrue(StringUtil.naturalCompare(s2, s3) < 0);
    assertTrue("non-transitive", StringUtil.naturalCompare(s1, s3) < 0);
  }


  @Test
  public void testNaturalCompareStability() {
    assertTrue(StringUtil.naturalCompare("01a1", "1a01") != StringUtil.naturalCompare("1a01", "01a1"));
    assertTrue(StringUtil.naturalCompare("#01A", "# 1A") != StringUtil.naturalCompare("# 1A", "#01A"));
    assertTrue(StringUtil.naturalCompare("aA", "aa") != StringUtil.naturalCompare("aa", "aA"));
  }

  @Test
  public void testNaturalCompare() {

    final List<String> numbers = Arrays.asList("1a000001", "000001a1", "001a0001", "0001A001" , "00001a01", "01a00001");
    numbers.sort(NaturalComparator.INSTANCE);
    assertEquals(Arrays.asList("1a000001", "01a00001", "001a0001", "0001A001" , "00001a01", "000001a1"), numbers);

    final List<String> test = Arrays.asList("test011", "test10", "test10a", "test010");
    test.sort(NaturalComparator.INSTANCE);
    assertEquals(Arrays.asList("test10", "test10a", "test010", "test011"), test);

    final List<String> strings = Arrays.asList("Test99", "tes0", "test0", "testing", "test", "test99", "test011", "test1",
                    "test 3", "test2", "test10a", "test10", "1.2.10.5", "1.2.9.1");
    strings.sort(NaturalComparator.INSTANCE);
    assertEquals(Arrays.asList("1.2.9.1", "1.2.10.5", "tes0", "test", "test0", "test1", "test2", "test 3", "test10", "test10a",
                               "test011", "Test99", "test99", "testing"), strings);

    final List<String> strings2 = Arrays.asList("t1", "t001", "T2", "T002", "T1", "t2");
    strings2.sort(NaturalComparator.INSTANCE);
    assertEquals(Arrays.asList("T1", "t1", "t001", "T2", "t2", "T002"), strings2);
    assertEquals(1 ,StringUtil.naturalCompare("7403515080361171695", "07403515080361171694"));
    assertEquals(-14, StringUtil.naturalCompare("_firstField", "myField1"));
    //idea-80853
    final List<String> strings3 =
      Arrays.asList("C148A_InsomniaCure", "C148B_Escape", "C148C_TersePrincess", "C148D_BagOfMice", "C148E_Porcelain");
    strings3.sort(NaturalComparator.INSTANCE);
    assertEquals(Arrays.asList("C148A_InsomniaCure", "C148B_Escape", "C148C_TersePrincess", "C148D_BagOfMice", "C148E_Porcelain"), strings3);

    final List<String> l = Arrays.asList("a0002", "a0 2", "a001");
    l.sort(NaturalComparator.INSTANCE);
    assertEquals(Arrays.asList("a0 2", "a001", "a0002"), l);
  }

  @Test
  public void testFormatLinks() {
    assertEquals("<a href=\"http://a-b+c\">http://a-b+c</a>", StringUtil.formatLinks("http://a-b+c"));
  }
//
//  @Test
//  public void testCopyHeapCharBuffer() {
//    String s = "abc.d";
//    CharBuffer buffer = CharBuffer.allocate(s.length());
//    buffer.append(s);
//    buffer.rewind();
//
//    assertNotNull(CharArrayUtil.fromSequenceWithoutCopying(buffer));
//    assertNotNull(CharArrayUtil.fromSequenceWithoutCopying(buffer.subSequence(0, 5)));
//    //assertNull(CharArrayUtil.fromSequenceWithoutCopying(buffer.subSequence(0, 4))); // end index is not checked
//    assertNull(CharArrayUtil.fromSequenceWithoutCopying(buffer.subSequence(1, 5)));
//    assertNull(CharArrayUtil.fromSequenceWithoutCopying(buffer.subSequence(1, 2)));
//  }

  @Test
  public void testTitleCase() {
    assertEquals("Couldn't Connect to Debugger", StringUtil.wordsToBeginFromUpperCase("Couldn't connect to debugger"));
    assertEquals("Let's Make Abbreviations Like I18n, SQL and CSS", StringUtil.wordsToBeginFromUpperCase("Let's make abbreviations like I18n, SQL and CSS"));
  }

  @Test
  public void testSentenceCapitalization() {
    assertEquals("couldn't connect to debugger", StringUtil.wordsToBeginFromLowerCase("Couldn't Connect to Debugger"));
    assertEquals("let's make abbreviations like I18n, SQL and CSS s SQ sq", StringUtil.wordsToBeginFromLowerCase("Let's Make Abbreviations Like I18n, SQL and CSS S SQ Sq"));
  }

  @Test
  public void testEscapeStringCharacters() {
    assertEquals("\\\"\\n", StringUtil.escapeStringCharacters(3, "\\\"\n", "\"", false, new StringBuilder()).toString());
    assertEquals("\\\"\\n", StringUtil.escapeStringCharacters(2, "\"\n", "\"", false, new StringBuilder()).toString());
    assertEquals("\\\\\\\"\\n", StringUtil.escapeStringCharacters(3, "\\\"\n", "\"", true, new StringBuilder()).toString());
  }

  @Test
  public void testEscapeSlashes() {
    assertEquals("\\/", StringUtil.escapeSlashes("/"));
    assertEquals("foo\\/bar\\foo\\/", StringUtil.escapeSlashes("foo/bar\\foo/"));

    assertEquals("\\\\\\\\server\\\\share\\\\extension.crx", StringUtil.escapeBackSlashes("\\\\server\\share\\extension.crx"));
  }

  @Test
  public void testEscapeQuotes() {
    assertEquals("\\\"", StringUtil.escapeQuotes("\""));
    assertEquals("foo\\\"bar'\\\"", StringUtil.escapeQuotes("foo\"bar'\""));
  }

  @Test
  public void testUnquote() {
    assertEquals("", StringUtil.unquoteString(""));
    assertEquals("\"", StringUtil.unquoteString("\""));
    assertEquals("", StringUtil.unquoteString("\"\""));
    assertEquals("\"", StringUtil.unquoteString("\"\"\""));
    assertEquals("foo", StringUtil.unquoteString("\"foo\""));
    assertEquals("\"foo", StringUtil.unquoteString("\"foo"));
    assertEquals("foo\"", StringUtil.unquoteString("foo\""));
    assertEquals("", StringUtil.unquoteString(""));
    assertEquals("\'", StringUtil.unquoteString("\'"));
    assertEquals("", StringUtil.unquoteString("\'\'"));
    assertEquals("\'", StringUtil.unquoteString("\'\'\'"));
    assertEquals("foo", StringUtil.unquoteString("\'foo\'"));
    assertEquals("\'foo", StringUtil.unquoteString("\'foo"));
    assertEquals("foo\'", StringUtil.unquoteString("foo\'"));

    assertEquals("\'\"", StringUtil.unquoteString("\'\""));
    assertEquals("\"\'", StringUtil.unquoteString("\"\'"));
    assertEquals("\"foo\'", StringUtil.unquoteString("\"foo\'"));
  }

  @Test
  public void testStripQuotesAroundValue() {
    assertEquals("", StringUtil.stripQuotesAroundValue(""));
    assertEquals("", StringUtil.stripQuotesAroundValue("'"));
    assertEquals("", StringUtil.stripQuotesAroundValue("\""));
    assertEquals("", StringUtil.stripQuotesAroundValue("''"));
    assertEquals("", StringUtil.stripQuotesAroundValue("\"\""));
    assertEquals("", StringUtil.stripQuotesAroundValue("'\""));
    assertEquals("foo", StringUtil.stripQuotesAroundValue("'foo'"));
    assertEquals("foo", StringUtil.stripQuotesAroundValue("'foo"));
    assertEquals("foo", StringUtil.stripQuotesAroundValue("foo'"));
    assertEquals("f'o'o", StringUtil.stripQuotesAroundValue("'f'o'o'"));
    assertEquals("f\"o'o", StringUtil.stripQuotesAroundValue("\"f\"o'o'"));
    assertEquals("f\"o'o", StringUtil.stripQuotesAroundValue("f\"o'o"));
    assertEquals("\"'f\"o'o\"", StringUtil.stripQuotesAroundValue("\"\"'f\"o'o\"\""));
    assertEquals("''f\"o'o''", StringUtil.stripQuotesAroundValue("'''f\"o'o'''"));
    assertEquals("foo' 'bar", StringUtil.stripQuotesAroundValue("foo' 'bar"));
  }

  @Test
  public void testUnquoteWithQuotationChar() {
    assertEquals("", StringUtil.unquoteString("", '|'));
    assertEquals("|", StringUtil.unquoteString("|", '|'));
    assertEquals("", StringUtil.unquoteString("||", '|'));
    assertEquals("|", StringUtil.unquoteString("|||", '|'));
    assertEquals("foo", StringUtil.unquoteString("|foo|", '|'));
    assertEquals("|foo", StringUtil.unquoteString("|foo", '|'));
    assertEquals("foo|", StringUtil.unquoteString("foo|", '|'));
  }

  @Test
  public void testIsQuotedString() {
    assertFalse(StringUtil.isQuotedString(""));
    assertFalse(StringUtil.isQuotedString("'"));
    assertFalse(StringUtil.isQuotedString("\""));
    assertTrue(StringUtil.isQuotedString("\"\""));
    assertTrue(StringUtil.isQuotedString("''"));
    assertTrue(StringUtil.isQuotedString("'ab'"));
    assertTrue(StringUtil.isQuotedString("\"foo\""));
  }

  @Test
  public void testJoin() {
    assertEquals("", StringUtil.join(Collections.emptyList(), ","));
    assertEquals("qqq", StringUtil.join(Collections.singletonList("qqq"), ","));
    assertEquals("", StringUtil.join(Collections.singletonList(null), ","));
    assertEquals("a,b", StringUtil.join(Arrays.asList("a", "b"), ","));
    assertEquals("foo,,bar", StringUtil.join(Arrays.asList("foo", "", "bar"), ","));
    assertEquals("foo,,bar", StringUtil.join(new String[]{"foo", "", "bar"}, ","));
  }

  @Test
  public void testSplitByLineKeepingSeparators() {
    assertEquals(Collections.singletonList(""), Arrays.asList(StringUtil.splitByLinesKeepSeparators("")));
    assertEquals(Collections.singletonList("aa"), Arrays.asList(StringUtil.splitByLinesKeepSeparators("aa")));
    assertEquals(Arrays.asList("\n", "\n", "aa\n", "\n", "bb\n", "cc\n", "\n"),
                 Arrays.asList(StringUtil.splitByLinesKeepSeparators("\n\naa\n\nbb\ncc\n\n")));

    assertEquals(Arrays.asList("\r", "\r\n", "\r"), Arrays.asList(StringUtil.splitByLinesKeepSeparators("\r\r\n\r")));
    assertEquals(Arrays.asList("\r\n", "\r", "\r\n"), Arrays.asList(StringUtil.splitByLinesKeepSeparators("\r\n\r\r\n")));

    assertEquals(Arrays.asList("\n", "\r\n", "\n", "\r\n", "\r", "\r", "aa\r", "bb\r\n", "cc\n", "\r", "dd\n", "\n", "\r\n", "\r"),
                 Arrays.asList(StringUtil.splitByLinesKeepSeparators("\n\r\n\n\r\n\r\raa\rbb\r\ncc\n\rdd\n\n\r\n\r")));
  }
//
//  @Test
//  public void testReplaceReturnReplacementIfTextEqualsToReplacedText() {
//    String newS = "/tmp";
//    assertThat(newS).isSameAs(StringUtil.replace("$PROJECT_FILE$", "$PROJECT_FILE$".toLowerCase().toUpperCase() /* ensure new String instance */, newS));
//  }
//
//  @Test
//  public void testReplace() {
//    assertThat("/tmp/filename").isEqualTo(StringUtil.replace("$PROJECT_FILE$/filename", "$PROJECT_FILE$", "/tmp"));
//  }
//
//  @Test
//  public void testReplaceListOfChars() {
//    assertThat("/tmp/filename").isEqualTo(StringUtil.replace("$PROJECT_FILE$/filename", Collections.singletonList("$PROJECT_FILE$"), Collections.singletonList("/tmp")));
//    assertThat("/someTextBefore/tmp/filename").isEqualTo(StringUtil.replace("/someTextBefore/$PROJECT_FILE$/filename", Collections.singletonList("$PROJECT_FILE$"), Collections.singletonList("tmp")));
//  }

//  @Test
//  public void testReplaceReturnReplacementIfTextEqualsToReplacedText() {
//    String newS = "/tmp";
//    assertThat(newS).isSameAs(StringUtil.replace("$PROJECT_FILE$", "$PROJECT_FILE$".toLowerCase().toUpperCase() /* ensure new String instance */, newS));
//  }
//
//  @Test
//  public void testReplace() {
//    assertThat("/tmp/filename").isEqualTo(StringUtil.replace("$PROJECT_FILE$/filename", "$PROJECT_FILE$", "/tmp"));
//  }
//
//  @Test
//  public void testReplaceListOfChars() {
//    assertThat("/tmp/filename").isEqualTo(StringUtil.replace("$PROJECT_FILE$/filename", Collections.singletonList("$PROJECT_FILE$"), Collections.singletonList("/tmp")));
//    assertThat("/someTextBefore/tmp/filename").isEqualTo(StringUtil.replace("/someTextBefore/$PROJECT_FILE$/filename", Collections.singletonList("$PROJECT_FILE$"), Collections.singletonList("tmp")));
//  }
//
//  @Test
//  public void testReplaceReturnTheSameStringIfNothingToReplace() {
//    String s = "/tmp/filename";
//    assertThat(StringUtil.replace(s, "$PROJECT_FILE$/filename", "$PROJECT_FILE$")).isSameAs(s);
//  }

  @Test
  public void testEqualsIgnoreWhitespaces() {
    assertTrue(StringUtil.equalsIgnoreWhitespaces(null, null));
    assertFalse(StringUtil.equalsIgnoreWhitespaces("", null));

    assertTrue(StringUtil.equalsIgnoreWhitespaces("", ""));
    assertTrue(StringUtil.equalsIgnoreWhitespaces("\n\t ", ""));
    assertTrue(StringUtil.equalsIgnoreWhitespaces("", "\t\n \n\t"));
    assertTrue(StringUtil.equalsIgnoreWhitespaces("\t", "\n"));

    assertTrue(StringUtil.equalsIgnoreWhitespaces("x", " x"));
    assertTrue(StringUtil.equalsIgnoreWhitespaces("x", "x "));
    assertTrue(StringUtil.equalsIgnoreWhitespaces("x\n", "x"));

    assertTrue(StringUtil.equalsIgnoreWhitespaces("abc", "a\nb\nc\n"));
    assertTrue(StringUtil.equalsIgnoreWhitespaces("x y x", "x y x"));
    assertTrue(StringUtil.equalsIgnoreWhitespaces("xyx", "x y x"));

    assertFalse(StringUtil.equalsIgnoreWhitespaces("x", "\t\n "));
    assertFalse(StringUtil.equalsIgnoreWhitespaces("", " x "));
    assertFalse(StringUtil.equalsIgnoreWhitespaces("", "x "));
    assertFalse(StringUtil.equalsIgnoreWhitespaces("", " x"));
    assertFalse(StringUtil.equalsIgnoreWhitespaces("xyx", "xxx"));
    assertFalse(StringUtil.equalsIgnoreWhitespaces("xyx", "xYx"));
  }

  @Test
  public void testStringHashCodeIgnoreWhitespaces() {
    assertTrue(Comparing.equal(StringUtil.stringHashCodeIgnoreWhitespaces(""), StringUtil.stringHashCodeIgnoreWhitespaces("")));
    assertTrue(Comparing.equal(StringUtil.stringHashCodeIgnoreWhitespaces("\n\t "), StringUtil.stringHashCodeIgnoreWhitespaces("")));
    assertTrue(Comparing.equal(StringUtil.stringHashCodeIgnoreWhitespaces(""), StringUtil.stringHashCodeIgnoreWhitespaces("\t\n \n\t")));
    assertTrue(Comparing.equal(StringUtil.stringHashCodeIgnoreWhitespaces("\t"), StringUtil.stringHashCodeIgnoreWhitespaces("\n")));

    assertTrue(Comparing.equal(StringUtil.stringHashCodeIgnoreWhitespaces("x"), StringUtil.stringHashCodeIgnoreWhitespaces(" x")));
    assertTrue(Comparing.equal(StringUtil.stringHashCodeIgnoreWhitespaces("x"), StringUtil.stringHashCodeIgnoreWhitespaces("x ")));
    assertTrue(Comparing.equal(StringUtil.stringHashCodeIgnoreWhitespaces("x\n"), StringUtil.stringHashCodeIgnoreWhitespaces("x")));

    assertTrue(Comparing.equal(StringUtil.stringHashCodeIgnoreWhitespaces("abc"), StringUtil.stringHashCodeIgnoreWhitespaces("a\nb\nc\n")));
    assertTrue(Comparing.equal(StringUtil.stringHashCodeIgnoreWhitespaces("x y x"), StringUtil.stringHashCodeIgnoreWhitespaces("x y x")));
    assertTrue(Comparing.equal(StringUtil.stringHashCodeIgnoreWhitespaces("xyx"), StringUtil.stringHashCodeIgnoreWhitespaces("x y x")));

    assertFalse(Comparing.equal(StringUtil.stringHashCodeIgnoreWhitespaces("x"), StringUtil.stringHashCodeIgnoreWhitespaces("\t\n ")));
    assertFalse(Comparing.equal(StringUtil.stringHashCodeIgnoreWhitespaces(""), StringUtil.stringHashCodeIgnoreWhitespaces(" x ")));
    assertFalse(Comparing.equal(StringUtil.stringHashCodeIgnoreWhitespaces(""), StringUtil.stringHashCodeIgnoreWhitespaces("x ")));
    assertFalse(Comparing.equal(StringUtil.stringHashCodeIgnoreWhitespaces(""), StringUtil.stringHashCodeIgnoreWhitespaces(" x")));
    assertFalse(Comparing.equal(StringUtil.stringHashCodeIgnoreWhitespaces("xyx"), StringUtil.stringHashCodeIgnoreWhitespaces("xxx")));
    assertFalse(Comparing.equal(StringUtil.stringHashCodeIgnoreWhitespaces("xyx"), StringUtil.stringHashCodeIgnoreWhitespaces("xYx")));
  }

  @Test
  public void testContains() {
    assertTrue(StringUtil.contains("1", "1"));
    assertFalse(StringUtil.contains("1", "12"));
    assertTrue(StringUtil.contains("12", "1"));
    assertTrue(StringUtil.contains("12", "2"));
  }

  @Test
  public void testDetectSeparators() {
    assertNull(StringUtil.detectSeparators(""));
    assertNull(StringUtil.detectSeparators("asd"));
    assertNull(StringUtil.detectSeparators("asd\t"));

    assertEquals(LineSeparator.LF, StringUtil.detectSeparators("asd\n"));
    assertEquals(LineSeparator.LF, StringUtil.detectSeparators("asd\nads\r"));
    assertEquals(LineSeparator.LF, StringUtil.detectSeparators("asd\nads\n"));

    assertEquals(LineSeparator.CR, StringUtil.detectSeparators("asd\r"));
    assertEquals(LineSeparator.CR, StringUtil.detectSeparators("asd\rads\r"));
    assertEquals(LineSeparator.CR, StringUtil.detectSeparators("asd\rads\n"));

    assertEquals(LineSeparator.CRLF, StringUtil.detectSeparators("asd\r\n"));
    assertEquals(LineSeparator.CRLF, StringUtil.detectSeparators("asd\r\nads\r"));
    assertEquals(LineSeparator.CRLF, StringUtil.detectSeparators("asd\r\nads\n"));
  }

  @Test
  public void testFindStartingLineSeparator() {
    assertNull(StringUtil.getLineSeparatorAt("", -1));
    assertNull(StringUtil.getLineSeparatorAt("", 0));
    assertNull(StringUtil.getLineSeparatorAt("", 1));
    assertNull(StringUtil.getLineSeparatorAt("\nHello", -1));
    assertNull(StringUtil.getLineSeparatorAt("\nHello", 1));
    assertNull(StringUtil.getLineSeparatorAt("\nH\rel\nlo", 6));

    assertEquals(LineSeparator.LF, StringUtil.getLineSeparatorAt("\nHello", 0));
    assertEquals(LineSeparator.LF, StringUtil.getLineSeparatorAt("\nH\rel\nlo", 5));
    assertEquals(LineSeparator.LF, StringUtil.getLineSeparatorAt("Hello\n", 5));

    assertEquals(LineSeparator.CR, StringUtil.getLineSeparatorAt("\rH\r\nello", 0));
    assertEquals(LineSeparator.CR, StringUtil.getLineSeparatorAt("Hello\r", 5));
    assertEquals(LineSeparator.CR, StringUtil.getLineSeparatorAt("Hello\b\r", 6));

    assertEquals(LineSeparator.CRLF, StringUtil.getLineSeparatorAt("\rH\r\nello", 2));
    assertEquals(LineSeparator.CRLF, StringUtil.getLineSeparatorAt("\r\nH\r\nello", 0));
    assertEquals(LineSeparator.CRLF, StringUtil.getLineSeparatorAt("\r\nH\r\nello\r\n", 9));
  }

  @Test
  public void testFormatFileSize() {
    assertEquals("0 B", StringUtil.formatFileSize(0));
    assertEquals("1 B", StringUtil.formatFileSize(1));
    assertEquals("2.15 GB", StringUtil.formatFileSize(Integer.MAX_VALUE));
    assertEquals("9.22 EB", StringUtil.formatFileSize(Long.MAX_VALUE));

    assertEquals("60.1 kB", StringUtil.formatFileSize(60_100));

    assertEquals("1.23 kB", StringUtil.formatFileSize(1_234));
    assertEquals("12.35 kB", StringUtil.formatFileSize(12_345));
    assertEquals("123.46 kB", StringUtil.formatFileSize(123_456));
    assertEquals("1.23 MB", StringUtil.formatFileSize(1234_567));
    assertEquals("12.35 MB", StringUtil.formatFileSize(1_2345_678));
    assertEquals("123.46 MB", StringUtil.formatFileSize(123_456_789));
    assertEquals("1.23 GB", StringUtil.formatFileSize(1_234_567_890));

    assertEquals("999 B", StringUtil.formatFileSize(999));
    assertEquals("1 kB", StringUtil.formatFileSize(1000));
    assertEquals("999.99 kB", StringUtil.formatFileSize(999_994));
    assertEquals("1 MB", StringUtil.formatFileSize(999_995));
    assertEquals("999.99 MB", StringUtil.formatFileSize(999_994_999));
    assertEquals("1 GB", StringUtil.formatFileSize(999_995_000));
    assertEquals("999.99 GB", StringUtil.formatFileSize(999_994_999_999L));
    assertEquals("1 TB", StringUtil.formatFileSize(999_995_000_000L));
  }

  @Test
  public void testFormatDuration() {
    assertEquals("0 ms", StringUtil.formatDuration(0));
    assertEquals("1 ms", StringUtil.formatDuration(1));
    assertEquals("24 d 20 h 31 m 23 s 647 ms", StringUtil.formatDuration(Integer.MAX_VALUE));
    assertEquals("29 ep 6533 ml 3 c 8 yr 9 mo 17 d 7 h 12 m 55 s 807 ms", StringUtil.formatDuration(Long.MAX_VALUE));

    assertEquals("1 m 0 s 100 ms", StringUtil.formatDuration(60100));

    assertEquals("1 s 234 ms", StringUtil.formatDuration(1234));
    assertEquals("12 s 345 ms", StringUtil.formatDuration(12345));
    assertEquals("2 m 3 s 456 ms", StringUtil.formatDuration(123456));
    assertEquals("20 m 34 s 567 ms", StringUtil.formatDuration(1234567));
    assertEquals("3 h 25 m 45 s 678 ms", StringUtil.formatDuration(12345678));
    assertEquals("1 d 10 h 17 m 36 s 789 ms", StringUtil.formatDuration(123456789));
    assertEquals("14 d 6 h 56 m 7 s 890 ms", StringUtil.formatDuration(1234567890));

    assertEquals("1 yr 1 mo 1 d 1 h 1 m 1 s 1 ms", StringUtil.formatDuration(33786061001L));
  }
  
  @Test
  public void testGetPackageName() {
    assertEquals("java.lang", StringUtil.getPackageName("java.lang.String"));
    assertEquals("java.util.Map", StringUtil.getPackageName("java.util.Map.Entry"));
    assertEquals("Map", StringUtil.getPackageName("Map.Entry"));
    assertEquals("", StringUtil.getPackageName("Number"));
  }

  @Test
  public void testIndexOf_1() {
    char[] chars = new char[]{'a','b','c','d','a','b','c','d','A','B','C','D'};
    assertEquals(2, StringUtil.indexOf(chars, 'c', 0, 12, false));
    assertEquals(2, StringUtil.indexOf(chars, 'C', 0, 12, false));
    assertEquals(10, StringUtil.indexOf(chars, 'C', 0, 12, true));
    assertEquals(2, StringUtil.indexOf(chars, 'c', -42, 99, false));
  }


  @Test
  public void testIndexOf_2() {
    assertEquals(1, StringUtil.indexOf("axaxa", 'x', 0, 5));
    assertEquals(2, StringUtil.indexOf("abcd", 'c', -42, 99));
  }


  @Test
  public void testIndexOf_3() {
    assertEquals(1, StringUtil.indexOf("axaXa", 'x', 0, 5, false));
    assertEquals(3, StringUtil.indexOf("axaXa", 'X', 0, 5, true));
    assertEquals(2, StringUtil.indexOf("abcd", 'c', -42, 99, false));
  }

  @Test
  public void testIndexOfAny() {
    assertEquals(1, StringUtil.indexOfAny("axa", "x", 0, 5));
    assertEquals(1, StringUtil.indexOfAny("axa", "zx", 0, 5));
    assertEquals(2, StringUtil.indexOfAny("abcd", "c", -42, 99));
  }

  @Test
  public void testLastIndexOf() {
    assertEquals(1, StringUtil.lastIndexOf("axaxa", 'x', 0, 2));
    assertEquals(1, StringUtil.lastIndexOf("axaxa", 'x', 0, 3));
    assertEquals(3, StringUtil.lastIndexOf("axaxa", 'x', 0, 5));
    assertEquals(2, StringUtil.lastIndexOf("abcd", 'c', -42, 99));  // #IDEA-144968
  }


  @Test
  public void testCountChars() {
    assertEquals(0, StringUtil.countChars("abcdefgh", 'x'));
    assertEquals(1, StringUtil.countChars("abcdefgh", 'd'));
    assertEquals(5, StringUtil.countChars("abcddddefghd", 'd'));
    assertEquals(4, StringUtil.countChars("abcddddefghd", 'd', 4, false));
    assertEquals(3, StringUtil.countChars("abcddddefghd", 'd', 4, true));
    assertEquals(2, StringUtil.countChars("abcddddefghd", 'd', 4, 6, false));
    assertEquals(3, StringUtil.countChars("aaabcddddefghdaaaa", 'a', -20, 20, true));
    assertEquals(4, StringUtil.countChars("aaabcddddefghdaaaa", 'a', 20, -20, true));
  }

  @Test
  public void testSubstringBeforeLast() {
    assertEquals("a", StringUtil.substringBeforeLast("abc", "b"));
    assertEquals("abab", StringUtil.substringBeforeLast("ababbccc", "b"));
    assertEquals("abc", StringUtil.substringBeforeLast("abc", ""));
    assertEquals("abc", StringUtil.substringBeforeLast("abc", "1"));
    assertEquals("", StringUtil.substringBeforeLast("", "1"));
  }

  @Test
  public void testSubstringAfterLast() {
    assertEquals("c", StringUtil.substringAfterLast("abc", "b"));
    assertEquals("ccc", StringUtil.substringAfterLast("ababbccc", "b"));
    assertEquals("", StringUtil.substringAfterLast("abc", ""));
    assertNull(StringUtil.substringAfterLast("abc", "1"));
    assertNull(StringUtil.substringAfterLast("", "1"));
  }

  @Test
  public void testGetWordIndicesIn() {
    assertEquals(Arrays.asList(new TextRange(0, 5), new TextRange(6, 12)), StringUtil.getWordIndicesIn("first second"));
    assertEquals(Arrays.asList(new TextRange(1, 6), new TextRange(7, 13)), StringUtil.getWordIndicesIn(" first second"));
    assertEquals(Arrays.asList(new TextRange(1, 6), new TextRange(7, 13)), StringUtil.getWordIndicesIn(" first second    "));
    assertEquals(Arrays.asList(new TextRange(0, 5), new TextRange(6, 12)), StringUtil.getWordIndicesIn("first:second"));
    assertEquals(Arrays.asList(new TextRange(0, 5), new TextRange(6, 12)), StringUtil.getWordIndicesIn("first-second"));
    assertEquals(Arrays.asList(new TextRange(0, 12)), StringUtil.getWordIndicesIn("first-second", ContainerUtil.set(' ', '_', '.')));
    assertEquals(Arrays.asList(new TextRange(0, 5), new TextRange(6, 12)),
                 StringUtil.getWordIndicesIn("first-second", ContainerUtil.set('-')));
  }

  @Test
  public void testIsLatinAlphanumeric() {
    assertTrue(StringUtil.isLatinAlphanumeric("1234567890"));
    assertTrue(StringUtil.isLatinAlphanumeric("123abc593"));
    assertTrue(StringUtil.isLatinAlphanumeric("gwengioewn"));
    assertTrue(StringUtil.isLatinAlphanumeric("FiwnFWinfs"));
    assertTrue(StringUtil.isLatinAlphanumeric("b"));
    assertTrue(StringUtil.isLatinAlphanumeric("1"));

    assertFalse(StringUtil.isLatinAlphanumeric("йцукен"));
    assertFalse(StringUtil.isLatinAlphanumeric("ЙцуTYuio"));
    assertFalse(StringUtil.isLatinAlphanumeric("йцу626кен"));
    assertFalse(StringUtil.isLatinAlphanumeric("12 12"));
    assertFalse(StringUtil.isLatinAlphanumeric("."));
    assertFalse(StringUtil.isLatinAlphanumeric("_"));
    assertFalse(StringUtil.isLatinAlphanumeric("-"));
    assertFalse(StringUtil.isLatinAlphanumeric("fhu384 "));
    assertFalse(StringUtil.isLatinAlphanumeric(""));
    assertFalse(StringUtil.isLatinAlphanumeric(null));
    assertFalse(StringUtil.isLatinAlphanumeric("'"));
  }

  @Test
  public void testIsShortNameOf() {
    assertTrue(StringUtil.isShortNameOf("a.b.c", "c"));
    assertTrue(StringUtil.isShortNameOf("foo", "foo"));
    assertFalse(StringUtil.isShortNameOf("foo", ""));
    assertFalse(StringUtil.isShortNameOf("", "foo"));
    assertFalse(StringUtil.isShortNameOf("a.b.c", "d"));
    assertFalse(StringUtil.isShortNameOf("x.y.zzz", "zz"));
    assertFalse(StringUtil.isShortNameOf("x", "a.b.x"));
  }

  @Test
  public void startsWith() {
    assertTrue(StringUtil.startsWith("abcdefgh", 5, "fgh"));
    assertTrue(StringUtil.startsWith("abcdefgh", 2, "cde"));
    assertTrue(StringUtil.startsWith("abcdefgh", 0, "abc"));
    assertTrue(StringUtil.startsWith("abcdefgh", 0, "abcdefgh"));
    assertFalse(StringUtil.startsWith("abcdefgh", 5, "cde"));

    assertTrue(StringUtil.startsWith("abcdefgh", 0, ""));
    assertTrue(StringUtil.startsWith("abcdefgh", 4, ""));
    assertTrue(StringUtil.startsWith("abcdefgh", 7, ""));
    assertTrue(StringUtil.startsWith("abcdefgh", 8, ""));

    assertTrue(StringUtil.startsWith("", 0, ""));

    assertFalse(StringUtil.startsWith("ab", 0, "abcdefgh"));
    assertFalse(StringUtil.startsWith("ab", 1, "abcdefgh"));
    assertFalse(StringUtil.startsWith("ab", 2, "abcdefgh"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void startsWithNegativeIndex() {
    StringUtil.startsWith("abcdefgh", -1, "");
  }

  @Test(expected = IllegalArgumentException.class)
  public void startsWithIndexGreaterThanLength() {
    StringUtil.startsWith("abcdefgh", 9, "");
  }

  @Test(expected = IllegalArgumentException.class)
  public void startsWithEmptyStringNegativeIndex() {
    StringUtil.startsWith("", -1, "");
  }

  @Test(expected = IllegalArgumentException.class)
  public void startsWithEmptyStringIndexGreaterThanLength() {
    StringUtil.startsWith("", 1, "");
  }

  @Test(expected = IllegalArgumentException.class)
  public void startsWithLongerSuffixNegativeIndex() {
    StringUtil.startsWith("ab", -1, "abcdefgh");
  }

  @Test(expected = IllegalArgumentException.class)
  public void startsWithLongerSuffixIndexGreaterThanLength() {
    StringUtil.startsWith("ab", 3, "abcdefgh");
  }

  @Test
  public void offsetToLineNumberCol() {
    assertEquals(LineColumn.of(0, 0), StringUtil.offsetToLineColumn("abc\nabc", 0));
    assertEquals(LineColumn.of(0, 1), StringUtil.offsetToLineColumn("abc\nabc", 1));
    assertEquals(LineColumn.of(0, 2), StringUtil.offsetToLineColumn("abc\nabc", 2));
    assertEquals(LineColumn.of(0, 3), StringUtil.offsetToLineColumn("abc\nabc", 3));
    assertEquals(LineColumn.of(1, 0), StringUtil.offsetToLineColumn("abc\nabc", 4));
    assertEquals(LineColumn.of(1, 1), StringUtil.offsetToLineColumn("abc\nabc", 5));
    assertEquals(LineColumn.of(1, 3), StringUtil.offsetToLineColumn("abc\nabc", 7));
    assertNull(StringUtil.offsetToLineColumn("abc\nabc", 8));
    assertEquals(LineColumn.of(0, 3), StringUtil.offsetToLineColumn("abc\r\nabc", 3));
    assertEquals(LineColumn.of(1, 0), StringUtil.offsetToLineColumn("abc\r\nabc", 5));
    assertEquals(LineColumn.of(2, 1), StringUtil.offsetToLineColumn("abc\n\nabc", 6));
    assertEquals(LineColumn.of(1, 1), StringUtil.offsetToLineColumn("abc\r\nabc", 6));
  }
}