package br.com.lbenaducci.qualifier.implementations.string;

import br.com.lbenaducci.qualifier.ComparatorQualifier;

public class RegexQualifier implements ComparatorQualifier<String, String> {
	private String regex = ".*";

	@Override
	public boolean isSatisfiedBy(String s) {
		return s != null && s.matches(regex);
	}

	@Override
	public void setProperty(String regex) {
		this.regex = regex;
	}
}
