package common.data;

/**
 * interface for storable object
 */
public interface Collectionable extends Comparable<Collectionable>, Validateable {

    public int getId();
    /**
     * sets id, useful for replacing object in collection
     * @param id
     */
    public void setId(int id);

    public String getName();

    /**
     * compairs two objects
     */
    public int compareTo(Collectionable labWork);

    public boolean validate();
}
