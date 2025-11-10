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

    /**
     * Unique identifier of the sound device.
     */
    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    /**
     * Windows Plug and Play device identifier.
     */
    @SerializedName("PNPDeviceID")
    @Nullable
    String pnpDeviceId;

    /**
     * Friendly name of the sound device as recognized by the system.
     */
    @SerializedName("Name")
    @Nullable
    String name;

    /**
     * Manufacturer of the sound device.
     */
    @SerializedName("Manufacturer")
    @Nullable
    String manufacturer;

    /**
     * Current operational status of the sound device.
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
     * Numeric state of the logical device.
     * <ul>
     *   <li>1 - Other</li>
     *   <li>2 - Unknown</li>
     *   <li>3 - Enabled</li>
     *   <li>4 - Disabled</li>
     *   <li>5 - Not Applicable</li>
     * </ul>
     */
    @SerializedName("StatusInfo")
    @Nullable
    Integer statusInfo;

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
