package fi.tanik.harjoitustyo_ruokalista;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ProgressBar;
import android.widget.TextView;
 
public class LoadingScreenWIP extends Activity{
 /*
    private Calendar calendar;
    private String[] LoadingText = {"Etsitään pitsaa", "Pitsaa ei löydetty", "Etsitään kebabbia", "Ei löydetty sitäkään", "Toivotaan jotain muuta hyvää"};
    private ProgressBar progressBar;
    private TextView textview;
	private ArrayList<String> MondayFI, TuesdayFI, WednesdayFI, ThursdayFI, FridayFI;
	private ArrayList<String> MondayEN, TuesdayEN, WednesdayEN, ThursdayEN, FridayEN;
	private String Location;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Show the splash screen
        setContentView(R.layout.activity_loading);
        // Find the progress bar
        progressBar = (ProgressBar) findViewById(R.id.loading_progressBar);
        textview = (TextView) findViewById(R.id.loading_text);
        // Start your loading
        Bundle extras = getIntent().getExtras();
		calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, extras.getInt("calendar"));
		
		MondayFI = new ArrayList<String>();
		TuesdayFI = new ArrayList<String>();
		WednesdayFI = new ArrayList<String>();
		ThursdayFI = new ArrayList<String>();
		FridayFI = new ArrayList<String>();
		
		MondayEN = new ArrayList<String>();
		TuesdayEN = new ArrayList<String>();
		WednesdayEN = new ArrayList<String>();
		ThursdayEN = new ArrayList<String>();
		FridayEN = new ArrayList<String>();
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		Location = sharedPref.getString("Location_preference", "5865");
		if(Location.equals("5865"))	
			this.setTitle("Loading menu for Dynamo...");
		else if(Location.equals("5859"))
			this.setTitle("Loading menu for Main Campus...");
		else if(Location.equals("5861"))
			this.setTitle("Loading menu for Rajacafé...");
		else if(Location.equals("5868"))
			this.setTitle("Loading menu for Music Campus...");

        new RequestTask().execute(""); // Pass in whatever you need a url is just an example we don't use it in this tutorial
    }
	
	class RequestTask extends AsyncTask<String, Integer, String> {
		
		@Override
		// username, password, message, mobile
		protected String doInBackground(String...removeThis) {
		    switch(calendar.get(Calendar.DAY_OF_WEEK)){
			    case(Calendar.SATURDAY):{
					calendar.add(Calendar.DAY_OF_YEAR, 2);
					break;
				}
			    case(Calendar.SUNDAY):{
					calendar.add(Calendar.DAY_OF_YEAR, 1);
					break;
				}
				case(Calendar.TUESDAY):{
					calendar.add(Calendar.DAY_OF_YEAR, -1);
					break;
				}
				case(Calendar.WEDNESDAY):{
					calendar.add(Calendar.DAY_OF_YEAR, -2);
					break;
				}
				case(Calendar.THURSDAY):{
					calendar.add(Calendar.DAY_OF_YEAR, -3);
					break;
				}
				case(Calendar.FRIDAY):{
					calendar.add(Calendar.DAY_OF_YEAR, -4);
					break;
				}
			}
		    
		    int progress = 1;
		    // Add urls for whole week
		    for(int i=0; i<5; i++){
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH) + 1;
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				
				AddDayText(year, month, day);
				progress += 1;
				publishProgress(progress);								
				
				calendar.add(Calendar.DAY_OF_YEAR, 1);
	        }
				
			return "";
		}
		
	    protected void onProgressUpdate(Integer... values) {
	        super.onProgressUpdate(values);
	        
	        int progress = (int) ((values[0] / (float) 5) * 100);
	        progressBar.setProgress(progress); // This is ran on the UI thread so it is ok to update our progress bar ( a UI view ) here
	        
	        if(progress >= 75) {textview.setText(LoadingText[3]);}
	        else if(progress >= 50) {textview.setText(LoadingText[2]);}
	        else if(progress >= 25) {textview.setText(LoadingText[1]);}
	        else {textview.setText(LoadingText[0]);}
	        
	    }
		
		@Override
		protected void onPostExecute(String result) {
			
			// create a new intent to pass data
	    	Intent intent = new Intent();
	    	
	    	// Add arrays to bundle
	    	Bundle b = new Bundle();
	    	b.putStringArrayList("MondayFI", (ArrayList<String>) MondayFI);
	    	b.putStringArrayList("TuesdayFI", (ArrayList<String>) TuesdayFI);
	    	b.putStringArrayList("WednesdayFI", (ArrayList<String>) WednesdayFI);
	    	b.putStringArrayList("ThursdayFI", (ArrayList<String>) ThursdayFI);	
	    	b.putStringArrayList("FridayFI", (ArrayList<String>) FridayFI);
	    	
	    	b.putStringArrayList("MondayEN", (ArrayList<String>) MondayEN);
	    	b.putStringArrayList("TuesdayEN", (ArrayList<String>) MondayEN);	
	    	b.putStringArrayList("WednesdayEN", (ArrayList<String>) WednesdayEN);
	    	b.putStringArrayList("ThursdayEN", (ArrayList<String>) ThursdayEN);
	    	b.putStringArrayList("FridayEN", (ArrayList<String>) FridayEN);
	    	
	    	// add result to intent
	    	intent.putExtra("Bundle", b);
	    			
	    	// al ok here, result is set
	    	setResult(RESULT_OK ,intent);
	    	
	    	finish(); // Don't forget to finish this Splash Activity so the user can't return to it!
		}
		
		private String CheckObject(JSONObject object, String key) {
			String text = "";
			
			try {text = object.getString(key);} 
			catch (JSONException e){text = "";};
			
			return text;
		}
		
		private void AddDayText(int year, int month, int day) {
			 // constants
		    int timeoutSocket = 5000;
		    int timeoutConnection = 5000;
		    
		    StringBuilder total = new StringBuilder();
			String error = "";		    	
			
		    HttpParams httpParameters = new BasicHttpParams();
		    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		    HttpProtocolParams.setContentCharset(httpParameters, "utf-8");
		    HttpClient httpclient = new DefaultHttpClient(httpParameters);
		
		    HttpPost httppost = new HttpPost("http://student.labranet.jamk.fi/~G3217/Android/Harkka/ruoka/index.php");

		    try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("username", getUsername()));
		        nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httppost);
		        final int statusCode = response.getStatusLine().getStatusCode();
		
		        if(statusCode != HttpStatus.SC_OK) {
		        	JsonArrayFI.add("Yhteysvirhe: " + statusCode);
					JsonArrayEN.add("Connection Error: " + statusCode);
		        	error = "error";		        	
		        }
		
		        
		        
		
		        HttpEntity getResponseEntity = response.getEntity();		
		        BufferedReader reader = new BufferedReader(new InputStreamReader(getResponseEntity.getContent()));  
		        
		        String line = "";
		        while((line = reader.readLine()) != null) {
		            total.append(line);
		        }
		
		        line = total.toString();
		    } catch (Exception e) {
		    	JsonArrayFI.add("Latausvirhe: " + e.toString());
				JsonArrayEN.add("Download Exception: " + e.toString());
	        	error = "error";
	    	}
		    		    
		    if(error.equals("")) {
				try {
					JSONObject jObject = new JSONObject(total.toString());
							
					JSONArray foodIdArray = jObject.getJSONArray("id");
					JSONArray foodNameFiArray = jObject.getJSONArray("nimi");
					JSONArray foodNameEnArray = jObject.getJSONArray("nimien");
					JSONArray foodAdditionalFiArray = jObject.getJSONArray("lisa");
					JSONArray foodAdditionalEnArray = jObject.getJSONArray("lisaen");
					JSONArray foodCategoryArray = jObject.getJSONArray("kategoria");
					JSONArray foodAllergiesArray = jObject.getJSONArray("aller");
					JSONArray foodRankArray = jObject.getJSONArray("arvo");
					JSONArray foodUserRankArray = jObject.getJSONArray("userarvo");
					
					if(foodIdArray.length() == 0) {
						throw new JSONException("");
					} else {
						ArrayList<String> tempListFI = new ArrayList<String>();
						ArrayList<String> tempListEN = new ArrayList<String>();
						ArrayList<ArrayList<String>> tempListAll = new ArrayList<ArrayList<String>>();
						
						JSONObject oneObject;
						for (int indexloc = 0; indexloc < foodIdArray.length(); indexloc++)
						{
							try {
								oneObject = foodIdArray.getJSONObject(indexloc);
								tempListFI.add(CheckObject(oneObject, "id"));
								tempListEN.add(CheckObject(oneObject, "id"));
								
								oneObject = foodNameFiArray.getJSONObject(indexloc);
								tempListFI.add(CheckObject(oneObject, "nimi"));								
								oneObject = foodNameEnArray.getJSONObject(indexloc);
								tempListEN.add(CheckObject(oneObject, "nimien"));
								
								oneObject = foodAdditionalFiArray.getJSONObject(indexloc);
								tempListFI.add(CheckObject(oneObject, "lisa"));
								oneObject = foodAdditionalEnArray.getJSONObject(indexloc);
								tempListEN.add(CheckObject(oneObject, "lisaen"));
								
								oneObject = foodIdArray.getJSONObject(indexloc);
								tempList.add(CheckObject(oneObject, "id"));
								
							} catch (JSONException e) {								
								JsonArrayFI.add("JsonArray virhe: " + e.toString());
								JsonArrayEN.add("JsonArray Exception: " + e.toString());
							}
						}
					}
				} catch (JSONException e) {
					JsonArrayFI.add("Ruokalistaa ei löydetty hakemallesi päivälle!");
					JsonArrayEN.add("Menu not found for this specific day!");
				} 		            
		    }
		      
		    JsonArrayAllFI.add(JsonArrayFI);
		    JsonArrayAllEN.add(JsonArrayEN);
		    //JsonArrayAll.add(Ratings);
		    JsonArrayFI = new ArrayList<String>();
		    JsonArrayEN = new ArrayList<String>();
		    Ratings = new ArrayList<String>();
		}
		
		public String getUsername() {
			AccountManager manager = AccountManager.get(getApplication());
			Account[] accounts = manager.getAccountsByType("com.google");
			List<String> possibleEmails = new LinkedList<String>();

			for (Account account : accounts) {
				possibleEmails.add(account.name);
			}

			if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
				String email = possibleEmails.get(0);
			    String[] parts = email.split("@");
			    
			    if (parts.length > 0 && parts[0] != null)
			    	return parts[0];
			    else
			        return "";
			    
			 } else
			    return "";
		}
	}
	*/
}
