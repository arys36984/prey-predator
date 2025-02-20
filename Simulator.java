import java.util.*;

/**
 * A simple predator-prey simulator, based on a rectangular field containing 
 * prey and predators.
 *  
 * @author David J. Barnes, Aryan Sanvee Vijayan, Alexander Sukhin and Michael Kölling
 * @version 19/02/2025
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 150;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 100;
    // The probability that a ocelot will be created in any given grid position.
    private static final double OCELOT_CREATION_PROBABILITY = 0.03;
    // The probability that a armadillo will be created in any given position.
    private static final double ARMADILLO_CREATION_PROBABILITY = 0.08;    
    // The probability that a giraffe will be created in any given position.
    private static final double GIRAFFE_CREATION_PROBABILITY = 0.03;
    // The probability that a snake will be created in any given position.
    private static final double SNAKE_CREATION_PROBABILITY = 0.03;
    // The probability that a lion will be created in any given position.
    private static final double LION_CREATION_PROBABILITY = 0.02;
    // The probability that a berry shrub will be created in any given position
    private static final double BERRYSHRUB_CREATION_PROBABILITY = 0.02;
    // The probability that a tree will be created in any given position
    private static final double TREE_CREATION_PROBABILITY = 0.02;
    // The number of steps in one day/night cycle.
    private static final int DAY_STEPS = 5;
    // The number of steps in one weather cycle.
    private static final int WEATHER_STEPS = 10;
    // The current state of day/night.
    private Time time;
    // The current state of weather.
    private Weather weather;

    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private final SimulatorView view;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Construct a simulation field with specific size.
     * @param depth The simulation's depth.
     * @param width The simulation's width.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be >= zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        field = new Field(depth, width);
        view = new SimulatorView(depth, width);
        time = Time.DAY;
        weather = Weather.CLEAR;

        reset();
    }

    /**
     * Run the simulation from its current state for a reasonably long 
     * period (700 steps).
     */
    public void runLongSimulation()
    {
        simulate(700);
    }

    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each ocelot and armadillo.
     */
    public void simulateOneStep()
    {
        step++;
        // Use a separate Field to store the starting state of
        // the next step.
        Field nextFieldState = new Field(field.getDepth(), field.getWidth());

        List<Animal> animals = field.getAnimals();
        for (Animal anAnimal : animals) {
            anAnimal.act(field, nextFieldState, time, weather);
        }

        List<Plant> plants = field.getPlants();
        for (Plant plant : plants) {
            plant.act(field, nextFieldState, time);
        }

        // Replace the old state with the new one.
        field = nextFieldState;

        // Changes the day/time cycle every 10 steps.
        changeTime();

        // Changes the weather cycle every 5 steps.
        changeWeather();

        reportStats();
        view.showStatus(step, time, field, weather);
    }

    /**
     * Run the simulation for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        reportStats();
        for(int n = 1; n <= numSteps && field.isViable(); n++) {
            simulateOneStep();
            delay(50);         // adjust this to change execution speed
        }
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        time = Time.DAY;
        step = 0;
        populate();
        view.showStatus(step, time, field, weather);
    }

    /**
     * Randomly populate the field with prey, predators and plants.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= GIRAFFE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Giraffe giraffe = new Giraffe(true, location);
                    field.placeAnimal(giraffe, location);
                }
                else if(rand.nextDouble() <= LION_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Lion lion = new Lion(true, location);
                    field.placeAnimal(lion, location);
                }
                else if(rand.nextDouble() <= SNAKE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Snake snake = new Snake(true, location);
                    field.placeAnimal(snake, location);
                }
                else if(rand.nextDouble() <= OCELOT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Ocelot ocelot = new Ocelot(true, location);
                    field.placeAnimal(ocelot, location);
                }
                else if(rand.nextDouble() <= ARMADILLO_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Armadillo armadillo = new Armadillo(true, location);
                    field.placeAnimal(armadillo, location);
                }
                else if(rand.nextDouble() <= BERRYSHRUB_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    BerryShrub berryShrub = new BerryShrub(location);
                    field.placePlant(berryShrub, location);
                }
                else if(rand.nextDouble() <= TREE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Tree tree = new Tree(location);
                    field.placePlant(tree, location);
                }
                // else leave the location empty.
            }
        }
    }

    /**
     * Report on the number of each type of animal in the field.
     */
    public void reportStats()
    {
        //System.out.print("Step: " + step + " ");
        field.fieldStats();
    }

    /**
     * Pause for a given time.
     * @param milliseconds The time to pause for, in milliseconds
     */ 
    private void delay(int milliseconds)
    {
        try {
            Thread.sleep(milliseconds);
        }
        catch(InterruptedException e) {
            // ignore
        }
    }

    /**
     * Every step cycle, checks whether we are on the 50th multiple step.
     * If we are, changes day/night cycle via flag.
     */
    private void changeTime()
    {
        if (step % DAY_STEPS == 0) {
            time = (time == Time.DAY) ? Time.NIGHT : Time.DAY;
        }
    }

    /**
     * Every step cycle, checks whether we are on the 10th multiple step.
     * If we are, changes weather cycle via flag.
     */
    private void changeWeather()
    {
        if (step % WEATHER_STEPS == 0) {
            weather = Weather.randomWeather();
        }
    }
}
