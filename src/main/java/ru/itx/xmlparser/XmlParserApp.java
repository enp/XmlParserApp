package ru.itx.xmlparser;

import java.io.InputStream;

public class XmlParserApp {

	public static void main(String[] args) throws Exception {
		
		InputStream input = XmlParserApp.class.getResourceAsStream("/hbs.xml");
		
		if (args.length > 0) {
			String type = args[0];
			type = Character.toUpperCase(type.charAt(0)) + type.substring(1); // Use StringUtils.capitalize from spring or commons-lang
			CommonParser parser = (CommonParser)Class.forName("ru.itx.xmlparser."+type+"Parser").newInstance();
			parser.parse(input);
		} else {
			System.err.println("No parser implementation selected");
		}

	}

}
