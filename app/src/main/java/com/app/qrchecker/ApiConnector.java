package com.app.qrchecker;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public interface ApiConnector{
    // private static String guest = "HackEPS_Guest_.";
    // public static boolean isGuest(String uid) {
	// 	return uid.matches(guest);
	// }
	/*String event_id="";
	static String base_url = "http://localhost:8080/eventmanagment";
	public static void registerUser(int event_id, String uid, ScannerActivity c){
		String url = base_url + event_id + "/register/"+uid;

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
				(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				c.log(false, "Guest User registered");
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				c.log(true, "User not existent or already registered");
			}
		});

		// Access the RequestQueue through your singleton class.
		MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);	
	}


	public static void getUsers(MainActivity c) {
		String url = base_url + event_id + "/status";

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
				(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					try {
						c.setUsers(response.getInt("users"));
						c.setRegUsers(response.getInt("regUsers"));
						c.setFriDinner(response.getInt("friDinner"));
						c.setSatLunch(response.getInt("satLunch"));
						c.setSatDin(response.getInt("satDin"));
						c.setSunLunch(response.getInt("sunLunch"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					// else c.log(true, "User not existent or already registered");
				}
			}
		);
		// Access the RequestQueue through your singleton class.
		MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);	
	}

	public static void accessUser(String uid, ScannerActivity c) {
		//TODO finish this function
		// LocalDateTime now = LocalDateTime.now();
		// Map<String, Object> data = new HashMap<>();
		// data.put("time_in", dtf.format(now));
		// logCollection.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
		// 	@Override
		// 	public void onComplete(@NonNull Task<DocumentSnapshot> task) {
		// 		if (task.isSuccessful()) {
		// 			DocumentSnapshot document = task.getResult();
		// 			if (document.exists()) {
		// 				logCollection.document(uid)
		// 						.set(data)
		// 						.addOnSuccessListener(new OnSuccessListener<Void>() {
		// 							@Override
		// 							public void onSuccess(Void aVoid) {
		// 								c.log(false, "User access correctly.");
		// 							}
		// 						})
		// 						.addOnFailureListener(new OnFailureListener() {
		// 							@Override
		// 							public void onFailure(@NonNull Exception e) {
		// 								c.log(false, "Error occurred");
		// 							}
		// 						});
		// 			} else {
		// 				c.log(true, "User is not registered");
		// 			}
		// 		} else {
		// 			c.log(true, "Error occurred");
		// 		}
		// 	}
		// });
	}

	public int getIdFromEat(EatOptions eat){
		if (eat == EatOptions.dinner_fri)
			return 0;
		else if (eat == EatOptions.lunch_sat)
			return 1;
		else if (eat == EatOptions.dinner_sat)
			return 2;
		else if (eat == EatOptions.lunch_sun)
			return 3;
	}

	public static void eatUser(String uid, EatOptions eat, ScannerActivity c) {
		int eatId = getIdFromEat(eat);
		String url = base_url + event_id + "/eat/" + eatId + "/" + uid;

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
				(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					c.log(false, "User action registered");
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					// else c.log(true, "User not existent or already registered");
					c.log(true, "User action already registered");
				}
			}
		);
		// Access the RequestQueue through your singleton class.
		MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);	
	}*/
}