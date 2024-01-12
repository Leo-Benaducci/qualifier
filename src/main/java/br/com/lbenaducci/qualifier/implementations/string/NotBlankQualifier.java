package br.com.lbenaducci.qualifier.implementations.string;

import br.com.lbenaducci.qualifier.Qualifier;

public class NotBlankQualifier implements Qualifier<String> {
	@Override
	public boolean isSatisfiedBy(String t) {
		return t != null && !t.isBlank();
	}
}
