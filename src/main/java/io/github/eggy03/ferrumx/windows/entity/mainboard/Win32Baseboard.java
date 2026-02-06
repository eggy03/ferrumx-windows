/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.entity.mainboard;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of a motherboard device on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_Baseboard} WMI class.
 * </p>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * Win32Baseboard board = Win32Baseboard.builder()
 *     .manufacturer("ASUS")
 *     .serialNumber("ABC123456")
 *     .build();
 *
 * // Create a modified copy
 * Win32Baseboard updated = board.toBuilder()
 *     .serialNumber("XYZ987654")
 *     .build();
 * }</pre>
 *
 * {@link Win32PortConnector} contains details about ports on this mainboard.
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-baseboard">Win32_Baseboard Documentation</a>
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */

@Value
@Builder(toBuilder = true)
public class Win32Baseboard {

    /**
     * Name of the organization responsible for producing the baseboard.
     */
    @SerializedName("Manufacturer")
    @Nullable
    String manufacturer;

    /**
     * Name by which the baseboard is known.
     */
    @SerializedName("Model")
    @Nullable
    String model;

    /**
     * Baseboard part number defined by the manufacturer.
     */
    @SerializedName("Product")
    @Nullable
    String product;

    /**
     * Manufacturer-allocated number used to identify the baseboard.
     */
    @SerializedName("SerialNumber")
    @Nullable
    String serialNumber;

    /**
     * Version of the baseboard.
     */
    @SerializedName("Version")
    @Nullable
    String version;

    /**
     * Retrieves the entity in a JSON pretty-print formatted string
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