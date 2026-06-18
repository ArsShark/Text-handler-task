package by.grechanikovars.texthandler.parser;

import by.grechanikovars.texthandler.entity.TextComponent;
import by.grechanikovars.texthandler.exception.TextHandlerException;

public abstract class TextHandler {

  protected TextHandler next;

  public TextHandler setNext(TextHandler next) {
    this.next = next;
    return next;
  }

  public abstract TextComponent handle(String text) throws TextHandlerException;
}