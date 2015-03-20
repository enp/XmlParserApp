package ru.itx.xmlparser;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DomParser implements CommonParser {

	XPathFactory factory = XPathFactory.newInstance();
	
	XPathExpression hotelName		= factory.newXPath().compile("./HotelInfo/Name");
	XPathExpression hotelCurrency	= factory.newXPath().compile("./Currency/@code");
	
	XPathExpression roomType		= factory.newXPath().compile("./HotelRoom/RoomType");
	XPathExpression roomPrice		= factory.newXPath().compile("./HotelRoom/Price/Amount");
	
	Map<String,Boolean> jsonConfig = new HashMap<String,Boolean>();
	JsonGeneratorFactory jsonFactory;
	
	public DomParser()  throws Exception {
		jsonConfig.put(JsonGenerator.PRETTY_PRINTING, true);
		jsonFactory = Json.createGeneratorFactory(jsonConfig);
	}

	@Override
	public void parse(InputStream input, OutputStream out) throws Exception {
		
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document root = builder.parse(input);		
		
		JsonGenerator json = jsonFactory.createGenerator(out).writeStartObject().writeStartArray("Hotels");
		
		NodeList hotels = root.getElementsByTagName("ServiceHotel");
		for (int i = 0; i < hotels.getLength(); i++) {
			Element hotel = (Element)hotels.item(i);
			String currency = (String)hotelCurrency.evaluate(hotel, XPathConstants.STRING);
			json.writeStartObject().write("Name", hotelName.evaluate(hotel, XPathConstants.STRING).toString()).writeStartArray("Rooms");
			NodeList rooms = hotel.getElementsByTagName("AvailableRoom");
			for (int j = 0; j < rooms.getLength(); j++) {
				Element room = (Element)rooms.item(j);
				json.writeStartObject()
					.write("Name", roomType.evaluate(room, XPathConstants.STRING).toString())
					.write("Price", roomPrice.evaluate(room, XPathConstants.STRING).toString()+" "+currency)
				.writeEnd();
			}
	        json.writeEnd().writeEnd();
		}
		
		json.writeEnd().writeEnd().close();
	}

}
