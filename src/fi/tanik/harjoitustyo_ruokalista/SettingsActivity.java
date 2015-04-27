package fi.tanik.harjoitustyo_ruokalista;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		String Lang = sharedPref.getString("Language_preference", "");				
		String Loc = sharedPref.getString("Location_preference", "");
		
		// open a fragment to settings 
		getFragmentManager().beginTransaction()
      					    .replace(android.R.id.content, new SettingsFragment(Lang, Loc))
      					    .commit();
	  
	}
}
