package com.app.qrchecker;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FirestoreConnector {
	//TODO Some code style improvements
	private static FirebaseFirestore db;
	private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/mm/YYYY-HH:mm:ss");

	private static CollectionReference userCollection;
	private static CollectionReference logCollection;
	private static CollectionReference satLunchCollection;
	private static CollectionReference satDinnerCollection;
	private static CollectionReference sunLunchCollection;

	private static String env="dev";
	private static String year="2021";
	private static String guest="HackEPS_Guest_.";

	private static void initFirebase() {
		if(db == null)
			db = FirebaseFirestore.getInstance();
		if(userCollection == null)
			userCollection = db.collection("hackeps-"+year).document(env).collection("users");
		if(logCollection == null)
			logCollection = db.collection("hackeps-"+year).document(env).collection("log");
		if(satLunchCollection == null)
			satLunchCollection = db.collection("hackeps-"+year).document(env).collection("events")
					.document("eats").collection("lunch_sat");

		if(satDinnerCollection == null)
			satDinnerCollection = db.collection("hackeps-"+year).document(env).collection("events")
					.document("eats").collection("dinner_sat");

		if(sunLunchCollection == null)
			sunLunchCollection = db.collection("hackeps-"+year).document(env).collection("events")
					.document("eats").collection("lunch_sun");

	}
	public static boolean isGuest(String uid){
		return uid.matches(guest);
	}
	public static void addGuest(String uid){
		Map<String, Object> docData = new HashMap<>();
		docData.put("registered", true);
		db.collection("data").document(uid)
				.set(docData)
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Log.w("TAG", "Error writing document", e);
					}
				});
	}
	public static void registerUser(String uid, ScannerActivity c){
		initFirebase();
		db.runTransaction(new Transaction.Function<Void>() {
			@Override
			public Void apply(Transaction transaction) throws FirebaseFirestoreException {
				DocumentSnapshot snapshot = transaction.get(userCollection.document(uid));
				Map<String, Object> data = new HashMap<>();
				data.put("time_in", dtf.format(LocalDateTime.now()));
				if(isGuest(uid)){
					addGuest(uid);
					transaction.set(logCollection.document(uid),data, SetOptions.merge());
					c.log(false,"Guest User registered");
				}
				else if(snapshot.exists() && (!snapshot.contains("registered") || !snapshot.get("registered",boolean.class))){
					transaction.set(logCollection.document(uid),data, SetOptions.merge());
					transaction.update(userCollection.document(uid),"registered",true);
					c.log(false,"User registered");
				}
				else c.log(true,"User not existent or already registered");
				// Success
				return null;
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				c.log(false,"Unexpected error occurred.");
			}
		});

	}
	public static void getUsers(MainActivity c){
		initFirebase();
		db.runTransaction(new Transaction.Function<Void>() {
			@Override
			public Void apply(Transaction transaction) throws FirebaseFirestoreException {
				Task<QuerySnapshot> usr=userCollection.get();
				Task<QuerySnapshot> log=logCollection.get();
				Task<QuerySnapshot> satLunch=satLunchCollection.get();
				Task<QuerySnapshot> satDin=satDinnerCollection.get();
				Task<QuerySnapshot> sunLunch=sunLunchCollection.get();
				usr.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							List<DocumentSnapshot> out=task.getResult().getDocuments();
							c.setUsers(out.size());
						} else {
							//Log.d(TAG, "Error getting documents: ", task.getException());
						}
					}
				});
				log.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							List<DocumentSnapshot> out=task.getResult().getDocuments();
							c.setRegUsers(out.size());
						} else {
							//Log.d(TAG, "Error getting documents: ", task.getException());
						}
					}
				});
				satLunch.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							List<DocumentSnapshot> out=task.getResult().getDocuments();
							c.setSatLunch(out.size());
						} else {
							//Log.d(TAG, "Error getting documents: ", task.getException());
						}
					}
				});
				satDin.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							List<DocumentSnapshot> out=task.getResult().getDocuments();
							c.setSatDin(out.size());
						} else {
							//Log.d(TAG, "Error getting documents: ", task.getException());
						}
					}
				});
				sunLunch.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							List<DocumentSnapshot> out=task.getResult().getDocuments();
							c.setSunLunch(out.size());
						} else {
							//Log.d(TAG, "Error getting documents: ", task.getException());
						}
					}
				});
				return null;
			}
		});
	}
	public static void accessUser(String uid, ScannerActivity c){
		//TODO finish this function
		LocalDateTime now = LocalDateTime.now();
		Map<String, Object> data = new HashMap<>();
		data.put("time_in", dtf.format(now));
		logCollection.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DocumentSnapshot> task) {
				if (task.isSuccessful()) {
					DocumentSnapshot document = task.getResult();
					if (document.exists()) {
						logCollection.document(uid)
								.set(data)
								.addOnSuccessListener(new OnSuccessListener<Void>() {
									@Override
									public void onSuccess(Void aVoid) {
										c.log(false,"User access correctly.");
									}
								})
								.addOnFailureListener(new OnFailureListener() {
									@Override
									public void onFailure(@NonNull Exception e) {
										c.log(false,"Error occurred");
									}
								});
					} else {
						c.log(true, "User is not registered");
					}
				} else {
					c.log(false,"Error occurred");
				}
			}
		});
	}


	public static void eatUser(String uid,EatOptions eat,ScannerActivity c){
		//TODO: afegir usuaris guest "HackEPS_Guest_* "
		initFirebase();
		db.runTransaction(new Transaction.Function<Void>() {
			@Override
			public Void apply(Transaction transaction) throws FirebaseFirestoreException {

				CollectionReference collection = null;
				if (eat == EatOptions.lunch_sat)
					collection = satLunchCollection;
				else if (eat == EatOptions.dinner_sat)
					collection = satDinnerCollection;
				else if (eat == EatOptions.lunch_sun)
					collection = sunLunchCollection;

				//List<String> lst= (List<String>) snapshotEat.get("users");
				DocumentSnapshot snapshot = transaction.get(logCollection.document(uid));

				if(snapshot.exists()){
					DocumentSnapshot snapshotEat = transaction.get(collection.document(uid));

					if(snapshotEat.exists()){
						// El usuario ya ha comido
						c.log(true,"User action already registered");
					}else{
						Map<String, Object> data = new HashMap<>();
						data.put("eatTime", dtf.format(LocalDateTime.now()));
						//EAT
						transaction.set(collection.document(uid),data, SetOptions.merge());
						c.log(false,"User action registered");
					}
				}
				else c.log(true,"User not registered");
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
}
