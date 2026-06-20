package by.grechanikovars.texthandler.service;

import by.grechanikovars.texthandler.entity.TextComponent;
import by.grechanikovars.texthandler.exception.TextHandlerException;
import by.grechanikovars.texthandler.parser.impl.CharacterHandler;
import by.grechanikovars.texthandler.parser.impl.LexemeHandler;
import by.grechanikovars.texthandler.parser.impl.ParagraphHandler;
import by.grechanikovars.texthandler.parser.impl.SentenceHandler;
import by.grechanikovars.texthandler.service.impl.LetterCountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LetterCountServiceImplTest {

  private static final String SHORT_TEXT       = "Hi.";
  private static final int    EXPECTED_LETTERS = 2;
  private static final int    EXPECTED_SYMBOLS = 3;

  private static final String TEXT_HELLO       = "Hello world.";
  private static final int    HELLO_LETTERS    = 10;
  private static final int    HELLO_SYMBOLS    = 11;

  private LetterCountService service;
  private ParagraphHandler paragraphHandler;

  @BeforeEach
  void setUp() {
    service = new LetterCountServiceImpl();

    CharacterHandler characterHandler = new CharacterHandler();
    LexemeHandler lexemeHandler = new LexemeHandler();
    SentenceHandler sentenceHandler = new SentenceHandler();
    paragraphHandler = new ParagraphHandler();
    paragraphHandler.setNext(sentenceHandler);
    sentenceHandler.setNext(lexemeHandler);
    lexemeHandler.setNext(characterHandler);
  }

  static Stream<Arguments> provideTextsAndExpectedLetters() {
    return Stream.of(
            Arguments.of("Hi.",           2),
            Arguments.of("Hello world.", 10),
            Arguments.of("A.",            1),
            Arguments.of("No.",           2)
    );
  }

  @ParameterizedTest
  @MethodSource("provideTextsAndExpectedLetters")
  void testCountLettersParametrized(String text, int expected) throws TextHandlerException {
    // given
    TextComponent parsed = paragraphHandler.handle(text);
    // when
    int actual = service.countLetters(parsed);
    // then
    assertEquals(expected, actual);
  }

  @Test
  void testCountLettersShortText() throws TextHandlerException {
    TextComponent parsed = paragraphHandler.handle(SHORT_TEXT);

    int actual = service.countLetters(parsed);

    assertEquals(EXPECTED_LETTERS, actual);
  }

  @Test
  void testCountSymbolsShortText() throws TextHandlerException {
    TextComponent parsed = paragraphHandler.handle(SHORT_TEXT);

    int actual = service.countSymbols(parsed);

    assertEquals(EXPECTED_SYMBOLS, actual);
  }

  @Test
  void testCountLettersHelloWorld() throws TextHandlerException {
    TextComponent parsed = paragraphHandler.handle(TEXT_HELLO);

    int actual = service.countLetters(parsed);

    assertEquals(HELLO_LETTERS, actual);
  }

  @Test
  void testCountSymbolsHelloWorld() throws TextHandlerException {
    TextComponent parsed = paragraphHandler.handle(TEXT_HELLO);

    int actual = service.countSymbols(parsed);

    assertEquals(HELLO_SYMBOLS, actual);
  }

  @Test
  void testLetterCountLessThanOrEqualToSymbolCount() throws TextHandlerException {
    TextComponent parsed = paragraphHandler.handle(TEXT_HELLO);

    int letters = service.countLetters(parsed);
    int symbols = service.countSymbols(parsed);

    assertTrue(letters <= symbols);
  }

  @Test
  void testBothCountsArePositive() throws TextHandlerException {
    TextComponent parsed = paragraphHandler.handle(SHORT_TEXT);

    int letters = service.countLetters(parsed);
    int symbols = service.countSymbols(parsed);

    assertAll(
            () -> assertTrue(letters > 0),
            () -> assertTrue(symbols > 0)
    );
  }
}