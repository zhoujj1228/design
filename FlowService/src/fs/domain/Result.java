package fs.domain;

public class Result<T> {
	private T result;

	public T get() {
		return result;
	}

	public void set(T result) {
		this.result = result;
	}
}
