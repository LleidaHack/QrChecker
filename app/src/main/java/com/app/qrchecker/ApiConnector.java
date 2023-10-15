package com.app.qrchecker;
// Import the necessary libraries

import com.app.qrchecker.Tasks.Status;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

// Define a class to handle the API connection
public abstract class ApiConnector {

	// Define a method to send a GET request to the /eventmanagment/{event_id}/status endpoint
	public static String getEventStatus(Integer event_id, MainActivity c) {
		// Initialize an empty string to store the response
		Status task = new Status();
		task.execute(c);
		return "";
	}

	// Define a method to send a POST request to the /eventmanagment/{event_id}/participate/{hacker_id} endpoint
	public static String participateInEvent(String event_id, String hacker_id) {
		return "";
	}

	private static void eat(String event_id, String hacker_code, String meal_id){

	}
}
