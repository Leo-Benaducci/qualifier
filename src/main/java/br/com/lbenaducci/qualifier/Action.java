package br.com.lbenaducci.qualifier;

import lombok.extern.log4j.Log4j2;

import java.util.function.Consumer;

@Log4j2
public class Action<T> {
	private final Consumer<T> consumer;
	private boolean executed;

	public Action(Consumer<T> action) {
		if(action == null) {
			throw new IllegalArgumentException("Action cannot be null");
		}
		this.consumer = action;
		this.executed = false;
	}

	public void execute(T value) {
		if(executed) {
			return;
		}
		this.executed = true;
		try {
			this.consumer.accept(value);
		} catch(Exception e) {
			log.error("Error on execute action", e);
		}
	}
}
