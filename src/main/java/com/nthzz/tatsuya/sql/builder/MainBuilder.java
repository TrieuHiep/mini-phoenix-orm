package com.nthzz.tatsuya.sql.builder;

import com.nthzz.tatsuya.persistence.Table;
import com.nthzz.tatsuya.sql.DynamicQuery;
import com.nthzz.tatsuya.sql.exception.SyntaxException;
import com.nthzz.tatsuya.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tatsuya
 */
public class MainBuilder<T> implements IBuilder<T> {
    private List<String> columns;
    private StringBuilder sb;
    private List<Object> params;

    public MainBuilder() {
        this.sb = new StringBuilder();
        this.columns = new ArrayList();
        this.params = new ArrayList();
    }

    @Override
    public DynamicQuery build() {
        if (columns.isEmpty()) return null;

        StringBuilder columnSb = new StringBuilder();
        for (int i = 0; i < columns.size(); i++) {
            columnSb.append("\n\t").append(columns.get(i));
            if (i != (columns.size() - 1)) {
                columnSb.append(",");
            }
        }
        String finalQuery = String.format(this.sb.toString(), columnSb.toString());
        return new DynamicQuery(finalQuery, this.params);
    }

    public void select() {
        this.sb.append("\n").append("SELECT ");
        this.sb.append("%s");
    }


    public void columns(String... columns) throws SyntaxException {
        if (!CommonUtils.isAllNotNull(columns)) {
            throw new SyntaxException("Column is not empty");
        }
        for (String column : columns) {
            this.columns.add(column);
        }
    }

    public void column(String column) throws SyntaxException {
        if (column == null || column.isEmpty()) {
            throw new SyntaxException("Column is not empty");
        }
        this.columns.add(column);
    }

    public void table(Class<T> entityClass) throws SyntaxException {

        if (entityClass.getAnnotation(Table.class) == null)
            throw new SyntaxException("Table not defined at entity class " + entityClass.getName());

        String table = entityClass.getAnnotation(Table.class).name();

        if (table.isEmpty()) {
            throw new SyntaxException("Table is not empty");
        }
        this.sb.append("\n").append("FROM ").append(table);
    }

    public void table(DynamicQuery subQuery) throws SyntaxException {
        if (subQuery == null) {
            throw new SyntaxException("Table is not empty");
        }
        this.sb.append("\n").append("FROM (")
                .append("\n").append(subQuery.getQuery())
                .append("\n").append(")");
        this.params.addAll(subQuery.getParams());
    }
}
