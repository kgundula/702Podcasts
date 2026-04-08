package defensivethinking.co.za.a702podcasts.utility;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by kgundula on 2015-12-20.
 */
public class XMLDOMParser {

    public Document getDocument(InputStream inputStream) {
        Document document = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        try {
            // Basic security features, wrapped in blocks to prevent crashes on unsupported devices
            try {
                factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            } catch (Exception e) {
                Log.w("XMLDOMParser", "Feature disallow-doctype-decl not supported");
            }
            try {
                factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            } catch (Exception e) {
                Log.w("XMLDOMParser", "Feature external-general-entities not supported");
            }
            
            DocumentBuilder db = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(inputStream);
            document = db.parse(inputSource);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            Log.e("XMLDOMParser", "Error parsing XML", e);
            return null;
        } catch (Exception e) {
            Log.e("XMLDOMParser", "Unexpected error", e);
            return null;
        }
        return document;
    }

    public Node getNode(String tagName, NodeList nodes) {
        for ( int x = 0; x < nodes.getLength(); x++ ) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                return node;
            }
        }

        return null;
    }

    public String getNodeValue( Node node ) {
        if (node == null) {
            return "";
        }
        NodeList childNodes = node.getChildNodes();
        if (childNodes == null) {
            return "";
        }
        for (int x = 0; x < childNodes.getLength(); x++ ) {
            Node data = childNodes.item(x);
            if ( data != null && data.getNodeType() == Node.TEXT_NODE )
                return data.getNodeValue();
        }
        return "";
    }

    public String getNodeValue(String tagName, NodeList nodes ) {
        for ( int x = 0; x < nodes.getLength(); x++ ) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                NodeList childNodes = node.getChildNodes();
                for (int y = 0; y < childNodes.getLength(); y++ ) {
                    Node data = childNodes.item(y);
                    if ( data.getNodeType() == Node.TEXT_NODE )
                        return data.getNodeValue();
                }
            }
        }
        return "";
    }

    public String getNodeAttr(String attrName, Node node ) {
        NamedNodeMap attrs = node.getAttributes();
        Node attr = attrs.getNamedItem(attrName);
        if (attr != null) {
            return attr.getNodeValue();
        }
        // Fallback for case-insensitivity
        for (int y = 0; y < attrs.getLength(); y++ ) {
            attr = attrs.item(y);
            if (attr.getNodeName().equalsIgnoreCase(attrName)) {
                return attr.getNodeValue();
            }
        }
        return "";
    }

    public String getNodeAttr(String tagName, String attrName, NodeList nodes ) {
        for ( int x = 0; x < nodes.getLength(); x++ ) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                NodeList childNodes = node.getChildNodes();
                for (int y = 0; y < childNodes.getLength(); y++ ) {
                    Node data = childNodes.item(y);
                    if ( data.getNodeType() == Node.ATTRIBUTE_NODE ) {
                        if ( data.getNodeName().equalsIgnoreCase(attrName) )
                            return data.getNodeValue();
                    }
                }
            }
        }

        return "";
    }


}
