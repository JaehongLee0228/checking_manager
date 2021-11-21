package com.firehydrant.firehydrant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Geocoder geocoder;

    private WebView webView;
    private ProgressDialog progressDialog;

    private LocationManager locationManager;
    private static final int REQUEST_CODE_LOCATION = 2;

    private EditText address_editText;
    private Button search_button;
    private Button cur_posi_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        geocoder = new Geocoder(this);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        progressDialog = new ProgressDialog(this);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        webView = (WebView)findViewById(R.id.my_webView);
        address_editText = (EditText)findViewById(R.id.address_search_editText);
        search_button = (Button)findViewById(R.id.search_button);
        cur_posi_button = (Button)findViewById(R.id.cur_posi_button);

        cur_posi_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cur_posi();
            }
        });

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move_to_address(address_editText.getText().toString());
            }
        });

        String DESKTOP_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36";
        webView.getSettings().setUserAgentString(DESKTOP_USER_AGENT);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                progressDialog.show();
            }

            //웹페이지 로딩 종료시 호출
            @Override
            public void onPageFinished(WebView view, String url){
                progressDialog.dismiss();
            }
        });

        cur_posi();
    }

    private void move_to_address(String address) {
        List<Address> list = null;
        String url = "";

        try {
            list = geocoder.getFromLocationName
                    (address, // 지역 이름
                            10); // 읽을 개수
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
        }

        if (list != null) {
            if (list.size() == 0) {
                Toast.makeText(this, "해당 주소가 없습니다.", Toast.LENGTH_SHORT);
            } else {
                // 해당되는 주소로 인텐트 날리기
                Address addr = list.get(0);
                double latitude = addr.getLatitude();
                double longitude = addr.getLongitude();

                System.out.println("////////////현재 내 위치값 : "+latitude+","+longitude);
                url = "https://www.google.com/maps/d/embed?mid=1_4bKVWjRq6bXBolPJwaGSzBBYRg&ll=" + latitude + "%2C" + longitude + "&z=18";
                webView.loadUrl(url);
            }
        }
    }

    private void cur_posi() {
        String url = "";

        Location userLocation = getMyLocation();
        if( userLocation != null ) {
            double latitude = userLocation.getLatitude();
            double longitude = userLocation.getLongitude();
            System.out.println("////////////현재 내 위치값 : "+latitude+","+longitude);
            url = "https://www.google.com/maps/d/embed?mid=1_4bKVWjRq6bXBolPJwaGSzBBYRg&ll=" + latitude + "%2C" + longitude + "&z=18";
            webView.loadUrl(url);
        }
        else {
            Toast.makeText(this, "현재 위치를 불러올 수 없습니다.", Toast.LENGTH_SHORT);
        }
    }

    private Location getMyLocation() {
        Location currentLocation = null;
        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("////////////사용자에게 권한을 요청해야함");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, this.REQUEST_CODE_LOCATION);
            getMyLocation(); //이건 써도되고 안써도 되지만, 전 권한 승인하면 즉시 위치값 받아오려고 썼습니다!
        }
        else {
            // 수동으로 위치 구하기
            System.out.println("---------------getMyLocation locationManager: " + locationManager);
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (currentLocation != null) {
                double lng = currentLocation.getLongitude();
                double lat = currentLocation.getLatitude();
                System.out.println("현재 위치: " + lng + "    " + lat);
            }
            else System.out.println("-----------currentLocation is null");
        }
        return currentLocation;
    }

}