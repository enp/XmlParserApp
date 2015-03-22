<?xml version="1.0"?>
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xalan="http://xml.apache.org/xalan"
	xmlns:hbs="http://www.hotelbeds.com/schemas/2005/06/messages"
	exclude-result-prefixes="hbs xalan">
	
	<xsl:output method="xml" indent="yes" xalan:indent-amount="2"/>

	<xsl:template match="hbs:HotelValuedAvailRS">
		<Hotels>
			<xsl:apply-templates select="hbs:ServiceHotel"/>
		</Hotels>
	</xsl:template>
	
	<xsl:template match="hbs:ServiceHotel">
		<Hotel>
			<Name><xsl:value-of select="hbs:HotelInfo/hbs:Name"/></Name>
			<Rooms>
				<xsl:apply-templates select="hbs:AvailableRoom/hbs:HotelRoom">
					<xsl:with-param name="currency" select="hbs:Currency/@code"/>
				</xsl:apply-templates>
			</Rooms>
		</Hotel>
	</xsl:template>
	
	<xsl:template match="hbs:HotelRoom">
		<xsl:param name="currency"/>
		<Room>
			<Name><xsl:value-of select="hbs:RoomType"/></Name>
			<Price><xsl:value-of select="concat(hbs:Price/hbs:Amount,' ',$currency)"/></Price>
		</Room>
	</xsl:template>

</xsl:stylesheet>