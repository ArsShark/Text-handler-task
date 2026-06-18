package by.grechanikovars.texthandler.parser;

import by.grechanikovars.texthandler.entity.TextComponent;
import by.grechanikovars.texthandler.entity.TextComponentType;
import by.grechanikovars.texthandler.exception.TextHandlerException;
import by.grechanikovars.texthandler.parser.impl.CharacterHandler;
import by.grechanikovars.texthandler.parser.impl.LexemeHandler;
import by.grechanikovars.texthandler.parser.impl.ParagraphHandler;
import by.grechanikovars.texthandler.parser.impl.SentenceHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TextHandlerChainTest {

  private static final String TWO_PARAGRAPH_TEXT =
          "Hello world. Bye now.\n\nSecond paragraph here.";
  private static final String SINGLE_SENTENCE = "Hello world.";
  private static final String TWO_SENTENCES    = "Hello world. Bye now.";

  private ParagraphHandler paragraphHandler;

  @BeforeEach
  void setUp() {
    CharacterHandler characterHandler = new CharacterHandler();
    LexemeHandler lexemeHandler = new LexemeHandler();
    SentenceHandler sentenceHandler = new SentenceHandler();
    paragraphHandler = new ParagraphHandler();

    paragraphHandler.setNext(sentenceHandler);
    sentenceHandler.setNext(lexemeHandler);
    lexemeHandler.setNext(characterHandler);
  }

  @Test
  void testRootNodeTypeIsText() throws TextHandlerException {
    // given
    // when
    TextComponent actual = paragraphHandler.handle(SINGLE_SENTENCE);
    // then
    assertEquals(TextComponentType.TEXT, actual.getType());
  }

  @Test
  void testTwoParagraphsProducesTwoChildren() throws TextHandlerException {
    // given
    int expected = 2;
    // when
    TextComponent actual = paragraphHandler.handle(TWO_PARAGRAPH_TEXT);
    // then
    assertEquals(expected, actual.getChildren().size());
  }

  @Test
  void testFirstChildIsParagraph() throws TextHandlerException {
    // given
    // when
    TextComponent root = paragraphHandler.handle(SINGLE_SENTENCE);
    // then
    TextComponent firstChild = root.getChildren().get(0);
    assertEquals(TextComponentType.PARAGRAPH, firstChild.getType());
  }

  @Test
  void testSentenceChildrenAreSentenceType() throws TextHandlerException {
    // given
    // when
    TextComponent root = paragraphHandler.handle(TWO_SENTENCES);
    // then
    TextComponent paragraph = root.getChildren().get(0);
    TextComponent sentence = paragraph.getChildren().get(0);
    assertEquals(TextComponentType.SENTENCE, sentence.getType());
  }

  @Test
  void testTwoSentencesInOneParagraph() throws TextHandlerException {
    // given
    int expected = 2;
    // when
    TextComponent root = paragraphHandler.handle(TWO_SENTENCES);
    // then
    TextComponent paragraph = root.getChildren().get(0);
    int actual = paragraph.getChildren().size();
    assertEquals(expected, actual);
  }

  @Test
  void testLexemeChildrenAreLexemeType() throws TextHandlerException {
    // given
    // when
    TextComponent root = paragraphHandler.handle(SINGLE_SENTENCE);
    // then
    TextComponent paragraph = root.getChildren().get(0);
    TextComponent sentence = paragraph.getChildren().get(0);
    TextComponent lexeme = sentence.getChildren().get(0);
    assertEquals(TextComponentType.LEXEME, lexeme.getType());
  }

  @Test
  void testLeafNodesAreCharacterType() throws TextHandlerException {
    // given
    // when
    TextComponent root = paragraphHandler.handle(SINGLE_SENTENCE);
    // then
    TextComponent paragraph = root.getChildren().get(0);
    TextComponent sentence = paragraph.getChildren().get(0);
    TextComponent lexeme = sentence.getChildren().get(0);
    TextComponent leaf = lexeme.getChildren().get(0);
    assertEquals(TextComponentType.CHARACTER, leaf.getType());
  }

  @Test
  void testRestoredTextMatchesInput() throws TextHandlerException {
    // given
    String text = TWO_SENTENCES;
    // when
    TextComponent root = paragraphHandler.handle(text);
    // then
    String actual = root.toString();
    assertEquals(text, actual);
  }

  @Test
  void testLeafContentMatchesFirstCharOfFirstLexeme() throws TextHandlerException {
    // given
    // when
    TextComponent root = paragraphHandler.handle(SINGLE_SENTENCE);
    // then
    TextComponent paragraph = root.getChildren().get(0);
    TextComponent sentence = paragraph.getChildren().get(0);
    TextComponent firstLexeme = sentence.getChildren().get(0);
    TextComponent firstLeaf = firstLexeme.getChildren().get(0);
    assertEquals('H', firstLeaf.getContent());
  }

  @ParameterizedTest
  @MethodSource("provideTextsAndExpectedParagraphCount")
  void testParagraphCountParametrized(String text, int expectedParagraphs)
          throws TextHandlerException {
    // given — text provided by method source
    // when
    TextComponent root = paragraphHandler.handle(text);
    // then
    int actual = root.getChildren().size();
    assertEquals(expectedParagraphs, actual);
  }

  static Stream<Arguments> provideTextsAndExpectedParagraphCount() {
    return Stream.of(
            Arguments.of("Hello world.", 1),
            Arguments.of("First paragraph.\n\nSecond paragraph.", 2),
            Arguments.of("One.\n\nTwo.\n\nThree.", 3)
    );
  }

  @ParameterizedTest
  @MethodSource("provideTextsAndExpectedSentenceCount")
  void testSentenceCountInFirstParagraphParametrized(String text, int expectedSentences)
          throws TextHandlerException {
    // given — text provided by method source
    // when
    TextComponent root = paragraphHandler.handle(text);
    // then
    TextComponent paragraph = root.getChildren().get(0);
    int actual = paragraph.getChildren().size();
    assertEquals(expectedSentences, actual);
  }

  static Stream<Arguments> provideTextsAndExpectedSentenceCount() {
    return Stream.of(
            Arguments.of("One sentence.", 1),
            Arguments.of("First. Second.", 2),
            Arguments.of("One. Two. Three.", 3)
    );
  }
}