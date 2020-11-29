package vn.poly.appdoctintuc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Customadapter customadapter;
    ArrayList<DocBao> mangdocbao;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mangdocbao = new ArrayList<DocBao>();
        swipeRefreshLayout = findViewById(R.id.srlRefesh);

        listView = (ListView) findViewById(R.id.lisview);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new Readdata().execute("https://cdn.24h.com.vn/upload/rss/tintuctrongngay.rss");
                }
            });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                intent.putExtra("link",mangdocbao.get(position).link);
                startActivity(intent);
            }
        });

        // gọi androidfast khi refesh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                customadapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                Log.d("frload", String.valueOf(customadapter));
            }
        });

    }

    class Readdata extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);

            NodeList nodeList = document.getElementsByTagName("item");
            NodeList nodeListdescription = document.getElementsByTagName("description");

            String link ="";
            String hinhanh="";
            for (int i = 0; i<nodeList.getLength(); i++ ){

                String cdata = nodeListdescription.item(i + 1).getTextContent();
                Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
                Matcher m = p.matcher(cdata);
                if (m.find()){
                    hinhanh = m.group(1);
//                    Log.d("hinhanh",hinhanh + "........." +i);
                }

                Element element = (Element) nodeList.item(i);
                link += parser.getValue(element,"link");

//                Toast.makeText(MainActivity.this,generator,Toast.LENGTH_LONG).show();
                mangdocbao.add(new DocBao(link,hinhanh));
            }
            customadapter = new Customadapter(MainActivity.this,android.R.layout.simple_list_item_1,mangdocbao);
            listView.setAdapter(customadapter);
            super.onPostExecute(s);
//            Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
        }
    }
    // 	ĐỌC NỘI DUNG CỦA MỘT URL INTERNET
    private String docNoiDung_Tu_URL(String theUrl){
        StringBuilder content = new StringBuilder();
        try    {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null){
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)    {
            e.printStackTrace();
        }
        return content.toString();
    }
}