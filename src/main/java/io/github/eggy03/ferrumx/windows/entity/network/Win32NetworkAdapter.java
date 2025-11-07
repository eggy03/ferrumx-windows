package io.github.eggy03.ferrumx.windows.entity.network;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
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
 *
 * {@link Win32NetworkAdapterConfiguration} contains related network configuration details.
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-networkadapter">Win32_NetworkAdapter Documentation</a>
 * @since 3.0.0
 * @author Egg-03
 */

@Value
@Builder(toBuilder = true)
public class Win32NetworkAdapter {

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

    @Getter(AccessLevel.NONE)
    @SerializedName("Installed")
    @Nullable
    Boolean installed;
    public @Nullable Boolean isInstalled(){ return installed;}

    @Getter(AccessLevel.NONE)
    @SerializedName("NetEnabled")
    @Nullable
    Boolean netEnabled;
    public @Nullable Boolean isNetEnabled() {return netEnabled;}

    @SerializedName("NetConnectionID")
    @Nullable
    String netConnectionId;

    @Getter(AccessLevel.NONE)
    @SerializedName("PhysicalAdapter")
    @Nullable
    Boolean physicalAdapter;
    public @Nullable Boolean isPhysicalAdapter() {return physicalAdapter;}

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