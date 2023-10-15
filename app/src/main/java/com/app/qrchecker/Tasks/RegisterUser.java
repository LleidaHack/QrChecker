package com.app.qrchecker.Tasks;

import android.os.AsyncTask;

import com.app.qrchecker.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterUser extends AsyncTask<String, Void, String> {
	MainActivity c;
	RegisterUser(MainActivity c){
		super();
		this.c = c;
	}
	// A method that runs on the background thread and performs the network operation
	@Override
	public String doInBackground(String... params) {
		// Get the event_id from the first parameter
		Integer event_id = 1;
		// Construct the URL by appending the event_id and the status path
		String url = "https://backend.integration.lleidahack.dev/eventmanagment/" + event_id + "/status";
		// Initialize a variable to store the response
		String response = null;
		// Try to create a HttpURLConnection and get the input stream
		try {
			// Create a URL object from the string
			URL apiUrl = new URL(url);
			// Open a connection using the URL
			HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
			// Set the request method to GET
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization","Bearer "+"HOLA");
			// Connect to the server
			connection.connect();
			// Get the input stream from the connection
			InputStream inputStream = connection.getInputStream();
			// Create a BufferedReader to read from the input stream
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			// Create a StringBuilder to append the lines
			StringBuilder builder = new StringBuilder();
			// Read each line from the reader and append it to the builder
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
				builder.append("\n");
			}
			// Close the reader and the input stream
			reader.close();
			inputStream.close();
			// Disconnect from the server
			connection.disconnect();
			// Convert the builder to a string and assign it to the response variable
			response = builder.toString();
		} catch (IOException e) {
			// Handle any IO exception
			e.printStackTrace();
		}
		// Return the response or null if there was an error
		return response;
	}

	// A method that runs on the UI thread and receives the result of the background task
	@Override
	protected void onPostExecute(String result) {
		// Check if the result is not null
		if (result != null) {
			// Try to parse the result as a JSON object
			try {
				JSONObject jsonObject = new JSONObject(result);
				// Get the values of registratedUsers, acceptedUsers, rejectedUsers, and participatingUsers from the JSON object
				int registratedUsers = jsonObject.getInt("registratedUsers");
				int acceptedUsers = jsonObject.getInt("acceptedUsers");
				int rejectedUsers = jsonObject.getInt("rejectedUsers");
				int participatingUsers = jsonObject.getInt("participatingUsers");
				// Do something with these values, such as updating UI, showing a toast, etc.
				c.setUsers(registratedUsers);
				c.setRegUsers(participatingUsers);
			} catch (JSONException e) {
				// Handle any JSON exception
				e.printStackTrace();
			}
		} else {
			// Handle the case where there was an error or no response
		}
	}
}