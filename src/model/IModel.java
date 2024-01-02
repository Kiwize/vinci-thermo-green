package model;

/**
 * <p>
 * Interface for model classes. Specifies methods to redefine for database communication.
 * </p>
 * 
 * @author Thomas PRADEAU
 * @version 3.0.0
 */
public interface IModel {

	/**
	 * <p>
	 * <b>Updates</b> object in database.
	 * </p>
	 * @return true if succeeded, false otherwise.
	 * 
	 * @author Thomas PRADEAU
	 * @version 3.0.0
	 */
	boolean save();

	/**
	 * <p>
	 * <b>Inserts</b> object inside database.
	 * </p>
	 * 
	 * @return true if succeeded, false otherwise.
	 * 
	 * @author Thomas PRADEAU
	 * @version 3.0.0
	 */
	boolean insert();

}
