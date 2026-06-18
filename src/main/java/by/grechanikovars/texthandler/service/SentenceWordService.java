package by.grechanikovars.texthandler.service;

import by.grechanikovars.texthandler.entity.TextComponent;
import java.util.List;

public interface SentenceWordService {
  List<TextComponent> findSentencesWithMostCommonWord(TextComponent text);
}