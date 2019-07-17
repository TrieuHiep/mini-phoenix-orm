package com.nthzz.tatsuya.sql.operator.conds;


import com.nthzz.tatsuya.sql.DynamicQuery;
import com.nthzz.tatsuya.sql.exception.SyntaxException;
import com.nthzz.tatsuya.sql.operator.Operator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 22/09/2017.
 */
public class Greater implements Operator {

	private String field;
	private Object value;

	public Greater(String field, Object value) {
		this.field = field;
		this.value = value;
	}

	@Override
	public DynamicQuery getQuery() throws SyntaxException {
		if (field == null || field.isEmpty()) {
			throw new SyntaxException("field is not empty");
		}

		DynamicQuery dynamicQuery = null;
		if (value != null) {
			StringBuilder sb = new StringBuilder();
			String query = sb.append(field).append(" >= ? ").toString();

			List<Object> params = new ArrayList();
			params.add(value);

			dynamicQuery = new DynamicQuery(query, params);
		}
		return dynamicQuery;
	}
}
