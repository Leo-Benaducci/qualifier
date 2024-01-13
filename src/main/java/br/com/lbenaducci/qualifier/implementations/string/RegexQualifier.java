package br.com.lbenaducci.qualifier.implementations.string;

import br.com.lbenaducci.qualifier.Qualifier;

public class RegexQualifier implements Qualifier<String> {
	private String regex = ".*";

	@Override
	public boolean isSatisfiedBy(String s) {
		return s != null && s.matches(regex);
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}
}
