package defensivethinking.co.za.a702podcasts.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Profusion on 2015-12-20.
 */
public class Podcasts {

    private static String title;
    private static String link;
    private static String description;
    private static String language;
    private static String copyright;
    private static String lastBuildDate;
    private static String ttl;
    private static String webMaster;

    //@XmlElement
    List<Podcast> podcastList = new ArrayList<>();
}
