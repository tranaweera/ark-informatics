/**
 * 
 */
package au.org.theark.gdmi.util;

import au.org.theark.gdmi.exception.StorageIOException;
import au.org.theark.gdmi.model.entity.MarkerGroup;

/**
 * IMapStorage is an interface that should be implemented by a class to  
 * store map data provided by the GWASImport processMap(..) method.
 * The motivation is to separate GWASImport from the back-end storage method.
 * 
 * @author elam
 *
 */
public interface IMapStorage {

	/**
	 * Called each time a new set of marker data is ready to be accepted
	 */
	void init() throws StorageIOException;

	//String getMarkerName();
	/**
	 * Called to accept the marker name
	 */
	void setMarkerName(String mkrName);
	
	//long getChromoNum();
	/**
	 * Called to accept the associated chromosome number
	 */
	void setChromosome(String chromoNum);
	
	//long getGeneDist();
	/**
	 * Called to accept the associated genetic distance (morgans)
	 */
	void setGeneDist(long geneDist);
	
	//long getBpPos();
	/**
	 * Called to accept the associated base-pair position (bp units)
	 */
	void setBpPos(long bpPos);
	
	/**
	 * Called when all data is provided and ready to be committed to storage.
	 * If the commit fails, then the an exception can be thrown.
	 */
	void commit() throws StorageIOException;
	
}
