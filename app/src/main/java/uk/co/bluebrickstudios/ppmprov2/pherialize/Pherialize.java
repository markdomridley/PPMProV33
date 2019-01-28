package uk.co.bluebrickstudios.ppmprov2.pherialize;

import java.nio.charset.Charset;


/**
 * The main interface to Pherialize. Just implements the static methods
 * serialize and unserialize for easier usage of the Serializer and
 * Unserializer classes.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @version $Revision$
 */

public class Pherialize
{
    /**
     * Hide constructor
     */

    private Pherialize()
    {
        // Empty
    }


    /**
     * Returns the serialized representation of the specified object.
     *
     * @param object
     *            The object to serialize
     * @param charset
     *            The charset of data.
     * @return The serialized representation of the object
     */

    public static String serialize(final Object object, Charset charset)
    {
        Serializer pherialize;

        pherialize = new Serializer(charset);
        return pherialize.serialize(object);
    }


    /**
     * Returns the serialized representation of the specified object.
     *
     * @param object
     *            The object to serialize
     * @return The serialized representation of the object
     */

    public static String serialize(final Object object)
    {
        Serializer pherialize;

        pherialize = new Serializer();
        return pherialize.serialize(object);
    }


    /**
     * Returns the unserialized object of the specified PHP serialize format
     * string. The returned object is wrapped in a Mixed object allowing easy
     * conversion to any data type needed. This wrapping is needed because PHP
     * is a loosely typed language and it is quite propable that a boolean is
     * sometimes a int or a string. So with the Mixed wrapper object you can
     * easily decide on your own how to interpret the unserialized data.
     *
     * @param data
     *            The serialized data
     * @param charset
     *            The charset of data.
     * @return The unserialized object
     */

    public static Mixed unserialize(final String data, Charset charset)
    {
        Unserializer unserializer;

        unserializer = new Unserializer(data, charset);
        return unserializer.unserializeObject();
    }


    /**
     * Returns the unserialized object of the specified PHP serialize format
     * string. The returned object is wrapped in a Mixed object allowing easy
     * conversion to any data type needed. This wrapping is needed because PHP
     * is a loosely typed language and it is quite propable that a boolean is
     * sometimes a int or a string. So with the Mixed wrapper object you can
     * easily decide on your own how to interpret the unserialized data.
     *
     * @param data
     *            The serialized data
     * @return The unserialized object
     */

    public static Mixed unserialize(final String data)
    {
        Unserializer unserializer;

        unserializer = new Unserializer(data);
        return unserializer.unserializeObject();
    }
}
