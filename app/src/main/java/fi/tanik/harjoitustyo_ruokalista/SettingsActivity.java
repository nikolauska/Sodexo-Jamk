package fi.tanik.harjoitustyo_ruokalista;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/*
 * 	This Activity show settings window where 
 * 	user can change language and location
 */

public class SettingsActivity extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get current preferences to send them to  fragment
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		String Lang = sharedPref.getString("Language_preference", "en");				
		String Loc = sharedPref.getString("Location_preference", "5865");
		
		// Open fragment 
		getFragmentManager().beginTransaction()
      					    .replace(android.R.id.content, new SettingsFragment(this, Lang, Loc))
      					    .commit();
	  
	}
}
