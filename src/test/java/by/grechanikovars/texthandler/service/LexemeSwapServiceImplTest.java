package by.grechanikovars.texthandler.service;

import by.grechanikovars.texthandler.entity.TextComponent;
import by.grechanikovars.texthandler.exception.TextHandlerException;
import by.grechanikovars.texthandler.parser.impl.CharacterHandler;
import by.grechanikovars.texthandler.parser.impl.LexemeHandler;
import by.grechanikovars.texthandler.parser.impl.ParagraphHandler;
import by.grechanikovars.texthandler.parser.impl.SentenceHandler;
import by.grechanikovars.texthandler.service.impl.LexemeSwapServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LexemeSwapServiceImplTest {

  // "Hello world." → after swap → "world. Hello"
  private static final String TWO_LEXEME_SENTENCE    = "Hello world.";
  private static final String EXPECTED_TWO_LEXEME    = "world. Hello";

  // "One two three." → after swap → "three. two One"
  private static final String THREE_LEXEME_SENTENCE  = "One two three.";
  private static final String EXPECTED_THREE_LEXEME  = "three. two One";

  // Single-lexeme sentence should stay unchanged
  private static final String SINGLE_LEXEME_SENTENCE = "Hello.";

  private LexemeSwapService service;
  private ParagraphHandler paragraphHandler;

  @BeforeEach
  void setUp() {
    service = new LexemeSwapServiceImpl();

    CharacterHandler characterHandler = new CharacterHandler();
    LexemeHandler lexemeHandler = new LexemeHandler();
    SentenceHandler sentenceHandler = new SentenceHandler();
    paragraphHandler = new ParagraphHandler();
    paragraphHandler.setNext(sentenceHandler);
    sentenceHandler.setNext(lexemeHandler);
    lexemeHandler.setNext(characterHandler);
  }

  @Test
  void testFirstAndLastLexemeSwappedInTwoLexemeSentence() throws TextHandlerException {
    // given
    TextComponent parsed = paragraphHandler.handle(TWO_LEXEME_SENTENCE);
    // when
    service.swapFirstAndLastLexeme(parsed);
    // then
    String actual = parsed.toString();
    assertEquals(EXPECTED_TWO_LEXEME, actual);
  }

  @Test
  void testFirstAndLastLexemeSwappedInThreeLexemeSentence() throws TextHandlerException {
    // given
    TextComponent parsed = paragraphHandler.handle(THREE_LEXEME_SENTENCE);
    // when
    service.swapFirstAndLastLexeme(parsed);
    // then
    String actual = parsed.toString();
    assertEquals(EXPECTED_THREE_LEXEME, actual);
  }

  @Test
  void testSingleLexemeSentenceRemainsUnchanged() throws TextHandlerException {
    // given
    TextComponent parsed = paragraphHandler.handle(SINGLE_LEXEME_SENTENCE);
    // when
    service.swapFirstAndLastLexeme(parsed);
    // then
    String actual = parsed.toString();
    assertEquals(SINGLE_LEXEME_SENTENCE, actual);
  }

  @Test
  void testLexemeCountRemainsUnchangedAfterSwap() throws TextHandlerException {
    // given
    TextComponent parsed = paragraphHandler.handle(THREE_LEXEME_SENTENCE);
    TextComponent paragraph = parsed.getChildren().get(0);
    TextComponent sentence = paragraph.getChildren().get(0);
    int expectedLexemeCount = sentence.getChildren().size();
    // when
    service.swapFirstAndLastLexeme(parsed);
    // then — number of lexemes must not change
    int actual = sentence.getChildren().size();
    assertEquals(expectedLexemeCount, actual);
  }

  @Test
  void testMultipleSentencesAllGetSwapped() throws TextHandlerException {
    // given — two sentences, each gets swapped
    String text = "Hello world. Foo bar.";
    TextComponent parsed = paragraphHandler.handle(text);
    // when
    service.swapFirstAndLastLexeme(parsed);
    // then
    String actual = parsed.toString();
    assertAll(
            () -> assertTrue(actual.contains("world.")),
            () -> assertTrue(actual.contains("bar."))
    );
  }

  @Test
  void testSwapIsAppliedAcrossParagraphs() throws TextHandlerException {
    // given — two paragraphs, each with one sentence of two lexemes
    String text = "Alpha beta.\n\nGamma delta.";
    TextComponent parsed = paragraphHandler.handle(text);
    // when
    service.swapFirstAndLastLexeme(parsed);
    // then
    String actual = parsed.toString();
    assertAll(
            () -> assertTrue(actual.contains("beta.")),
            () -> assertTrue(actual.contains("delta."))
    );
  }
}