package tp.mySpringBatch.exception;

public class MyProcessException extends RuntimeException {

	public MyProcessException() {
	}

	public MyProcessException(String message) {
		super(message);
	}

	public MyProcessException(String message, Throwable cause) {
		super(message, cause);
	}

}
