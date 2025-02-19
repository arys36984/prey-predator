
/**
 * This class represents the centre of each berry shrub, and is able
 * to evolve to a 2x2 area.
 * 
 * @author Alexander Sukhin
 * @version 19/02/2025
 */
public class BerryShrub extends CorePlant
{
    // The max phase of the plant.
    private static final int MAX_PHASE = 2;

    /**
     * Creates a berry shrub.
     * @param location The location within the field.
     */
    public BerryShrub(Location location)
    {
        super(location);
    }

    /**
     * Checks whether the final evolution of the berry shrub is 2x2.
     * @param phase The phase of the plant.
     * @return true if phase of the plant is less than or equal to the max phase of the plant.
     */
    public boolean validPhase(int phase)
    {
        return (phase <= MAX_PHASE);
    }
}
