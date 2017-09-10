package com.shuai.futures.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;


import com.shuai.futures.R;
import com.shuai.futures.ui.base.BaseFragment;


public class UserCenterLoginFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_center_login, container, false);
		
		Button btnRegister = (Button) view.findViewById(R.id.btn_register);
		btnRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), RegisterByPhoneActivity.class);
				startActivity(intent);
			}
		});
		
		Button btnLogin = (Button) view.findViewById(R.id.btn_login);
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
			}
		});
		
		return view;
	}
	
}
