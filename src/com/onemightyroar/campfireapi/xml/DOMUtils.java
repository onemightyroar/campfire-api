package com.onemightyroar.campfireapi.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DOMUtils {
	
	private static final ThreadLocal<DocumentBuilder> builderLocal = new ThreadLocal<DocumentBuilder>() {
		protected DocumentBuilder initialValue() {
			try {
				return DocumentBuilderFactory.newInstance().newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				throw new RuntimeException(e);
			}
		}
	};
	
    public static boolean isXML(String content) {
    	return content != null && content.startsWith("<?xml");
    }
    
    public static String stripXML(String content) {
    	content = content.replaceAll("\\<.*?\\>", "");
    	content = content.replaceAll("\\n", "");
    	return content;
    }
	
	public static Document buildDocument(InputStream in) {
		try {
			DocumentBuilder builder = builderLocal.get();
			Document document = builder.parse(in);
			//document.normalizeDocument();
			in.close();
			return document;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String getChildText(Element element, String tag) {
		String value = null;
		NodeList nodeList = element.getElementsByTagName(tag);
		if(nodeList != null && nodeList.getLength() > 0) {
			Element el = (Element)nodeList.item(0);
			if (el.getFirstChild() != null) {
				value =  el.getFirstChild().getNodeValue();
			}
		}
		return value;
	}
	
	public static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is), 8*1024);
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
