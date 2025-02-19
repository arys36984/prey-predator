import java.util.List;
import java.util.Random;

/**
 * A simple model of a armadillo.
 * Armadillos age, move, breed, and die.
 * 
 * @author David J. Barnes, Aryan Sanvee Vijayan and Michael Kölling
 * @version 18/02/2025
 */
public class Armadillo extends Prey
{
    // Characteristics shared by all armadillos (class variables).
    // The age at which a armadillo can start to breed.
    private static final int BREEDING_AGE = 10;
    // The age to which a armadillo can live.
    private static final int MAX_AGE = 30;
    // The likelihood of a armadillo breeding.
    private static final double BREEDING_PROBABILITY = 0.7;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 5;

    /**
     * Create a new armadillo. A armadillo may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the armadillo will have a random age.
     * @param location The location within the field.
     */
    public Armadillo(boolean randomAge, Location location)
    {
        super(location);
        if(randomAge) {
            setAge(randInt(MAX_AGE));
        }
        else {
            setAge(0);
        }
    }

    @Override
    public String toString() {
        return "Armadillo{" +
        "age=" + getAge() +
        ", alive=" + isAlive() +
        ", location=" + getLocation() +
        '}';
    }

    /**
     * Check if the armadillo is too old to live.
     */
    protected void checkIfTooOld()
    {
        if(getAge() > MAX_AGE) {
            setDead();
        }
    }

    /**
     * A armadillo can breed successfully if it has reached the breeding age,
     * and luck is on its side.
     * @return true if the armadillo breeds successfully, false otherwise.
     */
    protected boolean breedSuccess()
    {
        return getAge() >= BREEDING_AGE && randDouble() <= BREEDING_PROBABILITY;
    }

    /**
     * Create a new armadillo as offspring.
     * @param loc The location off the new offspring.
     * @return The offspring.
     */
    protected Animal offspring(Location loc) {
        Armadillo young = new Armadillo(false,loc);
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
     * Calculate if an armadillo can move at this step.
     * @param time The current day/night cycle.
     * @return true if the armadillo can move, false otherwise.
     */
    protected boolean canMove(Time time) {
        if (time == Time.DAY) {
            return true;
        }
        // If the time is night, there is a 50% chance of moving at this step.
        else if ((time == Time.NIGHT) && (randDouble() < 0.5)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Checks if a prey can eat the plant
     * depending on the type of prey.
     * @param leafCell The leaf to check.
     * @return true if the prey can eat the leaf, false if not.
     */
    protected boolean canEat(LeafCell leafCell) {
        // If the core plant which is being eaten belongs
        // to a berry shrub, the prey can eat the leaf.
        if (leafCell.parentClass() == BerryShrub.class) {
            return true;
        } else {
            return false;
        }
    }
}