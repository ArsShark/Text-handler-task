package by.grechanikovars.texthandler.service;

import by.grechanikovars.texthandler.entity.TextComponent;
import by.grechanikovars.texthandler.exception.TextHandlerException;
import by.grechanikovars.texthandler.parser.impl.CharacterHandler;
import by.grechanikovars.texthandler.parser.impl.LexemeHandler;
import by.grechanikovars.texthandler.parser.impl.ParagraphHandler;
import by.grechanikovars.texthandler.parser.impl.SentenceHandler;
import by.grechanikovars.texthandler.service.impl.TextRestoringServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TextRestoringServiceImplTest {

  private static final String SINGLE_SENTENCE    = "Hello world.";
  private static final String TWO_SENTENCES      = "Hello world. Bye now.";
  private static final String TWO_PARAGRAPHS     = "First sentence.\n\nSecond sentence.";

  private TextRestoringService service;
  private ParagraphHandler paragraphHandler;

  @BeforeEach
  void setUp() {
    service = new TextRestoringServiceImpl();

    CharacterHandler characterHandler = new CharacterHandler();
    LexemeHandler lexemeHandler = new LexemeHandler();
    SentenceHandler sentenceHandler = new SentenceHandler();
    paragraphHandler = new ParagraphHandler();
    paragraphHandler.setNext(sentenceHandler);
    sentenceHandler.setNext(lexemeHandler);
    lexemeHandler.setNext(characterHandler);
  }

  @Test
  void testRestoreSingleSentence() throws TextHandlerException {
    // given
    TextComponent parsed = paragraphHandler.handle(SINGLE_SENTENCE);
    // when
    String actual = service.restore(parsed);
    // then
    assertEquals(SINGLE_SENTENCE, actual);
  }

  @Test
  void testRestoreTwoSentences() throws TextHandlerException {
    // given
    TextComponent parsed = paragraphHandler.handle(TWO_SENTENCES);
    // when
    String actual = service.restore(parsed);
    // then
    assertEquals(TWO_SENTENCES, actual);
  }

  @Test
  void testRestoreTwoParagraphs() throws TextHandlerException {
    // given
    TextComponent parsed = paragraphHandler.handle(TWO_PARAGRAPHS);
    // when
    String actual = service.restore(parsed);
    // then
    assertEquals(TWO_PARAGRAPHS, actual);
  }

  @Test
  void testRestoredTextIsNotEmpty() throws TextHandlerException {
    // given
    TextComponent parsed = paragraphHandler.handle(SINGLE_SENTENCE);
    // when
    String actual = service.restore(parsed);
    // then
    assertFalse(actual.isBlank());
  }

  @ParameterizedTest
  @MethodSource("provideTexts")
  void testRestoreParametrized(String text) throws TextHandlerException {
    // given
    TextComponent parsed = paragraphHandler.handle(text);
    // when
    String actual = service.restore(parsed);
    // then
    assertEquals(text, actual);
  }

  static Stream<Arguments> provideTexts() {
    return Stream.of(
            Arguments.of("One sentence."),
            Arguments.of("First. Second."),
            Arguments.of("Paragraph one.\n\nParagraph two.")
    );
  }
}