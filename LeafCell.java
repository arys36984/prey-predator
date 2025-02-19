
/**
 * This class represents the leaf cells extending
 * from each core plant, acting as a food source
 * for prey.
 *
 * @author Alexander Sukhin
 * @version 13/02/2025
 */
public class LeafCell extends Plant
{
    // Represents the core plant which the leaf
    // cell extends from.
    private CorePlant parent;

    /**
     * Creates a leaf cell. A leaf cell is extended from a core plant.
     * 
     * @param location The location within the field.
     * @param corePlant The core plant which extends the leaf cell.
     */
    public LeafCell(Location location, CorePlant corePlant)
    {
        super(location);
        this.parent = corePlant;
    }

    /**
     * This is what the leaf cells do most of the time. It remains stationary
     * until eaten from a prey.
     * @param currentField The field currently occupied.
     * @param nextFieldState The updated field.
     */
    public void act(Field currentField, Field nextFieldState, Time time)
    {
        if (isAlive()) {
            nextFieldState.placePlant(this, getLocation());
        }
    }

    /**
     * Removes a leaf cell plant from the the field and
     * the list of leaf cells within the core plant.
     */
    public void removeLeaf()
    {
        setDead();
        parent.removeLeafCell(this);
    }

    /**
     * Returns the class of the parent plant.
     * @return the class of the parent plant.
     */
    public Class<?> parentClass()
    {
        return parent.getClass();
    }
}
