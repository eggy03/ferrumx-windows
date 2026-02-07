/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.entity.network;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable legacy representation of a network adapter on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_NetworkAdapter} WMI class.
 * </p>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * Win32NetworkAdapter adapter = Win32NetworkAdapter.builder()
 *     .name("Ethernet 1")
 *     .macAddress("00:1A:2B:3C:4D:5E")
 *     .netEnabled(true)
 *     .build();
 *
 * // Create a modified copy
 * Win32NetworkAdapter updated = adapter.toBuilder()
 *     .netEnabled(false)
 *     .build();
 * }</pre>
 * <p>
 * {@link Win32NetworkAdapterConfiguration} contains related network configuration details.
 *
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-networkadapter">Win32_NetworkAdapter Documentation</a>
 * @since 3.0.0
 */

@Value
@Builder(toBuilder = true)
public class Win32NetworkAdapter {

    /**
     * Unique identifier of the network adapter within the system.
     */
    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    /**
     * Index number of the network adapter, stored in the system registry.
     */
    @SerializedName("Index")
    @Nullable
    Integer index;

    /**
     * Friendly name of the network adapter.
     * <p>Example: {@code "Intel(R) Ethernet Connection"}</p>
     */
    @SerializedName("Name")
    @Nullable
    String name;

    /**
     * Text description of the network adapter.
     */
    @SerializedName("Description")
    @Nullable
    String description;

    /**
     * Windows Plug-and-Play device identifier for the network adapter.
     */
    @SerializedName("PNPDeviceID")
    @Nullable
    String pnpDeviceId;

    /**
     * Media access control (MAC) address for this adapter.
     */
    @SerializedName("MACAddress")
    @Nullable
    String macAddress;

    /**
     * Indicates whether the network adapter is installed in the system.
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("Installed")
    @Nullable
    Boolean installed;
    /**
     * Indicates whether the network adapter is currently enabled.
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("NetEnabled")
    @Nullable
    Boolean netEnabled;
    /**
     * Name of the network connection as displayed in the Network Connections Control Panel.
     */
    @SerializedName("NetConnectionID")
    @Nullable
    String netConnectionId;
    /**
     * Indicates whether the adapter represents a physical or logical device.
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("PhysicalAdapter")
    @Nullable
    Boolean physicalAdapter;
    /**
     * Date and time the network adapter was last reset.
     */
    @SerializedName("TimeOfLastReset")
    @Nullable
    String timeOfLastReset;

    public @Nullable Boolean isInstalled() {
        return installed;
    }

    public @Nullable Boolean isNetEnabled() {
        return netEnabled;
    }

    public @Nullable Boolean isPhysicalAdapter() {
        return physicalAdapter;
    }

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