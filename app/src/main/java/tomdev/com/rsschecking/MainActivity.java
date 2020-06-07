package tomdev.com.rsschecking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import android.content.pm.PackageManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import android.content.Context;
import android.Manifest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;

public class MainActivity extends AppCompatActivity {
    ProgressDialog mDialog;
    Activity activity;
    int CONTACT_PERMISSION_CODE = 111;
String code="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activity = this;
        if (!hasWriteSDPermission(activity)) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    CONTACT_PERMISSION_CODE);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        final EditText editText = (EditText) findViewById(R.id.editCountryCode);
        Button btn = (Button) findViewById(R.id.btnChecking);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code = editText.getText().toString();
                new MyAsyncTask().execute(code);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    static final int MIN_c = (int) 'd';
    static final int MAX_c = (int) 'v';
    static final int MIN_C = (int) 'n';
    static final int MAX_C = (int) 'm';
    static final int MIN_0 = (int) '3';
    static final int MAX_0 = (int) '6';

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

    String DateString;
    File root;
    File filepath;
    FileWriter writer;

    private String CheckingFeed(String urlString, String name, String encoding,int isSiteDeleted) {


        String result = "";

        if (name == "" || name.toLowerCase().contains("feed") || name.toLowerCase().contains("http") || name.length() < 2) {
            result = urlString + " :name invalid" + System.getProperty("line.separator");
        }
        try {
            if (isSiteDeleted == 2) {
                WriteLog(urlString, "Site was deleted");
            }
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "application/json, text/html, application/xhtml+xml, */*");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; Trident/6.0)");
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            String feedXML = convertStreamToString(inputStream);
            if (feedXML != null && feedXML.isEmpty()) {
                String urlStringNew = urlString.replace("http", "https");
                url = new URL(urlStringNew);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Accept", "application/json, text/html, application/xhtml+xml, */*");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; Trident/6.0)");
                conn.connect();
                inputStream = conn.getInputStream();
                feedXML = convertStreamToString(inputStream);
                if (feedXML != null && !feedXML.isEmpty() && feedXML.contains("<rss")) {
                    WriteLog(urlString, "https required");
                } else {
                    WriteLog(urlString, "not rss");
                }
            }
            if (!feedXML.toLowerCase().contains("utf-8") && feedXML.toLowerCase().contains("encoding") && encoding.equals("")) {
                result = urlString + ": encoding" + System.getProperty("line.separator");
                WriteLog(urlString, result);

            }
            if (encoding != null && !encoding.isEmpty() && encoding.toLowerCase().contains("utf-8") && !feedXML.toLowerCase().contains("utf-8")) {
                result = urlString + ": encoding" + System.getProperty("line.separator");
                WriteLog(urlString, result);

            }
            if (encoding != null && !encoding.isEmpty() && !encoding.toLowerCase().contains("utf-8") && feedXML.toLowerCase().contains("utf-8")) {
                result = urlString + ": encoding" + System.getProperty("line.separator");
                WriteLog(urlString, result);

            }
            int count = 0;
            if (feedXML.contains("<rss") || feedXML.contains("<rdf:RDF")) {

                int startIndex = feedXML.indexOf("<item");

                while (startIndex != -1 && count < 100) {

                    int endIndex = feedXML.indexOf("</item>", startIndex);
                    startIndex = feedXML.indexOf("<item", endIndex);
                    count++;
                }
            } else if (feedXML.contains("<feed>") || feedXML.contains("<feed ")) {

                int startIndex = feedXML.indexOf("<entry");
                count = 0;
                while (startIndex != -1 && count < 100) {
                    int endIndex = feedXML.indexOf("</entry>", startIndex);


                    startIndex = feedXML.indexOf("<entry", endIndex);
                    count++;
                }
            } else if (feedXML.contains("urlset")) {

                int startIndex = feedXML.indexOf("<url>");
                count = 0;
                while (startIndex != -1 && count < 100) {
                    int endIndex = feedXML.indexOf("</url>", startIndex);


                    startIndex = feedXML.indexOf("<url>", endIndex);
                    count++;
                }
            }
            if (count < 5) {
                WriteLog(urlString, count + " item");
            }


        } catch (Exception exxx) {
            result = urlString + ":" + exxx.getMessage();
        }

        return result;

    }

    private void WriteLog(String url, String message) {


        try {
            DateString = DateFormat.format("MM_dd_yyyyy", System.currentTimeMillis()).toString();
            // this will create a new name everytime and unique
            root = new File(Environment.getExternalStorageDirectory().getPath());
            // if external memory exists and folder with name Notes
            if (!root.exists()) {
                root.mkdirs(); // this will create folder.
            }
            filepath = new File(root, code+"_"+DateString + ".txt");  // file path to save


            writer = new FileWriter(filepath, true);
            writer.append(url + "    " + ":" + message);
            writer.append(System.getProperty("line.separator"));
            writer.flush();
            writer.close();


        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public static boolean hasWriteSDPermission(Context context) {
        return ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private class MyAsyncTask extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        InputStream inputStream = null;
        String result = "";
        List<SiteItem> siteItems = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(activity);

            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            mDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... param) {
             code = param[0];
            String result = "";
            try {


                String countryRSSData = "http://thealllatestnews.com/Resources/CountryList/" + code + "/database.txt";
                URL url = new URL(countryRSSData);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                inputStream = conn.getInputStream();
                result = convertStreamToString(inputStream);

                result = DecryptionString(result);
                Gson gson = new Gson();
                SitesSource sitesSource = gson.fromJson(result, SitesSource.class);

                for (SitesSource.SiteSource ss : sitesSource.getSites()) {

                    SitesSource.SiteItemSource[] siteItemSourceList = ss.getfeeds();
                    if (ss.isLeaf() == 1) {
                        SiteItem si = new SiteItem();
                        si.setSiteItemName(ss.getName());
                        si.setSiteItemURL(ss.getUrl());
                        si.setEncoding(ss.getEncoding());
                        si.setSiteModify(ss.getModify());
                        CheckingFeed(si.getSiteItemURL(), si.getSiteItemName(), si.getEncoding(),si.getSiteModify());
                        siteItems.add(si);
                    } else {
                        for (SitesSource.SiteItemSource sis : siteItemSourceList) {
                            SiteItem si = new SiteItem();
                            si.setSiteItemName(sis.getName());
                            si.setSiteItemURL(sis.getUrl());
                            si.setEncoding(sis.getEncoding());
                            si.setSiteModify(ss.getModify());
                            CheckingFeed(si.getSiteItemURL(), si.getSiteItemName(), si.getEncoding(),si.getSiteModify());
                            siteItems.add(si);


                        }
                    }
                }


            } catch (
                    Exception e4) {
                Log.d("", e4.getMessage());

            }

            return "";

        }
    }
}