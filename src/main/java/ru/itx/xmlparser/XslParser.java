package ru.itx.xmlparser;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XslParser implements CommonParser {

	@Override
	public void parse(InputStream input, OutputStream output) throws Exception {
		
		TransformerFactory factory = TransformerFactory.newInstance();		
		Transformer transformer = factory.newTransformer(new StreamSource(XmlParserApp.class.getResourceAsStream("/hbs.xsl")));
		
		Source source = new StreamSource(input);
		Result result = new StreamResult(output);
		
        transformer.transform(source, result);		
	}

}
