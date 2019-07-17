package com.nthzz.tatsuya.sql.builder;


import com.nthzz.tatsuya.filter.Filter;
import com.nthzz.tatsuya.sql.DynamicQuery;
import com.nthzz.tatsuya.sql.exception.SyntaxException;
import com.nthzz.tatsuya.sql.operator.Operator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 23/09/2017.
 */
public class QueryBuilder<T> implements IBuilder {

    private MainBuilder mainBuilder;
    private WhereBuilder whereBuilder;
    private OrderBuilder orderBuilder;
    private GroupBuilder groupBuilder;
    private LimitBuilder limitBuilder;

    private StringBuilder sb;
    private List<Object> params;

    public QueryBuilder() {
        sb = new StringBuilder();
        params = new ArrayList();

        mainBuilder = new MainBuilder();
        whereBuilder = new WhereBuilder();
        orderBuilder = new OrderBuilder();
        groupBuilder = new GroupBuilder();
        limitBuilder = new LimitBuilder();
    }

    public DynamicQuery build() {
        DynamicQuery mainPart = mainBuilder.build();
        DynamicQuery wherePart = whereBuilder.build();
        DynamicQuery groupByPart = groupBuilder.build();
        DynamicQuery orderByPart = orderBuilder.build();
        DynamicQuery limitPart = limitBuilder.build();

        if (mainPart != null) {
            sb.append(mainPart.getQuery());
            params.addAll(mainPart.getParams());
        }

        if (wherePart != null) {
            sb.append(wherePart.getQuery());
            params.addAll(wherePart.getParams());
        }
        if (groupByPart != null && !groupByPart.getQuery().isEmpty()) {
            sb.append(groupByPart.getQuery());
        }
        if (orderByPart != null && !orderByPart.getQuery().isEmpty()) {
            sb.append(orderByPart.getQuery());
        }
        if (limitPart != null) {
            sb.append(limitPart.getQuery());
            params.addAll(limitPart.getParams());
        }

        DynamicQuery dynamicQuery = new DynamicQuery(sb.toString(), params);
        return dynamicQuery;
    }

    public QueryBuilder select() {
        mainBuilder.select();
        return this;
    }

    public QueryBuilder columns(String... columns) throws SyntaxException {
        mainBuilder.columns(columns);
        return this;
    }

    public QueryBuilder column(String column) throws SyntaxException {
        mainBuilder.column(column);
        return this;
    }

    public QueryBuilder table(Class<T> entityClass) throws SyntaxException {
        mainBuilder.table(entityClass);
        return this;
    }

    public QueryBuilder table(DynamicQuery subQuery) throws SyntaxException {
        mainBuilder.table(subQuery);
        return this;
    }

    public QueryBuilder where(Operator... operators) throws SyntaxException {
        whereBuilder.where(operators);
        return this;
    }

    public QueryBuilder where(Filter filter) throws SyntaxException {
        whereBuilder.where(filter);
        return this;
    }

    public QueryBuilder orderBy(String field, String order) throws SyntaxException {
        orderBuilder.orderBy(field, order);
        return this;
    }

    public QueryBuilder groupBy(String... fields) throws SyntaxException {
        groupBuilder.groupBy(fields);
        return this;
    }

    public QueryBuilder limit(Integer limit) {
        limitBuilder.limit(limit);
        return this;
    }

    public QueryBuilder limit(Integer limit, Integer offset) {
        limitBuilder.limit(limit, offset);
        return this;
    }
}