package com.app.qrchecker;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity{
		String info;
		String title;
		TextView info_text;
		TextView title_text;
		Button back_button;
		RelativeLayout layout;
		boolean error;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			setContentView(R.layout.activity_info);
			info = getIntent().getExtras().getString("info");
			title = getIntent().getExtras().getString("title");
			error = getIntent().getExtras().getBoolean("error");
			setError();
			initStrings();
		}
		private void setError(){
			if(!error) {
				findViewById(R.id.layout).setBackgroundResource(R.color.green);
				findViewById(R.id.image).setBackgroundResource(R.drawable.ok);
//				((Button)findViewById(R.id.back)).setTextColor(R.color.green);
			}else{
				findViewById(R.id.layout).setBackgroundResource(R.color.red);
				findViewById(R.id.image).setBackgroundResource(R.drawable.nook);
//				((Button)findViewById(R.id.back)).setTextColor(R.color.red);
			}
		}
		private void initStrings(){
			findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			info_text = findViewById(R.id.info);
			info_text.setText(info);
			title_text = findViewById(R.id.title);
			title_text.setText(title);
		}
}
