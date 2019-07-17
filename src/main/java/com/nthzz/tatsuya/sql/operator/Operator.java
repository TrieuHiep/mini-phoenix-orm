package com.nthzz.tatsuya.sql.operator;

import com.nthzz.tatsuya.sql.DynamicQuery;
import com.nthzz.tatsuya.sql.exception.SyntaxException;

/**
 * Created by robert on 22/09/2017.
 */
public interface Operator {
	DynamicQuery getQuery() throws SyntaxException;
}
