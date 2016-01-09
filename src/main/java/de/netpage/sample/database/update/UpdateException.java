package de.netpage.sample.database.update;


/**
 * Exception, die geworfen wird, wenn die Aktualisierung nicht möglich war.
 *
 * @author Denis Wirries
 * @version 1.0
 * @since 16.08.2014
 */
public class UpdateException extends Exception {

    private static final long serialVersionUID = 2813724816177949708L;

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public UpdateException(final String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     * @since 1.4
     */
    public UpdateException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
