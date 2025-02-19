import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a ocelot.
 * Ocelotes age, move, eat armadilos, and die.
 * 
 * @author David J. Barnes, Aryan Sanvee Vijayan, Alexander Sukhi and Michael Kölling
 * @version 19/02/2025
 */
public class Ocelot extends Predator
{
    // Characteristics shared by all ocelotes (class variables).
    // The age at which a ocelot can start to breed.
    private static final int BREEDING_AGE = 15;
    // The age to which a ocelot can live.
    private static final int MAX_AGE = 150;
    // The likelihood of a ocelot breeding.
    private static final double BREEDING_PROBABILITY = 0.3;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 5;
    // Ocelots are nocturnal but are somewhat active during day as well.
    // The probability of an ocelot to hunt at night.
    private static final double NIGHT_HUNT_PROBABILITY = 0.75;
    // The probability of an ocelot to hunt at day.
    private static final double DAY_HUNT_PROBABILITY = 0.5;

    /**
     * Create a ocelot. A ocelot can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the ocelot will have random age and hunger level.
     * @param location The location within the field.
     */
    public Ocelot(boolean randomAge, Location location)
    {
        super(location, Armadillo.class);
        if(randomAge) {
            setAge(randInt(MAX_AGE));
        }
        else {
            setAge(0);
        }
    }

    /**
     * Calculate whether a hunt is successful.
     * @param time The time of day/night.
     * @return true if the hunt is successful, false otherwise.
     */
    public boolean huntSuccess(Time time) {
        if (time == Time.NIGHT) {
            return randDouble() <= NIGHT_HUNT_PROBABILITY;
        } 
        else {
            return randDouble() <= DAY_HUNT_PROBABILITY;
        }
    }

    @Override
    public String toString() {
        return "Ocelot{" +
        "age=" + getAge() +
        ", alive=" + isAlive() +
        ", location=" + getLocation() +
        '}';
    }

    /**
     *  Check if the ocelot is too old to live.
     */
    protected void checkIfTooOld()
    {
        if(getAge() > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Check if the given animal is a prey of Ocelot.
     * @param animal The animal to check if its a prey.
     * @return true, if the animal is a prey, otherwise false.
     */
    protected boolean isPrey(Animal animal) {
        if(animal instanceof Armadillo armadillo) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * A ocelot can breed successfully if it has reached the breeding age;
     * and luck is on the ocelot's side.
     * @return true if the ocelot breeds successfully, false otherwise.
     */
    protected boolean breedSuccess()
    {
        return getAge() >= BREEDING_AGE && randDouble() <= BREEDING_PROBABILITY;
    }

    /**
     * Create a new ocelot as offspring.
     * @param loc The location off the new offspring.
     * @return The offspring.
     */
    protected Animal offspring(Location loc) {
        Ocelot young = new Ocelot(false,loc);
        return young;
    }

    /**
     * Generate number of offspring if breeding is successful.
     * @return Number of offspring.
     */
    protected int birthNumber() {
        return randInt(MAX_LITTER_SIZE) + 1;
    }
}
