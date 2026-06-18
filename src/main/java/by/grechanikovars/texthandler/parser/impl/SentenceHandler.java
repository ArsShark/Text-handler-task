package by.grechanikovars.texthandler.parser.impl;

import by.grechanikovars.texthandler.entity.TextComponent;
import by.grechanikovars.texthandler.entity.TextComposite;
import by.grechanikovars.texthandler.entity.TextComponentType;
import by.grechanikovars.texthandler.exception.TextHandlerException;
import by.grechanikovars.texthandler.parser.TextHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;

public class SentenceHandler extends TextHandler {

  private static final Logger logger = LogManager.getLogger(SentenceHandler.class);
  private static final Pattern SENTENCE_PATTERN = Pattern.compile("(?<=[.!?])\\s+");

  @Override
  public TextComponent handle(String text) throws TextHandlerException {
    TextComposite paragraphComposite = new TextComposite(TextComponentType.PARAGRAPH);
    String[] sentences = SENTENCE_PATTERN.split(text);
    for (String sentence : sentences) {
      String trimmedSentence = sentence.trim();
      if (!trimmedSentence.isBlank()) {
        TextComponent sentenceComponent = next.handle(trimmedSentence);
        paragraphComposite.add(sentenceComponent);
        logger.debug("Sentence added: [{}]", trimmedSentence);
      }
    }
    logger.info("Paragraph split into {} sentence(s)", paragraphComposite.getChildren().size());
    return paragraphComposite;
  }
}