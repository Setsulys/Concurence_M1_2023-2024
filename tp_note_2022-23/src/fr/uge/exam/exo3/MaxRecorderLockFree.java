package fr.uge.exam.exo3;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class MaxRecorderLockFree {
	private final AtomicIntegerArray maximums;

	public MaxRecorderLockFree(int size) {
		if (size < 1) {
			throw new IllegalArgumentException("Invalid size");
		}
		this.maximums = new AtomicIntegerArray(size);
		for(var i = 0; i < size;i++) {
			maximums.set(i, -1);
		}
	}

	/**
	 * Return the index of the first occurrence of the minimal element in a
	 * non-empty array
	 */
	private static int findIndexOfMinimum(AtomicIntegerArray t) {
		if (t.length() == 0) {
			throw new IllegalArgumentException();
		}
		var min = t.get(0);
		var index = 0;
		for (var i = 1; i < t.length(); i++) {
			if (t.get(i) < min) {
				min = t.get(i);
				index = i;
			}
		}
		return index;
	}

	public boolean process(int value) {
		if (value < 0) {
			throw new IllegalArgumentException("Argument must be >=0");
		}
		var indexMin = findIndexOfMinimum(maximums);
		if (value > maximums.get(indexMin)) {
			maximums.set(indexMin, value);
			return true;
		}
		return false;
	}
}
