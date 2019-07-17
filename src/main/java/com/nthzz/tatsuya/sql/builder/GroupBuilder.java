package com.nthzz.tatsuya.sql.builder;

import com.nthzz.tatsuya.sql.DynamicQuery;
import com.nthzz.tatsuya.sql.exception.SyntaxException;
import com.nthzz.tatsuya.utils.CommonUtils;

public class GroupBuilder implements IBuilder {
	private StringBuilder sb;

	public GroupBuilder() {
		this.sb = new StringBuilder();
	}

	@Override
	public DynamicQuery build() {
		return new DynamicQuery(this.sb.toString());
	}

	public void groupBy(String... fields) throws SyntaxException {
		if (CommonUtils.isNull(fields)) {
			throw new SyntaxException("fields in GroupBy is not empty");
		}
		this.sb.append("\n").append("GROUP BY ");
		for (int i = 0; i < fields.length; i++) {
			if (fields[i] == null || fields[i].isEmpty()) {
				throw new SyntaxException("field in GroupBy is not empty");
			}
			this.sb.append(fields[i]);
			if (i != (fields.length - 1)) {
				this.sb.append(", ");
			}
		}
	}
}