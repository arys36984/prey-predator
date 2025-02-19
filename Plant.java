
/**
 * This class represents an abstraction of core plants
 * and leaf plants.
 *
 * @author Alexander Sukhin
 * @version 13/02/2025
 */
public abstract class Plant extends Organism
{
    /**
     * Constructor for objects of class Plant
     */
    public Plant(Location location)
    {
        super(location);
    }

    /**
     * Act.
     * @param currentField The current state of the field.
     * @param nextFieldState The new state being built.
     * @param time The current day/night cycle.
     */
    abstract public void act(Field currentField, Field nextFieldState, Time time);
}
