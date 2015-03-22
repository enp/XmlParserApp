package ru.itx.xmlparser;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.oxm.annotations.XmlPath;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class MoxyParser implements CommonParser {
	
	private static class HbsResponse {
		@XmlElement(name="ServiceHotel") @JsonProperty("Hotels") public List<Hotel> hotels;
	}
	
	private static class Hotel {
		@XmlPath(value="HotelInfo/Name/text()") @XmlElement(name="Name") @JsonProperty("Name") public String name;
		@XmlPath(value="Currency/@code") public @JsonIgnore String currency;
		@XmlElement(name="AvailableRoom") @JsonProperty("Rooms") public List<Room> rooms;
	}
	
	private static class Room {
		@XmlPath(value="HotelRoom/RoomType/text()") @XmlElement(name="Name") @JsonProperty("Name") public String name;
		@XmlPath(value="HotelRoom/Price/Amount/text()") @XmlElement(name="Price") @JsonProperty("Price") public String price;
	}

	private JAXBContext context = JAXBContextFactory.createContext(new Class[]{HbsResponse.class}, null);
	private XMLInputFactory factory = XMLInputFactory.newInstance();
	private ObjectMapper mapper = new ObjectMapper();

	public MoxyParser() throws Exception {
		factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
	}

	@Override
	public void parse(InputStream input, OutputStream output) throws Exception {
		
		Unmarshaller unmarshaller = context.createUnmarshaller();
		XMLStreamReader reader = factory.createXMLStreamReader(input);
		HbsResponse response = unmarshaller.unmarshal(reader, HbsResponse.class).getValue();		
		
		// It is possible to use MOXy as JSON marshaller but @XmlPath will be applied again :)
		//
		// Marshaller marshaller = context.createMarshaller();
		// marshaller.setProperty("eclipselink.media-type", "application/json");
		// marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		// marshaller.marshal(hotels, output);
		
		for (Hotel hotel : response.hotels) {
			for (Room room : hotel.rooms) {
				room.price += (' '+hotel.currency);
			}
		}
		
		mapper.writeValue(output, response);
	}

}
