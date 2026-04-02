package defensivethinking.co.za.a702podcasts.service;

import android.content.Intent;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import defensivethinking.co.za.a702podcasts.service.PodcastService;

public class PodcastServiceTest {

    private MockedStatic<Log> mockedLog;

    @Before
    public void setUp() {
        mockedLog = mockStatic(Log.class);
    }

    @After
    public void tearDown() {
        if (mockedLog != null) {
            mockedLog.close();
        }
    }

    @Test
    public void testOnHandleIntent_logsException_whenMalformedUrl() {
        // Setup
        PodcastService service = new PodcastService();
        Intent mockIntent = mock(Intent.class);

        // Return a malformed URL to trigger an exception
        when(mockIntent.getStringExtra("url")).thenReturn("https://www.omnycontent.com/malformed-url-that-does-not-exist");

        // The method protected void onHandleIntent(Intent intent) needs to be accessed
        // We can either change visibility for testing, or use reflection, or call it directly if it's package-private / protected and we are in same package
        service.onHandleIntent(mockIntent);

        // Verify Log.e was called with the specific tag and message and any exception
        mockedLog.verify(() -> Log.e(eq("PodcastService"), eq("Error handling podcast intent"), any(Exception.class)));
    }
}
