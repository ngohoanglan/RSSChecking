package tomdev.com.rsschecking;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * Created by User on 06/04/2015.
 */
public class Utitlites {
    public static final String DATE_FORMAT = "yyyy/MM/dd hh:mm:ss";
    public static final String DATE_FORMAT1 = "yyMMddHHmmss";

    public static Date ConvertStringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
        //  System.out.println(convertedDate);
    }

    public static Date ConvertLongToDate(long timeValue) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT1);
        try {
            d = sdf.parse(String.valueOf(timeValue));
        } catch (ParseException ex) {

        }
        return d;
    }

    public static long ConvertDateToLong(Date date) {
        long result = -1;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT1);
            result = Long.parseLong(sdf.format(date));
        } catch (Exception exx) {

        }
        return result;
    }
    public static  String ConvertDateToString(Date date)
    {
        String result = "";
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT1);
            //result = Integer.parseInt(sdf.format(date));
            result=sdf.format(date);
        } catch (Exception exx) {

        }
        return result;
    }
    public static boolean CompareTwoDateTime(Date dateNow, String loadTimeString) {
        Date dateLoaded = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT1);
            dateLoaded = sdf.parse(loadTimeString);
        } catch (Exception exx) {

        }
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateNow);

            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(dateLoaded);
            calendar1.add(Calendar.MINUTE, 5);

            if (calendar1.getTime().after(calendar.getTime()))
                return false;
            else
                return true;
        } catch (Exception ex) {
        }
        return  false;
    }

    public static boolean CompareTwoDateTime_UpdateData_CheckAdmobKey(Date dateNow, String loadTimeString) {
        Date dateLoaded = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT1);
            dateLoaded = sdf.parse(loadTimeString);
        } catch (Exception exx) {

        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateNow);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(dateLoaded);
        calendar1.add(Calendar.DATE, 1);

        if (calendar1.getTime().after(calendar.getTime()))
            return false;
        else
            return true;
    }

    public static String SubString(String source, int startIndex, int endIndex) {

        try {
            return new String(source).substring(startIndex, endIndex);
        } catch (Exception ex) {
            return "";
        }

    }
    public static String GetFullURL(String url, String baseURL) {
        url = url.trim();
        try {
            URI u = new URI(url);
            if (u.isAbsolute())
                return url;
            else {
                URI host = new URI(baseURL);
                if(!host.getHost().contains("http://") && baseURL.contains("https")) {
                    url = "https://" + host.getHost() + url;
                }
                if(!host.getHost().contains("http://") && baseURL.contains("http")) {
                    url = "http://" + host.getHost() + url;
                }
                else
                {
                    url =  host.getHost() + url;
                }

            }
        } catch (Exception ex) {
            url = url.trim();
        }
        return url;
    }

    public static boolean isGZipped(InputStream in) {
        if (!in.markSupported()) {
            in = new BufferedInputStream(in);
        }
        in.mark(2);
        int magic = 0;
        try {
            magic = in.read() & 0xff | ((in.read() << 8) & 0xff00);
            in.reset();
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return false;
        }
        return magic == GZIPInputStream.GZIP_MAGIC;
    }

    public static String convertStreamToString(InputStream inputStream, String encoding) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, encoding));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        /* Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }
        return result;
    }

    public static String convertStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        /* Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }
        return result;
    }

    public static String GetImageUrl(String description) {
        try {
            String endTag = ">";
            int imgTagStartIndex = description.indexOf("<img");
            if (imgTagStartIndex == -1) {
                imgTagStartIndex = description.indexOf("&lt;img");
                endTag = "&gt;";
            }

            if (imgTagStartIndex >= 0) {
                int imgTagEndIndex = description.indexOf(endTag, imgTagStartIndex);
                if (imgTagEndIndex >= 0 && imgTagEndIndex > imgTagStartIndex) {
                    String imgTag = SimpleEscapeHTML(Utitlites.SubString(description, imgTagStartIndex, imgTagEndIndex));

                    String imgUrl = GetPropertyValue(imgTag, "src");
                    if (!imgUrl.contains(".gif"))
                    {
                        imgUrl=imgUrl.replace("\\","").replace("\"","");
                        return imgUrl;
                    }}
            }
            String imgeLink=getImageLinkRegex(description);
            if(imgeLink!=null&&imgeLink.length()>5) {
                imgeLink=imgeLink.replace("\\","").replace("\"","");
                return imgeLink;
            }
        } catch (Exception ex) {
        }
        return null;
    }

    public static String getAllImageLinks(String html) {
        //String pattern = "(http://[^\\s]+(.jpg|.png))";
        Pattern pattern1 = Pattern.compile("(http://[^\\s]+(.jpg|.png|.gif))", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern1.matcher(html);
        while (matcher.find()) {
            String imageLink = matcher.group(1);
            if (imageLink.contains("http://")) {
                return imageLink;
            }

        }
        String imgeLink=getImageLinkRegex(html).replace("\"","");
        if(imgeLink!=null&&imgeLink.length()>5)
            return imgeLink;
        return null;
    }


    public static String FixTitleAndDescription(String  item) {
        String result=item;
        try {

            String value = SimpleEscapeHTML(item);
            if (value != null) {
                Html.fromHtml((String) value).toString();
                //item.SetTi = HttpUtility.HtmlDecode(value).Trim();
                value = Html.fromHtml((String) value).toString();
            }


            if (value != null) {

                value = Utitlites.ConvertDescription(value);
                if (value.contains("\uFFFC")) {
                    value = value.replace("\uFFFC", "");
                }

                result=value;

            }
        } catch (Exception ex) {
        }
        return result;
    }

    public static String ConvertDescription(String value) {
        if (value == null)
            return "";
        int maxLength = 3500;
        int strLength = 0;
        String fixedString = "";

        // Remove HTML tags and newline characters from the text, and decodes HTML encoded characters.
        // This is a basic method. Additional code would be needed to more thoroughly
        // remove certain elements, such as embedded Javascript.

        // Remove HTML tags.
        fixedString = value.replace("<[^>]+>", "");

        // Remove newline characters
        fixedString = fixedString.replace("\r", "").replace("\n", "").replace("&amp;nbsp;", " ");


        // Remove encoded HTML characters
        fixedString = Html.fromHtml((String) fixedString).toString();

        strLength = fixedString.length();

        // Some feed management tools include an image tag in the Description field of an RSS feed,
        // so even if the Description field (and thus, the Summary property) is not populated, it could still contain HTML.
        // Due to this, after we strip tags from the string, we should return null if there is nothing left in the resulting string.

        // Truncate the text if it is too long.
        if (strLength >= maxLength) {
            fixedString = SubString(fixedString, 0, maxLength);

            // Unless we take the next step, the string truncation could occur in the middle of a word.
            // Using LastIndexOf we can find the last space character in the string and truncate there.
            //fixedString = fixedString.Substring(0, fixedString.LastIndexOf(" "));

        }

        return fixedString;
    }

    public static String GetPropertyValue(String xml, String property) {
        String value = null;
        int startIndex = xml.indexOf(property + "=");
        if (startIndex != -1) {
            int doubleQuoteIndexStart = xml.indexOf("\"", startIndex);
            if (doubleQuoteIndexStart != -1) {
                int doubleQuoteIndexEnd = xml.indexOf("\"", doubleQuoteIndexStart + 1);
                if (doubleQuoteIndexEnd != -1) {
                    value = Utitlites.SubString(xml, doubleQuoteIndexStart + 1, doubleQuoteIndexEnd).trim();
                }
            } else {
                int singleQuoteIndexStart = xml.indexOf('\'', startIndex);
                if (singleQuoteIndexStart != -1) {
                    int singleQuoteIndexEnd = xml.indexOf('\'', singleQuoteIndexStart + 1);
                    if (singleQuoteIndexEnd != -1) {
                        value = Utitlites.SubString(xml, singleQuoteIndexStart + 1, singleQuoteIndexEnd).trim();
                    }
                }
            }
        }
        return value;
    }

    static String SimpleEscapeHTML(String html) {
        return html.replace("&gt;", ">")
                .replace("&lt;", "<")
                .replace("&quot;", "\"");
    }/*
    public static Bitmap downloadBitmap(String urlImg) {
        HttpURLConnection conn=null;
        try {
            URL url = new URL(urlImg);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(30000);
            conn.connect();

            final Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            return bitmap;
        }catch (Exception ex)
        {
            Log.e("Error",ex.getMessage());

        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }*/

    public static byte[]
    downloadImageFromUrl(String urlImg, int maxImageSize) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlImg);
            conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

            // this is storage overwritten on each iteration with bytes
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            // we need to know how may bytes were read to write them to the byteBuffer
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            byte[] result = byteBuffer.toByteArray();
            //Resize Image
            Bitmap bitmapResult = scaleDown(BitmapFactory.decodeByteArray(result, 0, result.length), maxImageSize, true);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmapResult.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            // and then we can return your byte array.
            return byteArray;

        } catch (Exception ex) {

        } finally {
            conn.disconnect();
        }
        return null;
    }

    public static byte[] ConvertBitmapToByteArray(Bitmap bitmapResult) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmapResult.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        // and then we can return your byte array.
        return byteArray;
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    // convert InputStream to String
    public static String ConvertStreamToString(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

    static final int MIN_c = (int) 'd';
    static final int MAX_c = (int) 'v';
    static final int MIN_C = (int) 'n';
    static final int MAX_C = (int) 'm';
    static final int MIN_0 = (int) '3';
    static final int MAX_0 = (int) '6';

    public static String EncryptionString(String str) {


        str = base64Encode(str);
        StringBuilder sb = new StringBuilder();
        int lenStr = str.length();
        for (int i = 0; i < lenStr; i++) {
            char c = str.charAt(i);
            int c_int = (int) c;

            if (c_int >= MIN_0 && c_int <= MAX_0) {
                int e = MAX_0 + MIN_0 - (int) c;
                sb.append((char) e);
            } else if (c_int >= MIN_C && c_int <= MAX_C) {
                int e = MAX_C + MIN_C - (int) c;
                sb.append((char) e);
            } else if (c_int >= MIN_c && c_int <= MAX_c) {
                int e = MAX_c + MIN_c - (int) c;
                sb.append((char) e);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String EncryptionString2(String str) {

        int MIN_c = (int) 'a';
        int MAX_c = (int) 'x';
        int MIN_C = (int) 'A';
        int MAX_C = (int) 'R';
        int MIN_0 = (int) '1';
        int MAX_0 = (int) '8';
        // str = base64Encode(str);
        try {
            byte[] byteData = Base64.encode(str.getBytes(), Base64.NO_WRAP);
            str = new String(byteData, "UTF-8");
        } catch (Exception ex) {
            str = base64Encode(str);
        }
        StringBuilder sb = new StringBuilder();
        int lenStr = str.length();
        for (int i = 0; i < lenStr; i++) {
            char c = str.charAt(i);
            int c_int = (int) c;

            if (c_int >= MIN_0 && c_int <= MAX_0) {
                int e = MAX_0 + MIN_0 - (int) c;
                sb.append((char) e);
            } else if (c_int >= MIN_C && c_int <= MAX_C) {
                int e = MAX_C + MIN_C - (int) c;
                sb.append((char) e);
            } else if (c_int >= MIN_c && c_int <= MAX_c) {
                int e = MAX_c + MIN_c - (int) c;
                sb.append((char) e);
            } else {
                sb.append(c);
            }
        }
        String result = sb.toString();
        return result;
    }

    public static String DecryptionString(String str) {


        //const int MIN_c = (int)'a';
        //const int MAX_c = (int)'b';
        //const int MIN_C = (int)'c';
        //const int MAX_C = (int)'e';
        //const int MIN_0 = (int)'1';
        //const int MAX_0 = (int)'9';

        str = str.replace('-', '+').replace('_', '/').replace(',', '=');
        StringBuilder sb = new StringBuilder();
        int lenStr = str.length();
        for (int i = 0; i < lenStr; i++) {
            char c = str.charAt(i);
            int c_int = (int) c;

            if (c_int >= MIN_0 && c_int <= MAX_0) {
                int e = MAX_0 + MIN_0 - (int) c;
                sb.append((char) e);
            } else if (c_int >= MIN_C && c_int <= MAX_C) {
                int e = MAX_C + MIN_C - (int) c;
                sb.append((char) e);
            } else if (c_int >= MIN_c && c_int <= MAX_c) {
                int e = MAX_c + MIN_c - (int) c;
                sb.append((char) e);
            } else {
                sb.append(c);
            }
        }
        String resultEndcoding = sb.toString();
        String resultDecoded = base64Decode(resultEndcoding);
        // byte[] byteData=  Base64.decode(resultEndcoding,Base64.DEFAULT);
        return resultDecoded;
    }

    private static String base64Decode(String data) {
        String result = null;
        try {
            byte[] byteData = Base64.decode(data, Base64.DEFAULT);
            result = new String(byteData, "UTF-8");

        } catch (Exception e) {
            Log.e("E", e.getMessage());
            //  throw new Exception("Error in base64Decode" + e.getMessage());
        }
        return result;
    }

    private static String base64Encode(String data) {
        String result = null;
        try {
            byte[] byteData = Base64.encode(data.getBytes(), Base64.DEFAULT);
            result = new String(byteData, "UTF-8");
        } catch (Exception e) {
            //  throw new Exception("Error in base64Encode" + e.Message);
        }
        return result;
    }

    public static boolean checkConnection(Context context) {
        final ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
            return false;

        } else if (networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static boolean checkFeedContent(String url) {
        String feedXML = "";
        try {
            URL _url = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
            conn.connect();
            InputStream inputStream = conn.getInputStream();


            feedXML = Utitlites.convertStreamToString(inputStream);
            if (feedXML.contains("<rss") || feedXML.contains("<rdf:RDF") || feedXML.contains("<feed>") || feedXML.contains("<feed ")) {
                return true;
            }

        } catch (Exception ex) {

            return false;
        }
        return false;


    }

    public static String getEncodingOfXMLFile(String dataXML) {
        String encoding = "UTF-8";
        try {
            String temp = dataXML;
            temp = temp.replace("'", "\"");
            String regex = "encoding=\"(.[^\"]+)\"";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(temp);
            if (matcher.find()) {
                String myencoding = matcher.group(1);
                encoding = myencoding;

            }
        } catch (Exception ex) {
            encoding = "UTF-8";
        }

        return encoding;
    }

    public static String getImageLinkRegex(String feedContent) {
        String result = "";
        Matcher mLink;
        Pattern pLink;
        String HTML_HREF_TAG_PATTERN = "\\s*(?i)src\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";
        pLink = Pattern.compile(HTML_HREF_TAG_PATTERN);
        mLink = pLink.matcher(feedContent);
        while (mLink.find()) {

            String link = mLink.group(1);
            if (link.toLowerCase().contains(".png") || link.toLowerCase().contains(".jpg") || link.toLowerCase().contains(".gif")) {
                result = link;
                break;
            }
            else if(link.contains("gstatic.com"))//Google News
            {
                link=link.replace("&quot;//","http://");
                link=link.replace("&quot;","");
                result=link;
                break;
            }
        }




        return result;
    }
    public static String getSubString(String content, String startString, String endString)
    {
        String result="";
        try
        {
            int startIndex = content.indexOf(startString);
            int endIndex = 0;
            if (startString.contains(endString))
            {
                endIndex = content.indexOf(endString, startIndex + startString.length());
            }
            else
            {
                endIndex = content.indexOf(endString, startIndex);
            }
            result= Substring(content, startIndex + startString.length(), endIndex);
        }
        catch (Exception ex)
        {

        }
        return result;
    }
    public static String Substring(String s, int startIndex, int endIndex)
    {
        String result= s.substring(startIndex, endIndex);
        return result;
    }
    public static String ParsePubDate(String feedContent)
    {
        String content = null;

        try
        {

            int startElement = feedContent.indexOf("<pubDate>");
            if (startElement != -1)
            {
                int endElement = feedContent.indexOf("</pubDate>", startElement);
                if (endElement != -1)
                {
                    content = Utitlites.SubString(feedContent, startElement + 9, endElement);
                    if (content.startsWith("<![CDATA["))
                    {
                        content = Utitlites.SubString(content, 9, content.length() - 3);
                    }
                }
            }

        }
        catch (Exception ex)
        {




        }
        return  content;
    }
}
