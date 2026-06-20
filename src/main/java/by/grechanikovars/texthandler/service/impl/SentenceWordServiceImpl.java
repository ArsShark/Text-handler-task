package by.grechanikovars.texthandler.service.impl;

import by.grechanikovars.texthandler.entity.TextComponent;
import by.grechanikovars.texthandler.service.SentenceWordService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SentenceWordServiceImpl implements SentenceWordService {

  private static final Logger logger = LogManager.getLogger(SentenceWordServiceImpl.class);
  private static final Pattern WORD_PATTERN = Pattern.compile("\\p{L}+");

  @Override
  public List<TextComponent> findSentencesWithMostCommonWord(TextComponent text) {
    List<TextComponent> allSentences = collectSentences(text);
    Map<String, List<TextComponent>> wordToSentences = buildWordIndex(allSentences);

    List<TextComponent> maxSentences = new ArrayList<>();
    String maxWord = "";
    for (Map.Entry<String, List<TextComponent>> entry : wordToSentences.entrySet()) {
      List<TextComponent> sentenceList = entry.getValue();
      if (sentenceList.size() > maxSentences.size()) {
        maxSentences = sentenceList;
        maxWord = entry.getKey();
      }
    }
    logger.info("Most common word: '{}' in {} sentence(s)", maxWord, maxSentences.size());
    return maxSentences;
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

  private Map<String, List<TextComponent>> buildWordIndex(List<TextComponent> sentences) {
    Map<String, List<TextComponent>> wordToSentences = new HashMap<>();
    for (TextComponent sentence : sentences) {
      Set<String> wordsInSentence = extractWords(sentence);
      for (String word : wordsInSentence) {
        List<TextComponent> sentenceList =
                wordToSentences.computeIfAbsent(word, k -> new ArrayList<>());
        sentenceList.add(sentence);
      }
    }
    return wordToSentences;
  }

  private Set<String> extractWords(TextComponent sentence) {
    Set<String> words = new HashSet<>();
    List<TextComponent> lexemes = sentence.getChildren();
    for (TextComponent lexeme : lexemes) {
      String lexemeText = lexeme.toString();
      Matcher matcher = WORD_PATTERN.matcher(lexemeText);
      while (matcher.find()) {
        String matched = matcher.group();
        String word = matched.toLowerCase();
        words.add(word);
      }
    }
    return words;
  }
}