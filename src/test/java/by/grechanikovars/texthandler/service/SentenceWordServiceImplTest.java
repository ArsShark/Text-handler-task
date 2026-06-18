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

  // "apple" appears in all 3 sentences — maximum
  private static final String TEXT_THREE_SENTENCES =
          "I like apple. She eats apple. He bought apple.";

  // "cat" is in 2 sentences, "dog" in 1 — expected max = 2
  private static final String TEXT_TWO_VS_ONE =
          "I have cat. She has cat. He has dog.";

  // All sentences have unique words
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
    // given
    TextComponent parsed = paragraphHandler.handle(TEXT_TWO_VS_ONE);
    int expected = 2;
    // when
    List<TextComponent> actual = service.findSentencesWithMostCommonWord(parsed);
    // then
    assertEquals(expected, actual.size());
  }

  @Test
  void testResultIsNotEmpty() throws TextHandlerException {
    // given
    TextComponent parsed = paragraphHandler.handle(TEXT_THREE_SENTENCES);
    // when
    List<TextComponent> actual = service.findSentencesWithMostCommonWord(parsed);
    // then
    assertFalse(actual.isEmpty());
  }

  @Test
  void testAllResultNodesAreSentenceType() throws TextHandlerException {
    // given
    TextComponent parsed = paragraphHandler.handle(TEXT_THREE_SENTENCES);
    // when
    List<TextComponent> actual = service.findSentencesWithMostCommonWord(parsed);
    // then
    for (TextComponent sentence : actual) {
      assertEquals(
              by.grechanikovars.texthandler.entity.TextComponentType.SENTENCE,
              sentence.getType()
      );
    }
  }

  @Test
  void testCommonWordInSingleSentenceReturnsOne() throws TextHandlerException {
    // given — "comes" is in all 3, but "alpha","beta","gamma" are unique
    // "comes" appears in all 3 sentences
    TextComponent parsed = paragraphHandler.handle(TEXT_NO_COMMON);
    // when
    List<TextComponent> actual = service.findSentencesWithMostCommonWord(parsed);
    // then — "comes" appears in all 3 sentences
    assertEquals(3, actual.size());
  }
}