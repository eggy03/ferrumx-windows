package io.github.eggy03.ferrumx.windows.entity.peripheral;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of a Sound device on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_SoundDevice} WMI class.
 * </p>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * // Build a new instance
 * Win32SoundDevice device = Win32SoundDevice.builder()
 *     .deviceId("AUDIO\\0001")
 *     .name("Realtek High Definition Audio")
 *     .pnpDeviceId("HDAUDIO\\FUNC_01&VEN_10EC&DEV_0256&SUBSYS_10431A00&REV_1000")
 *     .manufacturer("Realtek Semiconductor Corp.")
 *     .status("OK")
 *     .statusInfo(3)
 *     .build();
 *
 * // Modify using toBuilder()
 * Win32SoundDevice updated = device.toBuilder()
 *     .status("Degraded")
 *     .statusInfo(4)
 *     .build();
 * }</pre>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-sounddevice">Win32_SoundDevice Documentation</a>
 * @since 3.0.0
 * @author Egg-03
 */
@Value
@Builder(toBuilder = true)
public class Win32SoundDevice {

    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    @SerializedName("PNPDeviceID")
    @Nullable
    String pnpDeviceId;

    @SerializedName("Name")
    @Nullable
    String name;

    @SerializedName("Manufacturer")
    @Nullable
    String manufacturer;

    @SerializedName("Status")
    @Nullable
    String status;

    @SerializedName("StatusInfo")
    @Nullable
    Integer statusInfo;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}
