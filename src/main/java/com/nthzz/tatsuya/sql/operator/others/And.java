package com.nthzz.tatsuya.sql.operator.others;


import com.nthzz.tatsuya.sql.DynamicQuery;
import com.nthzz.tatsuya.sql.exception.SyntaxException;
import com.nthzz.tatsuya.sql.operator.Operator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 22/09/2017.
 */
public class And implements Operator {

	private Operator operator;

	public And(Operator operator) {
		this.operator = operator;
	}

	@Override
	public DynamicQuery getQuery() throws SyntaxException {
		if (this.operator == null) {
			throw new SyntaxException("field is not empty");
		}

		DynamicQuery dynamicQuery = null;
		if (operator.getQuery() != null) {
			StringBuilder sb = new StringBuilder();
			List<Object> params = new ArrayList();

			sb.append("\n\t").append("AND ").append(operator.getQuery().getQuery());
			params.addAll(operator.getQuery().getParams());

			dynamicQuery = new DynamicQuery(sb.toString(), params);
		}
		return dynamicQuery;
	}
}
