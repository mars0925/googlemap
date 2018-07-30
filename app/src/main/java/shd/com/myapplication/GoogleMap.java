package shd.com.myapplication;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GoogleMap extends AppCompatActivity {
    private WebView w_map;
    private double lat,lng;
    private String path;
    private JSONArray location;
    private Button b_button;
    private double metersPerPx;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        w_map = findViewById(R.id.w_map);
        b_button = findViewById(R.id.b_button);


        initWebView();
        /*設定位置座標*/
        lat = 25.014051;
        lng = 121.463815;

        path = "file:///android_asset/images.png";
        /*將變數傳到Javascript*/
        w_map.addJavascriptInterface(GoogleMap.this,"AndroidFunction");

        location = new JSONArray();

        for (int i=0;i<=50;i++){
            JSONObject data = new JSONObject();

            try {
                data.put("lat",lat + (Math.random()*0.2));
                data.put("lng", lng +(Math.random()*0.2));
                data.put("imagepath",path);//放入照片的路徑
                data.put("message","<h1>台北101</h1>");
                data.put("frequency",(int)(Math.random()*100));
                location.put(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        b_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*調用js的功能回傳中心座標*/
                w_map.evaluateJavascript("javascript:getCenter()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.d("getCenter",value);
                        Toast.makeText(GoogleMap.this,"座標" + value,Toast.LENGTH_LONG).show();
                    }
                });

                /*actionbar的高度*/
                int actionBarHeight = getSupportActionBar().getHeight();
                String actionBarTitle = getSupportActionBar().getTitle().toString();

                Log.d("actionBarHeight",""+actionBarHeight + ":" +actionBarTitle );
            }
        });

        metersPerPx = 156543.03392 * Math.cos(25.014051 * Math.PI / 180) / Math.pow(2, 14);


        int y = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
        int z = getApplicationContext().getResources().getDisplayMetrics().heightPixels;


//        /*狀態欄高度*/
//        int statusBarHeight = -1;
//        //获取status_bar_height资源的ID
//        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            //根据资源ID获取响应的尺寸值
//            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
//        }
//        Log.e("WangJ", "状态栏-方法1:" + statusBarHeight);
//
//        //metersPerPx = 156543.03392 * Math.cos(latLng.lat() * Math.PI / 180) / Math.pow(2, zoom)//計算每畫素多少公里的公式
//        System.out.print(metersPerPx);


        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Log.e("WangJ", "状态栏-方法1:" + statusBarHeight);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mapmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m_prhybrid:
                w_map.loadUrl("javascript:getMaptype(\"prhybrid\")");//調用js程式改變地圖類型
                break;
            case R.id.m_roadmap:
                w_map.loadUrl("javascript:getMaptype(\"roadmap\")");//調用js程式改變地圖類型
                break;
            case R.id.m_satellite:
                w_map.loadUrl("javascript:getMaptype(\"satellite\")");//調用js程式改變地圖類型
                break;
            case R.id.m_terrain:
                w_map.loadUrl("javascript:getMaptype(\"terrain\")");//調用js程式改變地圖類型
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initWebView(){
        w_map.getSettings().setJavaScriptEnabled(true);
        w_map.setWebChromeClient(new WebChromeClient());
        w_map.getSettings().getJavaScriptCanOpenWindowsAutomatically();
        w_map.loadUrl("file:///android_asset/mygooglemap.html");
    }

    /**傳中心點的經度*/
    @JavascriptInterface
    public double getLat(){ //上面記得要打JavascriptInterface
        return lat;
    }
    /**傳中心點的緯度*/
    @JavascriptInterface
    public double getLng(){
        return lng;
    }
    /**傳附近活動德資料
     * 回傳先轉成字串到js那邊才好處理*/
    @JavascriptInterface
    public String getLocation(){
        return location.toString();
    }

    /**傳地圖類型選項*/
    @JavascriptInterface
    public String getMaptype(String mapType){
        return mapType;
    }
}