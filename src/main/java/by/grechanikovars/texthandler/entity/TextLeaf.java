package by.grechanikovars.texthandler.entity;

public class TextLeaf extends TextComponent {

  private final char content;
  private final TextComponentType type;

  public TextLeaf(char content, TextComponentType type) {
    this.content = content;
    this.type = type;
  }

  @Override
  public TextComponentType getType() {
    return type;
  }

  @Override
  public char getContent() {
    return content;
  }

  @Override
  public String toString() {
    return String.valueOf(content);
  }
}