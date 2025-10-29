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
 * Instances are inherently thread-safe and may be safely shared or cached across threads.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * // Build a new monitor instance
 * Monitor monitor = Monitor.builder()
 *     .deviceId("MON1")
 *     .name("Generic PnP Monitor")
 *     .pixelsPerXLogicalInch(96)
 *     .build();
 *
 * // Modify using toBuilder (copy-on-write)
 * Monitor updated = monitor.toBuilder()
 *     .pixelsPerXLogicalInch(120)
 *     .build();
 * }</pre>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-desktopmonitor">Win32_DesktopMonitor</a>
 * @since 2.0.0
 * @author Egg-03
 */

@Value
@Builder(toBuilder = true)
public class Monitor {

    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    @SerializedName("Name")
    @Nullable
    String name;

    @SerializedName("PNPDeviceID")
    @Nullable
    String pnpDeviceId;

    @SerializedName("Status")
    @Nullable
    String status;

    @SerializedName("MonitorManufacturer")
    @Nullable
    String monitorManufacturer;

    @SerializedName("MonitorType")
    @Nullable
    String monitorType;

    @SerializedName("PixelsPerXLogicalInch")
    @Nullable
    Integer pixelsPerXLogicalInch;

    @SerializedName("PixelsPerYLogicalInch")
    @Nullable
    Integer pixelsPerYLogicalInch;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}