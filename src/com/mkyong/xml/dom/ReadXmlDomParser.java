package com.mkyong.xml.dom;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReadXmlDomParser {
    private static ArrayList<Object> objects = new ArrayList<>();

    public static void main(String[] args) {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = null;
        try {
            parser = factory.newSAXParser();
        } catch (ParserConfigurationException |SAXException e ) {
            throw new RuntimeException(e);
        }

        XMLHandler handler = new XMLHandler();
        try {
            parser.parse(new File("AS_ADDR_OBJ.xml"), handler);
        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }

        ArrayList<String> list = new ArrayList<>();
        list.add("1450233");
        System.out.println(getAddress("1900-01-01", list));
        System.out.println(getAddressByI());
    }
    private static class XMLHandler extends DefaultHandler {
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals("OBJECT")) {
                String objectId = attributes.getValue("OBJECTID");
                String name = attributes.getValue("NAME");
                String typeName = attributes.getValue("TYPENAME");
                String startDate = attributes.getValue("STARTDATE");
                String endDate = attributes.getValue("ENDDATE");
                String isActual = attributes.getValue("ISACTUAL");
                String isActive = attributes.getValue("ISACTIVE");
                objects.add(new Object(objectId, name, typeName, startDate, endDate, isActual, isActive));
            }
        }
    }

    private static List<String> getAddress(String date, List<String> objectIds) {
        List<String> address = new ArrayList<>();
        for (Object obj : objects){
            if(obj.getStartDate().equals(date)){
                if(objectIds.contains(obj.getObjectId())){
                    address.add(obj.getObjectId() + " " + obj.getTypeName() + " " + obj.getName());
                }
            }
        }
        return address;
    }

    private static List<String> getAddressByI(){
        List<String> getAddresses = new ArrayList<>();
        Collections.sort(getAddresses);

        for (Object obj : objects) {
            if(obj.getTypeName().contains("проезд")){
                getAddresses.add(obj.getTypeName() + " " + obj.getName() + " ");
            }
        }
        return getAddresses;
    }
}
