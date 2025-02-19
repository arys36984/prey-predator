
/**
 * This class represents the centre of each tree, and is able
 * to evolve to a 3x3 area.
 * 
 * @author Alexander Sukhin
 * @version 19/02/2025
 */
public class Tree extends CorePlant
{
    // The max phase of the plant.
    private static final int MAX_PHASE = 3;

    /**
     * Creates a tree.
     * @param location The location within the field.
     */
    public Tree(Location location)
    {
        super(location);
    }

    /**
     * Checks whether the final evolution of the tree is 3x3.
     * @param phase The phase of the plant.
     * @return true if phase of the plant is less than or equal to the max phase of the plant.
     */
    public boolean validPhase(int phase)
    {
        return (phase <= MAX_PHASE);
    }
}
