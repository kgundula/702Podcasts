package defensivethinking.co.za.a702podcasts.utility;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XMLDOMParserTest {

    private XMLDOMParser parser;
    private Document document;

    @Before
    public void setUp() throws Exception {
        parser = new XMLDOMParser();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.newDocument();
    }

    @Test
    public void testGetNodeValueWithValidTextNode() {
        // Create an element with a text node child
        Element element = document.createElement("testElement");
        Text textNode = document.createTextNode("Hello World");
        element.appendChild(textNode);

        String result = parser.getNodeValue(element);
        assertEquals("Hello World", result);
    }

    @Test
    public void testGetNodeValueWithNoTextChildren() {
        // Create an element with an element child, but no text child
        Element parent = document.createElement("parent");
        Element child = document.createElement("child");
        parent.appendChild(child);

        String result = parser.getNodeValue(parent);
        assertEquals("", result);
    }

    @Test
    public void testGetNodeValueWithEmptyElement() {
        // Create an empty element
        Element emptyElement = document.createElement("empty");

        String result = parser.getNodeValue(emptyElement);
        assertEquals("", result);
    }

    @Test
    public void testGetNodeValueWithNullNode() {
        // Test with null
        String result = parser.getNodeValue((Node) null);
        assertEquals("", result);
    }
}
