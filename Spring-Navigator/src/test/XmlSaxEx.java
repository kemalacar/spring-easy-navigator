package test;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import springplugin.utils.MySaxHandler;

public class XmlSaxEx {

	public static void main(String argv[]) {

		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			MySaxHandler handler = new MySaxHandler();

			handler.setElementName("tiles-definitions");
			saxParser.parse("C:\\Users\\EXT0173773\\workspace_sh\\SINGLE_HUB_WEB\\src\\main\\webapp\\WEB-INF\\tiles-definitions\\tiles-myaccount-web.xml", handler);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
