package br.com.lbenaducci.qualifier;

public interface ComparatorQualifier<T, P> extends Qualifier<T> {
	void setProperty(P value);
}
