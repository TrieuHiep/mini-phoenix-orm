package com.nthzz.tatsuya.dao;

import com.nthzz.tatsuya.sql.DynamicQuery;

import java.util.List;

/**
 * @author tatsuya
 */
public interface CrudRepository<T> {

    /**
     * Returns all instances of the type.
     *
     * @return all entities
     */
    public abstract List<T> findAll() throws Exception;

    /**
     * Retrieves entities by specified query.
     *
     * @param dynamicQuery sql command in dynamic type
     * @return the entity with the given id or {@literal Optional#empty()} if none found
     * @throws Exception if any error occurs
     */
    public abstract List<T> findByConditions(DynamicQuery dynamicQuery) throws Exception;

    /**
     * Returns the number of entities available.
     *
     * @return the number of entities
     */
    public abstract Long count() throws Exception;

    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity to persist
     *               .
     */
    public abstract void persist(T entity) throws Exception;

    /**
     * Saves a given entities in batch mode
     *
     * @param entityList entity list to persist
     * @param batchSize  size of batch              .
     */
    public abstract void persistBatch(List<T> entityList, long batchSize) throws Exception;
}
