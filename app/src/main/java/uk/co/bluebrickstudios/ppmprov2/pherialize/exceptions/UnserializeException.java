package uk.co.bluebrickstudios.ppmprov2.pherialize.exceptions;


/**
 * Exception thrown when someting goes wrong while unserializing.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @version $Revision$
 */

public class UnserializeException extends PherializeException
{
    /** Serial version UID */
    private static final long serialVersionUID = -7127648595193318947L;


    /**
     * Constructor
     */

    public UnserializeException()
    {
        super();
    }


    /**
     * Constructor
     *
     * @param message
     *            The exception message
     */

    public UnserializeException(final String message)
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

    public UnserializeException(final String message, final Throwable cause)
    {
        super(message, cause);
    }


    /**
     * Constructor
     *
     * @param cause
     *            The root cause
     */

    public UnserializeException(final Throwable cause)
    {
        super(cause);
    }
}
