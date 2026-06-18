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

  // "Aaaa." has 4 'a', "Aa." has 2 'a', "B." has 0 'a'
  // ascending: "B." → "Aa." → "Aaaa."
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

  @Test
  void testReturnsSameNumberOfSentences() throws TextHandlerException {
    // given
    TextComponent parsed = paragraphHandler.handle(TEXT_SORT_BY_A);
    int expected = 3;
    // when
    List<TextComponent> actual = service.sortSentencesByLetterCount(parsed, 'a');
    // then
    assertEquals(expected, actual.size());
  }

  @Test
  void testFirstSentenceHasFewestTargetLetters() throws TextHandlerException {
    // given
    TextComponent parsed = paragraphHandler.handle(TEXT_SORT_BY_A);
    // when
    List<TextComponent> sorted = service.sortSentencesByLetterCount(parsed, 'a');
    // then — "B." contains 0 'a', so it comes first
    String firstSentence = sorted.get(0).toString();
    assertFalse(firstSentence.toLowerCase().contains("a"));
  }

  @Test
  void testLastSentenceHasMostTargetLetters() throws TextHandlerException {
    // given
    TextComponent parsed = paragraphHandler.handle(TEXT_SORT_BY_A);
    // when
    List<TextComponent> sorted = service.sortSentencesByLetterCount(parsed, 'a');
    // then — "Aaaa." has 4 'a' letters, must be last
    int lastIndex = sorted.size() - 1;
    String lastSentence = sorted.get(lastIndex).toString();
    assertTrue(lastSentence.startsWith("Aaaa"));
  }

  @Test
  void testSortedListIsInAscendingOrder() throws TextHandlerException {
    // given
    TextComponent parsed = paragraphHandler.handle(TEXT_SORT_BY_A);
    // when
    List<TextComponent> sorted = service.sortSentencesByLetterCount(parsed, 'a');
    // then — each sentence has >= 'a' count than the previous one
    for (int i = 0; i < sorted.size() - 1; i++) {
      int currentCount = countLetter(sorted.get(i).toString(), 'a');
      int nextCount = countLetter(sorted.get(i + 1).toString(), 'a');
      assertTrue(currentCount <= nextCount);
    }
  }

  @Test
  void testResultIsNotEmpty() throws TextHandlerException {
    // given
    TextComponent parsed = paragraphHandler.handle(TEXT_SORT_BY_A);
    // when
    List<TextComponent> actual = service.sortSentencesByLetterCount(parsed, 'a');
    // then
    assertFalse(actual.isEmpty());
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

  static Stream<Arguments> provideTextsAndLetters() {
    return Stream.of(
            Arguments.of("One sentence.", 'e', 1),
            Arguments.of("First. Second.", 's', 2),
            Arguments.of("One. Two. Three.", 't', 3)
    );
  }

  private int countLetter(String text, char letter) {
    String lower = text.toLowerCase();
    char lowerLetter = Character.toLowerCase(letter);
    int count = 0;
    for (int i = 0; i < lower.length(); i++) {
      if (lower.charAt(i) == lowerLetter) {
        count++;
      }
    }
    return count;
  }
}