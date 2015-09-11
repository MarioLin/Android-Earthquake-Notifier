package animals.mariolin.com.youanimal;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by Mario Lin on 9/7/15.
 */
public class AnimalConversions {

    private final HashMap<String, Double> conversionHash = new HashMap<>();

    public AnimalConversions() {
        conversionHash.put("Bear (Oski)",2.0);
        conversionHash.put("Cat",3.2);
        conversionHash.put("Dog",3.64);
        conversionHash.put("Hamster",20.0);
        conversionHash.put("Hippopotamus",1.78);
        conversionHash.put("Kangaroo",8.89);
        conversionHash.put("Human",1.0);
    }

    /**
     * Converts from first animal to second animal years
     * @param firstAnimal
     * @param secondAnimal
     * @param years
     *      to convert
     * @return
     */
    public int[] convert(String firstAnimal, String secondAnimal, int years){
//        Log.v("dub", firstAnimal);
//        Log.v("dub", secondAnimal);

        double firstAnimalToHuman = conversionHash.get(firstAnimal);
        double secondAnimalToHuman = conversionHash.get(secondAnimal);
        double yearsWithDecimal = years * (secondAnimalToHuman/firstAnimalToHuman);
        int totalMonths;

        if (Math.floor(yearsWithDecimal) == yearsWithDecimal){
            totalMonths = 0;
        }
        else{

            totalMonths = (int) ((yearsWithDecimal-(int)yearsWithDecimal) * 12.0);
        }
        int totalYears = (int)yearsWithDecimal;

        int[] time = {totalYears, totalMonths};
        return time;
    }
}
