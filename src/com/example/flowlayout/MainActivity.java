package com.example.flowlayout;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

	MyFlowLayout mFLowLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mFLowLayout = (MyFlowLayout) findViewById(R.id.myflowlayout);
		addView();
	}

	private void addView() {
		for (int i = 0; i < 15; i++) {
			TextView view = (TextView)getLayoutInflater().inflate(R.layout.mytextview,mFLowLayout, false);
			view.setText("welcome");
			if(i==8){
				view.setText("you jump ,i jump. jump jump");
				//view.setVisibility(View.VISIBLE);
			}
			mFLowLayout.addView(view);
		}

	}
}
