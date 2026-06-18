package by.grechanikovars.texthandler.service;

import by.grechanikovars.texthandler.entity.TextComponent;

public interface LetterCountService {
  int countLetters(TextComponent text);
  int countSymbols(TextComponent text);
}
