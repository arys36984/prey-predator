import java.util.List;
import java.util.Iterator;
import java.util.Random;
/**
 * A general model of a prey in the simulation.
 *
 * @author Aryan Sanvee Vijayan
 * @version 18/02/2025
 */
public abstract class Prey extends Animal
{
    // Represents whether the prey is full or hungry.
    private boolean isFull;
    // Tracks how long the prey has been hungry or full (in steps).
    private int hungerTimer;
    // The number of steps before the prey goes hungry.
    private static final int FULL_STEPS = 10;
    // The number of steps before the prey dies of hunger.
    private static final int HUNGRY_STEPS = 20;

    /**
     * Constructor for objects of class Prey
     * @param location The prey's location.
     */
    public Prey(Location location)
    {
        // initialise instance variables
        super(location);
        isFull = true;
        hungerTimer = 0;
    }

    /**
     * This is what the prey does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param currentField The field occupied.
     * @param nextFieldState The updated field.
     * @param time The current day/night cycle.
     * @param weather The current weather conditions.
     */
    public void act(Field currentField, Field nextFieldState, Time time, Weather weather)
    {
        incrementAge();
        incrementHunger();  
        if(isAlive()) {
            List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(getLocation());

            // Check if the prey is infected in the next field state.
            checkIfInfected(nextFieldState); 

            if (isInfected() && randDouble() < 0.5) {
                // 50% chance of dying due to infection.
                setDead();
            }
            // if prey can act considering weather circumstances.
            else if (canAct(weather)) {
                if(!freeLocations.isEmpty()) {
                    // try to breed.
                    giveBirth(nextFieldState, freeLocations);
                }

                Location nextLocation = null;
                // Move towards a sourch of food if found.
                if (!isFull) {
                    Location plantLocation = findFood(currentField, time);
                    if (plantLocation != null) {
                        nextLocation = plantLocation;
                    }
                }

                // Try to move into a free location.
                if(nextLocation == null && !freeLocations.isEmpty() && canMove(time)) {
                    nextLocation = freeLocations.get(0);
                } 
                else if (!freeLocations.isEmpty()) {
                    nextLocation = getLocation();
                } 

                if (nextLocation != null) {
                    setLocation(nextLocation);
                    nextFieldState.placeAnimal(this, nextLocation);
                } 
                else {
                    // Overcrowding.
                    setDead();
                }
            }
            else {
                if (getLocation() != null) {
                    // let the prey sleep in the same place.
                    nextFieldState.placeAnimal(this, getLocation());
                }
            }
        }
    }

    /**
     * Make this prey more hungry. This could result in the prey's death.
     * After eating, the prey will be full for FULL_STEPS steps.
     */
    private void incrementHunger()
    {
        if (isFull) {
            hungerTimer++;
            // If the prey is full and the hunger timer is greater
            // than the FULL_STEPS variable, sets the prey to hungry
            // state and resets hunger timer back to 0.
            if (hungerTimer >= FULL_STEPS) {
                isFull = false;
                hungerTimer = 0;
            }
        } else {
            hungerTimer++;
            // If the prey is hungry and the hunger timer is greater
            // than the HUNGRY_STEPS variable, sets the prey to dead.
            if (hungerTimer >= HUNGRY_STEPS) {
                setDead();
                hungerTimer = 0;
            }
        }
    }

    /**
     * Look for plants adjacent to the current location.
     * Only the first live plant is eaten.
     * @param field The field currently occupied.
     * @param time The current day/night cycle.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood(Field field, Time time)
    {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        Location foodLocation = null;
        while(foodLocation == null && it.hasNext()) {
            Location loc = it.next();
            Plant plant = field.getPlantAt(loc);
            if (plant instanceof LeafCell leafCell) {
                // Checks whether the specific prey hunts the plant found.
                if (canEat(leafCell)) {
                    leafCell.removeLeaf();
                    hungerTimer = 0;
                    isFull = true;
                    foodLocation = loc;
                }
            }
        }
        return foodLocation;
    }

    /**
     * Check whether this prey is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param nextFieldState The updated field.
     * @param freeLocations The locations that are free in the current field.
     */
    protected void giveBirth(Field nextFieldState, List<Location> freeLocations)
    {
        // Places new offspring into adjacent locations.
        if (hasCompatibleMate(nextFieldState)) {    
            int births = breed();
            if(births > 0) {
                for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
                    Location loc = freeLocations.remove(0);
                    Animal young = offspring(loc);
                    nextFieldState.placeAnimal(young, loc);
                }
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
     * Create a new prey as offspring.
     * @param loc The location off the new offspring.
     * @return The offspring.
     */
    abstract protected Animal offspring(Location loc);

    /**
     * A prey can breed successfully if it has reached the breeding age,
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
     * Calculate if a prey can move at this step.
     * @param time The time of day/night.
     * @return true if the prey can move, false otherwise.
     */
    abstract protected boolean canMove(Time time);

    /**
     * Checks if a prey can eat the plant
     * depending on the type of prey.
     * @param leafCell The leaf to check.
     * @return true if the prey can eat the leaf, false if not.
     */
    abstract protected boolean canEat(LeafCell leafCell);

    /**
     * Check if the prey is too old to live.
     */
    abstract protected void checkIfTooOld();
}
