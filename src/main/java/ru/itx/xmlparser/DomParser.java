package ru.itx.xmlparser;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DomParser implements CommonParser {
	
	public DomParser()  throws Exception {}

	XPathFactory factory = XPathFactory.newInstance();
	
	XPathExpression hotelName		= factory.newXPath().compile("./HotelInfo/Name");
	XPathExpression hotelCurrency	= factory.newXPath().compile("./Currency/@code");
	
	XPathExpression roomType		= factory.newXPath().compile("./HotelRoom/RoomType");
	XPathExpression roomPrice		= factory.newXPath().compile("./HotelRoom/Price/Amount");

	@Override
	public void parse(InputStream input) throws Exception {
		
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document root = builder.parse(input);
		
		NodeList hotels = root.getElementsByTagName("ServiceHotel");
		for (int i = 0; i < hotels.getLength(); i++) {
			Element hotel = (Element)hotels.item(i);
			String currency = (String)hotelCurrency.evaluate(hotel, XPathConstants.STRING);
			System.out.println(hotelName.evaluate(hotel, XPathConstants.STRING));
			NodeList rooms = hotel.getElementsByTagName("AvailableRoom");
			for (int j = 0; j < rooms.getLength(); j++) {
				Element room = (Element)rooms.item(j);
				System.out.println(
					"\t"+roomType.evaluate(room, XPathConstants.STRING)+
					" -> "+roomPrice.evaluate(room, XPathConstants.STRING)+
					" "+currency
				);
			}
			
		}
	}

}
