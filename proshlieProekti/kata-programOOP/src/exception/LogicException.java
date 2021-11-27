package exception;

//наш RuntimeException с нашим сообщением
public class LogicException extends RuntimeException {

    public LogicException(String message) {
        super(message);
    }
}
