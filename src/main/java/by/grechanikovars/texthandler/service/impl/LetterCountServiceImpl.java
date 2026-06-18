package by.grechanikovars.texthandler.service.impl;

import by.grechanikovars.texthandler.entity.TextComponent;
import by.grechanikovars.texthandler.service.LetterCountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LetterCountServiceImpl implements LetterCountService {

  private static final Logger logger = LogManager.getLogger(LetterCountServiceImpl.class);

  @Override
  public int countLetters(TextComponent text) {
    String content = text.toString();
    int count = 0;
    for (int i = 0; i < content.length(); i++) {
      char c = content.charAt(i);
      if (Character.isLetter(c)) {
        count++;
      }
    }
    logger.info("Letter count: {}", count);
    return count;
  }

  @Override
  public int countSymbols(TextComponent text) {
    String content = text.toString();
    int count = 0;
    for (int i = 0; i < content.length(); i++) {
      char c = content.charAt(i);
      if (!Character.isWhitespace(c)) {
        count++;
      }
    }
    logger.info("Non-whitespace symbol count: {}", count);
    return count;
  }
}