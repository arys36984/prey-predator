
/**
 * Enumeration class Time - represents day and night
 *
 * @author Alexander Sukhin
 * @version 07/02/2025
 */
public enum Time
{
    DAY("day"), 
    NIGHT("night");

    private String nameString;

    /**
     * Initialise with the corresponding string.
     * @param nameString The name string.
     */
    Time(String nameString)
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
}
