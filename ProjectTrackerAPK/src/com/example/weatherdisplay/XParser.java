package com.example.weatherdisplay;

import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/*
 * Default Notification handler class for receiving ContentHandler
 * events raised by the SAX Parser
 * 
 * */
public class XParser extends DefaultHandler {

	ArrayList<String> idlist = new ArrayList<String>();
	ArrayList<String> citylist = new ArrayList<String>();
	ArrayList<String> condilist = new ArrayList<String>();
	ArrayList<String> templist = new ArrayList<String>();
	ArrayList<String> speedlist = new ArrayList<String>();
	ArrayList<String> iconlist = new ArrayList<String>();
	
	//temp variable to store the data chunk read while parsing 
	private String tempStore	=	null;
		
	public XParser() {
		// TODO Auto-generated constructor stub
	}
	
	/*
	 * Clears the tempStore variable on every start of the element
	 * notification
	 * 
	 * */
	public void startElement (String uri, String localName, String qName,
			   Attributes attributes) throws SAXException {
	
		super.startElement(uri, localName, qName, attributes);
		
		if (localName.equalsIgnoreCase("id")) {
			tempStore = "";
		} else if (localName.equalsIgnoreCase("name")) {
			tempStore = "";
		} 
		else if (localName.equalsIgnoreCase("description")) {
			tempStore = "";
		}
		else if (localName.equalsIgnoreCase("bugId")) {
			tempStore = "";
		}
		else if (localName.equalsIgnoreCase("starttime")) {
			tempStore = "";
		}
		else if (localName.equalsIgnoreCase("endtime")) {
			tempStore = "";
		}
		else {
			tempStore = "";
		}
	}
	
	/*
	 * updates the value of the tempStore variable into
	 * corresponding list on receiving end of the element
	 * notification
	 * */
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		
		if (localName.equalsIgnoreCase("id")) {
			idlist.add(tempStore);
		} 
		else if (localName.equalsIgnoreCase("name")) {
			citylist.add(tempStore);
		}
		else if (localName.equalsIgnoreCase("description")) {
			templist.add(tempStore);
		}
		else if (localName.equalsIgnoreCase("bugId")) {
			condilist.add(tempStore);
		}
		else if (localName.equalsIgnoreCase("starttime")) {
			speedlist.add(tempStore);
		}
		else if (localName.equalsIgnoreCase("endtime")) {
			iconlist.add(tempStore);
		}
		
		tempStore = "";
	}
	
	/*
	 * adds the incoming data chunk of character data to the 
	 * temp data variable - tempStore
	 * 
	 * */
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		tempStore += new String(ch, start, length);
	}

}
