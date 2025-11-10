package io.github.eggy03.ferrumx.windows.entity.display;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of a monitor device on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_DesktopMonitor} WMI class.
 * </p>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * // Build a new instance
 * Win32DesktopMonitor monitor = Win32DesktopMonitor.builder()
 *     .deviceId("MON1")
 *     .name("Generic PnP Monitor")
 *     .pixelsPerXLogicalInch(96)
 *     .build();
 *
 * // Modify using toBuilder (copy-on-write)
 * Win32DesktopMonitor updated = monitor.toBuilder()
 *     .pixelsPerXLogicalInch(120)
 *     .build();
 * }</pre>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-desktopmonitor">Win32_DesktopMonitor Documentation</a>
 * @since 3.0.0
 * @author Egg-03
 */

@Value
@Builder(toBuilder = true)
public class Win32DesktopMonitor {

    /**
     * Unique identifier of the desktop monitor on the system.
     * <p>
     * Example: {@code "DesktopMonitor1"}
     */
    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    /**
     * Label by which the object is known.
     * <p>
     * Example: {@code "Default Monitor"}
     */
    @SerializedName("Name")
    @Nullable
    String name;

    /**
     * Windows Plug and Play device identifier of the monitor.
     * <p>
     * Example: {@code "DISPLAY\\DELA0D1\\4&273ACF3E&0&UID1048858"}
     */
    @SerializedName("PNPDeviceID")
    @Nullable
    String pnpDeviceId;

    /**
     * Current operational status of the monitor device.
     * <p>Possible OPERATIONAL values:</p>
     * <ul>
     *   <li>"OK"</li>
     *   <li>"Degraded"</li>
     *   <li>"Pred Fail"</li>
     * </ul>
     * <p>Possible NON-OPERATIONAL values:</p>
     * <ul>
     *   <li>"Unknown"</li>
     *   <li>"Error"</li>
     *   <li>"Starting"</li>
     *   <li>"Stopping"</li>
     *   <li>"Service"</li>
     * </ul>
     * <p>Possible OTHER values:</p>
     * <ul>
     *   <li>"Stressed"</li>
     *   <li>"NonRecover"</li>
     *   <li>"No Contact"</li>
     *   <li>"Lost Comm"</li>
     * </ul>
     */
    @SerializedName("Status")
    @Nullable
    String status;

    /**
     * Name of the manufacturer of the monitor.
     * <p>
     * Example: {@code "NEC"}
     */
    @SerializedName("MonitorManufacturer")
    @Nullable
    String monitorManufacturer;

    /**
     * Type of monitor.
     * <p>
     * Example: {@code "NEC 5FGp"}
     */
    @SerializedName("MonitorType")
    @Nullable
    String monitorType;

    /**
     * Resolution along the x-axis (horizontal direction) of the monitor.
     */
    @SerializedName("PixelsPerXLogicalInch")
    @Nullable
    Integer pixelsPerXLogicalInch;

    /**
     * Resolution along the y-axis (vertical direction) of the monitor.
     */
    @SerializedName("PixelsPerYLogicalInch")
    @Nullable
    Integer pixelsPerYLogicalInch;

    /**
     * Prints the entity in a JSON pretty-print format
     * @return the {@link String} value of the object in JSON pretty-print format
     */
    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}