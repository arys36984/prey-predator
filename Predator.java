import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A general model of a predator in the simulation.
 *
 * @author Aryan Sanvee Vijayan
 * @version 18/02/2025
 */
public abstract class Predator extends Animal
{
    // Represents whether the predator is full or hungry.
    private boolean isFull;
    // Tracks how long the predator has been hungry or full (in steps).
    private int hungerTimer;
    // The number of steps before the predator goes hungry.
    private int FULL_STEPS;
    // The number of steps before the predator dies of hunger.
    private int HUNGRY_STEPS;

    /**
     * Create a predator. A predator can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param prey The class of the prey of the predator.
     * @param location The location within the field.
     */
    public Predator(Location location, Class<?> prey)
    {
        super(location);

        isFull = false;
        hungerTimer = 0;

        if (prey == Armadillo.class) {
            FULL_STEPS = 5;
            HUNGRY_STEPS = randInt(10);
        }
        else if (prey == Giraffe.class) {
            FULL_STEPS = 5;
            HUNGRY_STEPS = randInt(50);
        }
    }

    /**
     * This is what the predator does most of the time: it hunts for
     * armadillos. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param currentField The field currently occupied.
     * @param nextFieldState The updated field.
     */
    public void act(Field currentField, Field nextFieldState, Time time, Weather weather)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            List<Location> freeLocations =
                nextFieldState.getFreeAdjacentLocations(getLocation());

            // Check if the predator is infected in the next field state.
            checkIfInfected(nextFieldState); 

            if (isInfected() && randDouble() < 0.5) {
                // 50% chance of dying due to infection.
                setDead();
            }
            // if predator can act considering weather circumstances.
            else if (canAct(weather)) {
                if(! freeLocations.isEmpty()) {
                    // Try to breed.
                    giveBirth(nextFieldState, freeLocations);
                }
                // Move towards a source of food if found.
                Location nextLocation = findFood(currentField, time);
                if(nextLocation == null && ! freeLocations.isEmpty()) {
                    // No food found - try to move to a free location.
                    nextLocation = freeLocations.remove(0);
                }
                // See if it was possible to move.`
                if(nextLocation != null) {
                    setLocation(nextLocation);
                    nextFieldState.placeAnimal(this, nextLocation);
                }
                else {
                    // Overcrowding.
                    setDead();
                }
            }
            else {
                // let the predator stay in the same place.
                nextFieldState.placeAnimal(this, getLocation());
            }
        }
    }

    /**
     * Make this predator more hungry. This could result in the predator's death.
     */
    private void incrementHunger()
    {
        if (isFull) {
            hungerTimer++;
            if (hungerTimer >= FULL_STEPS) {
                isFull = false;
                hungerTimer = 0;
            }
        } else {
            hungerTimer++;
            if (hungerTimer >= HUNGRY_STEPS) {
                setDead();
                hungerTimer = 0;
            }
        }
    }

    /**
     * Look for prey adjacent to the current location.
     * Only the first live prey is eaten.
     * @param field The field currently occupied.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood(Field field, Time time)
    {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        Location foodLocation = null;
        while(foodLocation == null && it.hasNext()) {
            Location loc = it.next();
            Animal animal = field.getAnimalAt(loc);
            if(isPrey(animal) && huntSuccess(time)) {
                if(animal instanceof Armadillo armadillo) {
                    armadillo.setDead();
                    hungerTimer = 0;
                    isFull = true;
                    foodLocation = loc;
                }
                else if(animal instanceof Giraffe giraffe) {
                    giraffe.setDead();
                    hungerTimer = 0;
                    isFull = true;
                    foodLocation = loc;
                }
            }
        }
        return foodLocation;
    }

    /**
     * Check whether this predator is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param freeLocations The locations that are free in the current field.
     */
    protected void giveBirth(Field nextFieldState, List<Location> freeLocations)
    {
        // New armadillos are born into adjacent locations.
        // Get a list of adjacent free locations.
        int births = breed();
        if(births > 0) {
            for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Animal young = offspring(loc);
                nextFieldState.placeAnimal(young, loc);
            }
        }
    }

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    protected int breed()
    {
        int births;
        if(breedSuccess()) {
            births = birthNumber();
        }
        else {
            births = 0;
        }
        return births;
    }

    /**
     * Check if the given animal is a prey of the predator.
     * @param animal The animal to check if its a prey.
     * @return true, if the animal is a prey, otherwise false.
     */
    abstract protected boolean isPrey(Animal animal);

    /**
     * Create a new predator as offspring.
     * @param loc The location off the new offspring.
     * @return The offspring.
     */
    abstract protected Animal offspring(Location loc);

    /**
     * A predator can breed successfully if it has reached the breeding age,
     * and luck is on its side.
     * @return true if the prey breeds successfully, false otherwise.
     */
    abstract protected boolean breedSuccess();

    /**
     * Generate number of offspring if breeding is successful.
     * @return Number of offspring.
     */
    abstract protected int birthNumber();

    /**
     * Calculate whether a hunt is successful.
     * @param time The time of day/night.
     * @return true if the hunt is successful, false otherwise.
     */
    abstract protected boolean huntSuccess(Time time);

    /**
     *  Check if the predator is too old to live.
     */
    abstract protected void checkIfTooOld();
}
