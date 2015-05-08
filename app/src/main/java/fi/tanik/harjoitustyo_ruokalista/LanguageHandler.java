package fi.tanik.harjoitustyo_ruokalista;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.view.ContextMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/*
 * 	This class handles languages from languages.json file (WIP)
 */

public class LanguageHandler {
    private JSONObject json = null;
    private int langID = 0;
    private String Language = "";
    private Context context;

    public LanguageHandler(Context context) {
        this.context = context;

        // Load saved language id
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        Language = sharedPref.getString("Language_preference", "en");

        // Load json text here
        try {
            AssetManager assetManager = context.getAssets();
            InputStream is = assetManager.open("languages.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();
            String j = new String(buffer, "UTF-8");
            json = new JSONObject(j);

            JSONArray languageID = json.getJSONArray("language_id");
            for(int index = 0; index < languageID.length(); index++) {
                if(languageID.getString(index).equals(Language)) {
                    langID = index;
                }
            }
        } catch (IOException ex) {
            // File not found
            ex.printStackTrace();
        } catch (JSONException e) {
            // JSON loading error
            e.printStackTrace();
        }
    }

    private String getLanguageText(String text) {
        String returnText = "";

        // Load saved language id
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String lang = sharedPref.getString("Language_preference", "en");

        if(!Language.equals(lang)) {
            ChangeLanguage(lang);
        }

        // Get JSONObject and return empty if failed
        JSONObject jObject;
        try {
            jObject = json.getJSONObject(text);
        } catch (JSONException e) {
            // JSON loading error
            e.printStackTrace();

            return "";
        }

        // Try to get text in defined language
        try {
            returnText = jObject.getString(Language);
        } catch (JSONException e) {
            // JSON loading error
            e.printStackTrace();

            // Try to get text in english
            try {
                returnText = jObject.getString("en");
            } catch (JSONException r) {
                // JSON loading error
                r.printStackTrace();
            }
        }

        return returnText;
    }

    private void ChangeLanguage(String lang) {
        Language = lang;

        try {
            if(json != null) {
                JSONArray languageID = json.getJSONArray("language_id");
                for(int index = 0; index < languageID.length(); index++) {
                    if(languageID.getString(index).equals(lang)) {
                        langID = index;
                    }
                }
            }
        } catch (JSONException e) {
            // JSON loading error
            e.printStackTrace();
        }
    }

	/**************************************************************************/
	// errors
    /**************************************************************************/
	public String GetErrorSelfRatingValueText(String language) {
        return getLanguageText("error_ratingValueSelfZero");
	}
	
	public String GetErrorLoadingConnectionText(String language) {
        return getLanguageText("error_connection");
	}
	
	public String GetErrorLoadingDownloadText(String language) {
        return getLanguageText("error_download");
	}
	
	public String GetErrorLoadingJSONArrayText(String language) {
        return getLanguageText("error_jsonArray");
	}
	
	public String GetErrorLoadingNoMenuText(String language) {
        return getLanguageText("error_menuNotFound");
	}
	
	/**************************************************************************/
	// Rating
    /**************************************************************************/
	public String GetRatingPostConnectionErrorText(String language) {
        return getLanguageText("error_connection");
	}
	
	public String GetRatingPostSucceededText(String language) {
        return getLanguageText("rating_succeed");
	}
	
	public String GetRatingPostAlreadyVotedText(String language) {
        return getLanguageText("rating_alreadyVoted");
	}

    public String getRateButtonText() {
        return getLanguageText("rating_buttonRate");
    }
		
	/**************************************************************************/
	// Loading screen
    /**************************************************************************/
	public String GetLoadingScreenText(String language) {
        return getLanguageText("loading_menuText");
	}
	
	/**************************************************************************/
	// Allergies
    /**************************************************************************/
	public String GetAllergieLactoseText(String language) {
        return getLanguageText("allergies_lactose");
	}
	
	public String GetAllergieGlutenText(String language) {
        return getLanguageText("allergies_gluten");
	}
	
	public String GetAllergieSmallLactoseText(String language) {
        return getLanguageText("allergies_smallLactose");
	}
	
	public String GetAllergieNoMilkText(String language) {
        return getLanguageText("allergies_noMilk");
	}
	
	/**************************************************************************/
	// Review text
    /**************************************************************************/
    public String GetReviewText(String language) {
        return getLanguageText("review_text");
	}
	
	public String GetReviewSelfText(String language) {
        return getLanguageText("review_textSelf");
	}
	
	public String GetReviewSelfGivenText(String language) {
        return getLanguageText("review_textSelfGiven");
	}
	
	/**************************************************************************/
	// Menu items
    /**************************************************************************/
	public String GetMenuSettingsText(String language) {
        return getLanguageText("menu_settings");
	}
	
	public String GetMenuReturnTodayText(String language) {
        return getLanguageText("menu_returnToday");
	}
	
	/**************************************************************************/
	
	// Settings Languages
	private CharSequence[] SettingsFinnishLanguage = {"Suomi", "Englanti"};
	private CharSequence[] SettingsEnglishLanguage = {"Finnish", "English"};
	public CharSequence[] SettingsLanguageValues = {"fi", "en"};
		
	public void SetSettingsLanguageText(ListPreference langp, String language) {
		if(language.equals("fi")) {	        
	        langp.setTitle("Valikon kieli");
	        langp.setSummary("Suomi");
	        langp.setEntries(SettingsFinnishLanguage);	        
        } else {
	        langp.setTitle("Menu Language");
	        langp.setSummary("English");
	        langp.setEntries(SettingsEnglishLanguage);
        }
	}
	
	/**************************************************************************/
	
	// Settings Locations
	private CharSequence[] SettingsFinnishLocation = {"Dynamo", "Pääkampus", "Rajacafe", "Musiikkikampus"};
	private CharSequence[] SettingsEnglishLocation = {"Dynamo", "Main Campus", "Rajacafe", "Music Campus"};
	public CharSequence[] SettingsLocationValues = {"5865", "5859", "5861", "5868"};
	
	public void SetSettingsLocationText(ListPreference locp, String language) {
		if(language.equals("fi")) {    	
	    	locp.setEntries(SettingsFinnishLocation);
	        locp.setTitle("Sijainti");
	    } else {   	
	    	locp.setEntries(SettingsEnglishLocation);
	        locp.setTitle("Location");      
	    }
	}
	
	/**************************************************************************/
	
	public String GetFoodInfoText(Food day, String language) {
		String dayText = "";

        // Replace allergies text with long name
        String editedAllergies = day.allergies;
        editedAllergies = editedAllergies.replaceAll("\\bVL\\b", GetAllergieSmallLactoseText("fi"));
        editedAllergies = editedAllergies.replaceAll("\\bL\\b", GetAllergieLactoseText("fi"));
        editedAllergies = editedAllergies.replaceAll("\\bG\\b", GetAllergieGlutenText("fi"));
        editedAllergies = editedAllergies.replaceAll("\\bM\\b", GetAllergieNoMilkText("fi"));

		dayText += getLanguageText("food_name") + ": " + day.foodName + "\n";
		dayText += getLanguageText("food_category") + ": " + day.category + "\n";
		dayText += getLanguageText("food_allergy") + ": " + editedAllergies + "\n";
		dayText += getLanguageText("food_info") + ": " + day.additionalInfo + "\n";

		return dayText;
	}
	
	/**************************************************************************/
	
	//Button
	private String[] ButtonRate = {"Arvostele", "Rate"};
		
	public String GetButtonText(String language) {
		if(language.equals("fi"))
			return ButtonRate[0];				
		else 
			return ButtonRate[1];
	}
	
	/**************************************************************************/
	
	// Locations
	private String[] Dynamo = {"Dynamo", "Dynamo"};
	private String[] MainCampus = {"Pääkampus", "Main Campus"};
	private String[] RajaCafe = {"Rajacafe", "Rajacafe"};
	private String[] MusicCampus = {"Musiikkikampus", "Music Campus"};
		
	public String GetLocationText(String location, String language) {
		if(language.equals("fi")) {
			if(location.equals("5865"))	
				return Dynamo[0];
			else if(location.equals("5859"))
				return MainCampus[0];
			else if(location.equals("5861"))
				return RajaCafe[0];
			else if(location.equals("5868"))
				return MusicCampus[0];
        } else {
        	if(location.equals("5865"))	
				return Dynamo[1];
			else if(location.equals("5859"))
				return MainCampus[1];
			else if(location.equals("5861"))
				return RajaCafe[1];
			else if(location.equals("5868"))
				return MusicCampus[1];
        }
		return "";
	}
	
	/**************************************************************************/
	
	// Days
	private String[] Monday = {"Maanantai", "Monday"};
	private String[] Tuesday = {"Tiistai", "Tuesday"};
	private String[] Wednesday = {"Keskiviikko", "Wednesday"};
	private String[] Thursday = {"Torstai", "Thursday"};
	private String[] Friday = {"Perjantai", "Friday"};
	
	public String GetDayText(int dayNumber, String language) {
		switch(dayNumber) {
			case 0: {
				if(language.equals("fi"))
					return Monday[0];				
				else 
					return Monday[1];
			}
			case 1: {
				if(language.equals("fi"))
					return Tuesday[0];				
				else 
					return Tuesday[1];
			}
			case 2: {
				if(language.equals("fi"))
					return Wednesday[0];				
				else 
					return Wednesday[1];
			}
			case 3: {
				if(language.equals("fi"))
					return Thursday[0];				
				else 
					return Thursday[1];
			}
			case 4: {
				if(language.equals("fi"))
					return Friday[0];				
				else 
					return Friday[1];
			}
		}
		return null;
	}
}
