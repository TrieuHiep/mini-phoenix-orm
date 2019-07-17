package com.nthzz.tatsuya.sql.builder;


import com.nthzz.tatsuya.sql.DynamicQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 23/09/2017.
 */
public class LimitBuilder implements IBuilder {
    private StringBuilder sb;
    private List<Object> params;

    public LimitBuilder() {
        this.sb = new StringBuilder();
        this.params = new ArrayList();
    }

    @Override
    public DynamicQuery build() {
        return new DynamicQuery(this.sb.toString(), this.params);
    }

    public void limit(Integer limit) {
        if (limit == null) {
            limit = 100;
        }

        this.sb.append("\n").append("LIMIT ? ");
        this.params.add(limit);
    }

    public void limit(Integer limit, Integer offset) {
        if (limit == null) {
            limit = 100;
        }
        if (offset == null) {
            offset = 0;
        }

        this.sb.append("\n").append("LIMIT ? OFFSET ? ");
        this.params.add(limit);
        this.params.add(offset);
    }
}
