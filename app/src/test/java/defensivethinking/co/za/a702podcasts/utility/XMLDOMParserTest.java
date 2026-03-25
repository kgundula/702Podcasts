package defensivethinking.co.za.a702podcasts.utility;

import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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
}
