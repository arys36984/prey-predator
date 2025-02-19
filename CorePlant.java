import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the centre of each plant,
 * handling the logic between growing through a list
 * of leaf cells.   
 *
 * @author Alexander Sukhin
 * @version 11/02/2025
 */
public abstract class CorePlant extends Plant
{

    // The current growth state of the plant.
    private int growthState;
    // The current phase of the plant.
    private int phase;
    // The leaf cells extending from the plant.
    private List<LeafCell> leafCells;
    // The ages at which the plant evolves into different phases.
    private static final int PHASE_2_AGE = 25;
    private static final int PHASE_3_AGE = 50;

    /**
     * Creates a core plant. A core plant represents the centre
     * of each plant, and handles the logic of growing plants
     * via a list of leaf cells.
     * 
     * @param location The location within the field.
     */
    public CorePlant(Location location)
    {
        super(location);
        growthState = 0;
        leafCells = new ArrayList<>();
        phase = 1;
    }

    /** 
     * This is what the plant does most of the time. It increments
     * the age and goes to next phase when plant is old enough.
     * @param currentField The field currently occupied.
     * @param nextFieldState The updated field.
     */
    public void act(Field currentField, Field nextFieldState, Time time)
    {
        if (time == Time.DAY) {
            // We reset the plant whenever there are no more
            // leaf cells, i.e. prey ate the entire plant.
            if (growthState >= PHASE_2_AGE && leafCells.isEmpty()) {
                resetPlant();
            }

            grow();

            if (growthState == PHASE_2_AGE && validPhase(2)) {
                changePhase(2);
                handleGrowth(currentField, nextFieldState);
            } else if (growthState == PHASE_3_AGE && validPhase(3)) {
                changePhase(3);
                handleGrowth(currentField, nextFieldState);
            }
        }

        nextFieldState.placePlant(this, getLocation());
    }

    /**
     * This method resets the plant back to the first phase
     * and resets the age of the plant, allowing us to regrow.
     */
    private void resetPlant()
    {
        growthState = 0;
        phase = 1;
    }

    /** 
     * This method handles the growth of the plant when the plant
     * goes to the next phase. It updates the plant to a 2x2 area
     * or a 3x3 area depending on the current phase.
     * @param currentField The field currently occupied.
     * @param nextFieldState The updated field.
     */
    private void handleGrowth(Field currentField, Field nextFieldState)
    {
        int row = getLocation().row();
        int col = getLocation().col();
        int width = currentField.getWidth();
        int depth = currentField.getDepth();

        int minOffSet = (phase == 2) ? 0 : -1;

        if (phase == 3) leafCells.clear();

        for(int roffset = minOffSet; roffset <= 1; roffset++) {
            for(int coffset = minOffSet; coffset <= 1; coffset++) {
                int nextRow = row + roffset;
                int nextCol = col + coffset;

                if (nextRow >= 0 && nextRow < depth && nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                    Location newLocation = new Location(nextRow, nextCol);
                    addLeafCell(newLocation, nextFieldState);
                }
            }
        }
    }

    /**
     * Removes a leaf cell plant from the list containing
     * all leaf cells within the plant.
     */
    public void removeLeafCell(LeafCell leafCell)
    {
        leafCells.remove(leafCell);
    }

    /**
     * Increase the age.
     */
    private void grow()
    {
        growthState++;
    }

    /**
     * Changes the phase of the plant.
     * @param phase The phase which the plant must be updated to.
     */
    private void changePhase(int phase)
    {
        if (phase == 2 || phase == 3) {
            this.phase = phase;
        } else {
            System.out.println("Not possible!");
        }
    }

    /**
     * Adds a new leaf cell to the list of leaf cells and places it in the next field state.
     * @param location The location where the new leaf cell will be placed.
     * @param nextFieldState The updated field.
     */
    private void addLeafCell(Location location, Field nextFieldState) {
        LeafCell newLeafCell = new LeafCell(location, this);
        nextFieldState.placePlant(newLeafCell, location);
        leafCells.add(newLeafCell);
    }

    /**
     * Checks whether the final evolution of the plant is 2x2 or 3x3.
     * @param phase The phase of the plant.
     * @return true if phase of the plant is less than or equal to the max phase of the plant.
     */
    abstract public boolean validPhase(int phase);

}
