package fi.tanik.harjoitustyo_ruokalista;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * 	This class holds information of all foods for one week
 *
 * 	NOTE: We only have english and finnish language here as sodexo currently
 * 	        doesn't support any other languages
 */

public class Week  implements Serializable {	
	private static final long serialVersionUID = -2892922306031408581L;
	/***/
	public ArrayList<Food> MondayFi, TuesdayFi, WednesdayFi, ThursdayFi, FridayFi;
    public ArrayList<Food> MondayEn, TuesdayEn, WednesdayEn, ThursdayEn, FridayEn;
	public int weekNumber;
	
	public Week() {
		MondayFi = new ArrayList<Food>();
		TuesdayFi = new ArrayList<Food>();
		WednesdayFi = new ArrayList<Food>();
		ThursdayFi = new ArrayList<Food>();
		FridayFi = new ArrayList<Food>();
		
		MondayEn = new ArrayList<Food>();
		TuesdayEn = new ArrayList<Food>();
		WednesdayEn = new ArrayList<Food>();
		ThursdayEn = new ArrayList<Food>();
		FridayEn = new ArrayList<Food>();
		
		weekNumber = -1;
	}
	
	// Add new food to list
	public void addFoodtoList(int dayNumber, Food foodFi, Food foodEn) {
		switch(dayNumber) {
			case 0: {
				MondayFi.add(foodFi);
				MondayEn.add(foodEn);
				break;
			}
			case 1: {
				TuesdayFi.add(foodFi);
				TuesdayEn.add(foodEn);
				break;
			}
			case 2: {
				WednesdayFi.add(foodFi);
				WednesdayEn.add(foodEn);
				break;
			}
			case 3: {
				ThursdayFi.add(foodFi);
				ThursdayEn.add(foodEn);
				break;
			}
			case 4: {
				FridayFi.add(foodFi);
				FridayEn.add(foodEn);
				break;
			}
		}
	}
	
	// Get all foods for specific day
	public ArrayList<Food> getDayMenu(int dayNumber, String language) {
		switch(dayNumber) {
			case 0: {
				if(language.equals("fi"))
					return MondayFi;				
				else 
					return MondayEn;
			}
			case 1: {
				if(language.equals("fi"))
					return TuesdayFi;				
				else 
					return TuesdayEn;
			}
			case 2: {
				if(language.equals("fi"))
					return WednesdayFi;				
				else 
					return WednesdayEn;
			}
			case 3: {
				if(language.equals("fi"))
					return ThursdayFi;				
				else 
					return ThursdayEn;
			}
			case 4: {
				if(language.equals("fi"))
					return FridayFi;				
				else 
					return FridayEn;
			}
		}
		return null;
	}
	
	// Adds food rating to both lists
	public void AddRating(int dayNumber, float Score, int selectedItem) {
		switch(dayNumber) {
			case 0: {
				MondayFi.get(selectedItem).userScore = Score;
				MondayEn.get(selectedItem).userScore = Score;
				break;
			}
			case 1: {
				TuesdayFi.get(selectedItem).userScore = Score;
				TuesdayEn.get(selectedItem).userScore = Score;
				break;
			}
			case 2: {
				WednesdayFi.get(selectedItem).userScore = Score;
				WednesdayEn.get(selectedItem).userScore = Score;
				break;
			}
			case 3: {
				ThursdayFi.get(selectedItem).userScore = Score;
				ThursdayEn.get(selectedItem).userScore = Score;
				break;
			}
			case 4: {
				FridayFi.get(selectedItem).userScore = Score;
				FridayEn.get(selectedItem).userScore = Score;
				break;
			}
		}
	}
}
