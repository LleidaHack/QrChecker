package com.app.qrchecker;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FirestoreConnector {
	FirebaseFirestore db;
	public FirestoreConnector(){
		this.db = FirebaseFirestore.getInstance();
	}

	public boolean checkUser(){
		db.collection("users")
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							for (QueryDocumentSnapshot document : task.getResult()) {
								Log.d(TAG, document.getId() + " => " + document.getData());
							}
						} else {
							Log.w(TAG, "Error getting documents.", task.getException());
						}
					}
				});
	}
}
