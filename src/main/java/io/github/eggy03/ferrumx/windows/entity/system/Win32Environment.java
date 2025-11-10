/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.entity.system;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
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
 *     .isSystemVariable(true)
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
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-environment">Win32_Environment Documentation</a>
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
@Value
@Builder(toBuilder = true)
public class Win32Environment {

    @SerializedName("Name")
    @Nullable
    String name;

    @SerializedName("SystemVariable")
    @Nullable
    Boolean isSystemVariable;

    @SerializedName("VariableValue")
    @Nullable
    String variableValue;

    @Override
    public String toString(){
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}
