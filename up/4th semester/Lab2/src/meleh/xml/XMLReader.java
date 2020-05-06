package meleh.xml;

import meleh.devices.Device;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import meleh.devices.ElectricStove;
import meleh.devices.Fridge;
import meleh.devices.Washer;
import org.w3c.dom.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class XMLReader {

    public static ArrayList<Device> readXML (String path) throws IOException, SAXException, ParserConfigurationException {
        ArrayList<Device> arrayList = new ArrayList<>();

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document document = builder.parse(new File(path));

        document.getDocumentElement().normalize();

        Element root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node node = nodeList.item(i);
            if (node.getNodeName().equals("#text")) {
                continue;
            }
            NamedNodeMap attributes = node.getAttributes();
            String manufacturer = String.valueOf(attributes.getNamedItem("manufacturer").getNodeValue());
            int powerUsage = Integer.parseInt(attributes.getNamedItem("powerUsage").getNodeValue());
            boolean turnedOn = Boolean.parseBoolean(attributes.getNamedItem("turnedOn").getNodeValue());
            switch (node.getNodeName()) {
                case "fridge":
                    arrayList.add(new Fridge(manufacturer, powerUsage, turnedOn, Integer.parseInt(attributes.getNamedItem("volume").getNodeValue())));
                    break;
                case "washer":
                    arrayList.add(new Washer(manufacturer, powerUsage, turnedOn, Integer.parseInt(attributes.getNamedItem("spinSpeed").getNodeValue())));
                    break;
                case "electricStove":
                    arrayList.add(new ElectricStove(manufacturer, powerUsage, turnedOn, Integer.parseInt(attributes.getNamedItem("diameter").getNodeValue())));
                    break;
                default: {
                    break;
                }
            }
        }

        return arrayList;
    }

}
