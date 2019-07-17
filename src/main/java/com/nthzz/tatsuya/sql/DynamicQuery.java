package com.nthzz.tatsuya.sql;

import java.util.List;

/**
 * Created by vietnb on 9/1/17.
 */
public class DynamicQuery {

	private String query;

	private List<Object> params;

	public DynamicQuery(String query) {
		this.query = query;
	}

	public DynamicQuery(String query, List<Object> params) {
		this.query = query;
		this.params = params;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<Object> getParams() {
		return params;
	}

	public void union(DynamicQuery other) {
		StringBuilder sb = new StringBuilder();
		sb.append(this.query).append("\n").append("UNION ALL ").append(other.getQuery());

		this.query = sb.toString();
		this.params.addAll(other.getParams());
	}
}
