package fi.tanik.harjoitustyo_ruokalista;

import java.io.Serializable;

/*
 * 	This class holds information of single food 
 * 	with all necessary information neede for later time.
 *
 *	It also holds information for any error that 
 *	happened during loading menu
 */

public class Food implements Serializable {	
	private static final long serialVersionUID = 4229513834678025928L;
	/***/
	public int Id;
	public String foodName;
	public String additionalInfo;
	public String category;
	public String allergies;
	public double reviewScore;
	public double userScore;
	public String error;
	
	public Food() {
		Id = -1;
		foodName = "";
		additionalInfo = "";
		category = "";
		allergies = "";
		reviewScore = -1.0;
		userScore = -1.0;
		error = "";
	}
	
	// Copy constructor
	public Food(Food day) {
		Id = day.Id;
		foodName = day.foodName;
		additionalInfo = day.additionalInfo;
		category = day.category;
		allergies = day.allergies;
		reviewScore = day.reviewScore;
		userScore = day.userScore;
		error = day.error;
	}
}
