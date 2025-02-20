import java.util.Random;

/**
 * Enumeration class Weather - represents possible states of weather.
 *
 * @author Aryan Sanvee Vijayan
 * @version 12/02/2025
 */
public enum Weather
{
    RAIN("rain"), 
    STORM("storm"), 
    CLEAR("clear"), 
    CLOUDY("cloudy");

    // Random generator for weather
    private static final Random rand = Randomizer.getRandom();

    private String nameString;

    /**
     * Initialise with the corresponding string.
     * @param nameString The name string.
     */
    Weather(String nameString)
    {
        this.nameString = nameString;
    }

    /**
     * @return The name word as a string.
     */
    @Override
    public String toString()
    {
        return nameString;
    }

    /**
     * Return a random Weather from the ones declared.
     * @return Random weather
     */
    public static Weather randomWeather()  {
        Weather[] weathers = values();
        return weathers[rand.nextInt(weathers.length)];
    }
}
