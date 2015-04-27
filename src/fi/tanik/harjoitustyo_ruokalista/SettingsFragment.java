package fi.tanik.harjoitustyo_ruokalista;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	private String DefaultLanguage, DefaultLocation; 
	
	public SettingsFragment(String Lang, String Loc) {

		if(Lang.equals("fi"))
			this.DefaultLanguage = "Finnish";
		else
			this.DefaultLanguage = "English";
		
		if(Loc.equals("5865"))	
			this.DefaultLocation = "Dynamo";
		else if(Loc.equals("5859"))
			this.DefaultLocation = "Main Campus";
		else if(Loc.equals("5861"))
			this.DefaultLocation = "Rajacafé";
		else if(Loc.equals("5868"))
			this.DefaultLocation = "Music Campus";
		else
			this.DefaultLocation = "Dynamo";
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.application_preferences);
        
        ListPreference langp = (ListPreference)findPreference("Language_preference");
        
        CharSequence[] langentries = { "English", "Finnish"};
        CharSequence[] langentryValues = { "en", "fi"};
        	
        langp.setEntries(langentries);
        langp.setEntryValues(langentryValues);
        langp.setTitle("Menu Language");
        langp.setSummary(this.DefaultLanguage);
        	
        ListPreference locp = (ListPreference)findPreference("Location_preference");
        	
        CharSequence[] locentries = { "Dynamo", "Main Campus", "Rajacafé", "Music Campus"};
        CharSequence[] locentryValues = { "5865", "5859", "5861", "5868"};
        	
        locp.setEntries(locentries);
        locp.setEntryValues(locentryValues);
        locp.setTitle("Location");
        locp.setSummary(this.DefaultLocation);
    }
	
	@Override
	public void onStart() {
		super.onStart();
		// register preference change listener
		SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
    public void onStop() {
		super.onStop();
		// unregister
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

	@Override
	public void onResume() {
	    super.onResume();
	    getView().setBackgroundColor(Color.WHITE);
	}

	// change text or list values in PreferenceActivity ("Screen/Page")
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPref, String key) {
		Preference list = findPreference(key);
		if(key.equals("Language_preference")) {
			if(sharedPref.getString(key, "").equals("fi"))
				list.setSummary("Finnish");
			else
				list.setSummary("English");
		} else {
			if(sharedPref.getString(key, "").equals("5865"))	
				list.setSummary("Dynamo");
			else if(sharedPref.getString(key, "").equals("5859"))
				list.setSummary("Main Campus");
			else if(sharedPref.getString(key, "").equals("5861"))
				list.setSummary("Rajacafé");
			else if(sharedPref.getString(key, "").equals("5868"))
				list.setSummary("Music Campus");
		}
	}

}
