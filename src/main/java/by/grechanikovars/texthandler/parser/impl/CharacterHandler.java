package by.grechanikovars.texthandler.parser.impl;

import by.grechanikovars.texthandler.entity.TextComponent;
import by.grechanikovars.texthandler.entity.TextComposite;
import by.grechanikovars.texthandler.entity.TextComponentType;
import by.grechanikovars.texthandler.entity.TextLeaf;
import by.grechanikovars.texthandler.exception.TextHandlerException;
import by.grechanikovars.texthandler.parser.TextHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CharacterHandler extends TextHandler {

  private static final Logger logger = LogManager.getLogger(CharacterHandler.class);

  @Override
  public TextComponent handle(String text) throws TextHandlerException {
    TextComposite lexemeComposite = new TextComposite(TextComponentType.LEXEME);
    char[] chars = text.toCharArray();
    for (char c : chars) {
      TextLeaf leaf = new TextLeaf(c, TextComponentType.CHARACTER);
      lexemeComposite.add(leaf);
    }
    logger.debug("Lexeme [{}] split into {} character(s)", text, chars.length);
    return lexemeComposite;
  }
}