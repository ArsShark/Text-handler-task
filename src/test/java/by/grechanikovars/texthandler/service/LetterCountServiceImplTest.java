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

  @Test
  void testCountLettersShortText() throws TextHandlerException {
    // given
    TextComponent parsed = paragraphHandler.handle(SHORT_TEXT);
    // when
    int actual = service.countLetters(parsed);
    // then
    assertEquals(EXPECTED_LETTERS, actual);
  }

  @Test
  void testCountSymbolsShortText() throws TextHandlerException {
    // given
    TextComponent parsed = paragraphHandler.handle(SHORT_TEXT);
    // when
    int actual = service.countSymbols(parsed);
    // then
    assertEquals(EXPECTED_SYMBOLS, actual);
  }

  @Test
  void testCountLettersHelloWorld() throws TextHandlerException {
    // given
    TextComponent parsed = paragraphHandler.handle(TEXT_HELLO);
    // when
    int actual = service.countLetters(parsed);
    // then
    assertEquals(HELLO_LETTERS, actual);
  }

  @Test
  void testCountSymbolsHelloWorld() throws TextHandlerException {
    // given
    TextComponent parsed = paragraphHandler.handle(TEXT_HELLO);
    // when
    int actual = service.countSymbols(parsed);
    // then
    assertEquals(HELLO_SYMBOLS, actual);
  }

  @Test
  void testLetterCountLessThanOrEqualToSymbolCount() throws TextHandlerException {
    // given
    TextComponent parsed = paragraphHandler.handle(TEXT_HELLO);
    // when
    int letters = service.countLetters(parsed);
    int symbols = service.countSymbols(parsed);
    // then
    assertTrue(letters <= symbols);
  }

  @Test
  void testBothCountsArePositive() throws TextHandlerException {
    // given
    TextComponent parsed = paragraphHandler.handle(SHORT_TEXT);
    // when
    int letters = service.countLetters(parsed);
    int symbols = service.countSymbols(parsed);
    // then
    assertAll(
            () -> assertTrue(letters > 0),
            () -> assertTrue(symbols > 0)
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

  static Stream<Arguments> provideTextsAndExpectedLetters() {
    return Stream.of(
            Arguments.of("Hi.",           2),
            Arguments.of("Hello world.", 10),
            Arguments.of("A.",            1),
            Arguments.of("No.",           2)
    );
  }
}