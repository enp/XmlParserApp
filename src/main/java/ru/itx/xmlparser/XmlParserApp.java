package ru.itx.xmlparser;

import java.io.FileOutputStream;

public class XmlParserApp {

	public static double memory() {
		return ((double) ((double) (Runtime.getRuntime().totalMemory() / 1024) / 1024))
				- ((double) ((double) (Runtime.getRuntime().freeMemory() / 1024) / 1024));
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Begin memory : " + memory());
		long duration = 0, sum = 0;
		String type = "dom";
		int count = 3;
		if (args.length > 1) {
			type = args[0];
			count = Integer.parseInt(args[1]);
		}
		type = Character.toUpperCase(type.charAt(0)) + type.substring(1); // Use StringUtils.capitalize from spring or commons-lang
		CommonParser parser = (CommonParser) Class.forName("ru.itx.xmlparser." + type + "Parser").newInstance();
		for (int i = 0; i < count; i++) {
			long begin = System.currentTimeMillis();
			parser.parse(XmlParserApp.class.getResourceAsStream("/hbs.xml"), new FileOutputStream("/tmp/hbs.json"));
			duration = System.currentTimeMillis() - begin;
			sum += duration;
			System.out.println("Time         : " + duration);
		}
		System.out.println("Avg Time     : " + sum / count);
		System.out.println("End memory   : " + memory());
	}
}
