package com.qiyei.appdemo.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qiyei.appdemo.R;
import com.qiyei.appdemo.model.DiscoverListResult;
import com.qiyei.appdemo.net.Bean2;
import com.qiyei.appdemo.net.DiscoverListReq;
import com.qiyei.appdemo.net.RequestObject;
import com.qiyei.appdemo.net.ResponseObject;
import com.qiyei.appdemo.net.RetrofitApiService;
import com.qiyei.sdk.https.api.HttpManager;
import com.qiyei.sdk.https.api.IHttpListener;
import com.qiyei.sdk.https.api.HttpRequest;
import com.qiyei.sdk.https.api.IHttpTransferListener;
import com.qiyei.sdk.log.LogManager;
import com.qiyei.sdk.util.AndroidUtil;

/**
 * @author Created by qiyei2015 on 2017/10/28.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description:
 */
public class NetworkTestActivity extends AppCompatActivity {

    private final String TAG = "BaseActivity";


    private ProgressBar mDownloadProgressBar;
    private ProgressBar mUploadProgressBar;

    private TextView mDownloadProgressTv;
    private TextView mUploadProgressTv;

    /**
     * 本地IP地址
     */
    private final String baseurl = "http://192.168.1.103:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDownloadProgressBar = (ProgressBar) findViewById(R.id.download_progress);
        mUploadProgressBar = (ProgressBar) findViewById(R.id.upload_progress);
        mDownloadProgressTv = (TextView) findViewById(R.id.download_progress_tv);
        mUploadProgressTv = (TextView) findViewById(R.id.upload_progress_tv);


        mDownloadProgressBar.setMax(100);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //get请求
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpManager().execute(getSupportFragmentManager(), buildGetRequest(), new IHttpListener<DiscoverListResult>() {

                    @Override
                    public void onSuccess(DiscoverListResult response) {
                        LogManager.d(TAG,"name --> "+response.getData().getCategories().getName());
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        LogManager.d(TAG,exception.getMessage());
                    }
                });
            }
        });

        //post请求
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpManager().execute(getSupportFragmentManager(),buildPostRequest(), new IHttpListener<ResponseObject<Bean2>>() {

                    @Override
                    public void onSuccess(ResponseObject<Bean2> response) {
                        Bean2 bean2 = response.getContent();
                        LogManager.d(TAG,"response --> "+ bean2.toString());
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        LogManager.d(TAG,exception.getMessage());
                    }
                });
            }
        });

        //download请求
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpManager().execute(getSupportFragmentManager(),buildDownloadRequest(), new IHttpTransferListener<String>() {

                    @Override
                    public void onProgress(long currentLength, long totalLength) {
                        int progress = (int) ((currentLength * 1.0 / totalLength) * 100);
                        mDownloadProgressBar.setProgress(progress);
                        mDownloadProgressTv.setText( progress +"%");
                        LogManager.i(TAG,"progress:"+ progress +" currentLength :" + currentLength + " totalLength:" + totalLength);
                    }

                    @Override
                    public void onSuccess(Object response) {
                        LogManager.i(TAG,"response:" + response);
                    }

                    @Override
                    public void onFailure(Exception exception) {

                    }
                });
            }
        });

        //upload请求
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    /**
     * retrofit请求
     * @return
     */
    private HttpRequest buildGetRequest(){
        DiscoverListReq req = new DiscoverListReq();
        req.setIid("6152551759");
        req.setAid("7");
        HttpRequest<DiscoverListReq> request = new HttpRequest.Builder<DiscoverListReq>()
                .get()
                .setBaseUrl("http://is.snssdk.com/2/essay/")
                .setPathUrl("discovery/v3/")
                .setBody(req)
                .setApiClazz(RetrofitApiService.class)
                .build();
        return request;
    }

    /**
     * retrofit post请求
     * @return
     */
    private HttpRequest buildPostRequest(){

        Bean2 bean = new Bean2("qiyei2016","123456","别名","1234567890@qq.com");

        RequestObject<Bean2> req = new RequestObject<>();
        req.setContent(bean);
        HttpRequest<RequestObject<Bean2>> request = new HttpRequest.Builder<>()
                .post()
                .setBaseUrl(baseurl)
                .setPathUrl("user/register")
                .setBody(req)
                .setApiClazz(RetrofitApiService.class)
                .build();
        return request;
    }


    /**
     * 下载请求
     * @return
     */
    private HttpRequest buildDownloadRequest(){

        String url = "http://central.maven.org/maven2/com/google/code/gson/gson/2.8.2/gson-2.8.2.jar";

        String url2  = "http://sw.bos.baidu.com/sw-search-sp/software/16d5a98d3e034/QQ_8.9.5.22062_setup.exe";

        HttpRequest<String> request = new HttpRequest.Builder<String>()
                .download()
                .setBaseUrl("http://sw.bos.baidu.com/")
                .setPathUrl("sw-search-sp/software/16d5a98d3e034/QQ_8.9.5.22062_setup.exe")
                .setFilePath(AndroidUtil.getExternalDataPath() + "/download/QQ.exe")
                .setBody(null)
                .setApiClazz(RetrofitApiService.class)
                .build();
        return request;

    }
}
