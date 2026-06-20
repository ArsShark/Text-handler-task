package by.grechanikovars.texthandler.service;

import by.grechanikovars.texthandler.entity.TextComponent;

import java.util.List;

public interface SentenceSortService {
  List<TextComponent> sortSentencesByLetterCount(TextComponent text, char letter);
}