package com.nthzz.tatsuya.sql.operator.conds;

import com.nthzz.tatsuya.sql.DynamicQuery;
import com.nthzz.tatsuya.sql.exception.SyntaxException;
import com.nthzz.tatsuya.sql.operator.Operator;
import com.nthzz.tatsuya.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 23/09/2017.
 */
public class In implements Operator {
	private String field;
	private List<? extends Object> values;

	public In(String field, List<? extends Object> values) {
		this.field = field;
		this.values = values;
	}

	@Override
	public DynamicQuery getQuery() throws SyntaxException {
		if (this.field == null || this.field.isEmpty()) {
			throw new SyntaxException("field is not empty");
		}

		if (CommonUtils.isNull(values)) return null;

		StringBuilder query = new StringBuilder();
		query.append(field).append(" IN (");

		List<Object> params = new ArrayList();
		for (int i = 0; i < this.values.size(); i++) {
			if (this.values.get(i) == null) continue;

			query.append("?");
			if (i != this.values.size() - 1) {
				query.append(",");
			}
			params.add(this.values.get(i));
		}
		query.append(")");
		return new DynamicQuery(query.toString(), params);
	}
}
