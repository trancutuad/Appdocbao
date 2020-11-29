package vn.poly.appdoctintuc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity2 extends AppCompatActivity {

    private WebView webView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.RefreshLayout2);
        webView = (WebView) findViewById(R.id.web_view);
        Intent intent = getIntent();
        String duonglink = intent.getStringExtra("link");
        webView.loadUrl(duonglink);
        webView.setWebViewClient(new WebViewClient());

        // gọi androidfast khi refesh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.loadUrl(duonglink);
                swipeRefreshLayout.setRefreshing(false);
                Log.d("frload", String.valueOf(duonglink));
            }
        });
    }
}