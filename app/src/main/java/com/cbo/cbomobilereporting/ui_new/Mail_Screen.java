package com.cbo.cbomobilereporting.ui_new;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.customtabs.CustomTabsIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.AdapterView;
import android.widget.GridView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.mail_activities.CreateMail1;
import com.cbo.cbomobilereporting.ui_new.mail_activities.Inbox_Mail;
import com.cbo.cbomobilereporting.ui_new.mail_activities.Notification;
import com.cbo.cbomobilereporting.ui_new.mail_activities.Outbox_Mail;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import utils.adapterutils.MailMenu_Grid_Adapter;
import utils.clearAppData.MyCustumApplication;
import utils.networkUtil.NetworkUtil;
import utils_new.Custom_Variables_And_Method;

public class Mail_Screen extends Fragment{
	Custom_Variables_And_Method customVariablesAndMethod;
	View v;
	CBO_DB_Helper cboDbHelper;
	ArrayList<String> listOfAllTab;
	ArrayList<Integer> count;
	ArrayList<String> getKeyList = new ArrayList<>();
	Map<String, String> keyValue = new LinkedHashMap<String, String>();
	Context context;
	GridView gridView;

	private void addAllTab() {


		keyValue = cboDbHelper.getMenu("MAIL","");
		count = new ArrayList<Integer>();
		listOfAllTab = new ArrayList<String>();
		for (String key : keyValue.keySet()) {
			getKeyList.add(key);
			count.add(get_count(key));
		}
		for (int i = 0; i < keyValue.size(); i++) {
			listOfAllTab.add(keyValue.get(getKeyList.get(i)));

		}
	}

	private int get_count(String menu){
		int result=0;
		String mail_category="";
		Boolean flag=false;
		switch (menu){
			case "M_IN":
				flag=true;
				mail_category="i";
				result=1;
				break;
			case "NOTIFICATION":
				flag=true;
				mail_category="NOTIFICATION";
				result=1;
				break;

		}
		if(flag && mail_category.equals("NOTIFICATION")){
			result=cboDbHelper.getNotification_count();
		}else if(flag && !mail_category.equals("")){
			result=cboDbHelper.getNoOfUnreadMail(mail_category);
		}
		return result;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.grid_menu_forall, container, false);


		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		context = getActivity();
		customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        cboDbHelper = new CBO_DB_Helper(context);
		final Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_alfa_2016);
		gridView = (GridView) v.findViewById(R.id.grid_view_example);
		addAllTab();
		Custom_Variables_And_Method.CURRENTTAB=((ViewPager_2016) getActivity()).getTabIndex();

		gridView.setAdapter(new MailMenu_Grid_Adapter(getActivity(), listOfAllTab, getKeyList,count));

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				v.startAnimation(anim);

				String onClick = getKeyList.get(position);
				String url=new CBO_DB_Helper(getActivity()).getMenuUrl("MAIL",onClick);
				if(url!=null && !url.equals("")) {
//					Intent i = new Intent(getActivity(), CustomWebView.class);
//					i.putExtra("A_TP", url);
//					i.putExtra("Title", listOfAllTab.get(position));
//					startActivity(i);
					MyCustumApplication.getInstance().LoadURL(listOfAllTab.get(position),url);
				}else {
					switch (onClick) {

						case "M_COM": {

							onClickCompose();

							break;

						}

						case "M_IN": {

							onClickInbox();
							break;
						}
						case "M_SITEM": {

							onClickSent();

							break;
						}

						case "NOTIFICATION": {

							onClickNotification();

							break;
						}
						default: {
							url = new CBO_DB_Helper(getActivity()).getMenuUrl("MAIL", getKeyList.get(position));
							if (url != null && !url.equals("")) {
								/*Intent i = new Intent(getActivity(), CustomWebView.class);
								i.putExtra("A_TP", url);
								i.putExtra("Title", listOfAllTab.get(position));
								startActivity(i);*/
								MyCustumApplication.getInstance().LoadURL(listOfAllTab.get(position),url);
							} else {
								customVariablesAndMethod.msgBox(context, "Page Under Development");
							}
						}
					}
				}


			}
		});




	}

	private void onClickNotification() {
		Intent intent=new Intent(getActivity(),Notification.class);
		startActivity(intent);
		//getActivity().finish();

	}

	private void onClickSent() {

		String networkStatus= NetworkUtil.getConnectivityStatusString(getActivity());
		if(networkStatus.equals("Not connected to Internet"))
		{
			customVariablesAndMethod.Connect_to_Internet_Msg(context);
		}
		else
		{
			Intent i = new Intent(getActivity(), Outbox_Mail.class);
			i.putExtra("mail_type","s");
			startActivity(i);


		}
	}

	private void onClickInbox() {

		String networkStatus= NetworkUtil.getConnectivityStatusString(getActivity());
		if(networkStatus.equals("Not connected to Internet"))
		{
			customVariablesAndMethod.Connect_to_Internet_Msg(context);
		}
		else
		{

			Intent i = new Intent(getActivity(), Inbox_Mail.class);
			startActivity(i);
		}

	}

	private void onClickCompose() {

	String networkStatus= NetworkUtil.getConnectivityStatusString(getActivity());
		if(networkStatus.equals("Not connected to Internet"))
		{
			customVariablesAndMethod.Connect_to_Internet_Msg(context);
		}
		else
		{

			Intent i=new Intent(getActivity(),CreateMail1.class);
			startActivity(i);
		}

	}

	@Override
	public void onStart(){
		super.onStart();
		addAllTab();
		cboDbHelper.getDetailsForOffline();
		gridView.setAdapter(new MailMenu_Grid_Adapter(getActivity(), listOfAllTab, getKeyList,count));

	}

	@Override
	public void onDestroy() {

		///getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
	//	getActivity().finish();

	super.onDestroy();
	}




	


}
