package fi.tanik.harjoitustyo_ruokalista;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
 
public class LoadingScreen extends Activity{
	
 
    private Calendar calendar;
    private String[] LoadingText = {"Etsitään pitsaa", "Pitsaa ei löydetty", "Etsitään kebabbia", "Ei löydetty sitäkään", "Toivotaan jotain muuta hyvää"};
    private ProgressBar progressBar;
    private ArrayList<String> JsonArrayFI, JsonArrayEN;
	private ArrayList<List<String>> JsonArrayAllFI;
	private ArrayList<List<String>> JsonArrayAllEN;
	private ArrayList<String> Ratings;
	private String Location;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
        // Show the splash screen
        setContentView(R.layout.activity_loading);
        
        WebView view = (WebView) findViewById(R.id.loadingView);
        view.loadUrl("file:///android_asset/loading.gif");
        view.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
        
        // Find the progress bar
        progressBar = (ProgressBar) findViewById(R.id.loading_progressBar);
        // Start your loading
        Bundle extras = getIntent().getExtras();
		calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, extras.getInt("calendar"));
		
		JsonArrayAllFI = new ArrayList<List<String>>();
		JsonArrayAllEN = new ArrayList<List<String>>();
		JsonArrayFI = new ArrayList<String>();
		JsonArrayEN = new ArrayList<String>();
		Ratings = new ArrayList<String>();
		
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
			//gifView = (GifView) findViewById(R.id.gifview);
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
				
				AddDayText("http://www.sodexo.fi/ruokalistat/output/daily_json/" + Location + "/" + year + "/" + month + "/" + day +"/fi");
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
	    }
		
		@Override
		protected void onPostExecute(String result) {
			
			// create a new intent to pass data
	    	Intent intent = new Intent();
	    	
	    	// Add arrays to bundle
	    	Bundle b = new Bundle();
	    	b.putStringArrayList("MondayFI", (ArrayList<String>) JsonArrayAllFI.get(0));
	    	b.putStringArrayList("TuesdayFI", (ArrayList<String>) JsonArrayAllFI.get(1));
	    	b.putStringArrayList("WednesdayFI", (ArrayList<String>) JsonArrayAllFI.get(2));
	    	b.putStringArrayList("ThursdayFI", (ArrayList<String>) JsonArrayAllFI.get(3));	
	    	b.putStringArrayList("FridayFI", (ArrayList<String>) JsonArrayAllFI.get(4));
	    	
	    	b.putStringArrayList("MondayEN", (ArrayList<String>) JsonArrayAllEN.get(0));
	    	b.putStringArrayList("TuesdayEN", (ArrayList<String>) JsonArrayAllEN.get(1));	
	    	b.putStringArrayList("WednesdayEN", (ArrayList<String>) JsonArrayAllEN.get(2));
	    	b.putStringArrayList("ThursdayEN", (ArrayList<String>) JsonArrayAllEN.get(3));
	    	b.putStringArrayList("FridayEN", (ArrayList<String>) JsonArrayAllEN.get(4));
	    	
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
		
		private void AddDayText(String URL) {
			 // constants
		    int timeoutSocket = 5000;
		    int timeoutConnection = 5000;
		    String line = "";
			String error = "";		    	
			
		    HttpParams httpParameters = new BasicHttpParams();
		    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		    HttpProtocolParams.setContentCharset(httpParameters, "utf-8");
		    HttpClient client = new DefaultHttpClient(httpParameters);
		
		    HttpGet httpget = new HttpGet(URL);
		
		    try {
		        HttpResponse getResponse = client.execute(httpget);
		        final int statusCode = getResponse.getStatusLine().getStatusCode();
		
		        if(statusCode != HttpStatus.SC_OK) {
		        	JsonArrayFI.add("Yhteysvirhe: " + statusCode + "| URL osoitteelle: " + URL);
					JsonArrayEN.add("Connection Error: " + statusCode + "| for URL: " + URL);
		        	error = "error";		        	
		        }
		
		        
		        StringBuilder total = new StringBuilder();
		
		        HttpEntity getResponseEntity = getResponse.getEntity();
		
		        BufferedReader reader = new BufferedReader(new InputStreamReader(getResponseEntity.getContent()));  
		
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
					JSONObject jObject = new JSONObject(line);
							
					JSONArray jArray = jObject.getJSONArray("courses");
					
					if(jArray.length() == 0) {
						throw new JSONException("");
					} else {
						for (int indexloc = 0; indexloc < jArray.length(); indexloc++)
						{
							try {
								JSONObject oneObject = jArray.getJSONObject(indexloc);
								// Pulling items from the array
								
								String title = CheckObject(oneObject, "title_fi");
								//String category = CheckObject(oneObject, "category");
								//String price = CheckObject(oneObject, "price");
								String properties = CheckObject(oneObject, "properties");				
								String desc = CheckObject(oneObject, "desc_fi");
									
								JsonArrayFI.add(//"Kategoria: " + category + "\n" + 
											"Nimi: " + title + "\n" + 
											//"Hinta: " + price + "\n" + 	
											"Allergiat: " + properties + "\n" +
								        	"Lisätiedot: " + desc);							
								        	
								title = CheckObject(oneObject, "title_en");
								//category = CheckObject(oneObject, "category");
								//price = CheckObject(oneObject, "price");
								properties = CheckObject(oneObject, "properties");				
								desc = CheckObject(oneObject, "desc_en");
									
								JsonArrayEN.add(//"Category: " + category + "\n" + 
											"Name: " + title + "\n" + 
											//"Price: " + price + "\n" + 	
											"Allergies: " + properties + "\n" +
								        	"Description: " + desc);
								
								//Kuvitteellinen json
								//{
								//	"id":"35135m23b2b52n235nj25",
								//	"food":[{"name_fi":"Pitsaa", "name_en":"Pizza"},
								//			{"name_fi":"Kebabbia", "name_en":"kebab"}]
								//}
								// Ratings.add(GetRating("URL", JSON));
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
		
		private String GetRating(String URL, JSONObject json) {
			try {
				 
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost("http://localhost:8080/RESTfulExample/json/product/post");
				
				if(CheckObject(json, "id").equals("")) {return "Rating JSON Error: ID is empty!";} // person hashed email
				if(CheckObject(json, "food").equals("")) {return "Rating JSON Error: food is empty";} // Food name
				StringEntity input = new StringEntity(json.toString());
				input.setContentType("application/json");
				postRequest.setEntity(input);
		 
				HttpResponse response = httpClient.execute(postRequest);
		 
				if (response.getStatusLine().getStatusCode() != 201) {
					throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
				}
		 
				BufferedReader br = new BufferedReader(
		                        new InputStreamReader((response.getEntity().getContent())));
		 
				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
				}
		 
				httpClient.getConnectionManager().shutdown();
		 
			  } catch (MalformedURLException e) {
		 
				e.printStackTrace();
		 
			  } catch (IOException e) {
		 
				e.printStackTrace();
			  }
			return "";
		}
	}
}
