package by.grechanikovars.texthandler.entity;

import by.grechanikovars.texthandler.exception.TextHandlerException;

import java.util.List;

public abstract class TextComponent {

  public abstract TextComponentType getType();

  public void add(TextComponent component) throws TextHandlerException {
    throw new TextHandlerException("Cannot add child to a leaf node");
  }

  public List<TextComponent> getChildren() {
    return List.of();
  }

  public char getContent() { return '\0';}

  @Override
  public abstract String toString();
}