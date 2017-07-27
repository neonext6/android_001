package com.example.androidsendreceivetest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import static android.R.attr.data;
import static android.R.id.list;

public class MainActivity extends Activity {

    public String m_emp_id;
    public String m_pwd;

    private LinearLayout ll_layout1;
    private TextView tvDebug;

    private LinearLayout ll_layout2;
    private EditText et_emp_id;
    private EditText et_pwd;
    private Button btnSend;

    private LinearLayout ll_layout3;
    private EditText et_title;
    private EditText et_message;
    private Button btnPush;

    private LinearLayout ll_layout4;
    private TextView tvRecvData;

    private LinearLayout ll_layout5;
    private ListView listView;

    private LinearLayout ll_layout6;
    private ListView lv_doc_list;
    private DocListAdapter docListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ll_layout1 = (LinearLayout) findViewById(R.id.ll_layout1);
        tvDebug = (TextView) findViewById(R.id.tvDebug);
        ll_layout1.setVisibility(View.GONE); //숨김처리

        et_emp_id = (EditText) findViewById(R.id.et_emp_id);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        btnSend = (Button) findViewById(R.id.btn_sendData);

        ll_layout3 = (LinearLayout) findViewById(R.id.ll_layout3);
        et_title = (EditText) findViewById(R.id.et_title);
        et_message = (EditText) findViewById(R.id.et_message);
        btnPush = (Button) findViewById(R.id.btn_sendPush);
        ll_layout3.setVisibility(View.GONE); //숨김처리

        tvRecvData = (TextView)	findViewById(R.id.tv_recvData);

        ll_layout5 = (LinearLayout) findViewById(R.id.ll_layout5);
        listView = (ListView) findViewById(R.id.listview1);
        ll_layout5.setVisibility(View.GONE); //숨김처리


        /*
        //------------------------------------------------------------------------------------------
        // Custon List View 샘플
        // list_item_layout.xml, User.java, UserAdapter.java
        //------------------------------------------------------------------------------------------
        // Adapter 생성
        adapter = new UserAdapter(getApplicationContext());
        // 리스트뷰 참조 및 Adapter달기
        userList = (ListView) findViewById(R.id.user_list);
        userList.setAdapter(adapter);
        // Data 추가
        User u1 = new User(getResources().getDrawable(R.drawable.test_user_icon1), "김씨", "010-1234-5678");
        adapter.add(u1);
        User u2 = new User(getResources().getDrawable(R.drawable.test_user_icon2), "이씨", "010-8765-4321");
        adapter.add(u2);
        User u3 = new User(getResources().getDrawable(R.drawable.test_user_icon3), "박씨", "010-0000-0000");
        adapter.add(u3);
        // Data가 변경 되있음을 알려준다.
        adapter.notifyDataSetChanged();
        //------------------------------------------------------------------------------------------
        */


        //파일을 읽어서 사번값이 존재하면 사번, 비번을 EditText에 세팅
        try {
            StringBuffer data = new StringBuffer();
            FileInputStream fis = openFileInput("properties.txt");
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fis));
            String str = buffer.readLine(); // 파일에서 한줄을 읽어옴
            while (str != null) {
                data.append(str + "\n");
                str = buffer.readLine();
            }
            JSONObject json = new JSONObject(data.toString());

            if( !"".equals(json.getString("emp_id")) ) {
                et_emp_id.setText(json.getString("emp_id"));
                et_pwd.setText(json.getString("pwd"));
            }

            buffer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


		//조회 버튼을 눌렀을 때
        btnSend.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                // Thread로 웹서버에 접속
                new Thread() {
                    public void run() {
                        String emp_id = et_emp_id.getText().toString(); // 안드로이드 에디트박스에 입력한 메시지
                        String pwd    = et_pwd.getText().toString(); // 안드로이드 에디트박스에 입력한 메시지
                        String url = "http://197.200.90.67:8080/hit_rest2/hit_rest_select01.do?emp_id="+emp_id+"&pwd="+pwd;
                        String jsonStrDocList = getData(url); //web에 접속하여 데이터를 리턴 받음

                        //비밀번호 체크
                        try {
                            JSONObject json = new JSONObject(jsonStrDocList);
                            int checkPwdCnt = json.getInt("checkPwdCnt");

                            //사번, 비번 일치하면 클라이언트 안드로이드 기기의 사번과 Firebase Token 값을 DB에 저장 : 푸시 알림 대상 기기 저장
                            if(checkPwdCnt == 1)
                            {
                                //푸시 알림 관련 (FCM)
                                //주제를 구독하려면 클라이언트 앱에서 FCM 주제 이름과 함께 Firebase 클라우드 메시징 subscribeToTopic()을 호출합니다.
                                //한가지 유의해야 할것은 토픽이 즉시 적용되지 않는다. 생성되는데 몇시간 길게는 하루가 걸리기도 한다고 한다. ???
                                FirebaseMessaging.getInstance().subscribeToTopic("news");
                                //FirebaseMessaging.getInstance().subscribeToTopic("androidsendreceivetest");
                                String token = FirebaseInstanceId.getInstance().getToken(); //이 기기(안드로이드)의 FCM tokenId
                                Log.d("FCM_Token", token);

                                //token 값을 DB에 저장
                                String saveFcmTokenUrl = "http://197.200.90.67:8080/hit_rest2/hit_rest_saveFcmToken.do?emp_id="+emp_id+"&token="+token;
                                String saveFcmTokenResult = getData(saveFcmTokenUrl); //web에 접속하여 데이터를 리턴 받음

                                //TextView에 FCM token 세팅
                                Bundle bunDebug = new Bundle();
                                bunDebug.putString("msg", "== 사번, 비번 일치하여 FCM_Token 값을 DB에 저장함 : " + token);
                                Message msgDebug = handlerDebug.obtainMessage();
                                msgDebug.setData(bunDebug);
                                handlerDebug.sendMessage(msgDebug);
                            }
                        } catch (JSONException e) {
                        }

                         String[] parsedData = jsonParserList(jsonStrDocList); // JSON 데이터 파싱

                        //TextView에 데이터 건수 세팅
                        Bundle bun = new Bundle();
                        bun.putString("dataCnt", Integer.toString(parsedData.length));
                        Message msg = handler.obtainMessage();
                        msg.setData(bun);
                        handler.sendMessage(msg);

                        //ListView에 조회된 데이터 세팅
                        Bundle bun2 = new Bundle();
                        bun2.putStringArray("parsedData", parsedData);
                        Message msg2 = handler2.obtainMessage();
                        msg2.setData(bun2);
                        handler2.sendMessage(msg2);

                        //Custon List View에 조회된 데이터 세팅
                        Bundle bun3 = new Bundle();
                        bun3.putString("jsonStrDocList", jsonStrDocList);
                        Message msg3 = handler3.obtainMessage();
                        msg3.setData(bun3);
                        handler3.sendMessage(msg3);

                        //입력된 사번, 비번을 porperties.txt 파일에 저장
                        try {
                            m_emp_id = et_emp_id.getText().toString();
                            m_pwd = et_pwd.getText().toString();

                            FileOutputStream fos = openFileOutput("properties.txt", Context.MODE_PRIVATE);
                            PrintWriter out = new PrintWriter(fos);
                            out.println("{emp_id: "+et_emp_id.getText().toString()+", pwd: "+et_pwd.getText().toString()+"}");
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }.start();

            }
        });

        //푸시 버튼을 눌렀을 때
        btnPush.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                // Thread로 웹서버에 접속
                new Thread() {
                    public void run() {
                        String emp_id = et_emp_id.getText().toString(); // 안드로이드 에디트박스에 입력한 메시지
                        String title = et_title.getText().toString(); // 안드로이드 에디트박스에 입력한 메시지
                        String message = et_message.getText().toString(); // 안드로이드 에디트박스에 입력한 메시지
                        String url = "http://197.200.90.67:8080/hit_rest2/sendPush01.do?arr_str_emp_id="+emp_id+"&title="+title+"&message="+message;
                        String result = getData(url); //web에 접속하여 데이터를 리턴 받음

                    }
                }.start();
            }
        });

        //listView의 로우를 클릭했을 때
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "listView 클릭 ", Toast.LENGTH_LONG).show();
            }
        });

    }

    private String getData(String strUrl) {
        String rtnStr = "";

        URL url = null;
        HttpURLConnection http = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try{
            //"http://197.200.90.67:8080/hit_rest2/hit_rest_select01.do?emp_id="+emp_id+"&pwd="+pwd
            url = new URL(strUrl);
            http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(3*1000);
            http.setReadTimeout(3*1000);

            isr = new InputStreamReader(http.getInputStream(), "UTF-8");
            //isr = new InputStreamReader(http.getInputStream());
            br = new BufferedReader(isr);

            String str = null;
            while ((str = br.readLine()) != null) {
                System.out.println("===== line : " + str);
                rtnStr += str + "\n";
            }
        }catch(Exception e){
            Log.e("Exception", e.toString());
        }finally{
            if(http != null){
                try{http.disconnect();}catch(Exception e){}
            }
            if(isr != null){
                try{isr.close();}catch(Exception e){}
            }
            if(br != null){
                try{br.close();}catch(Exception e){}
            }
        }

        return rtnStr;
    }

    /**
     * 받은 JSON 객체를 파싱하는 메소드
     * @param pRecvServerPage
     * @return
     */
    private String[] jsonParserList(String pRecvServerPage) {
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("docList");

            // 받아온 pRecvServerPage를 분석하는 부분
            String[] parseredData = new String[jArr.length()];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                String tmpStr = "";
                tmpStr += "일자 : " + json.getString("applYmd")    + "\n";
                tmpStr += "상태 : " + json.getString("applStatNm") + "\n";
                tmpStr += "유형 : " + json.getString("applTypeNm") + "\n";
                tmpStr += "제목 : " + json.getString("docuTitle");
                parseredData[i] = tmpStr;
            }

            return parseredData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    Handler handlerDebug = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();
            tvDebug.setText(bun.getString("msg"));
        }
    };

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();
            tvRecvData.setText(bun.getString("dataCnt"));
        }
    };

    Handler handler2 = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();
            ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, bun.getStringArray("parsedData"));
            listView.setAdapter(adapter);
        }
    };

    Handler handler3 = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();

            // Adapter 생성
            docListAdapter = new DocListAdapter(getApplicationContext());
            // 리스트뷰 참조 및 Adapter달기
            lv_doc_list = (ListView) findViewById(R.id.lv_doc_list);
            lv_doc_list.setAdapter(docListAdapter);

            // json 형태의 문자열을 parsing해서 adapter의 data로 추가
            try {
                JSONObject json = new JSONObject(bun.getString("jsonStrDocList"));
                JSONArray jArr = json.getJSONArray("docList");

                for (int i = 0; i < jArr.length(); i++) {
                    json = jArr.getJSONObject(i);

                    String emp_id = et_emp_id.getText().toString();
                    String pwd    = et_pwd.getText().toString();

                    String rn         = json.getString("rn");
                    String applYmd    = json.getString("applYmd");
                    String applStatNm = json.getString("applStatNm");
                    String applTypeNm = json.getString("applTypeNm");
                    String docuTitle  = json.getString("docuTitle");
                    String applId     = json.getString("applId");
                    String mUrl       = json.getString("mUrl");
                    String docuContent= json.getString("docuContent");
                    Log.d("===== applId [" + rn + "] : ", applId);

                    Drawable icon;
                    if( "".equals(docuContent) || "null".equals(docuContent) )
                        icon = getResources().getDrawable(R.drawable.icon_internet); //인터넷 아이콘
                    else
                        icon = getResources().getDrawable(R.drawable.icon_document); //문서 아이콘

                    DocList docList = new DocList(emp_id, pwd, icon, rn, applYmd, applStatNm, applTypeNm, docuTitle, applId, mUrl, docuContent);
                    docListAdapter.add(docList);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Data가 변경 되있음을 알려준다.
            docListAdapter.notifyDataSetChanged();
        }
    };

}
