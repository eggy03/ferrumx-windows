/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.entity.display;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of a GPU device on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_VideoController} WMI class.
 * </p>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 * <p>
 * Hardware that is not compatible with Windows Display Driver Model (WDDM) returns inaccurate
 * property values for instances of this class.
 * </p>
 * <h2>Usage example</h2>
 * <pre>{@code
 * // Build a new instance
 * Win32VideoController gpu = Win32VideoController.builder()
 *     .deviceId("GPU1")
 *     .name("AMD Radeon HD 5450")
 *     .currentRefreshRate(60)
 *     .build();
 *
 * // Modify using toBuilder (copy-on-write)
 * Win32VideoController updated = gpu.toBuilder()
 *     .currentRefreshRate(144)
 *     .build();
 * }</pre>
 *
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-videocontroller">Win32_VideoController Documentation</a>
 * @since 3.0.0
 */

@Value
@Builder(toBuilder = true)
public class Win32VideoController {

    /**
     * Identifier (unique to the computer system) for this video controller.
     */
    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    /**
     * Label by which the video controller is known.
     */
    @SerializedName("Name")
    @Nullable
    String name;

    /**
     * Windows Plug and Play device identifier of the video controller.
     * <p>
     * Example: "*PNP030b"
     * </p>
     */
    @SerializedName("PNPDeviceID")
    @Nullable
    String pnpDeviceId;

    /**
     * Number of bits used to display each pixel.
     */
    @SerializedName("CurrentBitsPerPixel")
    @Nullable
    Integer currentBitsPerPixel;

    /**
     * Current number of horizontal pixels.
     */
    @SerializedName("CurrentHorizontalResolution")
    @Nullable
    Integer currentHorizontalResolution;

    /**
     * Current number of vertical pixels.
     */
    @SerializedName("CurrentVerticalResolution")
    @Nullable
    Integer currentVerticalResolution;

    /**
     * Frequency at which the video controller refreshes the image for the monitor.
     */
    @SerializedName("CurrentRefreshRate")
    @Nullable
    Integer currentRefreshRate;

    /**
     * Maximum refresh rate of the video controller in hertz.
     */
    @SerializedName("MaxRefreshRate")
    @Nullable
    Integer maxRefreshRate;

    /**
     * Minimum refresh rate of the video controller in hertz.
     */
    @SerializedName("MinRefreshRate")
    @Nullable
    Integer minRefreshRate;

    /**
     * Name or identifier of the digital-to-analog converter (DAC) chip.
     * The character set of this property is alphanumeric.
     */
    @SerializedName("AdapterDACType")
    @Nullable
    String adapterDacType;

    /**
     * Memory size of the video adapter in bytes.
     * <p>
     * Example: 64000
     * </p>
     */
    @SerializedName("AdapterRAM")
    @Nullable
    Long adapterRam;

    /**
     * Last modification date and time of the currently installed video driver.
     */
    @SerializedName("DriverDate")
    @Nullable
    String driverDate;

    /**
     * Version number of the video driver.
     */
    @SerializedName("DriverVersion")
    @Nullable
    String driverVersion;

    /**
     * Free-form string describing the video processor.
     * <p>Example {@code AMD Radeon HD 5450}</p>
     */
    @SerializedName("VideoProcessor")
    @Nullable
    String videoProcessor;

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