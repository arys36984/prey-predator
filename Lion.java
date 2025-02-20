import java.util.Random;
import java.util.List;

/**
 * A simple model of a lion.
 * Lions age, move, eat giraffe, and die.
 * 
 * @author Aryan Sanvee Vijayan, Alexander Sukhin
 * @version 18/02/2025
 */
public class Lion extends Predator
{
    // Characteristics shared by all lions (class variables).
    // The age at which a lion can start to breed.
    private static final int BREEDING_AGE = 15;
    // The age to which a lion can live.
    private static final int MAX_AGE = 180;
    // The likelihood of a lion breeding.
    private static final double BREEDING_PROBABILITY = 0.4;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // Lions are primarily nocturnal, with low hunting during day.
    // The likelihood of a lion hunting during the night.
    private static final double NIGHT_HUNT_PROBABILITY = 0.9;
    // The likelihood of a lion hunting during the night.
    private static final double DAY_HUNT_PROBABILITY = 0.25;

    /**
     * Create a lion. A lion can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the lion will have random age and hunger level.
     * @param location The location within the field.
     */
    public Lion(boolean randomAge, Location location)
    {
        super(location, Giraffe.class);
        if(randomAge) {
            setAge(randInt(MAX_AGE));
        }
        else {
            setAge(0);
        }
    }

    @Override
    public String toString() {
        return "Lion{" +
        "age=" + getAge() +
        ", alive=" + isAlive() +
        ", location=" + getLocation() +
        ", foodLevel=" +
        '}';
    }

    /**
     * Check if the given animal is a prey of Lion.
     * @param Animal The animal to check if its a prey.
     * @return true, if the animal is a prey, otherwise false.
     */
    protected boolean isPrey(Animal animal) {
        if(animal instanceof Giraffe giraffe) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Check if the lion is too old to live.
     */
    protected void checkIfTooOld()
    {
        if(getAge() > MAX_AGE) {
            setDead();
        }
    }

    /**
     * A lion can breed successfully if it has reached the breeding age,
     * and luck is on its side.
     * @return true if the lion breeds successfully, false otherwise.
     */
    protected boolean breedSuccess()
    {
        return getAge() >= BREEDING_AGE && randDouble() <= BREEDING_PROBABILITY;
    }

    /**
     * Create a new lion as offspring.
     * @param loc The location off the new offspring.
     * @return The offspring.
     */
    protected Animal offspring(Location loc) {
        Lion young = new Lion(false,loc);
        return young;
    }

    /**
     * Generate number of offspring if breeding is successful.
     * @return Number of offspring.
     */
    protected int birthNumber() {
        return randInt(MAX_LITTER_SIZE) + 1;
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
}
