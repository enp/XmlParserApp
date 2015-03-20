package ru.itx.xmlparser

import java.io.InputStream

class GroovyParser implements CommonParser {

	@Override
	public void parse(InputStream input) throws Exception {
		
		def xml = new XmlSlurper().parse(input)
		xml.ServiceHotel.each {
			def currency = it.Currency.@code
			println it.HotelInfo.Name
			it.AvailableRoom.each {
				def roomType = it.HotelRoom.RoomType
				def roomPrice = it.HotelRoom.Price.Amount
				println "\t${roomType} -> ${roomPrice} ${currency}"
			}
		}
	}

}
