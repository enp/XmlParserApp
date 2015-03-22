package ru.itx.xmlparser;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;

public class FakeParser implements CommonParser {

	Map<String,Boolean> jsonConfig = new HashMap<String,Boolean>();
	JsonGeneratorFactory jsonFactory;
	
	public FakeParser()  throws Exception {
		jsonConfig.put(JsonGenerator.PRETTY_PRINTING, true);
		jsonFactory = Json.createGeneratorFactory(jsonConfig);
	}

	@Override
	public void parse(InputStream input, OutputStream out) throws Exception {
		
		JsonGenerator json = jsonFactory.createGenerator(out).writeStartObject().writeStartArray("Hotels");
		
		json.writeStartObject().write("Name", "One").writeStartArray("Rooms");
		json.writeStartObject()
			.write("Name", "Single".toString())
			.write("Price", "10 USD")
		.writeEnd();
		json.writeEnd().writeEnd();
				
		json.writeEnd().writeEnd().close();
	}

}
