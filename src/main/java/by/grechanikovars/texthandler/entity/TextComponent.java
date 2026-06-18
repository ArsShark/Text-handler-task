package by.grechanikovars.texthandler.entity;

import by.grechanikovars.texthandler.exception.TextHandlerException;

import java.util.List;

public abstract class TextComponent {

  public abstract TextComponentType getType();

  public void add(TextComponent component) throws TextHandlerException {
    throw new TextHandlerException("Cannot add child to a leaf node");
  }

  public List<TextComponent> getChildren() {
    throw new UnsupportedOperationException("Leaf node has no children");
  }

  public char getContent() {
    throw new UnsupportedOperationException("Composite node has no single character content");
  }

  @Override
  public abstract String toString();
}