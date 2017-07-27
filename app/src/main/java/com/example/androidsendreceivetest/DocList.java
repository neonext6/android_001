package com.example.androidsendreceivetest;

import android.graphics.drawable.Drawable;

public class DocList {
	private String m_emp_id;
	private String m_pwd;

	private Drawable m_icon;

	private String m_RN;
	private String m_APPL_YMD;
	private String m_APPL_STAT_NM;
	private String m_APPL_TYPE_NM;
	private String m_DOCU_TITLE;
	private String m_APPL_ID;
	private String m_M_URL;
	private String m_DOCU_CONTENT;

	DocList(String emp_id, String pwd, Drawable icon, String RN, String APPL_YMD, String APPL_STAT_NM, String APPL_TYPE_NM, String DOCU_TITLE, String APPL_ID, String M_URL, String DOCU_CONTENT){
		m_emp_id = emp_id;
		m_pwd = pwd;

		m_icon = icon;

		m_RN = RN;
		m_APPL_YMD = APPL_YMD;
		m_APPL_STAT_NM = APPL_STAT_NM;
		m_APPL_TYPE_NM = APPL_TYPE_NM;
		m_DOCU_TITLE = DOCU_TITLE;
		m_APPL_ID = APPL_ID;
		m_M_URL = M_URL;
		m_DOCU_CONTENT = DOCU_CONTENT;
	}

	public String get_emp_id() {
		return m_emp_id;
	}
	public String get_pwd() { return m_pwd; }

	public Drawable get_icon() {
		return m_icon;
	}

	public String get_RN() { return m_RN; }
	public String get_APPL_YMD() {
		return m_APPL_YMD;
	}
	public String get_APPL_STAT_NM() { return m_APPL_STAT_NM; }
	public String get_APPL_TYPE_NM() {
		return m_APPL_TYPE_NM;
	}
	public String get_DOCU_TITLE() {
		return m_DOCU_TITLE;
	}
	public String get_APPL_ID() {
		return m_APPL_ID;
	}
	public String get_M_URL() { return m_M_URL; }
	public String get_DOCU_CONTENT() { return m_DOCU_CONTENT; }

}
