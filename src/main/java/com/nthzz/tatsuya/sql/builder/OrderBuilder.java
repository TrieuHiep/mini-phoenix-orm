package com.nthzz.tatsuya.sql.builder;


import com.nthzz.tatsuya.sql.DynamicQuery;
import com.nthzz.tatsuya.sql.exception.SyntaxException;
import com.nthzz.tatsuya.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 22/09/2017.
 */
public class OrderBuilder implements IBuilder {
    private List<String> orders;
    private StringBuilder sb;

    public OrderBuilder() {
        this.orders = new ArrayList();
        this.sb = new StringBuilder();
    }

    @Override
    public DynamicQuery build() {
        if (orders.isEmpty()) return null;

        this.sb.append("\n").append("ORDER BY ");
        for (int i = 0; i < orders.size(); i++) {
            this.sb.append(orders.get(i));
            if (i != (orders.size() - 1)) {
                this.sb.append(", ");
            }
        }
        return new DynamicQuery(this.sb.toString());
    }

    public void orderBy(String field, String order) throws SyntaxException {
        if (field == null || field.isEmpty()) {
            throw new SyntaxException("order by's field is not empty");
        }
        if (order == null || order.isEmpty()) {
            order = "ASC";
        }
        StringBuilder sb = new StringBuilder();
        this.orders.add(sb.append(field).append(" ").append(order).toString());
    }

    public void orderBy(String... fieldOrder) throws SyntaxException {
        if (CommonUtils.isNull(fieldOrder)) {
            throw new SyntaxException("order by's field is not empty");
        }
        for (int i = 0; i < fieldOrder.length; i++) {
            if (fieldOrder[i] == null || fieldOrder[i].isEmpty()) {
                throw new SyntaxException("order by clause's is not empty");
            }
            String[] arr = fieldOrder[i].trim().split(" ");
            if (arr.length > 2) {
                throw new SyntaxException("order by clause's param can not greater than 2");
            }
            String order = "ASC";
            if (arr.length == 2) {
                order = arr[1];
            }
            StringBuilder sb = new StringBuilder();
            this.orders.add(sb.append(arr[0]).append(" ").append(order).toString());
        }
    }
}
