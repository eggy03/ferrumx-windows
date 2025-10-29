package io.github.eggy03.ferrumx.windows.entity.network;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable legacy representation of a network adapter on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_NetworkAdapter} WMI class.
 * </p>
 * <p>
 * Instances are thread-safe and may be safely cached or shared across threads.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * NetworkAdapter adapter = NetworkAdapter.builder()
 *     .name("Ethernet 1")
 *     .macAddress("00:1A:2B:3C:4D:5E")
 *     .netEnabled(true)
 *     .build();
 *
 * // Create a modified copy
 * NetworkAdapter updated = adapter.toBuilder()
 *     .netEnabled(false)
 *     .build();
 * }</pre>
 *
 * {@link NetworkAdapterConfiguration} contains related network configuration details.
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-networkadapter">Win32_NetworkAdapter</a>
 * @since 2.0.0
 * @author Egg-03
 */

@Value
@Builder(toBuilder = true)
public class NetworkAdapter {

    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    @SerializedName("Index")
    @Nullable
    Integer index;

    @SerializedName("Name")
    @Nullable
    String name;

    @SerializedName("Description")
    @Nullable
    String description;

    @SerializedName("PNPDeviceID")
    @Nullable
    String pnpDeviceId;

    @SerializedName("MACAddress")
    @Nullable
    String macAddress;

    @SerializedName("Installed")
    @Nullable
    Boolean installed;

    @SerializedName("NetEnabled")
    @Nullable
    Boolean netEnabled;

    @SerializedName("NetConnectionID")
    @Nullable
    String netConnectionId;

    @SerializedName("PhysicalAdapter")
    @Nullable
    Boolean physicalAdapter;

    @SerializedName("TimeOfLastReset")
    @Nullable
    String timeOfLastReset;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}