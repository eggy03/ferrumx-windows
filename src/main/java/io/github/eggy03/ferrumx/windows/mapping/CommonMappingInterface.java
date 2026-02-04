/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.mapping;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * A common mapping interface for mapping JSON strings to Java objects.
 * <p>
 * Provides default methods to convert JSON responses
 * into either a {@link List} of objects or a single {@link Optional} object.
 * The default methods in this interface use Gson for JSON deserialization.
 * </p>
 *
 * @param <S> the entity type returned by the service implementation
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
public interface CommonMappingInterface<S> {

    Gson GSON = new Gson();

    /**
     * Converts a JSON string into a list of objects of the specified type {@code <S>}.
     * <p>
     *      If the JSON represents a single object, it is returned as a singleton list.
     *      If the JSON is null or empty, returns an empty list.
     * </p>
     * <p>
     *     Useful for implementing the mappers of classes which return more than one instance
     *     such as the {@code Win32_NetworkAdapter}
     * </p>
     *
     * @param json        the JSON string to parse; must not be null
     * @param objectClass the class of the objects in the list; must not be null
     * @return an immutable, non-null list of objects deserialized from JSON.
     *         If the JSON string is empty, it will return an empty unmodifiable list.
     *         If the JSON string does not match the schema of the {@code objectClass}, an immutable
     *         list containing exactly 1 object of type {@code objectClass} will be returned.
     *         If the object allows for null values in its fields, the returned object in the list will have null fields.
     *         If null values are disallowed, a {@link NullPointerException} may be thrown.
     *
     * @throws NullPointerException if the JSON string or the objectClass is null
     * @throws JsonSyntaxException if the JSON is malformed
     * @since 3.0.0
     */
    @NotNull
    default List<S> mapToList(@NonNull String json, @NonNull Class<S> objectClass) {

        if(json.startsWith("[")) {
            Type listType = TypeToken.getParameterized(List.class, objectClass).getType();
            List<S> result = GSON.fromJson(json, listType);
            return result!=null ? Collections.unmodifiableList(result) : Collections.emptyList();
        } else {
            S singleObject = GSON.fromJson(json, objectClass);
            return singleObject!=null ? Collections.singletonList(singleObject) : Collections.emptyList();
        }
    }

    /**
     * Converts a JSON string into an {@link Optional} object of the specified type {@code <S>}.
     * <p>
     *     Useful for implementing the mappers classes which return exactly one instance
     *     such as the {@code Win32_ComputerSystem} WMI class
     * </p>
     *
     * @param json        the JSON string to parse; must not be null
     * @param objectClass the class of the object; must not be null
     * @return an {@link Optional} containing the deserialized object,
     *         or {@link Optional#empty()} if the JSON is empty.
     *         If the JSON string does not match the schema of the objectClass,
     *         an {@link Optional} containing an object of type {@code objectClass}
     *         will be created with null fields. If null values are disallowed,
     *         a {@link NullPointerException} may be thrown.
     *
     * @throws JsonSyntaxException if the JSON is malformed
     * @throws NullPointerException if the JSON string or the objectClass is null
     * @since 3.0.0
     */
    @NotNull
    default Optional<S> mapToObject(@NonNull String json, @NonNull Class<S> objectClass) {
        S object = GSON.fromJson(json, objectClass);
        return Optional.ofNullable(object);
    }
}
