package fi.tanik.harjoitustyo_ruokalista;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 	This is the main activity which is run on the start of
 * 	program and handles almost all the functionality  	
 */

public class MainActivity extends Activity  implements OnItemSelectedListener{
	
	// Class variables
	private CalendarHandler calendar = new CalendarHandler();
	private LanguageHandler languageHandler = new LanguageHandler();
	private Week currentWeek = new Week();
	private Food SelectedFood = new Food();
	private UsernameHandler user;
	
	// Arraylists
	private ArrayList<Week> savedWeeks = new ArrayList<Week>();
	private ArrayList<Food> foodList = new ArrayList<Food>();
	
	// UI elements
	private Button btnSubmit;	
	private RatingBar Rating;
	private RatingBar RatingSelf;	
	private TextView tvDate, tvJson, tvWeekDate, tvReview, tvReviewSelf;
	private Spinner spinner;
	
	// int variables
	public int id = 0, selectedItem = 0;
	private final int loadingReturnCode = 1, SettingsReturnCode = 2;
	
	// Gesture detection variables
	private int swipVelocityThreshold, swipeThreshold;
	private GestureDetector mDetector;
    
	// String variables
    private String Language, Location;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Init username for later use
		user = new UsernameHandler(this);
		
		// Init spinner
		spinner = (Spinner) findViewById(R.id.FoodList);
		spinner.setOnItemSelectedListener(this);
		
		// Init rating stars
		Rating = (RatingBar)findViewById(R.id.ratingBar);
		Rating.setStepSize(0.5f);
		RatingSelf = (RatingBar)findViewById(R.id.ratingBarSelf);
		RatingSelf.setStepSize(0.5f);
		
		// Init rest of the UI elements
		btnSubmit = (Button)findViewById(R.id.button);	
		tvJson = (TextView) findViewById(R.id.JsonText);
		tvDate = (TextView) findViewById(R.id.DateText);
		tvWeekDate = (TextView) findViewById(R.id.DateWeekText);
		tvReview = (TextView) findViewById(R.id.ReviewText);
		tvReviewSelf = (TextView) findViewById(R.id.ReviewTextSelf);
		
		// Init gesture detector to detect user swiping
		ViewConfiguration configuration = ViewConfiguration.get(this);
        swipVelocityThreshold = configuration.getScaledMinimumFlingVelocity();
        swipeThreshold = configuration.getScaledTouchSlop();
        mDetector = new GestureDetector(this, new MyGestureListener());
        
        // init button listenre
		listenerOnButton();
		
		// If user is checking on weekend then set day to next monday
		switch(calendar.getWeekDate()) {
			case Calendar.SATURDAY: 
				calendar.AddDay(2);
				break;
				
			case Calendar.SUNDAY:
				calendar.AddDay(1);
				break;
		}
		
		// get saved preferences language and location
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		Language = sharedPref.getString("Language_preference", "en");
		Location = sharedPref.getString("Location_preference", "5865");
		
		// Set app title text to location
		this.setTitle(languageHandler.GetLocationText(Location, Language));
		
		// Set rate button text
		btnSubmit.setText(languageHandler.GetButtonText(Language));
		
		// Start loading
		LoadingScreen();
	}
	
	// used to dynamically change menu text depending on language selected
	public boolean onPrepareOptionsMenu(Menu menu) {
	    menu.clear();
	    getMenuInflater().inflate(R.menu.main, menu);
	    		
	    menu.findItem(R.id.action_settings).setTitle(languageHandler.GetMenuSettingsText(Language));
	    menu.findItem(R.id.action_today).setTitle(languageHandler.GetMenuReturnTodayText(Language));
	    
	    return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	// Creates menu
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	// When menu item is selected this will be run
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Start settings activity
	        case R.id.action_settings: {        		        	
	        	Intent i = new Intent(this, SettingsActivity.class);
	            startActivityForResult(i, SettingsReturnCode);
	            return true;
	        }
	        // Return to current day
	        case R.id.action_today: {
	        	ReturnToday();
	        	return true;
	        }
	    }
		return false;
	}
	
	@Override
	// When player touches scren this is run
	public boolean onTouchEvent(MotionEvent event) {
	    this.mDetector.onTouchEvent(event);
	    return super.onTouchEvent(event);
	}
	
	@Override
	// When user stops touching screen this is run
	public boolean dispatchTouchEvent(MotionEvent ev) {
	    super.dispatchTouchEvent(ev);
	    return mDetector.onTouchEvent(ev);
	}
	
	// Gesture detector class to determinen what user wants to do
	class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

	    @Override
	    // User is still pressing...
	    public boolean onDown(MotionEvent event) {
	        return true;
	    }

	    @Override
	    // user has done fling motion
	    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	        try {
	        	// Get X,Y position after and before fling
	            float deltaX = e2.getRawX() - e1.getRawX();
	            float deltaY = e2.getRawY() - e1.getRawY();
	            
	            // Calculate was motion to left or right
	            if (Math.abs(deltaX) > Math.abs(deltaY)) {
	                if (Math.abs(deltaX) > swipeThreshold && Math.abs(velocityX) > swipVelocityThreshold) {
	                    if (deltaX < 0) {
	                    	// Fling was to right, go to next day
	                    	CalendarUpdate(1);
	                        return true;
	                    } else {
	                    	// Fling was to left, go to last day
	                    	CalendarUpdate(-1);
	                        return true;
	                    }
	                }
	            }
	        } catch (Exception e) {}
	        return false;
	    }

	}
	
	public void CalendarUpdate(int value) {
		// Going to last day requested
		if(value == -1) {
			// If day is monday then go to last week friday
			if(calendar.getWeekDate() == Calendar.MONDAY) {
				calendar.AddDay(-3);
				
				// Check if user has already downloaded this weeks information to remove unnecessary downloads
				boolean weekFound = false;
				for(Week week : savedWeeks) {
					if(week.weekNumber == calendar.week) {
						currentWeek = week;
						weekFound = true;
					}
				}
				
				// If week was not found then start loading, otherwise you can just get text for that day
				if(!weekFound)
					LoadingScreen();
				else
					GetDateText();
			} else {
				// Go to last day
				calendar.AddDay(value);
				GetDateText();
			}
		} else if(value == 1) {
			// If day is friday then go to next week monday
			if(calendar.getWeekDate() == Calendar.FRIDAY) {
				calendar.AddDay(3);
				
				// Check if user has already downloaded this weeks information to remove unnecessary downloads
				boolean weekFound = false;
				for(Week week : savedWeeks) {
					if(week.weekNumber == calendar.week) {
						currentWeek = week;
						weekFound = true;
					}
				}
				
				// If week was not found then start loading, otherwise you can just get text for that day
				if(!weekFound)
					LoadingScreen();
				else
					GetDateText();
			} else {
				// Go to next day
				calendar.AddDay(value);
				GetDateText();
			}
		}  	
	}
	
	// Go to current date
	private void ReturnToday() {		
		if(calendar.week == Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)) {
			// If week is same then just change text
			calendar.SetToday();
			GetDateText();
		} else {
			// Load week from saved weeks
			calendar.SetToday();
			
			for(Week week : savedWeeks) {
				if(week.weekNumber == calendar.week) {
					currentWeek = week;
				}
			}
		}
			
	}
	
	// Function to start loading screen activity 
	private void LoadingScreen() {		
		Intent intent = new Intent(this, LoadingScreen.class);
		intent.putExtra("calendar", calendar);
		intent.putExtra("username", user);
		startActivityForResult(intent,loadingReturnCode);		
	}
	
	// Function that handles result that activitys return
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			// Loading screen activity
			case loadingReturnCode:
				if(resultCode == Activity.RESULT_OK) {
					// Get Week class loading screen activity return and save it 
					currentWeek = (Week) data.getSerializableExtra("week");
					savedWeeks.add(currentWeek);
					
					// Get text to screen
					GetDateText();
				}
				break;
			case SettingsReturnCode: {
				// Check if user changed language or location 
				SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
				String Lang = sharedPref.getString("Language_preference", "en");				
				String Loc = sharedPref.getString("Location_preference", "5865");
				
				// Check if location was changed
				if(!Location.equals(Loc)) {
					// Check language also
					if(!Language.equals(Lang)) {
						// Save new language
						Language = Lang;
						
						// Change title and button text
						this.setTitle(languageHandler.GetLocationText(Location, Language));
						btnSubmit.setText(languageHandler.GetButtonText(Language));
					}
					// Save new location
					Location = Loc;
					
					// Empty saved weeks
					savedWeeks = new ArrayList<Week>();
					
					// Start loading screen again as location was changed
					LoadingScreen();
				}
				else if(!Language.equals(Lang)) {
					// Save new language
					Language = Lang;
					
					// Change title and button text
					this.setTitle(languageHandler.GetLocationText(Location, Language));
					btnSubmit.setText(languageHandler.GetButtonText(Language));
					
					// Update Text
					GetDateText();
				}				
				break;
			}
		}
	}
	
	// Function to get food for selected day
	private void GetDateText() {	
		switch(calendar.getWeekDate()){
			case(Calendar.MONDAY):{	
				tvWeekDate.setText(languageHandler.GetDayText(0, Language));
				foodList = currentWeek.getDayMenu(0, Language);
				break;
			}
			case(Calendar.TUESDAY):{
				tvWeekDate.setText(languageHandler.GetDayText(1, Language));
				foodList = currentWeek.getDayMenu(1, Language);
				break;
			}
			case(Calendar.WEDNESDAY):{
				tvWeekDate.setText(languageHandler.GetDayText(2, Language));
				foodList = currentWeek.getDayMenu(2, Language);
				break;
			}
			case(Calendar.THURSDAY):{
				tvWeekDate.setText(languageHandler.GetDayText(3, Language));
				foodList = currentWeek.getDayMenu(3, Language);
				break;
			}
			case(Calendar.FRIDAY):{
				tvWeekDate.setText(languageHandler.GetDayText(4, Language));
				foodList = currentWeek.getDayMenu(4, Language);
				break;
			}
		}
		
		
		
		// Add food names to list
		List<String> list = new ArrayList<String>();
		String error = "";
		for(Food food : foodList) {		
			list.add(food.foodName);
			if(!food.error.equals(""))
				error = food.error;
		}
		if(!error.equals(""))
			Toast.makeText(MainActivity.this, error,Toast.LENGTH_SHORT).show();
		
		// Save list of food names to spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
		
		// Set selected food to first
		SelectedFood = foodList.get(0);
		id = SelectedFood.Id;
		
		int year = calendar.year;
		int month = calendar.month;
		int day = calendar.day;
		
		tvDate.setText(day + "." + month + "." + year);
	}
	 
	public void onItemSelected(AdapterView<?> parent, View v, int position, long ids) {
		
		// Set select selected food and item id
		SelectedFood = foodList.get(position);
		selectedItem = position;
		
		// Save food id
		id = SelectedFood.Id;
		
		// Set food information text
		tvJson.setText(languageHandler.GetFoodInfoText(SelectedFood, Language));
		
		// Set review text
		tvReview.setText(languageHandler.GetReviewText(Language));
		if(SelectedFood.userScore == -1)
			// When user has not voted
			tvReviewSelf.setText(languageHandler.GetReviewSelfText(Language));
		else
			// When user has voted
			tvReviewSelf.setText(languageHandler.GetReviewSelfGivenText(Language));
		
		// Set ratings for that food
		// If value is -1 then user has not yet voted for that food
		if(SelectedFood.userScore != -1)
			RatingSelf.setRating((float) SelectedFood.userScore);
		else
			RatingSelf.setRating((float) 0.0);
		
		Rating.setRating((float) SelectedFood.reviewScore);
		
		if(calendar.DayToday())
			btnSubmit.setVisibility(View.VISIBLE);
		else if(!calendar.DayToday()) {
			 // Hide voting buttons and self rating if day user is checking is not today
			 btnSubmit.setVisibility(View.GONE); 
			 tvReviewSelf.setText(languageHandler.GetReviewSelfGivenText(Language));
		}
		else {
			// Show rating stars and text
			RatingSelf.setVisibility(View.VISIBLE);
			tvReviewSelf.setVisibility(View.VISIBLE);
			btnSubmit.setVisibility(View.GONE);
						
			
			// If user has not voted then show rate button
			/*if(SelectedFood.userScore != -1)
				btnSubmit.setVisibility(View.GONE);
			else
				btnSubmit.setVisibility(View.VISIBLE);*/
		}
	}


	@Override
	// Needed for onItemSelected implementation
	public void onNothingSelected(AdapterView<?> arg0) {}
	
	// Button listener initialization
	public void listenerOnButton(){
    	btnSubmit.setOnClickListener(new View.OnClickListener(){
    		
    		@Override
    		public void onClick(View v){
    				// Save rating to both english and finish food class    				
    				switch(calendar.getWeekDate()){
	    				case(Calendar.MONDAY):{
	    					currentWeek.AddRating(0, RatingSelf.getRating(), selectedItem);
	    					break;
	    				}
	    				case(Calendar.TUESDAY):{
	    					currentWeek.AddRating(1, RatingSelf.getRating(), selectedItem);
	    					break;
	    				}
	    				case(Calendar.WEDNESDAY):{
	    					currentWeek.AddRating(2, RatingSelf.getRating(), selectedItem);
	    					break;
	    				}
	    				case(Calendar.THURSDAY):{
	    					currentWeek.AddRating(3, RatingSelf.getRating(), selectedItem);
	    					break;
	    				}
	    				case(Calendar.FRIDAY):{
	    					currentWeek.AddRating(4, RatingSelf.getRating(), selectedItem);
	    					break;
	    				}
	    			}
    				
    				//Save userscore to current food class
    				SelectedFood.userScore = RatingSelf.getRating();
    				
    				// Start posting new rating
    				new postData().execute("");
    				
    				// Hide rate button
    				btnSubmit.setVisibility(View.GONE);
    				
    				// Change self ratintg text
    				tvReviewSelf.setText(languageHandler.GetReviewSelfGivenText(Language));
    			
    		}
    	});
    }
	
	// Post data task
	private class postData extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... values) {
        	HttpClient httpclient = new DefaultHttpClient();
        	HttpPost httppost = new HttpPost("http://student.labranet.jamk.fi/~G3217/Android/Harkka/aanestys/index.php");
        	
        	// Save current date for posting
        	int day = calendar.day;
        	int month = calendar.month;
        	int year = calendar.year;
        	String date = day + "." + month + "." + year;
        	
        	String total = "";
        	
        	try{
        		// Add post data
        		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
        		nameValuePairs.add(new BasicNameValuePair("username", user.Username));
        		nameValuePairs.add(new BasicNameValuePair("date",date));
        		nameValuePairs.add(new BasicNameValuePair("arvosana",Float.toString(RatingSelf.getRating())));
        		nameValuePairs.add(new BasicNameValuePair("ruokaid", Integer.toString(id)));
        		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        		
        		// Execute post and get response
        		HttpResponse response = httpclient.execute(httppost);      		
        		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        		
        		// Save response message
        		String line = "";        		
        		while((line = rd.readLine()) != null){
        			total += line;
        		}
        	} catch(ClientProtocolException e){
        		total = "-1";
        	} catch(IOException e){
        		e.printStackTrace();
        	}
        	
        	// return response message
        	return total;
        }

        protected void onPostExecute(String result) {
        	String res = "";
        	// Replacing result string with only one char so compare works
        	if(result.contains("-1"))
        		res = "-1";
        	else if(result.contains("1"))
        		res = "1";
        	else
        		res = "0";
        	
        	if(res.equals("-1"))
        		// Network error
        		Toast.makeText(MainActivity.this, languageHandler.GetRatingPostConnectionErrorText(Language),Toast.LENGTH_SHORT).show();
        	else if(res.equals("1"))
        		// Succeeded
        		Toast.makeText(MainActivity.this, languageHandler.GetRatingPostSucceededText(Language),Toast.LENGTH_SHORT).show();        		
        	else
        		// Already voted
        		Toast.makeText(MainActivity.this, languageHandler.GetRatingPostAlreadyVotedText(Language),Toast.LENGTH_SHORT).show();
        }
    }
	
	
}



