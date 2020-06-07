package tomdev.com.rsschecking;
import java.util.List;
public class SitesSource {

    public SiteSource[] sites;
    public int version;

    public int getVersion() {
        return version;
    }

    public SiteSource[] getSites() {
        return sites;
    }

    public void setSites(SiteSource[] sites) {
        this.sites = sites;
    }


    public class SiteSource {

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public int isDirect() {
            return direct;
        }

        public int isLeaf() {
            return leaf;
        }

        public String getIcon() {
            return icon;
        }

        public String getHide() {
            return hide;
        }

        public int getModify() {
            return m;
        }

        public String getCountrycode() {
            return countrycode;
        }

        public String getMobilizer() {
            return mobilizer;
        }

        public int getMagazinecode() {
            return magazinecode;
        }

        public String getEncoding() {
            return encoding;
        }

        public int isDeleted() {
            return deleted;
        }

        public int is_default() {
            return _default;
        }

        public String name;


        public String url;


        public int direct;


        public int leaf;


        public String icon;


        public void set_default(int _default) {
            this._default = _default;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setDirect(int direct) {
            this.direct = direct;
        }

        public void setLeaf(int leaf) {
            this.leaf = leaf;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setHide(String hide) {
            this.hide = hide;
        }

        public void setM(int m) {
            this.m = m;
        }

        public void setCountrycode(String countrycode) {
            this.countrycode = countrycode;
        }

        public void setMobilizer(String mobilizer) {
            this.mobilizer = mobilizer;
        }

        public void setMagazinecode(int magazinecode) {
            this.magazinecode = magazinecode;
        }

        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
        }

        public String hide;


        public int m;


        public String countrycode;

        public String mobilizer;


        public int magazinecode;

        public String encoding;

        public int deleted;

        public int _default;

        public SiteItemSource[] getfeeds() {
            return feeds;
        }

        public void setfeeds(SiteItemSource[] siteItemSources) {
            this.feeds = siteItemSources;
        }

        public SiteItemSource[] feeds;

    }

    public class SiteItemSource extends SiteSource {

    }


}