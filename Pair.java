
/**
 * Represents a pair of Animal and Plant.
 *
 * @author Alexander Sukhin
 * @version 19/02/2025
 */
class Pair<F, S>
{
    // Represents the first element of the pair.
    private final F first;

    // Represents the second element of the pair.
    private final S second;

    /**
     * Creates a pair of Animal and Plant to be
     * represented within the field.
     * 
     * @param first The first element within the pair.
     * @param second The second element within the pair.
     */
    public Pair(F first, S second)
    {
        this.first = first;
        this.second = second;
    }

    /**
     * @return the first element within the pair
     */
    public F first()
    {
        return first;
    }

    /**
     * @return the second element within the pair
     */
    public S second()
    {
        return second;
    }
}
