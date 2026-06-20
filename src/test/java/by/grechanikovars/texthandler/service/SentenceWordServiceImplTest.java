package by.grechanikovars.texthandler.service;

import by.grechanikovars.texthandler.entity.TextComponent;
import by.grechanikovars.texthandler.exception.TextHandlerException;
import by.grechanikovars.texthandler.parser.impl.CharacterHandler;
import by.grechanikovars.texthandler.parser.impl.LexemeHandler;
import by.grechanikovars.texthandler.parser.impl.ParagraphHandler;
import by.grechanikovars.texthandler.parser.impl.SentenceHandler;
import by.grechanikovars.texthandler.service.impl.SentenceWordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SentenceWordServiceImplTest {
  private static final String TEXT_THREE_SENTENCES =
          "I like apple. She eats apple. He bought apple.";

  private static final String TEXT_TWO_VS_ONE =
          "I have cat. She has cat. He has dog.";

  private static final String TEXT_NO_COMMON =
          "Alpha comes first. Beta comes second. Gamma comes third.";

  private SentenceWordService service;
  private ParagraphHandler paragraphHandler;

  @BeforeEach
  void setUp() {
    service = new SentenceWordServiceImpl();

    CharacterHandler characterHandler = new CharacterHandler();
    LexemeHandler lexemeHandler = new LexemeHandler();
    SentenceHandler sentenceHandler = new SentenceHandler();
    paragraphHandler = new ParagraphHandler();
    paragraphHandler.setNext(sentenceHandler);
    sentenceHandler.setNext(lexemeHandler);
    lexemeHandler.setNext(characterHandler);
  }

  @Test
  void testWordPresentInAllThreeSentencesReturnsThree() throws TextHandlerException {
    // given
    TextComponent parsed = paragraphHandler.handle(TEXT_THREE_SENTENCES);
    int expected = 3;
    // when
    List<TextComponent> actual = service.findSentencesWithMostCommonWord(parsed);
    // then
    assertEquals(expected, actual.size());
  }

  @Test
  void testMaxCommonWordCountIsTwoWhenTwoVsOne() throws TextHandlerException {
    TextComponent parsed = paragraphHandler.handle(TEXT_TWO_VS_ONE);
    int expected = 2;

    List<TextComponent> actual = service.findSentencesWithMostCommonWord(parsed);

    assertEquals(expected, actual.size());
  }

  @Test
  void testResultIsNotEmpty() throws TextHandlerException {
    TextComponent parsed = paragraphHandler.handle(TEXT_THREE_SENTENCES);

    List<TextComponent> actual = service.findSentencesWithMostCommonWord(parsed);

    assertFalse(actual.isEmpty());
  }

  @Test
  void testAllResultNodesAreSentenceType() throws TextHandlerException {
    TextComponent parsed = paragraphHandler.handle(TEXT_THREE_SENTENCES);

    List<TextComponent> actual = service.findSentencesWithMostCommonWord(parsed);

    for (TextComponent sentence : actual) {
      assertEquals(
              by.grechanikovars.texthandler.entity.TextComponentType.SENTENCE,
              sentence.getType()
      );
    }
  }

  @Test
  void testCommonWordInSingleSentenceReturnsOne() throws TextHandlerException {
    TextComponent parsed = paragraphHandler.handle(TEXT_NO_COMMON);

    List<TextComponent> actual = service.findSentencesWithMostCommonWord(parsed);

    assertEquals(3, actual.size());
  }
}