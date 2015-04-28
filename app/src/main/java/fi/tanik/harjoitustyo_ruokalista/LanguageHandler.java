package fi.tanik.harjoitustyo_ruokalista;

import android.preference.ListPreference;

/*
 * 	This class handles all language related, making 
 * 	it easier to add more languages if needed.	
 */

public class LanguageHandler {
	
	/**************************************************************************/
	
	// error
	private String[] SelfRatingValueError = {"Arvosana ei voi olla 0", "Score cannot be 0"};
	private String[] LoadingConnectionError = {"Yhteyttä internettiin ei voitu muodostaa", "Unable to connect to internet"};
	private String[] LoadingDownloadError = {"Vastausviestiä ei voitu ladata", "Response message could not be downloaded"};
	private String[] LoadingJSONArrayError = {"JSON Arrayta ei pystytty käsittelemään", "JSON Array could not be handled"};
	private String[] LoadingNoMenuError = {"Ruokalistaa ei löydetty tälle päivälle", "Menu not found for this specific day"};
	
	public String GetErrorSelfRatingValueText(String language) {
		if(language.equals("fi"))
			return SelfRatingValueError[0];				
		else 
			return SelfRatingValueError[1];
	}
	
	public String GetErrorLoadingConnectionText(String language) {
		if(language.equals("fi"))
			return LoadingConnectionError[0];				
		else 
			return LoadingConnectionError[1];
	}
	
	public String GetErrorLoadingDownloadText(String language) {
		if(language.equals("fi"))
			return LoadingDownloadError[0];				
		else 
			return LoadingDownloadError[1];
	}
	
	public String GetErrorLoadingJSONArrayText(String language) {
		if(language.equals("fi"))
			return LoadingJSONArrayError[0];				
		else 
			return LoadingJSONArrayError[1];
	}
	
	public String GetErrorLoadingNoMenuText(String language) {
		if(language.equals("fi"))
			return LoadingNoMenuError[0];				
		else 
			return LoadingNoMenuError[1];
	}
	
	/**************************************************************************/
	
	// Rating post messages
	private String[] RatingPostConnectionError = {"Yhteyttä internettiin ei voitu muodostaa", "Unable to connect to internet"};
	private String[] RatingPostSucceeded = {"äänestys onnistui", "Voting succeeded"};
	private String[] RatingPostAlreadyVoted = {"Olet jo äänestänyt", "You have already voted"};
	
	public String GetRatingPostConnectionErrorText(String language) {
		if(language.equals("fi"))
			return RatingPostConnectionError[0];				
		else 
			return RatingPostConnectionError[1];
	}
	
	public String GetRatingPostSucceededText(String language) {
		if(language.equals("fi"))
			return RatingPostSucceeded[0];				
		else 
			return RatingPostSucceeded[1];
	}
	
	public String GetRatingPostAlreadyVotedText(String language) {
		if(language.equals("fi"))
			return RatingPostAlreadyVoted[0];				
		else 
			return RatingPostAlreadyVoted[1];
	}
		
	/**************************************************************************/
	
	// Loading screen
	private String[] LoadingScreen = {"Ladataan ruokalistaa...", "Loading menu..."};
	
	public String GetLoadingScreenText(String language) {
		if(language.equals("fi"))
			return LoadingScreen[0];				
		else 
			return LoadingScreen[1];
	}
	
	/**************************************************************************/
	
	// Allergies
	private String[] lactose = {"Laktoositon", "Lactose Free"};
	private String[] Gluten = {"Gluteeniton", "Gluten Free"};
	private String[] SmallLactose = {"Vähälaktoosinen", "Low-lactose"};
	private String[] NoMilk = {"Maidoton", "No Milk"};
	
	public String GetAllergieLactoseText(String language) {
		if(language.equals("fi"))
			return lactose[0];				
		else 
			return lactose[1];
	}
	
	public String GetAllergieGlutenText(String language) {
		if(language.equals("fi"))
			return Gluten[0];				
		else 
			return Gluten[1];
	}
	
	public String GetAllergieSmallLactoseText(String language) {
		if(language.equals("fi"))
			return SmallLactose[0];				
		else 
			return SmallLactose[1];
	}
	
	public String GetAllergieNoMilkText(String language) {
		if(language.equals("fi"))
			return NoMilk[0];				
		else 
			return NoMilk[1];
	}
	
	/**************************************************************************/
	
	// Review text 
	private String[] Review = {"Arvosana", "Score"};
	private String[] ReviewSelf = {"Arvostele Ruoka", "Rate Food"};
	private String[] ReviewSelfGiven = {"Sinun Arvostelu", "Your Score"};
	
	public String GetReviewText(String language) {
		if(language.equals("fi"))
			return Review[0];				
		else 
			return Review[1];
	}
	
	public String GetReviewSelfText(String language) {
		if(language.equals("fi"))
			return ReviewSelf[0];				
		else 
			return ReviewSelf[1];
	}
	
	public String GetReviewSelfGivenText(String language) {
		if(language.equals("fi"))
			return ReviewSelfGiven[0];				
		else 
			return ReviewSelfGiven[1];
	}
	
	/**************************************************************************/
	
	// Menu items
	private String[] Settings = {"Asetukset", "Settings"};
	private String[] ReturnToday = {"Palaa nykypäivään", "Return today"};
	
	public String GetMenuSettingsText(String language) {
		if(language.equals("fi"))
			return Settings[0];				
		else 
			return Settings[1];
	}
	
	public String GetMenuReturnTodayText(String language) {
		if(language.equals("fi"))
			return ReturnToday[0];				
		else 
			return ReturnToday[1];
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
	
	// Food info text
	private String[] FoodName = {"Ruoka", "Food"};
	private String[] Category = {"Kategoria", "Category"};
	private String[] Allergy = {"Allergiat", "Allergies"};
	private String[] Additional = {"Lisätiedot", "Additional"};
	
	public String GetFoodInfoText(Food day, String language) {
		String dayText = "";
		if(language.equals("fi")) {			
			dayText += FoodName[0] + ": " + day.foodName + "\n";
			dayText += Category[0] + ": " + day.category + "\n";
			dayText += Allergy[0] + ": " + day.allergies + "\n";
			dayText += Additional[0] + ": " + day.additionalInfo + "\n";
		} else {		
			dayText += FoodName[1] + ": " + day.foodName + "\n";
			dayText += Category[1] + ": " + day.category + "\n";
			dayText += Allergy[1] + ": " + day.allergies + "\n";
			dayText += Additional[1] + ": " + day.additionalInfo + "\n";
		}
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
