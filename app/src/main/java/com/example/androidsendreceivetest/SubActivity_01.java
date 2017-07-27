package com.example.androidsendreceivetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SubActivity_01 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_01);

        //MainActivity에서 보낸 인텐트 받기
        Intent intent = new Intent(this.getIntent());
        String docu_title = intent.getStringExtra("docu_title");
        String docu_content = intent.getStringExtra("docu_content");

        //TextView에 전자결재문서 제목 세팅
        TextView sub01_tv_01 = (TextView) findViewById(R.id.sub01_tv_01);
        sub01_tv_01.setText(docu_title);

        //WebView에 전자결재문서 내용 세팅
        WebView sub01_wv_01 = (WebView)findViewById(R.id.sub01_wv_01);
        sub01_wv_01.getSettings().setJavaScriptEnabled(true); //자바스크립트 허용
        sub01_wv_01.loadData(docu_content,  "text/html; charset=UTF-8", null);  // 한글깨짐 방지(Android 4.1 이상 버전)
    }

}
