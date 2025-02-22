import java.util.*;

/**
 * Represent a rectangular grid of field positions.
 * Each position is able to store a pair of Animal and Plant.
 * 
 * @author David J. Barnes, Aryan Sanvee Vijayan, Alexander Sukhin and Michael Kölling
 * @version 02/02/2025
 */
public class Field
{
    // A random number generator for providing random locations.
    private static final Random rand = Randomizer.getRandom();
    // The dimensions of the field.
    private final int depth, width;
    // Each location holds a pair of Animal, Plant.
    private final Map<Location, Pair<Animal, Plant>> field = new HashMap<>();
    // The animals.
    private final List<Animal> animals = new ArrayList<>();
    // The plants.
    private final List<Plant> plants = new ArrayList<>();

    /**
     * Represent a field of the given dimensions.   
     * @param depth The depth of the field.
     * @param width The width of the field.
     */
    public Field(int depth, int width)
    {
        this.depth = depth;
        this.width = width;
    }

    /**
     * Place an animal at the given location.
     * If there is already an animal at the location it will
     * be lost.
     * @param anAnimal The animal to be placed.
     * @param location Where to place the animal.
     */
    public void placeAnimal(Animal anAnimal, Location location)
    {
        // Ensures location is not null.
        assert location != null;

        // Retrieves the pair at location if not null. If there
        // is no pair in the location, retrieves a null pair.
        Pair<Animal, Plant> existingPair = field.getOrDefault(location, new Pair<>(null, null));

        // Removes the animal from the current pair.
        if (existingPair.first() != null) {
            animals.remove(existingPair.first());
        }

        // Creates a new pair with the new animal and the existing plant.
        Pair<Animal, Plant> newPair = new Pair<>(anAnimal, existingPair.second());
        field.put(location, newPair);
        animals.add(anAnimal);
    }

    /**
     * Place a plant at the given location.
     * If there is already a plant at the location it will
     * be lost.
     * @param plant The plant to be placed.
     * @param location Where to place the plant.
     */
    public void placePlant(Plant plant, Location location)
    {
        // Ensures location is not null.
        assert location != null;

        // Retrieves the pair at location if not null. If there
        // is no pair in the location, retrieves a null pair.
        Pair<Animal, Plant> existingPair = field.getOrDefault(location, new Pair<>(null, null));

        // Removes the plant from the current pair.
        if (existingPair.second() != null) {
            plants.remove(existingPair.second());
        }

        // Creates a new pair with the existing animal and the new plant.
        Pair<Animal, Plant> newPair = new Pair<>(existingPair.first(), plant);
        field.put(location, newPair);
        plants.add(plant);
    }

    /**
     * Return the animal at the given location, if any.
     * @param location Where in the field.
     * @return The animal at the given location, or null if there is none.
     */
    public Animal getAnimalAt(Location location)
    {   
        // Returns the animal at the given location if there is any, and
        // returns null if there is none.
        return field.containsKey(location) ? field.get(location).first() : null;
    }

    /**
     * Return the plant at the given location, if any.
     * @param location Where in the field.
     * @return The plant at the given location, or null if there is none.
     */
    public Plant getPlantAt(Location location)
    {
        // Returns the plant at the given location if there is any, and
        // returns null if there is none.
        return field.containsKey(location) ? field.get(location).second() : null;
    }

    /**
     * Get a shuffled list of the free adjacent locations of animals.
     * @param location Get locations adjacent to this.
     * @return A list of free adjacent locations.
     */
    public List<Location> getFreeAdjacentLocations(Location location)
    {
        // The list of free locations to be returned.
        List<Location> free = new LinkedList<>();
        // The list of adjacent locations.
        List<Location> adjacent = getAdjacentLocations(location);
        for(Location next : adjacent) {

            Pair<Animal, Plant> existingPair = field.get(next);

            if (existingPair == null) {
                free.add(next);
            } else {
                Animal anAnimal = existingPair.first();
                // Includes null animals and dead animals to
                // free adjacent locations.
                if (anAnimal == null || !anAnimal.isAlive()) {
                    free.add(next);
                }
            }
        }
        return free;
    }

    /** 
     * Return a shuffled list of locations adjacent to the given one.
     * The list will not include the location itself.
     * All locations will lie within the grid.
     * @param location The location from which to generate adjacencies.
     * @return A list of locations adjacent to that given.
     */
    public List<Location> getAdjacentLocations(Location location)
    {
        // The list of locations to be returned.
        List<Location> locations = new ArrayList<>();
        if(location != null) {
            int row = location.row();
            int col = location.col();
            for(int roffset = -1; roffset <= 1; roffset++) {
                int nextRow = row + roffset;
                if(nextRow >= 0 && nextRow < depth) {
                    for(int coffset = -1; coffset <= 1; coffset++) {
                        int nextCol = col + coffset;
                        // Exclude invalid locations and the original location.
                        if(nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                            locations.add(new Location(nextRow, nextCol));
                        }
                    }
                }
            }

            // Shuffle the list. Several other methods rely on the list
            // being in a random order.
            Collections.shuffle(locations, rand);
        }
        return locations;
    }

    /** 
     * Return a shuffled list of locations nearby (within 2 blocks)
     * to the given one.
     * The list will not include the location itself.
     * All locations will lie within the grid.
     * @param location The location from which to generate adjacencies.
     * @return A list of locations adjacent to that given.
     */
    public List<Location> getNearbyLocations(Location location)
    {
        // The list of locations to be returned.
        List<Location> locations = new ArrayList<>();
        if(location != null) {
            int row = location.row();
            int col = location.col();
            for(int roffset = -2; roffset <= 2; roffset++) {
                int nextRow = row + roffset;
                if(nextRow >= 0 && nextRow < depth) {
                    for(int coffset = -2; coffset <= 2; coffset++) {
                        int nextCol = col + coffset;
                        // Exclude invalid locations and the original location.
                        if(nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                            locations.add(new Location(nextRow, nextCol));
                        }
                    }
                }
            }

            // Shuffle the list. Several other methods rely on the list
            // being in a random order.
            Collections.shuffle(locations, rand);
        }
        return locations;
    }

    /**
     * Print out the number of animals and edible leaves in the field.
     * Plant cores are not edible so ignore those.
     */
    public void fieldStats()
    {
        // A hashmap of classes of animals and integers representing the count.
        HashMap<Class<?>, Integer> counts = new HashMap<>();
        for(Pair<Animal, Plant> pair : field.values()) {
            Animal anAnimal = pair.first();

            if (anAnimal != null && anAnimal.isAlive()) {
                Class<?> animalClass = anAnimal.getClass();
                // Puts the class and integer 1 into hashmap if
                // not in the hashmap.
                if (counts.get(animalClass) == null) {
                    counts.put(animalClass, 1);
                }
                // Increments the value of the corresponding class
                // for every animal.
                else {
                    int newCount = counts.get(animalClass) + 1;
                    counts.put(animalClass, newCount);
                }
            }
        }

        Set<Class<?>> animalClasses = counts.keySet();
        String output = "";
        for (Class<?> animalClass : animalClasses) {
            output += animalClass.getSimpleName() + ": " + counts.get(animalClass) + " ";
        }
        System.out.println(output + "Infected: " + infectedCount()
            + " " + "Leaves: " + ediblePlantCount());
    }

    /**
     * Generate a count of all the infected animals in the field.
     * @return The number of infected animals.
     */
    public int infectedCount() {
        int numInfected = 0;
        for(Pair<Animal, Plant> pair : field.values()) {

            Animal anAnimal = pair.first();

            // Increments count by one for every infected animal.
            if (anAnimal != null && anAnimal.isInfected()) {
                numInfected++;
            }
        }
        return numInfected;
    }

    /**
     * Generate a count of all the leaves (edible) in the field.
     * @return The number of leaves
     */
    public int ediblePlantCount() {
        int numPlants = 0;
        for(Pair<Animal, Plant> pair : field.values()) {

            Plant plant = pair.second();

            // Increments count by one for every leaf.
            if (plant != null && plant.getClass() == LeafCell.class) {
                numPlants++;
            }
        }
        return numPlants;
    }

    /**
     * Empty the field.
     */
    public void clear()
    {
        field.clear();
    }

    /**
     * Return whether there is at least one prey and one predator.
     * There will always be plant cores in the field, only number of edible leaves changes.
     * @return true if there is at least one prey and one predator.
     */
    public boolean isViable()
    {
        boolean preyFound = false;
        boolean predatorFound = false;
        Iterator<Animal> it = animals.iterator();
        while(it.hasNext() && ! (preyFound && predatorFound)) {
            Animal anAnimal = it.next();
            if(anAnimal instanceof Prey prey) {
                if(prey.isAlive()) {
                    // if a prey is found, set preyFound to true.
                    preyFound = true;
                }
            }
            else if(anAnimal instanceof Predator predator) {
                if(predator.isAlive()) {
                    // if a predator is found, set predatorFound to true.
                    predatorFound = true;
                }
            }
        }
        return preyFound && predatorFound;
    }

    /**
     * Get the list of animals.
     * @return The current list of animals.
     */
    public List<Animal> getAnimals()
    {
        return animals;
    }

    /**
     * Get the list of plants.
     * @return The current list of plants.
     */
    public List<Plant> getPlants()
    {
        return plants;
    }

    /**
     * Return the depth of the field.
     * @return The depth of the field.
     */
    public int getDepth()
    {   
        return depth;
    }

    /**
     * Return the width of the field.
     * @return The width of the field.
     */
    public int getWidth()
    {
        return width;
    }
}
