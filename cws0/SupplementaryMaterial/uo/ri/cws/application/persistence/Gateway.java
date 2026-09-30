package uo.ri.cws.application.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import uo.ri.util.exception.PersistenceException;

/**
 *	Common interface for a gateway that provides common operations for any gateway.
 *  That is, CRUD operations: add, remove, update, find by id and find all.
 * @param <T>
 */
public interface Gateway<T extends Gateway.BaseRecord> {
	
	/**
	 * Adds a new item to the table 
	 * @param new item 
	 */
	void add(T t) throws PersistenceException;
	
	/**
	 * Removes an object from the table
	 * @param key to delete
	 */
	void remove(String id) throws PersistenceException;
	
	/**
	 * Updates a row
	 * @param new data to overwrite old one
	 */
	void update(T t) throws PersistenceException;
	
	/**
	 * Finds a row in the table
	 * @param record's primary key to retrieve
	 * @return dto from that record, probably null
	 */
	Optional<T> findById(String id) throws PersistenceException;
	
	/**
	 * Retrieves all data in a table
	 */
	List<T> findAll() throws PersistenceException;

	public class BaseRecord {
		
		public String id = UUID.randomUUID().toString();
	    public String entityState = "ENABLED"; // ENABLED, DISABLED
	    public long version = 1;
	    public LocalDateTime createdAt = LocalDateTime.now();
	    public LocalDateTime updatedAt = createdAt;
	    
	}
}

