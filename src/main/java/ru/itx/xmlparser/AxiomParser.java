package ru.itx.xmlparser;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.xpath.AXIOMXPath;

public class AxiomParser implements CommonParser {
	
	private AXIOMXPath hotelName		= new AXIOMXPath("HotelInfo/Name");
	private AXIOMXPath hotelCurrency	= new AXIOMXPath("Currency/@code");
	
	private AXIOMXPath roomName			= new AXIOMXPath("HotelRoom/RoomType");
	private AXIOMXPath roomPrice		= new AXIOMXPath("HotelRoom/Price/Amount");
	
	private XMLInputFactory factory = XMLInputFactory.newInstance();

	private Map<String,Boolean> jsonConfig = new HashMap<String,Boolean>();
	private JsonGeneratorFactory jsonFactory;
	
	public AxiomParser()  throws Exception {
		factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
		jsonConfig.put(JsonGenerator.PRETTY_PRINTING, true);
		jsonFactory = Json.createGeneratorFactory(jsonConfig);
	}

	@Override
	public void parse(InputStream input, OutputStream output) throws Exception {

		XMLStreamReader reader = factory.createXMLStreamReader(input);		
		OMElement root = new StAXOMBuilder(reader).getDocumentElement();
		
		JsonGenerator json = jsonFactory.createGenerator(output).writeStartObject().writeStartArray("Hotels");
		
		Iterator<?> hotels = root.getChildrenWithLocalName("ServiceHotel");
		while(hotels.hasNext()){
			OMElement hotel = (OMElement)hotels.next();
			String currency =  hotelCurrency.stringValueOf(hotel);
			json.writeStartObject().write("Name", hotelName.stringValueOf(hotel)).writeStartArray("Rooms");
			Iterator<?> rooms = hotel.getChildrenWithLocalName("AvailableRoom");
			while(rooms.hasNext()){
				OMNode room = (OMNode)rooms.next();
				json.writeStartObject()
					.write("Name", roomName.stringValueOf(room))
					.write("Price", roomPrice.stringValueOf(room)+" "+currency)
				.writeEnd();
			}
			json.writeEnd().writeEnd();			
		}
				
		json.writeEnd().writeEnd().close();
	}

}
