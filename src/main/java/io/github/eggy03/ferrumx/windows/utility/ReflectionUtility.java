/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.utility;

import com.google.gson.annotations.SerializedName;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * A utility class that provides helper methods that use Java reflection
 * <p>
 * <b>For internal use only</b>
 *
 * @author Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * @since 3.0.0
 */
@UtilityClass
public class ReflectionUtility {

    /**
     * Retrieves all {@link SerializedName} values declared on the fields of the specified class
     * and returns them as a comma-separated string.
     *
     * <p>If a field does not declare a {@link SerializedName} annotation,
     * its actual field name is used instead.</p>
     *
     * <p>The method inspects only fields declared directly within the provided class;
     * inherited fields are not included.</p>
     *
     * @param tClass the class whose fields should be inspected
     * @param <T>    the type of the class
     * @return a comma-separated string containing either the value of each
     * {@link SerializedName} annotation or the field name if the annotation is absent
     */
    @NotNull
    public static <T> String getFromSerializedNames(@NonNull Class<T> tClass) {

        StringBuilder properties = new StringBuilder();

        Arrays.stream(tClass.getDeclaredFields())
                .sequential()
                .filter(field -> !field.isSynthetic()) // filter out synthetic fields since jacoco creates $jacocoData field during tests which fails the assertions. This behavior is not observed in scenarios where code coverage is not run
                .forEach(field -> {
                    SerializedName property = field.getAnnotation(SerializedName.class);
                    properties.append(property != null ? property.value() : field.getName()).append(", ");
                });

        // remove trailing ", "
        if (properties.length() > 0) {
            properties.delete(properties.length() - 2, properties.length());
        }

        return properties.toString();
    }
}
