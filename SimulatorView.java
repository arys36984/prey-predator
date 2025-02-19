import java.awt.*;
import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;

/**
 * A graphical view of the simulation grid.
 * The view displays a colored rectangle for each location 
 * representing its contents. It uses a default background color.
 * Colors for each type of species can be defined using the
 * setColor method.
 * 
 * @author David J. Barnes, Aryan Sanvee Vijayan, Alexander Sukhin and Michael Kölling
 * @version 18/02/2025
 */
public class SimulatorView extends JFrame
{
    // Colors used for empty locations.
    private static final Color EMPTY_COLOR = Color.white;

    // Color used for objects that have no defined color.
    private static final Color UNKNOWN_COLOR = Color.gray;

    private final String STEP_PREFIX = "Step: ";
    private final String TIME_PREFIX = "Time: ";
    private final String WEATHER_PREFIX = "Weather: ";
    private final String POPULATION_PREFIX = "Population: ";
    private final JLabel northLabel;
    private final JLabel population;
    private final FieldView fieldView;

    // A map for storing colors for participants in the simulation
    private final Map<Class<?>, Color> colors;
    // A statistics object computing and storing simulation information
    private final FieldStats stats;

    /**
     * Create a view of the given width and height.
     * @param height The simulation's height.
     * @param width  The simulation's width.
     */
    public SimulatorView(int height, int width)
    {
        stats = new FieldStats();
        colors = new LinkedHashMap<>();
        setColor(Armadillo.class, Color.orange);
        setColor(Ocelot.class, Color.blue);
        setColor(Giraffe.class, Color.cyan);
        setColor(Snake.class, Color.red);
        setColor(Lion.class, Color.magenta);
        setColor(BerryShrub.class, Color.green);
        setColor(Tree.class, new Color(0, 153, 51));

        setTitle("Prey and Predator Simulation");
        // figure out why north label requires this first parameter...
        northLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);

        setLocation(100, 50);

        fieldView = new FieldView(height, width);

        Container contents = getContentPane();
        contents.add(northLabel, BorderLayout.NORTH);
        contents.add(fieldView, BorderLayout.CENTER);
        contents.add(population, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    /**
     * Define a color to be used for a given class of organism.
     * @param animalClass The organism's Class object.
     * @param color The color to be used for the given class.
     */
    public void setColor(Class<?> organismClass, Color color)
    {
        colors.put(organismClass, color);
    }

    /**
     * @return The color to be used for a given class of organism.
     */
    private Color getColor(Class<?> organismClass)
    {
        Color col = colors.get(organismClass);
        if(col == null) {
            // no color defined for this class
            return UNKNOWN_COLOR;
        }
        else {
            return col;
        }
    }

    /**
     * Show the current status of the field.
     * @param step Which iteration step it is.
     * @param time The current state of day/night cycle
     * @param field The field whose status is to be displayed.
     * @param weather The current state of weather.
     */
    public void showStatus(int step, Time time, Field field, Weather weather)
    {
        if(!isVisible()) {
            setVisible(true);
        }

        northLabel.setText(STEP_PREFIX + step + " " + TIME_PREFIX + time + " " + 
            WEATHER_PREFIX + weather);

        stats.reset();

        fieldView.preparePaint();

        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Animal animal = field.getAnimalAt(new Location(row, col));
                Plant plant = field.getPlantAt(new Location(row, col));
                Class<?> plantClass = null;

                if (plant != null) {

                    plantClass = plant.getClass();

                    if (plantClass == LeafCell.class) {
                        if (plant instanceof LeafCell leafCell) {
                            Class<?> parentClass = leafCell.parentClass();

                            if (parentClass == BerryShrub.class) {
                                fieldView.drawMark(col, row, Color.green);
                            } else if (parentClass == Tree.class) {
                                fieldView.drawMark(col, row, new Color(0, 153, 51));
                            }   
                        }
                    } else {
                        fieldView.drawMark(col, row, getColor(plant.getClass()));
                    }
                }

                if(animal != null) {
                    stats.incrementCount(animal.getClass());

                    if (animal.isInfected() == true) {
                        fieldView.drawMark(col, row, getColor(animal.getClass()).darker());
                    } else {
                        fieldView.drawMark(col, row, getColor(animal.getClass()));
                    }
                }

                if (plant == null && animal == null){
                    fieldView.drawMark(col, row, EMPTY_COLOR);
                }
            }
        }
        stats.countFinished();

        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        fieldView.repaint();
    }

    /**
     * Determine whether the simulation should continue to run.
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Field field)
    {
        return stats.isViable(field);
    }

    /**
     * Provide a graphical view of a rectangular field. This is 
     * a nested class (a class defined inside a class) which
     * defines a custom component for the user interface. This
     * component displays the field.
     * This is rather advanced GUI stuff - you can ignore this 
     * for your project if you like.
     */
    private class FieldView extends JPanel
    {
        private final int GRID_VIEW_SCALING_FACTOR = 6;

        private final int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Create a new FieldView component.
         */
        public FieldView(int height, int width)
        {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Tell the GUI manager how big we would like to be.
         */
        public Dimension getPreferredSize()
        {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepare for a new round of painting. Since the component
         * may be resized, compute the scaling factor again.
         */
        public void preparePaint()
        {
            if(! size.equals(getSize())) {  // if the size has changed...
                size = getSize();
                fieldImage = fieldView.createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if(xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if(yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }

        /**
         * Paint on grid location on this field in a given color.
         */
        public void drawMark(int x, int y, Color color)
        {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale-1, yScale-1);
        }

        /**
         * The field view component needs to be redisplayed. Copy the
         * internal image to screen.
         */
        public void paintComponent(Graphics g)
        {
            if(fieldImage != null) {
                Dimension currentSize = getSize();
                if(size.equals(currentSize)) {
                    g.drawImage(fieldImage, 0, 0, null);
                }
                else {
                    // Rescale the previous image.
                    g.drawImage(fieldImage, 0, 0, currentSize.width, currentSize.height, null);
                }
            }
        }
    }
}
