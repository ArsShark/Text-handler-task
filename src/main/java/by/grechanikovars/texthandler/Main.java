package by.grechanikovars.texthandler;

import by.grechanikovars.texthandler.entity.TextComponent;
import by.grechanikovars.texthandler.exception.TextHandlerException;
import by.grechanikovars.texthandler.parser.impl.CharacterHandler;
import by.grechanikovars.texthandler.parser.impl.LexemeHandler;
import by.grechanikovars.texthandler.parser.impl.ParagraphHandler;
import by.grechanikovars.texthandler.parser.impl.SentenceHandler;
import by.grechanikovars.texthandler.reader.TextReader;
import by.grechanikovars.texthandler.reader.impl.FileTextReader;
import by.grechanikovars.texthandler.service.LetterCountService;
import by.grechanikovars.texthandler.service.LexemeSwapService;
import by.grechanikovars.texthandler.service.SentenceSortService;
import by.grechanikovars.texthandler.service.SentenceWordService;
import by.grechanikovars.texthandler.service.TextRestoringService;
import by.grechanikovars.texthandler.service.impl.LetterCountServiceImpl;
import by.grechanikovars.texthandler.service.impl.LexemeSwapServiceImpl;
import by.grechanikovars.texthandler.service.impl.SentenceSortServiceImpl;
import by.grechanikovars.texthandler.service.impl.SentenceWordServiceImpl;
import by.grechanikovars.texthandler.service.impl.TextRestoringServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Main {

  private static final Logger logger = LogManager.getLogger(Main.class);
  private static final String DATA_FILE = "data/text.txt";

  public static void main(String[] args) {
    TextReader reader = new FileTextReader();

    CharacterHandler characterHandler = new CharacterHandler();
    LexemeHandler lexemeHandler = new LexemeHandler();
    SentenceHandler sentenceHandler = new SentenceHandler();
    ParagraphHandler paragraphHandler = new ParagraphHandler();

    paragraphHandler.setNext(sentenceHandler);
    sentenceHandler.setNext(lexemeHandler);
    lexemeHandler.setNext(characterHandler);

    TextRestoringService restoringService = new TextRestoringServiceImpl();
    LetterCountService letterCountService = new LetterCountServiceImpl();
    SentenceWordService sentenceWordService = new SentenceWordServiceImpl();
    SentenceSortService sentenceSortService = new SentenceSortServiceImpl();
    LexemeSwapService lexemeSwapService = new LexemeSwapServiceImpl();

    try {
      String rawText = reader.readText(DATA_FILE);

      TextComponent parsedText = paragraphHandler.handle(rawText);

      String restored = restoringService.restore(parsedText);
      logger.info("=== Restored text ===\n{}", restored);

      int letters = letterCountService.countLetters(parsedText);
      int symbols = letterCountService.countSymbols(parsedText);
      logger.info("Letters: {}   Non-whitespace symbols: {}", letters, symbols);

      List<TextComponent> commonWordSentences =
              sentenceWordService.findSentencesWithMostCommonWord(parsedText);
      logger.info("=== Task 1: {} sentence(s) share the most common word ===",
              commonWordSentences.size());
      for (TextComponent sentence : commonWordSentences) {
        logger.info("  {}", sentence);
      }

      char targetLetter = 'i';
      List<TextComponent> sortedSentences =
              sentenceSortService.sortSentencesByLetterCount(parsedText, targetLetter);
      logger.info("=== Task 2: sentences sorted by letter '{}' ===", targetLetter);
      for (TextComponent sentence : sortedSentences) {
        logger.info("  {}", sentence);
      }

      lexemeSwapService.swapFirstAndLastLexeme(parsedText);
      logger.info("=== Task 3: text after lexeme swap ===\n{}", parsedText);

    } catch (TextHandlerException e) {
      logger.error("Fatal error: {}", e.getMessage(), e);
    }
  }
}