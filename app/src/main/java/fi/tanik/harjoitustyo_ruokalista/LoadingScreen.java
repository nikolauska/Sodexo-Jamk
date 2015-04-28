package fi.tanik.harjoitustyo_ruokalista;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.webkit.WebView;
import android.widget.ProgressBar;

/*
 * 	This Activity is for loading screen 
 * 	
 *	It will download all needed information from internet
 * 	and show cool animation :)
 * 
 */

public class LoadingScreen extends Activity{
	
 
    private ProgressBar progressBar;
	private String Location, Language;
	
	private Week week;
	private CalendarHandler calendar;
	private UsernameHandler user;
	private LanguageHandler languageHandler;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
        setContentView(R.layout.activity_loading);
        
        // Start gif animation
        WebView view = (WebView) findViewById(R.id.loadingView);
        view.loadUrl("file:///android_asset/gif.html");
        
        // Init progress bar 
        progressBar = (ProgressBar) findViewById(R.id.loading_progressBar);
        
        // Init Week class to return it to main activity
        week = new Week();
        
        // Get classes that main activity sent here
        Bundle extras = getIntent().getExtras();
		calendar = new CalendarHandler((CalendarHandler) extras.getSerializable("calendar"));
		user = (UsernameHandler) extras.getSerializable("username");
		
		// Load saved preferences
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		Location = sharedPref.getString("Location_preference", "5865");
		Language = sharedPref.getString("Language_preference", "en");
		
		// Init LanguageHander to set loading screen text 
		languageHandler = new LanguageHandler();
		this.setTitle(languageHandler.GetLoadingScreenText(Language));

		// Start loading information from internet on another thread
        new RequestTask().execute("");
    }
	
	// This is used so user cannot exit loadingscreen by pressing back button
	public void onBackPressed() {}
	
	class RequestTask extends AsyncTask<String, Integer, String> {		
		@Override
		protected String doInBackground(String...removeThis) {
			// Check day that main activity sent us and set it to monday
		    switch(calendar.getWeekDate()){
			    case(Calendar.SATURDAY):{
					calendar.AddDay(2);
					break;
				}
			    case(Calendar.SUNDAY):{
			    	calendar.AddDay(1);
					break;
				}
				case(Calendar.TUESDAY):{
					calendar.AddDay(-1);
					break;
				}
				case(Calendar.WEDNESDAY):{
					calendar.AddDay(-2);
					break;
				}
				case(Calendar.THURSDAY):{
					calendar.AddDay(-3);
					break;
				}
				case(Calendar.FRIDAY):{
					calendar.AddDay(-4);
					break;
				}
			}
		    
		    // Save week number for later use
		    week.weekNumber = calendar.week;
		    
		    // Loop through whole week to get info of all week days
		    for(int i=0; i<5; i++){
				int year = calendar.year;
				int month = calendar.month;
				int day = calendar.day;
				
				// Starts loading function
				AddDayText(i, day, month, year);
				
				// Show user how much is done
				publishProgress((int) (((i+1) / (float) 5) * 100));							
				
				// Switch to next day
				calendar.AddDay(1);
	        }
			
		    // End backgroundtask and got to post execute
			return "";
		}
		
		// Update progress to user
	    protected void onProgressUpdate(Integer... values) {
	        super.onProgressUpdate(values);
	        progressBar.setProgress(values[0]);        
	    }
		
		@Override 
		protected void onPostExecute(String result) {	
			// Save Week class we filled with food info to return it to main activity
	    	Intent intent = new Intent(); 	
	    	intent.putExtra("week", week);
	    	
	    	// Set result to ok to show all went as expected here
	    	setResult(RESULT_OK ,intent);
	    	
	    	// end activity
	    	finish();
		}
		
		// Function to load data from internat and handle returning JSON
		private void AddDayText(int dayNumber, int day, int month, int year) {
		    int timeoutSocket = 5000;
		    int timeoutConnection = 5000;
		    String total = "";
			String error = "";
			String URL = "http://student.labranet.jamk.fi/~G3217/Android/Harkka/ruoka/index.php";
			
			// Classes to save in Week class later on
			Food foodFi;
			Food foodEn;
			
			// Add HTTP parameters
		    HttpParams httpParameters = new BasicHttpParams();
		    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		    HttpProtocolParams.setContentCharset(httpParameters, "utf-8");
		    HttpClient client = new DefaultHttpClient(httpParameters);

		    // Add HTTP post with given URL
		    HttpPost httppost = new HttpPost(URL);
		
		    try {
		    	// Add information to HTTP post request
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("username", user.Username));
		        nameValuePairs.add(new BasicNameValuePair("paikka", Location));
		        nameValuePairs.add(new BasicNameValuePair("day", Integer.toString(day)));
		        nameValuePairs.add(new BasicNameValuePair("month", Integer.toString(month)));
		        nameValuePairs.add(new BasicNameValuePair("year", Integer.toString(year)));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));        

		        // Execute HTTP Post Request
		        HttpResponse response = client.execute(httppost);
		        
		        // Get response status
		        final int statusCode = response.getStatusLine().getStatusCode();
		        
		        // If status is not OK then inform user of specific problems
		        if(statusCode != HttpStatus.SC_OK) {
		        	foodFi = new Food();
		        	foodEn = new Food();
		        	
		        	foodFi.error = languageHandler.GetErrorLoadingConnectionText("fi");
		        	foodEn.error = languageHandler.GetErrorLoadingConnectionText("en");
					
					week.addFoodtoList(dayNumber, foodFi, foodEn);
		        	error = "error";		        	
		        }
		        
		        // Get response message 
		        HttpEntity getResponseEntity = response.getEntity();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(getResponseEntity.getContent(), "UTF-8"));  
		        
		        // Loop through returned message and save it 
		        StringBuilder builder = new StringBuilder();
		        String line = "";
                while ((line = reader.readLine()) != null) {
                        builder.append(line);
                }
		        total = builder.toString();
		        
		        // Edit string to remove \r\n and change invalid ä and ö letters from encoding
		        total = total.replaceAll("(\\\\r\\\\n)", "");
		        total = total.replaceAll("(\\\\u00e4)", "ä");
		        total = total.replaceAll("(\\\\u00f6)", "ö");
		        total = total.substring(1);
		     
		    } catch (Exception e) {
		    	// Another error message to show user
		    	foodFi = new Food();
		    	foodEn = new Food();
				
		    	foodFi.error = languageHandler.GetErrorLoadingDownloadText("fi");
		    	foodEn.error = languageHandler.GetErrorLoadingDownloadText("en");
				
				week.addFoodtoList(dayNumber, foodFi, foodEn);
	        	error = "error";
	    	}
		    		    
		    if(error.equals("")) {
				try {
					// Change String to JSON Array 
					JSONArray jArray = new JSONArray(total);
					if(jArray.length() == 0) {
						// If JSON array is empty (no menu for that day) throw error
						throw new JSONException("");
					} else {
						// Loop through JSON Array and save information
						for (int indexloc = 0; indexloc < jArray.length(); indexloc++)
						{
							// initialize new Food classes to save information to
							foodFi = new Food();
							foodEn = new Food();
							try {
								// get one JSON object from JSON array
								JSONObject oneObject = jArray.getJSONObject(indexloc);
								
								// Save food ID
								foodFi.Id = oneObject.getInt("id");
								foodEn.Id = oneObject.getInt("id");
								
								// Save food name
								foodFi.foodName = oneObject.getString("nimi");
								foodEn.foodName = oneObject.getString("nimien");
								
								// Save additional info
								foodFi.additionalInfo = oneObject.getString("lisa");
								foodEn.additionalInfo = oneObject.getString("lisan");
								
								// Save category
								foodFi.category = oneObject.getString("kategoria");
								foodEn.category = oneObject.getString("kategoria");
								
								// Save allergies and edit their text to full text compared to couple characters
								foodFi.allergies = oneObject.getString("aller");
								foodFi.allergies = foodFi.allergies.replaceAll("\\bVL\\b", languageHandler.GetAllergieSmallLactoseText("fi"));
								foodFi.allergies = foodFi.allergies.replaceAll("\\bL\\b", languageHandler.GetAllergieLactoseText("fi"));
								foodFi.allergies = foodFi.allergies.replaceAll("\\bG\\b", languageHandler.GetAllergieGlutenText("fi"));							
								foodFi.allergies = foodFi.allergies.replaceAll("\\bM\\b", languageHandler.GetAllergieNoMilkText("fi"));
								
								foodEn.allergies = oneObject.getString("aller");
								foodEn.allergies = foodEn.allergies.replaceAll("\\bVL\\b", languageHandler.GetAllergieSmallLactoseText("en"));
								foodEn.allergies = foodEn.allergies.replaceAll("\\bL\\b", languageHandler.GetAllergieLactoseText("en"));
								foodEn.allergies = foodEn.allergies.replaceAll("\\bG\\b", languageHandler.GetAllergieGlutenText("en"));
								foodEn.allergies = foodEn.allergies.replaceAll("\\bM\\b", languageHandler.GetAllergieNoMilkText("en"));
								
								// Get food rating
								foodFi.reviewScore = oneObject.getInt("arvo");
								foodEn.reviewScore = oneObject.getInt("arvo");
								
								// Get rating user has given
								foodFi.userScore = oneObject.getInt("userarvo");
								foodEn.userScore = oneObject.getInt("userarvo");
								
								// Save save foods to Week Class
								week.addFoodtoList(dayNumber, foodFi, foodEn);	
								
							} catch (JSONException e) {
								// JSON error
								foodFi = new Food();
								foodEn = new Food();
								
								foodFi.error = languageHandler.GetErrorLoadingJSONArrayText("fi");
								foodEn.error = languageHandler.GetErrorLoadingJSONArrayText("en");
								
								week.addFoodtoList(dayNumber, foodFi, foodEn);
							}
						}
					}
				} catch (JSONException e) {
					// Given if there is no menu for day user wanted
					foodFi = new Food();
					foodEn = new Food();
					
					foodFi.error = languageHandler.GetErrorLoadingNoMenuText("fi");
					foodEn.error = languageHandler.GetErrorLoadingNoMenuText("en");
					
					week.addFoodtoList(dayNumber, foodFi, foodEn);
				} 		            
		    }
		}		
	}
}
