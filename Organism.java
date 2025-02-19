
/**
 * Common attributes of animals and plants.
 *
 * @author Alexander Sukhin
 * @version 07/02/2025
 */
public abstract class Organism
{   
    // Whether the organism is alive or not.
    private boolean alive;
    // The organism's position.
    private Location location;

    /**
     * Constructor for objects of class Organism
     */
    public Organism(Location location)
    {
        this.alive = true;
        this.location = location;
    }

    /**
     * Check whether the organism is alive or not.
     * @return true if the organism is still alive.
     */
    public boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the organism is no longer alive.   
     */
    protected void setDead()
    {
        alive = false;
        location = null;
    }

    /**
     * Return the organism's location.
     * @return The organism's location.
     */
    public Location getLocation()
    {
        return location;
    }

    /**
     * Set the organism's location.
     * @param location The new location.
     */
    protected void setLocation(Location location)
    {
        this.location = location;
    }
}
