package com.nthzz.tatsuya.sql.operator.others;


import com.nthzz.tatsuya.sql.DynamicQuery;
import com.nthzz.tatsuya.sql.exception.SyntaxException;
import com.nthzz.tatsuya.sql.operator.Operator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 22/09/2017.
 */
public class Ands implements Operator {

	private List<Operator> operators;

	public Ands(List<Operator> operators) {
		this.operators = operators;
	}

	@Override
	public DynamicQuery getQuery() throws SyntaxException {
		if (this.operators == null) {
			throw new SyntaxException("field is not empty");
		}

		DynamicQuery dynamicQuery = null;
		if (!this.operators.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			List<Object> params = new ArrayList();

			sb.append("\n\t").append("AND (");
			for (Operator operator : operators) {
				if (operator.getQuery() != null) {
					sb.append("\n\t").append(operator.getQuery().getQuery());
					params.addAll(operator.getQuery().getParams());
				}
			}
			sb.append("\n\t").append(")");
			dynamicQuery = new DynamicQuery(sb.toString(), params);
		}
		return dynamicQuery;
	}
}
