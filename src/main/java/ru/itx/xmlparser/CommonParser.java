package ru.itx.xmlparser;

import java.io.InputStream;
import java.io.OutputStream;

public interface CommonParser {
	
	public void parse(InputStream input, OutputStream out) throws Exception;

}
