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
 * @author Egg-03
 */
public interface CommonMappingInterface<S> {

    Gson GSON = new Gson();

    /**
     * Converts a JSON string into a list of objects of the specified type {@code <S>}.
     * <p>
     * If the JSON represents a single object, it is returned as a singleton list.
     * If the JSON is null or empty, returns an empty list.
     * </p>
     *
     * @param json        the JSON string to parse; must not be null
     * @param objectClass the class of the objects in the list; must not be null
     * @return a non-null list of objects deserialized from JSON
     * @throws JsonSyntaxException if the JSON is malformed
     * @since 3.0.0
     */
    @NotNull
    default List<S> mapToList(@NotNull String json, @NotNull Class<S> objectClass) {

        if(json.startsWith("[")) {
            Type listType = TypeToken.getParameterized(List.class, objectClass).getType();
            List<S> result = GSON.fromJson(json, listType);
            return result!=null ? result : Collections.emptyList();
        } else {
            S singleObject = GSON.fromJson(json, objectClass);
            return singleObject!=null ? Collections.singletonList(singleObject) : Collections.emptyList();
        }
    }

    /**
     * Converts a JSON string into an {@link Optional} object of the specified type {@code <S>}.
     * <p>
     * Returns {@link Optional#empty()} if the JSON is null or cannot be parsed into an object.
     * </p>
     *
     * @param json        the JSON string to parse; must not be null
     * @param objectClass the class of the object; must not be null
     * @return an {@link Optional} containing the deserialized object, or empty if null
     * @throws JsonSyntaxException if the JSON is malformed
     * @since 3.0.0
     */
    @NotNull
    default Optional<S> mapToObject(@NonNull String json, @NonNull Class<S> objectClass) {
        S object = GSON.fromJson(json, objectClass);
        return Optional.ofNullable(object);
    }
}
