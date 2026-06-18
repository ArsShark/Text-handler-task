package by.grechanikovars.texthandler.reader;

import by.grechanikovars.texthandler.exception.TextHandlerException;

public interface TextReader {

  String readText(String filePath) throws TextHandlerException;
}