package com.app.qrchecker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Vibrator;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
		setButtons();
	}

	private void setButtons() {
		MainActivity m = this;
		Button register = findViewById(R.id.registre);
		Button menjar = findViewById(R.id.menjar);
		ImageView refresh = findViewById(R.id.refresh);

		register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				gotoScan(ScanOptions.REGISTER);
			}
		});
		menjar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//todo -> Implementar el leer comidas/cena/desayuno
				//FirestoreConnector.eatUser("",EatOptions.lunch_sat,null);
				gotoScan(ScanOptions.EAT, EatOptions.lunch_sat);
			}
		});
		refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ApiConnector.getEventStatus(1,m);
			}
		});
		ApiConnector.getEventStatus(1,m);
	}

	public void gotoScan(ScanOptions opt, EatOptions eatType) {

		startActivity(new Intent(MainActivity.this, ScannerActivity.class)
				.putExtra("ScanOption", opt.ordinal()).putExtra("eatType", eatType));
	}

	public void gotoScan(ScanOptions opt) {

		gotoScan(opt, null);
	}



	private void vibrate(){
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		// Vibrate for 400 milliseconds
		v.vibrate(200);
	}
	public void setUsers(int size) {
		TextView access = findViewById(R.id.users);
		access.setText("Inscrits -> " + size);
		vibrate();
	}

	public void setRegUsers(int size) {
		TextView access = findViewById(R.id.regUsers);
		access.setText("Registrats -> " + size);
		vibrate();
	}
	public void setSatLunch(int size) {
		TextView access = findViewById(R.id.dinarDissabte);
		access.setText("Usuaris dinats (Dissabte) -> " + size);
		vibrate();
	}

	public void setSatDin(int size) {
		TextView access = findViewById(R.id.soparDissabte);
		access.setText("Usuaris sopats (Dissabte) -> " + size);
		vibrate();
	}

	public void setSunLunch(int size) {
		TextView access = findViewById(R.id.dinarDiumenge);
		access.setText("Usuaris dinats (Diumenge) -> " + size);
		vibrate();
	}
}
