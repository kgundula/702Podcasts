package defensivethinking.co.za.a702podcasts.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import defensivethinking.co.za.a702podcasts.BR;

/**
 * Created by kgundula on 2015-12-14.
 */
public class Podcast  extends BaseObservable {

    private String itemTitle = "";
    private String itemDescription = "";
    private String itemPubDate = "";
    private String itemUrl = "";
    private String itemType = "";

    public Podcast () {
        this.itemTitle = "";
        this.itemDescription = "";
        this.itemUrl = "";
        this.itemType = "";
        this.itemPubDate = "";
    }

    public Podcast (String item_title, String item_description, String item_pubDate, String item_type, String item_url) {
        this.itemTitle = item_title;
        this.itemDescription = item_description;
        this.itemPubDate = item_pubDate;
        this.itemUrl = item_url;
        this.itemType = item_type;
    }
    /**/

    @Bindable
    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String item_title) {
        this.itemTitle = item_title;
        notifyPropertyChanged(BR.itemTitle);
    }

    @Bindable
    public String getItemDescription() {
        return this.itemDescription;
    }

    public void setItemDescription(String item_desc) {
        this.itemDescription = item_desc;
        notifyPropertyChanged(BR.itemDescription);
    }

    @Bindable
    public String getItemUrl() {
        return this.itemUrl;
    }

    public void setItemUrl(String item_url) {
        this.itemUrl = item_url;
        notifyPropertyChanged(BR.itemUrl);
    }

    @Bindable
    public String getItemPubDate() {
        return this.itemPubDate;
    }

    public void setItemPubDate(String item_pubDate) {
        this.itemPubDate = item_pubDate;
        notifyPropertyChanged(BR.itemPubDate);
    }

    public void setItemType(String item_type) {
        this.itemType = item_type;
        notifyPropertyChanged(BR.itemType);
    }
    @Bindable
    public String getItemType() {
        return this.itemType;
    }


}
