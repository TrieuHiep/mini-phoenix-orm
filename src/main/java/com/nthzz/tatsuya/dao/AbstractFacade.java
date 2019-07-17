package com.nthzz.tatsuya.dao;

import com.nthzz.tatsuya.factory.PhoenixConnFactory;
import com.nthzz.tatsuya.persistence.Column;
import com.nthzz.tatsuya.persistence.Table;
import com.nthzz.tatsuya.sql.DynamicQuery;
import com.nthzz.tatsuya.utils.ConfigUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * @author tatsuya
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected String addPrefix(String prefix, String field) {
        return new StringBuilder(prefix)
                .append(Character.toUpperCase(field.charAt(0)))
                .append(field.substring(1))
                .toString();
    }

    /**
     * Returns all instances of the type.
     *
     * @return all entities
     */
    public List<T> findAll() throws Exception {

        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM ")
                .append(entityClass.getAnnotation(Table.class).name());

        Connection connection = getDatasourceConnection();
        PreparedStatement statement = connection.prepareStatement(sqlBuilder.toString());
        ResultSet resultSet = statement.executeQuery();

        return fetchData(resultSet);
    }

    /**
     * Returns the number of entities available.
     *
     * @return the number of entities
     */
    public Long count() throws Exception {
        StringBuilder SQL = new StringBuilder("SELECT COUNT(*) AS TOTAL FROM ").append(entityClass.getAnnotation(Table.class).name());
        Connection connection = getDatasourceConnection();
        PreparedStatement statement = connection.prepareStatement(SQL.toString());
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            return resultSet.getLong("TOTAL");
        }
        return 0L;
    }

    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity to persist
     *               .
     */
    public void persist(T entity) throws Exception {

        String fullSQL = generateUpsertQL();
        Connection connection = getDatasourceConnection();
        PreparedStatement statement = connection.prepareStatement(fullSQL);
        bindParams(statement, entity);

//        Field[] fields = entityClass.getDeclaredFields();
//        for (int i = 0; i < fields.length; i++) {
//            Method m = entityClass.getMethod(addPrefix("get", fields[i].getName()).toString());
//            Object o = m.invoke(entity);
//            statement.setObject(i + 1, o);
//        }

        int affRow = statement.executeUpdate();
        connection.close();
    }

    /**
     * Saves a given entities in batch mode
     *
     * @param entityList entity list to persist
     * @param batchSize  size of batch              .
     */
    public void persistBatch(List<T> entityList, long batchSize) throws Exception {
        String sql = generateUpsertQL();
        Connection connection = getDatasourceConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        int count = 0;
        for (T entity : entityList) {
            bindParams(statement, entity);
            statement.addBatch();
            if (++count % batchSize == 0) {
                int[] num = statement.executeBatch();
                System.out.println("finish to insert " + num.length + " records to phoenix dbms");
            }
            int[] num = statement.executeBatch();
        }

        statement.clearBatch();
        statement.close();
        connection.close();
    }

    private void bindParams(PreparedStatement statement, T entity) throws Exception {
        Field[] fields = entityClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Method m = entityClass.getMethod(addPrefix("get", fields[i].getName()).toString());
            Object o = m.invoke(entity);
            statement.setObject(i + 1, o);
        }
    }

    /**
     * Retrieves entities by specified query.
     *
     * @param dynamicQuery sql command in dynamic type
     * @return the entity with the given id or {@literal Optional#empty()} if none found
     * @throws Exception if any error occurs
     */
    public List<T> findByConditions(DynamicQuery dynamicQuery) throws Exception {

        String SQL = dynamicQuery.getQuery();

        Connection connection = getDatasourceConnection();
        PreparedStatement statement = connection.prepareStatement(SQL);

        List<Object> paramList = dynamicQuery.getParams();

        for (int i = 0; i < paramList.size(); i++) {
            statement.setObject(i + 1, paramList.get(i));
        }

        ResultSet resultSet = statement.executeQuery();
        return fetchData(resultSet);
    }

    private List<T> fetchData(ResultSet resultSet) throws Exception {
        List<T> dataList = new ArrayList<>();
        Constructor<T> constructor = entityClass.getConstructor();
        while (resultSet.next()) {
            Field[] fields = entityClass.getDeclaredFields();
            T newEntity = constructor.newInstance();
            for (Field field : fields) {
                if (field.getAnnotation(Column.class) == null) continue;
                Column column = field.getAnnotation(Column.class);
                String type = field.getType().getName();

                Class<?> classType = field.getType();
                Method m = entityClass.getDeclaredMethod(addPrefix("set", field.getName()).toString(), classType);
                m.setAccessible(true);
                m.invoke(newEntity, resultSet.getObject(column.name()));
            }
            dataList.add(newEntity);
        }
        return dataList;
    }

    private String generateUpsertQL() {
        StringBuilder sqlCommand = new StringBuilder("UPSERT INTO ")
                .append(entityClass.getAnnotation(Table.class).name())
                .append("(");

        StringBuilder tableFields = new StringBuilder("(");

        Field[] fields = entityClass.getDeclaredFields();

        for (Field field : fields) {
            if (field.getAnnotation(Column.class) == null) continue;
            String tableField = field.getAnnotation(Column.class).name();
            sqlCommand.append(tableField).append(",");
        }

        String formattedSQL = sqlCommand.toString().replaceFirst(".$", "") + ")";
        StringBuilder sb = new StringBuilder(formattedSQL).append("VALUES(");

        for (int i = 1; i <= fields.length; i++) {
            sb.append("?,");
        }

        String fullSQL = sb.toString().replaceFirst(".$", "") + ")";
        return fullSQL;
    }

    /**
     * find all getter and setter method of specified class
     *
     * @param entityClass entity class
     * @return all getter and setter methods
     */
    protected List<Method> findGettersSetters(Class<?> entityClass) {
        List<Method> list = new ArrayList<Method>();
        Method[] methods = entityClass.getDeclaredMethods();
        for (Method method : methods)
            if (isGetter(method) || isSetter(method))
                list.add(method);
        return list;
    }

    private static boolean isGetter(Method method) {
        if (Modifier.isPublic(method.getModifiers()) &&
                method.getParameterTypes().length == 0) {
            if (method.getName().matches("^get[A-Z].*") &&
                    !method.getReturnType().equals(void.class))
                return true;
            if (method.getName().matches("^is[A-Z].*") &&
                    method.getReturnType().equals(boolean.class))
                return true;
        }
        return false;
    }

    private static boolean isSetter(Method method) {
        return Modifier.isPublic(method.getModifiers()) &&
                method.getReturnType().equals(void.class) &&
                method.getParameterTypes().length == 1 &&
                method.getName().matches("^set[A-Z].*");
    }

    /**
     * Gets an array of all methods in a class hierarchy walking up to parent classes
     *
     * @param objectClass the class
     * @return the methods array
     */
    private static Method[] getAllMethodsInHierarchy(Class<?> objectClass) {
        Set<Method> allMethods = new HashSet<Method>();
        Method[] declaredMethods = objectClass.getDeclaredMethods();
        Method[] methods = objectClass.getMethods();
        if (objectClass.getSuperclass() != null) {
            Class<?> superClass = objectClass.getSuperclass();
            Method[] superClassMethods = getAllMethodsInHierarchy(superClass);
            allMethods.addAll(Arrays.asList(superClassMethods));
        }
        allMethods.addAll(Arrays.asList(declaredMethods));
        allMethods.addAll(Arrays.asList(methods));
        return allMethods.toArray(new Method[allMethods.size()]);
    }

    private Connection getDatasourceConnection() throws Exception {
        String phoenixURL = ConfigUtils.getInstance().getPhoenixDatasource();
        return PhoenixConnFactory.getPhoenixConn(phoenixURL, true);
    }
}
