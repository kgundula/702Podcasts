package defensivethinking.co.za.a702podcasts.utility;

import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mockStatic;

public class XMLDOMParserTest {

    private MockedStatic<Log> mockedLog;

    @Before
    public void setUp() {
        mockedLog = mockStatic(Log.class);
        mockedLog.when(() -> Log.e(anyString(), nullable(String.class))).thenReturn(0);
    }

    @After
    public void tearDown() {
        if (mockedLog != null) {
            mockedLog.close();
        }
    }

    @Test
    public void testGetDocument_SAXException() {
        XMLDOMParser parser = new XMLDOMParser();
        // A malformed XML string that will throw a SAXException
        String malformedXml = "<root><unclosed_tag></root>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(malformedXml.getBytes());

        assertNull(parser.getDocument(inputStream));
    }

    @Test
    public void testGetDocument_IOException() {
        XMLDOMParser parser = new XMLDOMParser();
        // An InputStream that throws an IOException on read
        InputStream errorStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Simulated IOException");
            }
        };

        assertNull(parser.getDocument(errorStream));
    }

    @Test
    public void testGetNodeAttr() throws Exception {
        XMLDOMParser parser = new XMLDOMParser();
        String xml = "<item id=\"123\" category=\"podcast\" />";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
        Node node = doc.getDocumentElement();

        // Test existing attribute
        assertEquals("123", parser.getNodeAttr("id", node));

        // Test case-insensitive
        assertEquals("123", parser.getNodeAttr("ID", node));

        // Test another attribute
        assertEquals("podcast", parser.getNodeAttr("category", node));

        // Test non-existent attribute
        assertEquals("", parser.getNodeAttr("nonexistent", node));
    }

    @Test
    public void testGetNode() throws Exception {
        XMLDOMParser parser = new XMLDOMParser();
        String xml = "<root><child1>value1</child1><child2>value2</child2></root>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
        NodeList nodes = doc.getDocumentElement().getChildNodes();

        // Test exact match
        Node node1 = parser.getNode("child1", nodes);
        org.junit.Assert.assertNotNull(node1);
        assertEquals("child1", node1.getNodeName());

        // Test case-insensitive match
        Node node2 = parser.getNode("CHILD2", nodes);
        org.junit.Assert.assertNotNull(node2);
        assertEquals("child2", node2.getNodeName());

        // Test not found
        Node node3 = parser.getNode("child3", nodes);
        assertNull(node3);
    }

    @Test
    public void testGetNode_EmptyList() throws Exception {
        XMLDOMParser parser = new XMLDOMParser();
        String xml = "<root></root>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
        NodeList nodes = doc.getDocumentElement().getChildNodes();

        Node node = parser.getNode("any", nodes);
        assertNull(node);
    }

    @Test
    public void testGetNode_NullTagName() throws Exception {
        XMLDOMParser parser = new XMLDOMParser();
        String xml = "<root><child1>value1</child1></root>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
        NodeList nodes = doc.getDocumentElement().getChildNodes();

        assertNull(parser.getNode(null, nodes));
    }

    @Test
    public void testGetNode_NullNodeList() throws Exception {
        XMLDOMParser parser = new XMLDOMParser();
        assertNull(parser.getNode("child1", null));
    }

    @Test
    public void testGetNodeValue_WithNodeList() throws Exception {
        XMLDOMParser parser = new XMLDOMParser();
        String xml = "<root><child1>value1</child1><child2>value2</child2><noText></noText></root>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
        NodeList nodes = doc.getDocumentElement().getChildNodes();

        // Exact match
        assertEquals("value1", parser.getNodeValue("child1", nodes));

        // Case-insensitive match
        assertEquals("value2", parser.getNodeValue("CHILD2", nodes));

        // Tag not found
        assertEquals("", parser.getNodeValue("nonexistent", nodes));

        // Tag found but no text child
        assertEquals("", parser.getNodeValue("noText", nodes));
    }

    @Test
    public void testGetNodeValue_WithNode() throws Exception {
        XMLDOMParser parser = new XMLDOMParser();
        String xml = "<root><child>value</child><empty></empty></root>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        Node childNode = doc.getElementsByTagName("child").item(0);
        assertEquals("value", parser.getNodeValue(childNode));

        Node emptyNode = doc.getElementsByTagName("empty").item(0);
        assertEquals("", parser.getNodeValue(emptyNode));

        assertEquals("", parser.getNodeValue(null));
    }

    @Test
    public void testGetNodeAttr_WithNodeList() throws Exception {
        XMLDOMParser parser = new XMLDOMParser();
        String xml = "<root><item id=\"123\"/></root>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
        NodeList nodes = doc.getDocumentElement().getChildNodes();

        // Note: The implementation of getNodeAttr(String, String, NodeList)
        // searches childNodes for ATTRIBUTE_NODE, which is unusual for standard DOM.
        // We'll test it as implemented.
        assertEquals("", parser.getNodeAttr("item", "id", nodes));
    }

}