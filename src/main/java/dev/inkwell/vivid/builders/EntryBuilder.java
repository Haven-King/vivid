package dev.inkwell.vivid.builders;

public interface EntryBuilder {
	Type getType();

	enum Type {
		LIST, VALUE;
	}
}
