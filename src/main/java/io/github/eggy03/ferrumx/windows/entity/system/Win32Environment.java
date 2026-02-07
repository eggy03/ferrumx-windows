/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.entity.system;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of the environment variables in a system running Windows.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_Environment} WMI class.
 * </p>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Build a new instance
 * Win32Environment env = Win32Environment.builder()
 *     .name("DOT_NET_CLI_TELEMETRY_DISABLE")
 *     .systemVariable(true)
 *     .variableValue("0")
 *     .build();
 *
 * // Create a modified copy
 * Win32Environment updated = env.toBuilder()
 *     .variableValue("1")
 *     .build();
 *
 * }</pre>
 *
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-environment">Win32_Environment Documentation</a>
 * @since 3.0.0
 */
@Value
@Builder(toBuilder = true)
public class Win32Environment {

    /**
     * Character string that specifies the name of a Windows-based environment variable.
     */
    @SerializedName("Name")
    @Nullable
    String name;

    /**
     * Indicates whether the variable is a system variable.
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("SystemVariable")
    @Nullable
    Boolean systemVariable;
    /**
     * Placeholder variable of a Windows-based environment variable.
     * Information like the file system directory can change from computer to computer.
     * The operating system substitutes placeholders for these.
     */
    @SerializedName("VariableValue")
    @Nullable
    String variableValue;

    public @Nullable Boolean isSystemVariable() {
        return systemVariable;
    }

    /**
     * Retrieves the entity in a JSON pretty-print formatted string
     *
     * @return the {@link String} value of the object in JSON pretty-print format
     */
    @Override
    @NotNull
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}
