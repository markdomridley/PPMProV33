package uk.co.bluebrickstudios.ppmprov2.pherialize.exceptions;
/**
 * Base exception for Pherialize problems.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @version $Revision$
 */

abstract public class PherializeException extends RuntimeException
{
    /** Serial version UID */
    private static final long serialVersionUID = 1479169743443565173L;


    /**
     * Constructor
     */

    public PherializeException()
    {
        super();
    }


    /**
     * Constructor
     *
     * @param message
     *            The exception message
     */

    public PherializeException(final String message)
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

    public PherializeException(final String message, final Throwable cause)
    {
        super(message, cause);
    }


    /**
     * Constructor
     *
     * @param cause
     *            The root cause
     */

    public PherializeException(final Throwable cause)
    {
        super(cause);
    }
}
