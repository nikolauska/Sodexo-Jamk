package fi.tanik.harjoitustyo_ruokalista;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

/*
 * 	This fragment handles changing of preferences
 */

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	private String Language, Location;
	private ListPreference langp, locp;
	private Activity activity;
	private LanguageHandler languageHandler;
	
	public SettingsFragment(Activity activity, String Lang, String Loc) {
		// Save main activity to fragment
		this.activity = activity;
		
		// Start new languageHandler
		languageHandler = new LanguageHandler(activity.getApplicationContext());
		
		// Save language and locations
		Language = Lang;
		Location = Loc;	 
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.application_preferences);
        
        activity.setTitle(languageHandler.GetMenuSettingsText(Language));
        
        // Init language preference settings
        langp = (ListPreference)findPreference("Language_preference");
        langp.setEntryValues(languageHandler.SettingsLanguageValues);
        languageHandler.SetSettingsLanguageText(langp, Language);
        
        // Init location preference settings
        locp = (ListPreference)findPreference("Location_preference");
        locp.setEntryValues(languageHandler.SettingsLocationValues);
    	locp.setSummary(languageHandler.GetLocationText(Location, Language));
    	languageHandler.SetSettingsLocationText(locp, Language);      
    }
	
	@Override
	public void onStart() {
		super.onStart();
		// Register preference change listener
		SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
    public void onStop() {
		super.onStop();
		// Unregister preference change listener
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

	@Override
	public void onResume() {
	    super.onResume();
	    getView().setBackgroundColor(Color.WHITE);
	}

	@Override
	// When preference is changed
	public void onSharedPreferenceChanged(SharedPreferences sharedPref, String key) {
		
		// Get changed preference 
		Preference list = findPreference(key);
		if(key.equals("Language_preference")) {
			// Save changed language locally
			if(sharedPref.getString(key, "").equals("fi")) {
				Language = "fi";	        
			} else {
				Language = "en";
			}
			
			// Change settings language
			activity.setTitle(languageHandler.GetMenuSettingsText(Language));
			
			// Change language and location preference texts
			languageHandler.SetSettingsLanguageText(langp, Language);	        
			languageHandler.SetSettingsLocationText(locp, Language);
			
			// Change location summary text
			locp.setSummary(languageHandler.GetLocationText(Location, Language));
		} else {
			// Save new location
			Location = sharedPref.getString(key, "");
			
			// Change location summary text
			list.setSummary(languageHandler.GetLocationText(Location, Language));		
		}
	}

}
