package org.esourcer.core.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public final class JacksonSerializer implements Serializer {

    @NonNull
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public <T> byte[] serialize(final T object) {
        return objectMapper.writeValueAsBytes(object);
    }

    @SneakyThrows
    @Override
    public <T> T deserialize(final byte[] data, final String className) {
        return (T) objectMapper.readValue(data, Class.forName(className));
    }
}
