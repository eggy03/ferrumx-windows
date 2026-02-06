/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.entity.peripheral;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Immutable representation of a Printing device on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_Printer} WMI class.
 * </p>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * // Build a new instance
 * Win32Printer printer = Win32Printer.builder()
 *     .deviceId("PR1")
 *     .name("Primary Printer")
 *     .isShared(true)
 *     .shareName("Shared Primary Printer")
 *     .build();
 *
 * // Modify using toBuilder()
 * Win32Printer updated = printer.toBuilder()
 *     .isShared(false)
 *     .shareName(null)
 *     .build();
 * }</pre>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-printer">Win32_Printer Documentation</a>
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
@Value
@Builder(toBuilder = true)
public class Win32Printer {

    /**
     * System-assigned unique identifier of the printer.
     */
    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    /**
     * Name of the printer as recognized by the system.
     */
    @SerializedName("Name")
    @Nullable
    String name;

    /**
     * Windows Plug and Play device identifier
     */
    @SerializedName("PNPDeviceID")
    @Nullable
    String pnpDeviceId;

    /**
     * List of capability codes supported by the printer.
     * <ul>
     *   <li>1 - Other</li>
     *   <li>2 - Color printing</li>
     *   <li>3 - Duplex printing</li>
     *   <li>4 - Copies</li>
     *   <li>5 - Collation</li>
     *   <li>6 - Stapling</li>
     *   <li>7 - Transparency printing</li>
     *   <li>8 - Punch</li>
     *   <li>9 - Cover</li>
     *   <li>10 - Bind</li>
     *   <li>11 - Black-and-white printing</li>
     *   <li>12 - One-sided</li>
     *   <li>13 - Two-sided long edge</li>
     *   <li>14 - Two-sided short edge</li>
     *   <li>15 - Portrait</li>
     *   <li>16 - Landscape</li>
     *   <li>17 - Reverse Portrait</li>
     *   <li>18 - Reverse Landscape</li>
     *   <li>19 - Quality High</li>
     *   <li>20 - Quality Normal</li>
     *   <li>21 - Quality Low</li>
     * </ul>
     */
    @SerializedName("Capabilities")
    @Nullable
    List<Integer> capabilities;

    /**
     * Descriptive text corresponding to {@link #capabilities}.
     */
    @SerializedName("CapabilityDescriptions")
    @Nullable
    List<String> capabilityDescriptions;

    /**
     * Printer’s horizontal resolution in DPI (dots per inch).
     */
    @SerializedName("HorizontalResolution")
    @Nullable
    Long horizontalResolution;

    /**
     * Printer’s vertical resolution in DPI (dots per inch).
     */
    @SerializedName("VerticalResolution")
    @Nullable
    Long verticalResolution;

    /**
     * Numeric codes of paper sizes supported by the printer.
     * Refer to the documentation link attached at the class level for the exhaustive list of available sizes
     */
    @SerializedName("PaperSizesSupported")
    @Nullable
    List<Integer> paperSizesSupported;

    /**
     *  Names of paper types or forms supported by the printer.
     */
    @SerializedName("PrinterPaperNames")
    @Nullable
    List<String> printerPaperNames;

    /**
     * Current operational state of the printer.
     * <ul>
     *   <li>1 - Other</li>
     *   <li>2 - Unknown</li>
     *   <li>3 - Idle</li>
     *   <li>4 - Printing</li>
     *   <li>5 - Warm-up</li>
     *   <li>6 - Stopped printing</li>
     *   <li>7 - Offline</li>
     * </ul>
     */
    @SerializedName("PrinterStatus")
    @Nullable
    Integer printerStatus;

    /** Data type of print jobs
     *  Example: RAW or EMF
     */
    @SerializedName("PrintJobDataType")
    @Nullable
    String printJobDataType;

    /** Print processor used to process print jobs
     *  Example: WinPrint
     */
    @SerializedName("PrintProcessor")
    @Nullable
    String printProcessor;

    /**
     * Name of the printer driver installed.
     */
    @SerializedName("DriverName")
    @Nullable
    String driverName;

    /**
     * Indicates whether the printer is shared on the network.
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("Shared")
    @Nullable
    Boolean shared;
    public @Nullable Boolean isShared() {return shared;}

    /**
     *  Share name of the printer if it is shared.
     */
    @SerializedName("ShareName")
    @Nullable
    String shareName;

    /**
     * Indicates whether spooling is enabled for the printer.
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("SpoolEnabled")
    @Nullable
    Boolean spoolEnabled;
    public @Nullable Boolean hasSpoolEnabled() {return spoolEnabled;}

    /**
     * Specifies whether the printer is hidden from standard user interfaces.
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("Hidden")
    @Nullable
    Boolean hidden;
    public @Nullable Boolean isHidden() {return hidden;}

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