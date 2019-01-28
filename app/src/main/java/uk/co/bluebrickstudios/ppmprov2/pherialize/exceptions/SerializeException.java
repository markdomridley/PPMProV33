package uk.co.bluebrickstudios.ppmprov2.pherialize.exceptions;

/**
 * Exception thrown when someting goes wrong while serializing.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @version $Revision$
 */

public class SerializeException extends PherializeException
{
    /** Serial version UID */
    private static final long serialVersionUID = 5304443329670892370L;


    /**
     * Constructor
     */

    public SerializeException()
    {
        super();
    }


    /**
     * Constructor
     *
     * @param message
     *            The exception message
     */

    public SerializeException(final String message)
    {
        super(message);
    }


    /**
     * Constructor
     *
     * @param message
     *            The exception message
     * @param cause
     *            The root cause
     */

    public SerializeException(final String message, final Throwable cause)
    {
        super(message, cause);
    }


    /**
     * Constructor
     *
     * @param cause
     *            The root cause
     */

    public SerializeException(final Throwable cause)
    {
        super(cause);
    }
}
