package by.grechanikovars.texthandler.service.impl;

import by.grechanikovars.texthandler.entity.TextComponent;
import by.grechanikovars.texthandler.service.TextRestoringService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextRestoringServiceImpl implements TextRestoringService {

  private static final Logger logger = LogManager.getLogger(TextRestoringServiceImpl.class);

  @Override
  public String restore(TextComponent component) {
    String restored = component.toString();
    logger.info("Text restored, length={}", restored.length());
    return restored;
  }
}