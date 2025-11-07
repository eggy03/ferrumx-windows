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