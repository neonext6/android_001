package com.example.androidsendreceivetest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * UserAdapter - Custom ListView를 구현하기 위해 하나의 아이템 정보와 레이아웃을 가져와서 합친다.
 * 
 * @author Cloud Travel
 */
public class DocListAdapter extends BaseAdapter {

	// Activity에서 가져온 객체정보를 저장할 변수
	private DocList mDocList;
	private Context mContext;

	// 리스트 아이템 데이터를 저장할 배열
	private ArrayList<DocList> mData;

	public DocListAdapter(Context context) {
		super();
		mContext = context;
		mData = new ArrayList<DocList>();
	}


	@Override
	/**
	 * @return 아이템의 총 개수를 반환
	 */
	public int getCount() {		
		return mData.size();
	}

	@Override
	/**
	 * @return 선택된 아이템을 반환
	 */
	public DocList getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// 데이터를 추가하는 것을 위해서 만들어 준다.
	public void add(DocList docList){
		mData.add(docList);
	}

	@Override
	/**
	 * getView
	 * 
	 * @param position - 현재 몇 번째로 아이템이 추가되고 있는지 정보를 갖고 있다.
	 * @param convertView - 현재 사용되고 있는 어떤 레이아웃을 가지고 있는지 정보를 갖고 있다.
	 * @param parent - 현재 뷰의 부모를 지칭하지만 특별히 사용되지는 않는다.
	 * @return 리스트 아이템이 저장된 convertView
	 */
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;

		ViewHolder holder;
		  
		// 리스트 아이템이 새로 추가될 경우에는 v가 null값이다.
		// view는 어느 정도 생성된 뒤에는 재사용이 일어나기 때문에 효율을 위해서 해준다.
		if (v == null) {
			// inflater를 이용하여 사용할 레이아웃을 가져옵니다.
			v = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.doc_list_layout, null);

			holder = new ViewHolder();
			// 레이아웃이 메모리에 올라왔기 때문에 이를 이용하여 포함된 뷰들을 참조할 수 있습니다.
			holder.iv_icon = (ImageView) v.findViewById(R.id.iv_icon);
			holder.tv_RN = (TextView) v.findViewById(R.id.tv_RN);
			holder.tv_APPL_YMD = (TextView) v.findViewById(R.id.tv_APPL_YMD);
			holder.tv_APPL_STAT_NM = (TextView) v.findViewById(R.id.tv_APPL_STAT_NM);
			holder.tv_APPL_TYPE_NM = (TextView) v.findViewById(R.id.tv_APPL_TYPE_NM);
			holder.tv_DOCU_TITLE = (TextView) v.findViewById(R.id.tv_DOCU_TITLE);
			holder.tv_APPL_ID = (TextView) v.findViewById(R.id.tv_APPL_ID);
			holder.tv_M_URL = (TextView) v.findViewById(R.id.tv_M_URL);
			holder.ib_popup = (ImageButton) v.findViewById(R.id.ib_popup);

			v.setTag(holder);
		}
		else {
			holder = (ViewHolder) v.getTag();
		}
		
		// 받아온 position 값을 이용하여 배열에서 아이템을 가져온다.
		mDocList = getItem(position);
		
		// Tag를 이용하여 데이터와 뷰를 묶습니다.
		//ib_popup.setTag(mDocList);
		
		// 데이터의 실존 여부를 판별합니다.
		if ( mDocList != null ) {
			// 데이터가 있다면 갖고 있는 정보를 뷰에 알맞게 배치시킵니다.
			holder.tv_RN.setText(mDocList.get_RN());
			holder.tv_APPL_YMD.setText(mDocList.get_APPL_YMD());
			holder.tv_APPL_STAT_NM.setText(mDocList.get_APPL_STAT_NM());
			holder.tv_APPL_TYPE_NM.setText(mDocList.get_APPL_TYPE_NM());
			holder.tv_DOCU_TITLE.setText(mDocList.get_DOCU_TITLE());
			holder.tv_APPL_ID.setText(mDocList.get_APPL_ID());
			holder.tv_M_URL.setText(mDocList.get_M_URL());
			if (mDocList.get_icon() != null) {
				holder.ib_popup.setImageDrawable(mDocList.get_icon());
			}

			//리스트 아이템을 터치했을때 이벤트 발생 (로우 클릭)
			v.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					DocList clickItem = getItem(position);
					//Toast 띄우기
					Toast.makeText(mContext, "[신청서ID] " + clickItem.get_APPL_ID(), Toast.LENGTH_SHORT).show();
				}
			});

			//이미지버튼을 터치했을때 이벤트 발생
			holder.ib_popup.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					DocList clickItem = getItem(position);

					switch (v.getId()) {
						case R.id.ib_popup: {
							if ("".equals(clickItem.get_DOCU_CONTENT()) || "null".equals(clickItem.get_DOCU_CONTENT())) {
								//Toast 띄우기
								Toast.makeText(mContext, "[브라우저로 띄우기 ... 상세 페이지는 미구현] ", Toast.LENGTH_SHORT).show(); //holder 값 직접 접근

								//일반신청서 브라우저로 띄우기 -- 현재 미구현 상태임 .. 각 신청서별로 구현해야함
								String emp_id = clickItem.get_emp_id();
								String pwd = clickItem.get_pwd();
								String APPL_ID = clickItem.get_APPL_ID();
								String url = "http://197.200.90.18:9099";
								url += clickItem.get_M_URL();
								url += "?S_C_CD=&S_USER_ID=" + emp_id + "&S_PWD=" + pwd + "&S_APPL_ID=" + APPL_ID;

								Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
								mContext.startActivity(intent);
							} else {
								//전자결재문서 SubActivity로 띄우기
								Intent intent = new Intent(mContext, SubActivity_01.class);
								intent.putExtra("docu_title", clickItem.get_DOCU_TITLE());
								intent.putExtra("docu_content", clickItem.get_DOCU_CONTENT());
								mContext.startActivity(intent);
							}
						}
						break;
					}
				}
			});


		}

		//숨김 처리
		holder.iv_icon.setVisibility(View.GONE);
		//holder.tv_RN.setVisibility(View.GONE);
		//holder.tv_APPL_YMD.setVisibility(View.GONE);
		//holder.tv_APPL_STAT_NM.setVisibility(View.GONE);
		//holder.tv_APPL_TYPE_NM.setVisibility(View.GONE);
		//holder.tv_DOCU_TITLE.setVisibility(View.GONE);
		holder.tv_APPL_ID.setVisibility(View.GONE);
		holder.tv_M_URL.setVisibility(View.GONE);
		//holder.ib_popup.setVisibility(View.GONE);

		// 완성된 아이템 뷰를 반환합니다.
		return v;
	}

	static class ViewHolder {
		public ImageView iv_icon;
		public TextView tv_RN;
		public TextView tv_APPL_YMD;
		public TextView tv_APPL_STAT_NM;
		public TextView tv_APPL_TYPE_NM;
		public TextView tv_DOCU_TITLE;
		public TextView tv_APPL_ID;
		public TextView tv_M_URL;
		public ImageButton ib_popup;
	}

}
