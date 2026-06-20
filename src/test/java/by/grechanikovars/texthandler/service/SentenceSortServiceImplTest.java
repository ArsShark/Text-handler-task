package by.grechanikovars.texthandler.service;

import by.grechanikovars.texthandler.entity.TextComponent;
import by.grechanikovars.texthandler.exception.TextHandlerException;
import by.grechanikovars.texthandler.parser.impl.CharacterHandler;
import by.grechanikovars.texthandler.parser.impl.LexemeHandler;
import by.grechanikovars.texthandler.parser.impl.ParagraphHandler;
import by.grechanikovars.texthandler.parser.impl.SentenceHandler;
import by.grechanikovars.texthandler.service.impl.SentenceSortServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SentenceSortServiceImplTest {
  private static final String TEXT_SORT_BY_A = "Aaaa. Aa. B.";

  private SentenceSortService service;
  private ParagraphHandler paragraphHandler;

  @BeforeEach
  void setUp() {
    service = new SentenceSortServiceImpl();

    CharacterHandler characterHandler = new CharacterHandler();
    LexemeHandler lexemeHandler = new LexemeHandler();
    SentenceHandler sentenceHandler = new SentenceHandler();
    paragraphHandler = new ParagraphHandler();
    paragraphHandler.setNext(sentenceHandler);
    sentenceHandler.setNext(lexemeHandler);
    lexemeHandler.setNext(characterHandler);
  }
  static Stream<Arguments> provideTextsAndLetters() {
    return Stream.of(
            Arguments.of("One sentence.", 'e', 1),
            Arguments.of("First. Second.", 's', 2),
            Arguments.of("One. Two. Three.", 't', 3)
    );
  }

  @ParameterizedTest
  @MethodSource("provideTextsAndLetters")
  void testSortedSizeMatchesTotalSentences(String text, char letter, int expectedSize)
          throws TextHandlerException {
    // given
    TextComponent parsed = paragraphHandler.handle(text);
    // when
    List<TextComponent> actual = service.sortSentencesByLetterCount(parsed, letter);
    // then
    assertEquals(expectedSize, actual.size());
  }

  @Test
  void testReturnsSameNumberOfSentences() throws TextHandlerException {
    TextComponent parsed = paragraphHandler.handle(TEXT_SORT_BY_A);
    int expected = 3;

    List<TextComponent> actual = service.sortSentencesByLetterCount(parsed, 'a');

    assertEquals(expected, actual.size());
  }

  @Test
  void testFirstSentenceHasFewestTargetLetters() throws TextHandlerException {
    TextComponent parsed = paragraphHandler.handle(TEXT_SORT_BY_A);

    List<TextComponent> sorted = service.sortSentencesByLetterCount(parsed, 'a');

    String firstSentence = sorted.get(0).toString();
    assertFalse(firstSentence.toLowerCase().contains("a"));
  }

  @Test
  void testLastSentenceHasMostTargetLetters() throws TextHandlerException {
    TextComponent parsed = paragraphHandler.handle(TEXT_SORT_BY_A);

    List<TextComponent> sorted = service.sortSentencesByLetterCount(parsed, 'a');

    int lastIndex = sorted.size() - 1;
    String lastSentence = sorted.get(lastIndex).toString();
    assertTrue(lastSentence.startsWith("Aaaa"));
  }

  @Test
  void testResultIsNotEmpty() throws TextHandlerException {

    TextComponent parsed = paragraphHandler.handle(TEXT_SORT_BY_A);

    List<TextComponent> actual = service.sortSentencesByLetterCount(parsed, 'a');

    assertFalse(actual.isEmpty());
  }
}