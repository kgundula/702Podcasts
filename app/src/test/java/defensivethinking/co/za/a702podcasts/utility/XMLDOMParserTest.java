package defensivethinking.co.za.a702podcasts.utility;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XMLDOMParserTest {

    private XMLDOMParser parser;

    @Before
    public void setUp() {
        parser = new XMLDOMParser();
    }

    @Test
    public void testValidXMLParsing() {
        String validXML = "<?xml version=\"1.0\"?><podcast><title>My Podcast</title></podcast>";
        InputStream stream = new ByteArrayInputStream(validXML.getBytes());
        Document doc = parser.getDocument(stream);

        assertNotNull("Document should not be null for valid XML", doc);
        NodeList nodes = doc.getElementsByTagName("title");
        assertEquals(1, nodes.getLength());
        assertEquals("My Podcast", parser.getNodeValue(nodes.item(0)));
    }

    @Test
    public void testXXEVulnerabilityPrevention() {
        // This XML attempts to use an external entity.
        // If the parser is vulnerable, it might try to read /etc/passwd (or throw a specific error).
        // If DOCTYPE is disallowed, parsing should fail entirely.
        String maliciousXML = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "<!DOCTYPE foo [\n" +
                "  <!ELEMENT foo ANY >\n" +
                "  <!ENTITY xxe SYSTEM \"file:///etc/passwd\" >]>\n" +
                "<foo>&xxe;</foo>";

        InputStream stream = new ByteArrayInputStream(maliciousXML.getBytes());
        Document doc = parser.getDocument(stream);

        // Since we disallow DOCTYPE, parsing this XML should fail and return null
        assertNull("Document should be null for XML containing DOCTYPE when DOCTYPE is disallowed", doc);
    }

    @Test
    public void testBillionLaughsVulnerabilityPrevention() {
        // This XML attempts a Billion Laughs attack (entity expansion).
        // Since we disable entity expansion and DOCTYPE, this should fail.
        String maliciousXML = "<?xml version=\"1.0\"?>\n" +
                "<!DOCTYPE lolz [\n" +
                " <!ENTITY lol \"lol\">\n" +
                " <!ELEMENT lolz (#PCDATA)>\n" +
                " <!ENTITY lol1 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">\n" +
                " <!ENTITY lol2 \"&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;\">\n" +
                " <!ENTITY lol3 \"&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;\">\n" +
                " <!ENTITY lol4 \"&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;\">\n" +
                " <!ENTITY lol5 \"&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;\">\n" +
                " <!ENTITY lol6 \"&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;\">\n" +
                " <!ENTITY lol7 \"&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;\">\n" +
                " <!ENTITY lol8 \"&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;\">\n" +
                " <!ENTITY lol9 \"&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;\">\n" +
                "]>\n" +
                "<lolz>&lol9;</lolz>";

        InputStream stream = new ByteArrayInputStream(maliciousXML.getBytes());
        Document doc = parser.getDocument(stream);

        // Parsing should fail and return null
        assertNull("Document should be null for XML attempting entity expansion attacks when DOCTYPE is disallowed", doc);
    }
}
