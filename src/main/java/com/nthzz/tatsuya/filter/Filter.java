package com.nthzz.tatsuya.filter;


import com.nthzz.tatsuya.sql.operator.Operator;

import java.util.List;

public abstract class Filter {
    public abstract List<Operator> build();
}
