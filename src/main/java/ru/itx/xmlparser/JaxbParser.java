package ru.itx.xmlparser;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

public class JaxbParser implements CommonParser {
	
	public static class HotelValuedAvailRS {
		public List<ServiceHotel> ServiceHotel;
	}
	
	public static class ServiceHotel {
		public Currency Currency;
        public HotelInfo HotelInfo;
        public List<AvailableRoom> AvailableRoom;
	}
	
	public static class Currency {
		@XmlAttribute public String code;
	}
	
	public static class HotelInfo {
	    public String Name;
	}
	
	public static class AvailableRoom {
		public HotelRoom HotelRoom;
	}

	public static class HotelRoom {
		public String RoomType;
		public Price Price;
	}
	
	public static class Price {
		public String Amount;
	}

	private JAXBContext context = JAXBContext.newInstance(HotelValuedAvailRS.class);
	private XMLInputFactory factory = XMLInputFactory.newInstance();
	
	Map<String,Boolean> jsonConfig = new HashMap<String,Boolean>();
	JsonGeneratorFactory jsonFactory;

	public JaxbParser() throws Exception {
		factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
		jsonConfig.put(JsonGenerator.PRETTY_PRINTING, true);
		jsonFactory = Json.createGeneratorFactory(jsonConfig);
	}

	@Override
	public void parse(InputStream input, OutputStream out) throws Exception {
		
		Unmarshaller unmarshaller = context.createUnmarshaller();
		XMLStreamReader reader = factory.createXMLStreamReader(input);
		HotelValuedAvailRS hotels = unmarshaller.unmarshal(reader, HotelValuedAvailRS.class).getValue();		
		
		JsonGenerator json = jsonFactory.createGenerator(out).writeStartObject().writeStartArray("Hotels");		
		for (ServiceHotel hotel : hotels.ServiceHotel) {
			json.writeStartObject().write("Name", hotel.HotelInfo.Name).writeStartArray("Rooms");
			for (AvailableRoom room : hotel.AvailableRoom) {
				json.writeStartObject()
					.write("Name", room.HotelRoom.RoomType)
					.write("Price", room.HotelRoom.Price.Amount+" "+hotel.Currency.code)
				.writeEnd();
			}
	        json.writeEnd().writeEnd();
		}
		json.writeEnd().writeEnd().close();
	}

}
