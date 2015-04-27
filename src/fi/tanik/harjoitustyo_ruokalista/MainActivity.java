package fi.tanik.harjoitustyo_ruokalista;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;


public class MainActivity extends Activity {
	
	private Calendar calendar;
	private TextView tvDate, tvJson, tvWeekDate;
	
	private List<String> MondayFI, TuesdayFI, WednesdayFI, ThursdayFI, FridayFI;
	private List<String> MondayEN, TuesdayEN, WednesdayEN, ThursdayEN, FridayEN;
	private int year, month, day;
	private final int loadingReturnCode = 1, SettingsReturnCode = 2;
	
	private float x1, x2;
    private float y1, y2;
    
    private String Language, Location;
    //private String DbUrl = "http://student.labranet.jamk.fi/~G3217/Android/Harkka/";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		calendar = Calendar.getInstance();	
		tvJson = (TextView) findViewById(R.id.JsonText);
		tvDate = (TextView) findViewById(R.id.DateText);
		tvWeekDate = (TextView) findViewById(R.id.DateWeekText);
		
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
		
		CalendarUpdate(0);
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		Language = sharedPref.getString("Language_preference", "en");
		
		Location = sharedPref.getString("Location_preference", "5865");
		if(Location.equals("5865"))	
			this.setTitle("Dynamo");
		else if(Location.equals("5859"))
			this.setTitle("Main Campus");
		else if(Location.equals("5861"))
			this.setTitle("Rajacafé");
		else if(Location.equals("5868"))
			this.setTitle("Music Campus");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	        case R.id.action_settings: {        		        	
	        	Intent i = new Intent(this, SettingsActivity.class);
	            startActivityForResult(i, SettingsReturnCode);
	            return true;
	        }
	        case R.id.action_today: {
	        	ReturnToday();
	        	return true;
	        }
	    }
		return false;
	}
	
	public boolean onTouchEvent(MotionEvent touchevent) {
		
		switch (touchevent.getAction()) {
        	// when user first touches the screen we get x and y coordinate
            case MotionEvent.ACTION_DOWN: {
            	x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            }
            case MotionEvent.ACTION_UP: {
                x2 = touchevent.getX();
                y2 = touchevent.getY();
                
                double distance = Math.sqrt(((x1 - x2) * (x1 -x2)) + ((y1 - y2) * (y1 -y2)));
                
                if(Math.abs(x1 - x2) > Math.abs(y1 - y2)) {
                	//if left to right sweep event on screen
                	
                    if (x1 < x2 && distance >= 200) {
                    	CalendarUpdate(-1);   
                    }
                               
                    // if right to left sweep event on screen
                    if (x1 > x2 && distance >= 200) {
                    	CalendarUpdate(1);             	
                    }
                } else {                     
	                // if UP to Down sweep event on screen
	                if (y1 < y2 && distance >= 200) {
	                	//ReturnToday();
	                }
	                           
	                //if Down to UP sweep event on screen
	                if (y1 > y2 && distance >= 200) {
	                	//ReturnToday();
	                }
	                break;
                }
            }
		}
        return false;
    }
	
	private void CalendarUpdate(int value) {
		if(value == -1) {
			if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
				calendar.add(Calendar.DAY_OF_YEAR, -3);
				
				LoadingScreen();
			} else {
				calendar.add(Calendar.DAY_OF_YEAR, value);
				GetDateText();
			}
		} else if(value == 1) {
			if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
				calendar.add(Calendar.DAY_OF_YEAR, 3);
				LoadingScreen();
			} else {
				calendar.add(Calendar.DAY_OF_YEAR, value);
				GetDateText();
			}
		} else {
			LoadingScreen();
		}    	
	}
	
	private void ReturnToday() {		
		if(calendar.get(Calendar.WEEK_OF_YEAR) == Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)) {
			calendar = Calendar.getInstance();
			GetDateText();
		} else {
			calendar = Calendar.getInstance();
			LoadingScreen();
		}
			
	}
	
	private void LoadingScreen() {
		Intent intent = new Intent(this, LoadingScreen.class);
		intent.putExtra("calendar", calendar.get(Calendar.DAY_OF_YEAR));
		startActivityForResult(intent,loadingReturnCode);		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case loadingReturnCode:
				if(resultCode == Activity.RESULT_OK) {			
					Bundle arrays = data.getExtras().getBundle("Bundle");
					
					MondayFI = arrays.getStringArrayList("MondayFI");
					TuesdayFI = arrays.getStringArrayList("TuesdayFI");
					WednesdayFI = arrays.getStringArrayList("WednesdayFI");
					ThursdayFI = arrays.getStringArrayList("ThursdayFI");
					FridayFI = arrays.getStringArrayList("FridayFI");
					
					MondayEN = arrays.getStringArrayList("MondayEN");
					TuesdayEN = arrays.getStringArrayList("TuesdayEN");
					WednesdayEN = arrays.getStringArrayList("WednesdayEN");
					ThursdayEN = arrays.getStringArrayList("ThursdayEN");
					FridayEN = arrays.getStringArrayList("FridayEN");
					
					GetDateText();
				}
				break;
			case SettingsReturnCode: {
				SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
				String Lang = sharedPref.getString("Language_preference", "");				
				String Loc = sharedPref.getString("Location_preference", "");
				
				if(!Location.equals(Loc)) {
					if(!Language.equals(Lang)) {
						Language = Lang;
					}
					Location = Loc;
					
					if(Location.equals("5865"))	
						this.setTitle("Dynamo");
					else if(Location.equals("5859"))
						this.setTitle("Main Campus");
					else if(Location.equals("5861"))
						this.setTitle("Rajacafé");
					else if(Location.equals("5868"))
						this.setTitle("Music Campus");
					
					LoadingScreen();
				}
				else if(!Language.equals(Lang)) {
					Language = Lang;
					GetDateText();
				}
				
				break;
			}
		}
	}
	
	
	private void GetDateText() {
		String DayText = "";
		switch(calendar.get(Calendar.DAY_OF_WEEK)){
			case(Calendar.MONDAY):{				
				if(Language.equals("fi")) {
					for(String text : MondayFI) {
						DayText += text + "\n\n";
					}
					tvWeekDate.setText("Maanantai");					
				} else {
					for(String text : MondayEN) {
						DayText += text + "\n\n";
					}
					tvWeekDate.setText("Monday");
				}
				break;
			}
			case(Calendar.TUESDAY):{
				if(Language.equals("fi")) {
					for(String text : TuesdayFI) {
						DayText += text + "\n\n";
					}
					tvWeekDate.setText("Tiistai");
				} else {
					for(String text : TuesdayEN) {
						DayText += text + "\n\n";
					}
					tvWeekDate.setText("Tuesday");
				}
				break;
			}
			case(Calendar.WEDNESDAY):{
				if(Language.equals("fi")) {
					for(String text : WednesdayFI) {
						DayText += text + "\n\n";
					}
					tvWeekDate.setText("Keskiviikko");
				} else {
					for(String text : WednesdayEN) {
						DayText += text + "\n\n";
					}
					tvWeekDate.setText("Wednesday");
				}
				break;
			}
			case(Calendar.THURSDAY):{
				if(Language.equals("fi")) {
					for(String text : ThursdayFI) {
						DayText += text + "\n\n";
					}
					tvWeekDate.setText("Torstai");
				} else {
					for(String text : ThursdayEN) {
						DayText += text + "\n\n";
					}
					tvWeekDate.setText("Thursday");
				}
				break;
			}
			case(Calendar.FRIDAY):{
				if(Language.equals("fi")) {
					for(String text : FridayFI) {
						DayText += text + "\n\n";
					}
					tvWeekDate.setText("Perjantai");
				} else {
					for(String text : FridayEN) {
						DayText += text + "\n\n";
					}
					tvWeekDate.setText("Friday");
				}
				break;
			}
		}
		tvJson.setText(DayText);
		
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH) + 1;
		day = calendar.get(Calendar.DAY_OF_MONTH);
		
		tvDate.setText(day + "." + month + "." + year);
	}
}
