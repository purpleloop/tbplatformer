package tbplatformer;

/** A generic game exception. */
public class GameException extends RuntimeException {

    /** Serial tag */
    private static final long serialVersionUID = -2372931457081157432L;

    /**
     * @param message the message describing the exception
     * @param cause the initial cause
     */
    public GameException(String message, Throwable cause) {
        super(message, cause);
    }
}
