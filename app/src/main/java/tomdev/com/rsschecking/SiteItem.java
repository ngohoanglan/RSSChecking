package tomdev.com.rsschecking;

public class SiteItem {
    public SiteItem() {
    }

    public String siteItemName;


    public String getSiteItemName() {
        return siteItemName;
    }

    public void setSiteItemName(String siteItemName) {
        this.siteItemName = siteItemName;
    }

    public String getSiteItemURL() {
        return siteItemURL;
    }

    public void setSiteItemURL(String siteItemURL) {
        this.siteItemURL = siteItemURL;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String siteItemURL;
    public String encoding;

    public int getSiteModify() {
        return siteModify;
    }

    public void setSiteModify(int siteModify) {
        this.siteModify = siteModify;
    }

    public int siteModify;
}