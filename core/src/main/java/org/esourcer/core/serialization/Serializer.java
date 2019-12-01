package org.esourcer.core.serialization;

public interface Serializer {
    <T> byte[] serialize(final T object);

    <T> T deserialize(final byte[] data, final String className);
}
