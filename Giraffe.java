import java.util.Random;
import java.util.List;

/**
 * A simple model of a giraffe.
 * Giraffe age, move, breed, and die..
 *
 * @Aryan Sanvee Vijayan
 * @version 18/02/2025
 */
public class Giraffe extends Prey
{
    // Characteristics shared by all giraffes (class variables).
    // The age at which a giraffe can start to breed.
    private static final int BREEDING_AGE = 15;
    // The age to which a giraffe can live.
    private static final int MAX_AGE = 175;
    // The likelihood of a giraffe breeding.
    private static final double BREEDING_PROBABILITY = 0.7;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;

    /**
     * Create a new giraffe. A giraffe may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the giraffe will have a random age.
     * @param location The location within the field.
     */
    public Giraffe(boolean randomAge, Location location)
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
        return "Giraffe{" +
        "age=" + getAge() +
        ", alive=" + isAlive() +
        ", location=" + getLocation() +
        '}';
    }

    /**
     * Check if the giraffe is too old to live.
     */
    protected void checkIfTooOld()
    {
        if(getAge() > MAX_AGE) {
            setDead();
        }
    }

    /**
     * A giraffe can breed successfully if it has reached the breeding age,
     * and luck is on it's side.
     * @return true if the giraffe breeds successfully, false otherwise.
     */
    protected boolean breedSuccess()
    {
        return getAge() >= BREEDING_AGE && randDouble() <= BREEDING_PROBABILITY;
    }

    /**
     * Create a new giraffe as offspring.
     * @param loc The location off the new offspring.
     * @return The offspring.
     */
    protected Animal offspring(Location loc) {
        Giraffe young = new Giraffe(false,loc);
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
     * Calculate if a giraffe can move at this step.
     * @param time The time of day/night.
     * @return true if the giraffe can move, false otherwise.
     */
    protected boolean canMove(Time time) {
        if (time == Time.DAY) {
            return true;
        }
        else if ((time == Time.NIGHT) && (randDouble() < 0.6)) {
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
        if (leafCell.parentClass() == Tree.class) {
            return true;
        } else {
            return false;
        }
    }
}
