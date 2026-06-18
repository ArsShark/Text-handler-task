package by.grechanikovars.texthandler.service.impl;

import by.grechanikovars.texthandler.entity.TextComponent;
import by.grechanikovars.texthandler.service.LexemeSwapService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class LexemeSwapServiceImpl implements LexemeSwapService {

  private static final Logger logger = LogManager.getLogger(LexemeSwapServiceImpl.class);

  @Override
  public void swapFirstAndLastLexeme(TextComponent text) {
    List<TextComponent> paragraphs = text.getChildren();
    for (TextComponent paragraph : paragraphs) {
      List<TextComponent> sentences = paragraph.getChildren();
      for (TextComponent sentence : sentences) {
        swapInSentence(sentence);
      }
    }
    logger.info("First and last lexeme swapped in every sentence");
  }

  private void swapInSentence(TextComponent sentence) {
    List<TextComponent> lexemes = sentence.getChildren();
    int size = lexemes.size();
    if (size > 1) {
      TextComponent first = lexemes.get(0);
      int lastIndex = size - 1;
      TextComponent last = lexemes.get(lastIndex);
      lexemes.set(0, last);
      lexemes.set(lastIndex, first);
      logger.debug("Swapped lexemes in sentence of {} lexeme(s)", size);
    }
  }
}