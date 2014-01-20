package springplugin.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import springplugin.exceptions.BreakSaxException;

public class MySaxHandler extends DefaultHandler {

	String elementName;

	boolean dataFound=false;
	boolean searchElement = false;
	boolean searchByValue = false;

	boolean inDefinition=false;
	private String searchVal;
	private String retVal;
	
	public void setSearchVal(String searchVal){
		this.searchVal=searchVal;
		searchByValue();
	}
	public void setElementName(String elementName){
		this.elementName=elementName;
		searchElement();
	}

	public void searchByValue(){
		searchByValue = true;
	}
	public void searchElement(){
		searchElement = true;
	}

	public void startElement(String uri, String localName,String qName, 
			Attributes attributes) throws SAXException {

		//		System.out.println("Start Element :" + qName);

		if(searchElement)
		{
			if(qName.equalsIgnoreCase(elementName)){
				dataFound = true;
				throw new BreakSaxException("Data found");
			}
		}

		if(searchByValue){

			if( qName.equalsIgnoreCase("definition") && 
					searchVal.equalsIgnoreCase(attributes.getValue("name") )){
				inDefinition = true;
			}

			if(inDefinition && ("content".equalsIgnoreCase(attributes.getValue("name") ) 
					|| "page".equalsIgnoreCase(attributes.getValue("name") ) )){

				if(attributes.getValue("value")!=null ){
//					System.out.println(attributes.getValue("value"));
					retVal = attributes.getValue("value") ;
					throw new BreakSaxException("Data found");
				}
			}
		}
	}

	public void endElement(String uri, String localName,
			String qName) throws SAXException {

		if( qName.equalsIgnoreCase("definition") ) {
			inDefinition = false;
		}
		//		System.out.println("End Element :" + qName);

	}

	@Override
	public InputSource resolveEntity(String publicId, String sys)
			throws SAXException, IOException  {
		if (publicId.contains("DTD Tiles Configuration"))
			// this deactivates the open office DTD
			return new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes()));
		else return null;
	}
	public String getRetVal() {
		return retVal;
	}

}