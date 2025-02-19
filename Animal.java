import java.util.List;
import java.util.Random;

/**
 * Common elements of predators and preys.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.0
 */
public abstract class Animal extends Organism
{
    // Whether the animal is male or female.
    // True for female, false for male.
    private boolean female;
    // Whether the animal is infected
    private boolean infected;
    // Randomizer to generate randomness in simulation.
    private static final Random rand = Randomizer.getRandom();
    // Animal's age
    private int age;

    /**
     * Constructor for objects of class Animal.
     * @param location The animal's location.
     */
    public Animal(Location location)
    {
        super(location);
        Random random = new Random();
        female = random.nextBoolean();
        // Randomly infects animals with a 0.5% chance.
        if (rand.nextDouble() < 0.005) {
            infected = true;
        }
        else {
            infected = false;
        }
    }

    /**
     * Act.
     * @param currentField The current state of the field.
     * @param nextFieldState The new state being built.
     * @param time The current day/night cycle.
     * @param weather The current weather conditions.
    */
    abstract public void act(Field currentField, Field nextFieldState, Time time, Weather weather);

    /**
     * Check whether the animal will act at this step, considering weather conditions.
     * @param weather The current weather conditions
     * @return true if the animal acts, otherwise false.
     */
    public boolean canAct(Weather weather) {
        // Adjusts the probability of animals hunting and moving
        // based on the weather.
        if (weather == Weather.CLEAR) {
            return true;
        }
        else if (weather == Weather.RAIN && rand.nextDouble() < 0.6) {
            return true;
        }
        else if (weather == Weather.CLOUDY && rand.nextDouble() < 0.8) {
            return true;
        }
        else if (weather == Weather.STORM && rand.nextDouble() < 0.4) {
            return true;
        }
        return false;
    }

    /**
     * Checks if there is a compatible mate in the adjacent cells.
     * @param field The field to check for adjacent animals.
     * @return true if a compatible mate is found, false otherwise
     */
    public boolean hasCompatibleMate(Field field) {
        List<Location> adjacentLocations = field.getAdjacentLocations(getLocation());
        for (Location loc : adjacentLocations) {
            Animal animal = field.getAnimalAt(loc);
            // Checks every adjacent location, returning true if a location
            // contains a animal which is the same species as the current
            // animal and is the opposite gender to the current animal.
            if ((animal != null) && (animal.getClass() == this.getClass()) 
            && (animal.isFemale() != this.isFemale())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether this animal is infected.
     * @param field The current state of the field.
     */
    public void checkIfInfected(Field field) {
        List<Location> adjacentLocations = field.getAdjacentLocations(getLocation());
        for (Location loc : adjacentLocations) {
            Animal animal = field.getAnimalAt(loc);
            if (animal != null && animal.isInfected()) {
                infected= true;
            }
        }
    }

    /**
     * Check whether the animal is infected.
     * @return true if the animal is infected, false otherwise.
     */
    public boolean isInfected() {
        return infected;
    }

    /**
     * Check whether the animal is female or not.
     * @return true if the animal is female.
     */
    public boolean isFemale()
    {
        return female;
    }

    /**
     * Generate a random integer less than a given integer.
     * @param max The maximum below which get the random integer from.
     * @return The random integer.
     */
    public int randInt(int max)
    {
        return rand.nextInt(max);
    }

    /**
     * Generate a random double between 0 and 1.
     * @return The random double.
     */
    public double randDouble()
    {
        return rand.nextDouble();
    }

    /**
     * Increase the age. This could result in the animal's death.
     */
    public void incrementAge()
    {
        age++;
        checkIfTooOld(); // Check if the animal is too old to be alive.
    }

    /**
     * Get the age of the animal.
     * @return age Age of the animal
     */
    public int getAge()
    {
        return age;
    }

    /**
     * Set the age of the animal.
     * @param newAge The age to set the animal to.
     * @return age Age of the animal
     */
    public void setAge(int newAge)
    {
        age = newAge;
    }

    /**
     *  Check if the animal is too old to live.
     */
    abstract protected void checkIfTooOld();
}
