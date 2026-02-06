/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.entity.system;

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
 * Immutable representation of a Plug and Play (PnP) device on Windows systems.
 * <p>
 * Fields correspond to properties retrieved from the WMI {@code Win32_PnPEntity} class.
 * Instances of this class represent entries as they would appear in the Windows Device Manager.
 * </p>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Build a new Plug and Play device instance
 * Win32PnPEntity device = Win32PnPEntity.builder()
 *     .deviceId("USB\\VID_045E&PID_07A5\\6&1A2C0F8&0&2")
 *     .name("USB Composite Device")
 *     .manufacturer("Microsoft")
 *     .present(true)
 *     .status("OK")
 *     .build();
 *
 * // Create a modified copy using the builder
 * Win32PnPEntity updatedDevice = device.toBuilder()
 *     .status("Degraded")
 *     .build();
 * }</pre>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-pnpentity">Win32_PnPEntity Documentation</a>
 * @since 3.0.0
 * @author
 *     Sayan Bhattacharjee (Egg-03/Eggy)
 */

@Value
@Builder(toBuilder = true)
public class Win32PnPEntity {

    /**
     * Identifier of the Plug and Play device.
     */
    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    /**
     * Windows Plug and Play device identifier of the logical device.
     */
    @SerializedName("PNPDeviceID")
    @Nullable
    String pnpDeviceId;

    /**
     * A vendor-defined list of hardware identification strings used by Windows Setup
     * to match the device to an INF file.
     */
    @SerializedName("HardwareID")
    @Nullable
    List<String> hardwareId;

    /**
     * A vendor-defined list of compatible identification strings that Windows Setup
     * uses as fallback identifiers when no matching hardware ID is found.
     */
    @SerializedName("CompatibleID")
    @Nullable
    List<String> compatibleId;

    /**
     * The name by which the device is known.
     */
    @SerializedName("Name")
    @Nullable
    String name;

    /**
     * A human-readable description of the device.
     */
    @SerializedName("Description")
    @Nullable
    String description;

    /**
     * Name of the manufacturer of the Plug and Play device.
     */
    @SerializedName("Manufacturer")
    @Nullable
    String manufacturer;

    /**
     * Indicates whether this Plug and Play device is currently present in the system.
     * Note: This property is not supported on Windows Server 2012 R2,
     * Windows 8.1, Windows Server 2012, Windows 8, Windows Server 2008 R2, Windows 7,
     * Windows Server 2008 and Windows Vista.
     */
    @SerializedName("Present")
    @Getter(AccessLevel.NONE)
    @Nullable
    Boolean present;
    public @Nullable Boolean isPresent() {
        return present;
    }

    /**
     * Current operational status of the PnP Device.
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
