package com.nthzz.tatsuya.sql.operator.conds;


import com.nthzz.tatsuya.sql.DynamicQuery;
import com.nthzz.tatsuya.sql.exception.SyntaxException;
import com.nthzz.tatsuya.sql.operator.Operator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 22/09/2017.
 */
public class Between implements Operator {

	private String field;
	private Object from;
	private Object to;

	public Between(String field, Object from, Object to) {
		this.field = field;
		this.from = from;
		this.to = to;
	}

	@Override
	public DynamicQuery getQuery() throws SyntaxException {
		if (this.field == null || this.field.isEmpty()) {
			throw new SyntaxException("field is not empty");
		}

		DynamicQuery dynamicQuery = null;
		if (this.from != null && this.to != null) {
			StringBuilder sb = new StringBuilder();
			String query = sb.append(this.field).append(" BETWEEN ? AND ?").toString();

			List<Object> params = new ArrayList();
			params.add(this.from);
			params.add(this.to);
			dynamicQuery = new DynamicQuery(query, params);
		}
		return dynamicQuery;
	}
}
