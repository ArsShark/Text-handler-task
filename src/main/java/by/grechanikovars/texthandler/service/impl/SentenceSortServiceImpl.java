package by.grechanikovars.texthandler.service.impl;

import by.grechanikovars.texthandler.entity.TextComponent;
import by.grechanikovars.texthandler.service.SentenceSortService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SentenceSortServiceImpl implements SentenceSortService {

  private static final Logger logger = LogManager.getLogger(SentenceSortServiceImpl.class);

  @Override
  public List<TextComponent> sortSentencesByLetterCount(TextComponent text, char letter) {
    List<TextComponent> allSentences = collectSentences(text);
    char lowerLetter = Character.toLowerCase(letter);
    Comparator<TextComponent> byLetterCount =
            Comparator.comparingInt(s -> countLetter(s, lowerLetter));
    allSentences.sort(byLetterCount);
    logger.info("Sentences sorted by letter '{}', total={}", letter, allSentences.size());
    return allSentences;
  }

  private List<TextComponent> collectSentences(TextComponent text) {
    List<TextComponent> sentences = new ArrayList<>();
    List<TextComponent> paragraphs = text.getChildren();
    for (TextComponent paragraph : paragraphs) {
      List<TextComponent> paragraphSentences = paragraph.getChildren();
      sentences.addAll(paragraphSentences);
    }
    return sentences;
  }

  private int countLetter(TextComponent sentence, char letter) {
    String content = sentence.toString();
    String lowerContent = content.toLowerCase();
    int count = 0;
    for (int i = 0; i < lowerContent.length(); i++) {
      if (lowerContent.charAt(i) == letter) {
        count++;
      }
    }
    return count;
  }
}