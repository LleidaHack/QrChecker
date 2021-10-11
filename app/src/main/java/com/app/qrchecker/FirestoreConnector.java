package com.app.qrchecker;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

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

	private static void initFirebase() {
		if(db == null)
			db = FirebaseFirestore.getInstance();
		if(userCollection == null)
			userCollection = db.collection("hackeps-2021").document(env).collection("users");
		if(logCollection == null)
			logCollection = db.collection("hackeps-2021").document(env).collection("log");

	}
/*
	void runTransaction(){
		db.runTransaction(new Transaction.Function<Void>() {
			@Override
			public Void apply(Transaction transaction) throws FirebaseFirestoreException {
				DocumentSnapshot snapshot = transaction.get(sfDocRef);

				// Note: this could be done without a transaction
				//       by updating the population using FieldValue.increment()
				double newPopulation = snapshot.getDouble("population") + 1;
				transaction.update(sfDocRef, "population", newPopulation);

				// Success
				return null;
			}
		}).addOnSuccessListener(new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void aVoid) {
				//Log.d(TAG, "Transaction success!");
			}
		})
		.addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				//Log.w(TAG, "Transaction failure.", e);
			}
		});
	}*/

	public static void registerUser(String uid, RelativeLayout c){
		initFirebase();
		db.runTransaction(new Transaction.Function<Void>() {
			@Override
			public Void apply(Transaction transaction) throws FirebaseFirestoreException {
				DocumentSnapshot snapshot = transaction.get(userCollection.document(uid));
				if(snapshot.exists() && !snapshot.getBoolean("registered")){
					transaction.update(userCollection.document(uid),"registered",true);
				}
				else c.setBackgroundColor(Color.BLUE);
				// Success
				return null;
			}
		}).addOnSuccessListener(new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void aVoid) {
				//throw new RuntimeException("");
			}
		})
		.addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				//Log.w(TAG, "Transaction failure.", e);
			}
		});

	}
	public void accessUser(String uid){

		LocalDateTime now = LocalDateTime.now();
		Map<String, Object> data = new HashMap<>();
		logCollection.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DocumentSnapshot> task) {
				if (task.isSuccessful()) {
					DocumentSnapshot document = task.getResult();
					if (document.exists()) {
						//Log.d(TAG, "DocumentSnapshot data: " + document.getData());
						//getC
					} else {

					}
				} else {
					//Log.d(TAG, "get failed with ", task.getException());
				}
			}
		});
		//Query query = userCollection.whereEqualTo("uid", "0gIqnwC6BRhREctO0Mm1kmcRpAh1");
		data.put("time_in", dtf.format(now));
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
