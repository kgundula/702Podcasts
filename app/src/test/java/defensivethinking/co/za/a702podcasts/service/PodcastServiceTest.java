package defensivethinking.co.za.a702podcasts.service;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class PodcastServiceTest {

    private MockedStatic<Log> mockedLog;
    private MockedStatic<Uri> mockedUri;
    private PodcastService podcastService;
    private Intent mockIntent;

    @Before
    public void setUp() {
        mockedLog = Mockito.mockStatic(Log.class);
        mockedUri = Mockito.mockStatic(Uri.class);
        podcastService = Mockito.mock(PodcastService.class, Mockito.CALLS_REAL_METHODS);
        mockIntent = mock(Intent.class);
    }

    @After
    public void tearDown() {
        mockedLog.close();
        mockedUri.close();
    }

    @Test
    public void testValidUrl_Accepts() {
        String validUrl = "https://www.omnycontent.com/valid/path";
        when(mockIntent.getStringExtra("url")).thenReturn(validUrl);

        Uri mockUri = mock(Uri.class);
        when(mockUri.getScheme()).thenReturn("https");
        when(mockUri.getHost()).thenReturn("www.omnycontent.com");
        mockedUri.when(() -> Uri.parse(validUrl)).thenReturn(mockUri);

        // We can't fully run onHandleIntent without HTTP, but we can verify it doesn't log the URL rejection warning
        try {
            // It might throw exception later, we just care it passes the validation
            podcastService.onHandleIntent(mockIntent);
        } catch (Exception e) {}

        mockedLog.verify(() -> Log.w(
                eq(PodcastService.podcast),
                eq("Ignoring intent with untrusted URL: " + validUrl)), never());
    }

    @Test
    public void testNullUrl_Rejects() {
        when(mockIntent.getStringExtra("url")).thenReturn(null);

        podcastService.onHandleIntent(mockIntent);

        mockedLog.verify(() -> Log.w(PodcastService.podcast, "Ignoring intent with null URL"));
    }

    @Test
    public void testInvalidHost_Rejects() {
        String invalidUrl = "https://www.omnycontent.com.evil.com/path";
        when(mockIntent.getStringExtra("url")).thenReturn(invalidUrl);

        Uri mockUri = mock(Uri.class);
        when(mockUri.getScheme()).thenReturn("https");
        when(mockUri.getHost()).thenReturn("www.omnycontent.com.evil.com");
        mockedUri.when(() -> Uri.parse(invalidUrl)).thenReturn(mockUri);

        podcastService.onHandleIntent(mockIntent);

        mockedLog.verify(() -> Log.w(PodcastService.podcast, "Ignoring intent with untrusted URL: " + invalidUrl));
    }

    @Test
    public void testInvalidScheme_Rejects() {
        String invalidUrl = "http://www.omnycontent.com/path";
        when(mockIntent.getStringExtra("url")).thenReturn(invalidUrl);

        Uri mockUri = mock(Uri.class);
        when(mockUri.getScheme()).thenReturn("http");
        when(mockUri.getHost()).thenReturn("www.omnycontent.com");
        mockedUri.when(() -> Uri.parse(invalidUrl)).thenReturn(mockUri);

        podcastService.onHandleIntent(mockIntent);

        mockedLog.verify(() -> Log.w(PodcastService.podcast, "Ignoring intent with untrusted URL: " + invalidUrl));
    }
}
