package com.nthzz.tatsuya.sql.builder;

import com.nthzz.tatsuya.filter.Filter;
import com.nthzz.tatsuya.sql.DynamicQuery;
import com.nthzz.tatsuya.sql.exception.SyntaxException;
import com.nthzz.tatsuya.sql.operator.Operator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 22/09/2017.
 */
public class WhereBuilder {

	private static final Logger eLogger = LogManager.getLogger("ErrorLog");
	private List<Object> params;
	private StringBuilder sb;

	public WhereBuilder() {
		this.sb = new StringBuilder();
		this.params = new ArrayList();
	}

	public DynamicQuery build() {
		return new DynamicQuery(this.sb.toString(), this.params);
	}

	public void where(Filter filter) throws SyntaxException {
		if (filter == null) throw new SyntaxException("Where clause need at least one condition");

		List<Operator> operators = filter.build();
		if (operators == null) throw new SyntaxException("Where clause need at least one condition");

		where(operators.toArray(new Operator[operators.size()]));
	}

	public void where(Operator... operators) throws SyntaxException {

		this.sb.append("\n").append("WHERE 1 = 1");

		if (operators == null || operators.length == 0) {
			throw new SyntaxException("Where clause need at least one condition");
		}

		for (int i = 0; i < operators.length; i++) {
			DynamicQuery dynamicQuery = null;

			try {
				dynamicQuery = operators[i].getQuery();
			} catch (SyntaxException se) {
				eLogger.error("error sql syntax", se);
			}

			if (dynamicQuery != null) {
				this.sb.append(dynamicQuery.getQuery());
				this.params.addAll(dynamicQuery.getParams());
			}
		}
	}
}
