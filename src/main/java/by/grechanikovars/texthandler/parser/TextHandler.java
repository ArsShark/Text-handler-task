package by.grechanikovars.texthandler.parser;

import by.grechanikovars.texthandler.entity.TextComponent;
import by.grechanikovars.texthandler.exception.TextHandlerException;

public abstract class TextHandler {

  protected TextHandler next;

  public void setNext(TextHandler next) {
    this.next = next;
  }

  public abstract TextComponent handle(String text) throws TextHandlerException;
}