package dev.monarkhes.vivid.builders;

public interface EntryBuilder {
	Type getType();

	enum Type {
		LIST, VALUE;
	}
}
