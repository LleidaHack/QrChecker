package com.app.qrchecker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.type.DateTime;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

public class ScannerActivity extends AppCompatActivity {


	SurfaceView surfaceView;
	private BarcodeDetector barcodeDetector;
	private CameraSource cameraSource;
	private static final int REQUEST_CAMERA_PERMISSION = 201;
	private ScanOptions opt;
	private EatOptions eopt;
	private String last_barcode_value;
	private Date last_barcode_scan;
	Button lsat;
	Button lsun;
	Button dsat;
	VibrationEffect ve= VibrationEffect.createOneShot(400,10);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_scanner);
		initViews();
		last_barcode_value=null;
		opt = ScanOptions.values()[getIntent().getExtras().getInt("ScanOption")];
		showEatOptions(!opt.equals(ScanOptions.EAT));
		if (opt.equals(ScanOptions.EAT)) {
			setEatButtons();
		}
	}

	private void setEatButtons() {
		lsat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				lsat.setBackgroundResource(R.color.green);
				lsun.setBackgroundResource(R.color.white);
				dsat.setBackgroundResource(R.color.white);
				eopt = EatOptions.lunch_sat;
				last_barcode_value=null;
			}
		});
		lsun.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				lsat.setBackgroundResource(R.color.white);
				lsun.setBackgroundResource(R.color.green);
				dsat.setBackgroundResource(R.color.white);
				eopt = EatOptions.lunch_sun;
				last_barcode_value=null;
			}
		});
		dsat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				lsat.setBackgroundResource(R.color.white);
				lsun.setBackgroundResource(R.color.white);
				dsat.setBackgroundResource(R.color.green);
				eopt = EatOptions.dinner_sat;
				last_barcode_value=null;
			}
		});
	}

	private void showEatOptions(boolean hide) {
		int vis = hide ? View.INVISIBLE : View.VISIBLE;
		lsat.setVisibility(vis);
		lsat.setEnabled(!hide);
		lsat.setBackgroundResource(R.color.white);

		lsun.setVisibility(vis);
		lsun.setEnabled(!hide);
		lsun.setBackgroundResource(R.color.white);

		dsat.setVisibility(vis);
		dsat.setEnabled(!hide);
		dsat.setBackgroundResource(R.color.white);
	}

	private void initViews() {
		surfaceView = findViewById(R.id.surfaceView);
		lsat = findViewById(R.id.lunch_sat);
		lsun = findViewById(R.id.lunch_sun);
		dsat = findViewById(R.id.dinner_sat);
	}

	private void vibrate(){
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		// Vibrate for 400 milliseconds
		v.vibrate(ve);
	}

	private void initialiseDetectorsAndSources() {

		Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();
		barcodeDetector = new BarcodeDetector.Builder(this)
				.setBarcodeFormats(Barcode.ALL_FORMATS)
				.build();

		cameraSource = new CameraSource.Builder(this, barcodeDetector)
				.setRequestedPreviewSize(1000, 1000)
				.setAutoFocusEnabled(true) //you should add this feature
				.build();

		surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				try {
					if (ActivityCompat.checkSelfPermission(ScannerActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
						cameraSource.start(surfaceView.getHolder());
					} else {
						ActivityCompat.requestPermissions(ScannerActivity.this, new
								String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				cameraSource.stop();
			}
		});


		barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
			@Override
			public void release() {
//				Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void receiveDetections(Detector.Detections<Barcode> detections) {
				final SparseArray<Barcode> barcodes = detections.getDetectedItems();
				if (barcodes.size() != 0) {
					executeOperation(barcodes.valueAt(0));
				}
			}
		});
	}

	private void executeOperation(Barcode barcode) {
		if (!barcode.displayValue.equals(last_barcode_value) /*||
//				(last_barcode_scan != null && last_barcode_scan.getTime() - System.currentTimeMillis() > 2000)*/) {
			//Log.d("TEST QR", barcode.displayValue);
			if (opt == ScanOptions.REGISTER)
				FirestoreConnector.registerUser(barcode.displayValue, this);
			else if (opt == ScanOptions.EAT) {
				if (eopt != null)
					FirestoreConnector.eatUser(barcode.displayValue, eopt, this);
			} else if (opt == ScanOptions.ACCESS) {
//				FirestoreConnector.accessUser(barcode.displayValue, this);
			}
			last_barcode_value = barcode.displayValue;
		}
	}

	public void log(boolean error, String errorMsg, String info) {
//		runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//
////				TextView txt = findViewById(R.id.log);
////				int color = error ? Color.RED : Color.GREEN;
////				txt.setText(errorMsg);
////				txt.setBackgroundColor(color);
//			}
//		});
		startActivity(new Intent(ScannerActivity.this, InfoActivity.class)
				.putExtra("title", errorMsg)
				.putExtra("info", info)
				.putExtra("error", error));
		vibrate();
	}

	@Override
	protected void onPause() {
		super.onPause();
		cameraSource.release();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initialiseDetectorsAndSources();
	}
}
