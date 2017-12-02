package uk.co.stikman.aoc.utils;

public class Pair<T1, T2> {
	public T1	a;
	public T2	b;

	public Pair(T1 a, T2 b) {
		super();
		this.a = a;
		this.b = b;
	}

	public T1 getA() {
		return a;
	}

	public void setA(T1 a) {
		this.a = a;
	}

	public T2 getB() {
		return b;
	}

	public void setB(T2 b) {
		this.b = b;
	}

	@Override
	public String toString() {
		return "Pair [a=" + a + ", b=" + b + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		Pair other = (Pair) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		return true;
	}

}
