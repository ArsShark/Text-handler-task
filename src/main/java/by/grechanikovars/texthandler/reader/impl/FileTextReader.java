package by.grechanikovars.texthandler.reader.impl;

import by.grechanikovars.texthandler.exception.TextHandlerException;
import by.grechanikovars.texthandler.reader.TextReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileTextReader implements TextReader {

  private static final Logger logger = LogManager.getLogger(FileTextReader.class);

  @Override
  public String readText(String filePath) throws TextHandlerException {
    Path path = Paths.get(filePath);
    try {
      String content = Files.readString(path);
      logger.info("File read successfully: {}, chars={}", filePath, content.length());
      return content;
    } catch (IOException e) {
      throw new TextHandlerException(
              String.format("Failed to read file: %s", filePath), e);
    }
  }
}