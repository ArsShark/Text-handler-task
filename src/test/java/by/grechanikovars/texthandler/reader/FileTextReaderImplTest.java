package by.grechanikovars.texthandler.reader;

import by.grechanikovars.texthandler.exception.TextHandlerException;
import by.grechanikovars.texthandler.reader.impl.FileTextReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FileTextReaderImplTest {

  private static final String TEST_FILE        = "test-text.txt";
  private static final String MISSING_FILE     = "data/this_file_does_not_exist.txt";
  private static final String EXPECTED_CONTENT = "Hello world. Bye now.";

  private TextReader reader;

  @BeforeEach
  void setUp() {
    reader = new FileTextReader();
  }

  private String resolveTestFilePath(String resourceName) throws URISyntaxException {
    URL resource = getClass().getClassLoader().getResource(resourceName);
    if (resource == null) {
      throw new IllegalStateException("Test resource not found: " + resourceName);
    }
    return Paths.get(resource.toURI()).toString();
  }

  @Test
  void testReadTextReturnsNonEmptyString() throws TextHandlerException, URISyntaxException {
    // given
    String path = resolveTestFilePath(TEST_FILE);
    // when
    String actual = reader.readText(path);
    // then
    assertFalse(actual.isBlank());
  }

  @Test
  void testReadTextContainsExpectedContent() throws TextHandlerException, URISyntaxException {
    // given
    String path = resolveTestFilePath(TEST_FILE);
    // when
    String actual = reader.readText(path);
    // then
    assertTrue(actual.contains(EXPECTED_CONTENT));
  }

  @Test
  void testReadTextContainsBothParagraphs() throws TextHandlerException, URISyntaxException {
    // given
    String path = resolveTestFilePath(TEST_FILE);
    // when
    String actual = reader.readText(path);
    // then
    assertAll(
            () -> assertTrue(actual.contains("Hello world.")),
            () -> assertTrue(actual.contains("Second paragraph here."))
    );
  }

  @Test
  void testReadTextPreservesNewlines() throws TextHandlerException, URISyntaxException {
    // given
    String path = resolveTestFilePath(TEST_FILE);
    // when
    String actual = reader.readText(path);
    // then — paragraphs separated by blank line means two newlines present
    assertTrue(actual.contains("\n"));
  }

  @Test
  void testReadMissingFileThrowsTextHandlerException() {
    // given
    // when & then
    assertThrows(TextHandlerException.class, () -> reader.readText(MISSING_FILE));
  }

  @Test
  void testReadEmptyPathThrowsTextHandlerException() {
    // given
    String path = "";
    // when & then
    assertThrows(TextHandlerException.class, () -> reader.readText(path));
  }

  @Test
  void testReadTextReturnsSameContentOnMultipleCalls()
          throws TextHandlerException, URISyntaxException {
    // given
    String path = resolveTestFilePath(TEST_FILE);
    // when
    String firstRead  = reader.readText(path);
    String secondRead = reader.readText(path);
    // then
    assertEquals(firstRead, secondRead);
  }

  @ParameterizedTest
  @MethodSource("provideInvalidPaths")
  void testReadInvalidPathThrowsTextHandlerException(String path) {
    // given — path from method source
    // when & then
    assertThrows(TextHandlerException.class, () -> reader.readText(path));
  }

  static Stream<Arguments> provideInvalidPaths() {
    return Stream.of(
            Arguments.of("data/nonexistent.txt"),
            Arguments.of("completely/wrong/path.txt"),
            Arguments.of("missing_file.txt")
    );
  }
}