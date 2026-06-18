package by.grechanikovars.texthandler.parser.impl;

import by.grechanikovars.texthandler.entity.TextComponent;
import by.grechanikovars.texthandler.entity.TextComposite;
import by.grechanikovars.texthandler.entity.TextComponentType;
import by.grechanikovars.texthandler.exception.TextHandlerException;
import by.grechanikovars.texthandler.parser.TextHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;

public class ParagraphHandler extends TextHandler {

  private static final Logger logger = LogManager.getLogger(ParagraphHandler.class);
  private static final Pattern PARAGRAPH_PATTERN = Pattern.compile("(?:\\r?\\n){2,}");

  @Override
  public TextComponent handle(String text) throws TextHandlerException {
    TextComposite textComposite = new TextComposite(TextComponentType.TEXT);
    String normalizedText = text.replace("\r\n", "\n").replace("\r", "\n");
    String[] paragraphs = PARAGRAPH_PATTERN.split(normalizedText.trim());
    for (String paragraph : paragraphs) {
      String trimmedParagraph = paragraph.trim();
      if (!trimmedParagraph.isBlank()) {
        TextComponent paragraphComponent = next.handle(trimmedParagraph);
        textComposite.add(paragraphComponent);
        logger.debug("Paragraph added, length={}", trimmedParagraph.length());
      }
    }
    logger.info("Text split into {} paragraph(s)", textComposite.getChildren().size());
    return textComposite;
  }
}