package io.github.eggy03.ferrumx.windows.entity.peripheral;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Immutable representation of a battery device on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_Battery} WMI class.
 * </p>
 * <p>
 * Instances are inherently thread-safe and may be safely shared or cached across threads.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * // Build a new battery instance
 * Win32Battery battery = Win32Battery.builder()
 *     .deviceId("BAT0")
 *     .name("Primary Battery")
 *     .estimatedChargeRemaining(75)
 *     .build();
 *
 * // Modify using toBuilder (copy-on-write)
 * Win32Battery updated = battery.toBuilder()
 *     .estimatedChargeRemaining(50)
 *     .build();
 * }</pre>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-battery">Win32_Battery Documentation</a>
 * @since 3.0.0
 * @author Egg-03
 */

@Value
@Builder(toBuilder = true)
public class Win32Battery {

    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    @SerializedName("Caption")
    @Nullable
    String caption;

    @SerializedName("Description")
    @Nullable
    String description;

    @SerializedName("Name")
    @Nullable
    String name;

    @SerializedName("Status")
    @Nullable
    String status;

    @SerializedName("PowerManagementCapabilities")
    @Nullable
    List<String> powerManagementCapabilities;

    @SerializedName("PowerManagementSupported")
    @Nullable
    Boolean powerManagementSupported;

    @SerializedName("BatteryStatus")
    @Nullable
    Integer batteryStatus;

    @SerializedName("Chemistry")
    @Nullable
    Integer chemistry;

    @SerializedName("DesignCapacity")
    @Nullable
    Integer designCapacity;

    @SerializedName("DesignVoltage")
    @Nullable
    Integer designVoltage;

    @SerializedName("EstimatedChargeRemaining")
    @Nullable
    Integer estimatedChargeRemaining;

    @SerializedName("EstimatedRunTime")
    @Nullable
    Integer estimatedRunTime;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}