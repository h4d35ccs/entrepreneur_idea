package es.upm.emse.enteridea.persistence.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import es.upm.emse.enteridea.persistence.exception.DaoOperationException;
/**
 * Interface that define the basic operation of an Data Access Object
 * @author ottoabreu
 *
 * @param <T> Entity class
 * @param <PK>Object that hold the primary key
 */
public interface GenericDAO<T, PK extends Serializable> {
    
	/**
	 * creates a new instance of the given entity (Creates a new record in the
	 * database)
	 * 
	 * @param T
	 *            the entity class
	 * @throws DaoOperationException
	 *             if can't save or open the connection
	 */
	void create(T t) throws DaoOperationException;
	/**
	 * Retrieve an entity by the given Primary Key
	 * 
	 * @param PK
	 *            Primary Key
	 * @throws DaoOperationException
	 *             if can't retrieve the object or open the connection
	 */
    T read(PK id)throws DaoOperationException;
    /**
	 * Updates the entity (Updates the record)
	 * 
	 * @param T
	 *            the entity class
	 * @throws DaoOperationException
	 *             if can't update or open the connection
	 */
    void update(T t)throws DaoOperationException;
    
    /**
	 * delete the entity from the DB ( deletes the record)
	 *  @throws DaoOperationException if can't delete or open the connection
	 */
    void delete(PK id)throws DaoOperationException;
    /**
     * Returns all the records from the DB
     * @return List<T>
     * @throws DaoOperationException if can not get all results
     */
    List<T> getAll() throws DaoOperationException;
    
    /**
     * Returns all the records from the DB executing a specific query.
     * @param query String in HQL-SQL format
     * @param Params Map that contains attributes to put in the query and their name
     * @param firstResult integer that says where the query will start, can be null
     * @param maxResult integer that say where the query will end, can be null
     * @return List<T>
     * @throws DaoOperationException if can not execute the query and return the list
     */
    List<T> getByQuery(String query, Map<String,?>Params,  Integer firstResult, Integer maxResult) throws DaoOperationException;
    /**
     * Gets the total number of records in a table
     * @return Integer
     * @throws DaoOperationException if can't return the number
     */
    Integer getTotalRecords()throws DaoOperationException;
    /**
     * Returns the entity name in the DB
     * @return String
     */
    String getTableNameFromEntity();
    
    
}