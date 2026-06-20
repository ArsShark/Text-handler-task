package by.grechanikovars.texthandler.entity;

import by.grechanikovars.texthandler.exception.TextHandlerException;

import java.util.ArrayList;
import java.util.List;

public class TextComposite extends TextComponent {

  private final TextComponentType type;
  private final List<TextComponent> children;

  public TextComposite(TextComponentType type) {
    this.type = type;
    this.children = new ArrayList<>();
  }

  @Override
  public TextComponentType getType() {
    return type;
  }

  @Override
  public void add(TextComponent component) throws TextHandlerException {
    if (component == null) {
      throw new TextHandlerException("Cannot add null component");
    }
    children.add(component);
  }

  @Override
  public List<TextComponent> getChildren() {
    return children;
  }

  @Override
  public String toString() {
    String delimiter = resolveDelimiter();
    StringBuilder builder = new StringBuilder();
    int lastIndex = children.size() - 1;
    for (int i = 0; i <= lastIndex; i++) {
      TextComponent child = children.get(i);
      builder.append(child.toString());
      if (i < lastIndex) {
        builder.append(delimiter);
      }
    }
    return builder.toString();
  }

  private String resolveDelimiter() {
    return switch (type) {
      case TEXT -> "\n\n";
      case PARAGRAPH -> "\t";
      case SENTENCE -> " ";
      default -> "";
    };
  }
}