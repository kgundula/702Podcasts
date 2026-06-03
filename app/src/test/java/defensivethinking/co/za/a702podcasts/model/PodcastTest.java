package defensivethinking.co.za.a702podcasts.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class PodcastTest {

    @Test
    public void testDefaultConstructor() {
        Podcast podcast = new Podcast();
        assertEquals("", podcast.getItemTitle());
        assertEquals("", podcast.getItemDescription());
        assertEquals("", podcast.getItemPubDate());
        assertEquals("", podcast.getItemType());
        assertEquals("", podcast.getItemUrl());
    }

    @Test
    public void testParameterizedConstructor() {
        String title = "Test Title";
        String description = "Test Description";
        String pubDate = "2023-10-27";
        String type = "audio/mpeg";
        String url = "https://example.com/podcast.mp3";

        Podcast podcast = new Podcast(title, description, pubDate, type, url);

        assertEquals(title, podcast.getItemTitle());
        assertEquals(description, podcast.getItemDescription());
        assertEquals(pubDate, podcast.getItemPubDate());
        assertEquals(type, podcast.getItemType());
        assertEquals(url, podcast.getItemUrl());
    }
}
