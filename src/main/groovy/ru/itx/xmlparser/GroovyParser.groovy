package ru.itx.xmlparser

import groovy.json.JsonOutput

import java.io.InputStream
import java.io.OutputStream;

class GroovyParser implements CommonParser {

	@Override
	public void parse(InputStream input, OutputStream out) throws Exception {
		
		def reader = new XmlSlurper().parse(input)
		def hotels = []
		
		reader.ServiceHotel.each {
			def currency = it.Currency.@code
			def hotel = [Name:it.HotelInfo.Name.text(),Rooms:[]]
			it.AvailableRoom.each {
				hotel.Rooms << [Name:it.HotelRoom.RoomType.text(),Price:it.HotelRoom.Price.Amount.text()+' '+currency]
			}
			hotels << hotel
		}
		
		out.newWriter().write(JsonOutput.prettyPrint(JsonOutput.toJson([Hotels:hotels])))
	}

}
