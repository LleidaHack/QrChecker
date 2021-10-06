package com.app.qrchecker;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class FirestoreConnector {
	private static FirebaseFirestore db;
	private static CollectionReference userCollection;
	private static  CollectionReference logCollection;
	private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/mm/YYYY-HH:mm:ss");
	private static String env="dev";
	public FirestoreConnector(){
		initFirebase();
	}

	private void initFirebase() {
		if(db == null)
			db = FirebaseFirestore.getInstance();
		if(userCollection == null)
			userCollection = db.collection("hackeps-2021").document(env).collection("users");
		if(logCollection == null)
			logCollection = db.collection("hackeps-2021").document(env).collection("log");

	}

	public void registerUser(){
		//Query query = citiesRef.whereEqualTo("state", "CA");

	}
	public void accessUser(String uid){

		LocalDateTime now = LocalDateTime.now();
		Map<String, Object> data = new HashMap<>();
		data.put("time", dtf.format(now));

		logCollection.document(uid)
				.set(data)
				.addOnSuccessListener(new OnSuccessListener<Void>() {
					@Override
					public void onSuccess(Void aVoid) {
						Log.d("s", "DocumentSnapshot successfully written!");
					}
				})
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Log.w("s", "Error writing document", e);
					}
				});
	}
	public void checkUser(){
		Query query = userCollection.whereEqualTo("uid", "0gIqnwC6BRhREctO0Mm1kmcRpAh1");
		query.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							for (QueryDocumentSnapshot document : task.getResult()) {
								Log.d("FIREBASE", document.getId() + " => " + document.getData());
							}
						} else {
							Log.d("FIREBASE", "Error getting documents: ", task.getException());
						}
					}
				});
		/*userCol.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							for (QueryDocumentSnapshot document : task.getResult()) {
							}
						} else {
						}
					}
				});*/
	}

}
