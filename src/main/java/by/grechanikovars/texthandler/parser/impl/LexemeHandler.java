package by.grechanikovars.texthandler.parser.impl;

import by.grechanikovars.texthandler.entity.TextComponent;
import by.grechanikovars.texthandler.entity.TextComposite;
import by.grechanikovars.texthandler.entity.TextComponentType;
import by.grechanikovars.texthandler.exception.TextHandlerException;
import by.grechanikovars.texthandler.parser.TextHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;

public class LexemeHandler extends TextHandler {

  private static final Logger logger = LogManager.getLogger(LexemeHandler.class);
  private static final Pattern LEXEME_PATTERN = Pattern.compile("\\s+");

  @Override
  public TextComponent handle(String text) throws TextHandlerException {
    TextComposite sentenceComposite = new TextComposite(TextComponentType.SENTENCE);
    String[] lexemes = LEXEME_PATTERN.split(text.trim());
    for (String lexeme : lexemes) {
      if (!lexeme.isEmpty()) {
        TextComponent lexemeComponent = next.handle(lexeme);
        sentenceComposite.add(lexemeComponent);
      }
    }
    logger.debug("Sentence split into {} lexeme(s)", sentenceComposite.getChildren().size());
    return sentenceComposite;
  }
}