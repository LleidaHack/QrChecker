package com.app.qrchecker;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
		MainActivity m=this;
		Button access=findViewById(R.id.acces);
		Button register=findViewById(R.id.registre);
		Button menjar=findViewById(R.id.menjar);
		ImageView refresh=findViewById(R.id.refresh);
		access.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) { gotoScan(ScanOptions.ACCESS); }
		});
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
				FirestoreConnector.getUsers(m);
			}
		});
		FirestoreConnector.getUsers(m);
	}
	public void gotoScan(ScanOptions opt, EatOptions eatType) {

		startActivity(new Intent(MainActivity.this, ScannerActivity.class)
				.putExtra("ScanOption",opt.ordinal()).putExtra("eatType", eatType));
	}
	public void gotoScan(ScanOptions opt) {

		gotoScan(opt, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void setUsers(int size) {
		TextView access=findViewById(R.id.users);
		access.setText("users -> "+size);
	}

	public void setRegUsers(int size) {
		TextView access=findViewById(R.id.regUsers);
		access.setText("registered users -> "+size);
	}
	public void setSatLunch(int size) {
		TextView access=findViewById(R.id.esmorzarUsers);
		access.setText("Usuaris dinats (Dissabte) -> "+size);
	}

	public void setSatDin(int size) {
		TextView access=findViewById(R.id.dinarUsers);
		access.setText("Usuaris sopats -> "+size);
	}

	public void setSunLunch(int size) {
		TextView access=findViewById(R.id.soparUsers);
		access.setText("Usuaris dinats (Diumenge) -> "+size);
	}
}
