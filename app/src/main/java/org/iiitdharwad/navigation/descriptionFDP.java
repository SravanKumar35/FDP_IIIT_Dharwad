package org.iiitdharwad.navigation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class descriptionFDP extends AppCompatActivity{

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser_view);

        try {
            Toast.makeText(descriptionFDP.this, "Loading Page", Toast.LENGTH_LONG).show();
            webView = findViewById(R.id.browserContentView);
            webView.setWebViewClient(new WebViewClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setVerticalScrollBarEnabled(false);
            webView.setHorizontalScrollBarEnabled(false);
            webView.loadUrl("https://saankhyalabs.com/");
        }
        catch (Exception e){
            Toast.makeText(descriptionFDP.this, "Page Not Available, Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

}