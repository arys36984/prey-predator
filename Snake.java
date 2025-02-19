import java.util.*;

/**
 * A simple model of a snake.
 * Snakes age, move, eat armadillos, and die.
 *
 * @author Aryan Sanvee Vijayan, Alexander Sukhin
 * @version 18/02/2025
 */
public class Snake extends Predator
{
    // Characteristics shared by all snakes (class variables).
    // The age at which a snake can start to breed.
    private static final int BREEDING_AGE = 15;
    // The age to which a snake can live.
    private static final int MAX_AGE = 250;
    // The likelihood of a snake breeding.
    private static final double BREEDING_PROBABILITY = 0.3;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 30;
    // Snakes are mostly diurnal (active at day).
    // The likelihood of a snake hunting at night.
    private static final double NIGHT_HUNT_PROBABILITY = 0.25;
    // The likelihood of a snake hunting at night.
    private static final double DAY_HUNT_PROBABILITY = 0.9;

    /**
     * Create a snake. A snake can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the snake will have random age and hunger level.
     * @param location The location within the field.
     */
    public Snake(boolean randomAge, Location location)
    {
        super(location, Armadillo.class);
        if(randomAge) {
            setAge(randInt(MAX_AGE));
        }
        else {
            setAge(0);
        }
    }

    @Override
    public String toString() {
        return "Snake{" +
        "age=" + getAge() +
        ", alive=" + isAlive() +
        ", location=" + getLocation() +
        ", foodLevel=" +
        '}';
    }

    /**
     * Check if the snake is too old to live.
     */
    protected void checkIfTooOld()
    {
        if(getAge() > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Check if the given animal is a prey of Snake.
     * @param animal The animal to check if its a prey.
     * @return true, if the animal is a prey, otherwise false.
     */
    protected boolean isPrey(Animal animal) {
        if (animal instanceof Armadillo armadillo) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * A Snake can breed successfully if it has reached the breeding age,
     * and luck is on it's side.
     * @return true if the snake breeds successfully1, false otherwise.
     */
    protected boolean breedSuccess()
    {
        return getAge() >= BREEDING_AGE && randDouble() <= BREEDING_PROBABILITY;
    }

    /**
     * Create a new snake as offspring.
     * @param loc The location off the new offspring.
     * @return The offspring.
     */
    protected Animal offspring(Location loc) {
        Snake young = new Snake(false,loc);
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
