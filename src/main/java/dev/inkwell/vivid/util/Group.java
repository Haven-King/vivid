package dev.inkwell.vivid.util;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Group<T> implements Iterable<T> {
	private final List<T> list = new ArrayList<>();
	private final Set<T> set = new HashSet<>();

	protected final MutableText name;
	protected final List<Text> tooltips = new ArrayList<>();

	public Group(MutableText name) {
		this.name = name;
	}

	@SafeVarargs
	public final Group<T> add(T... members) {
		for (T member : members) {
			if (!set.contains(member)) {
				list.add(member);
				set.add(member);
			}
		}

		return this;
	}

	public final Group<T> add(Text tooltip) {
		this.tooltips.add(tooltip);

		return this;
	}

	public final Group<T> addAll(Collection<Text> tooltips) {
		this.tooltips.addAll(tooltips);

		return this;
	}

	public MutableText getName() {
		return this.name;
	}

	public int size() {
		return list.size();
	}

	public T get(int index) {
		return list.get(index);
	}

	@NotNull
	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}

	public List<Text> getTooltips() {
		return this.tooltips;
	}
}
