package io.github.eggy03.ferrumx.windows.entity.network;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable modern representation of a network adapter on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code MSFT_NetAdapter} class in the
 * {@code root/StandardCimv2} namespace. This class is a modern replacement for the now deprecated
 * {@code Win32_NetworkAdapter} WMI class in the {@code root/cimv2} namespace.
 * </p>
 * <p>However, the {@link NetworkAdapter} class which represents {@code Win32_NetworkAdapter},
 * will remain accessible for backwards compatibility will not be marked as deprecated by the library at this time.
 * </p>
 * <p>Instances of this class are thread-safe.</p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * MsftNetAdapter adapter = MsftNetAdapter.builder()
 *     .interfaceName("Ethernet1")
 *     .macAddress("00:1A:2B:3C:4D:5E")
 *     .linkSpeed("1Gbps)
 *     .build();
 *
 * // Create a modified copy
 * MsftNetAdapter updated = adapter.toBuilder()
 *     .linkSpeed("2.5Gbps")
 *     .build();
 * }</pre>
 *
 * See {@link NetworkAdapter}, the now deprecated equivalent WMI class.
 * @see <a href="https://learn.microsoft.com/en-us/previous-versions/windows/desktop/legacy/hh968170(v=vs.85)">MSFT_NetAdapter Documentation</a>
 * @since 2.3.0
 * @author Egg-03
 */
@Value
@Builder(toBuilder = true)
public class MsftNetAdapter {

    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    @SerializedName("PnPDeviceID")
    @Nullable
    String pnpDeviceId;

    @SerializedName("InterfaceIndex")
    @Nullable
    Integer interfaceIndex;

    @SerializedName("InterfaceName")
    @Nullable
    String interfaceName;

    @SerializedName("InterfaceType")
    @Nullable
    Integer interfaceType;

    @SerializedName("InterfaceDescription")
    @Nullable
    String interfaceDescription;

    @SerializedName("InterfaceAlias")
    @Nullable
    String interfaceAlias;

    @SerializedName("InterfaceOperationalStatus")
    @Nullable
    Integer interfaceOperationalStatus;

    @SerializedName("Virtual")
    @Nullable
    Boolean isVirtual;

    @SerializedName("FullDuplex")
    @Nullable
    Boolean isFullDuplex;

    @SerializedName("Hidden")
    @Nullable
    Boolean isHidden;

    @SerializedName("Status")
    @Nullable
    String status;

    @SerializedName("MacAddress")
    @Nullable
    String macAddress;

    @SerializedName("LinkSpeed")
    @Nullable
    String linkSpeed;

    @SerializedName("ReceiveLinkSpeed")
    @Nullable
    String receiveLinkSpeedRaw;

    @SerializedName("TransmitLinkSpeed")
    @Nullable
    String transmitLinkSpeedRaw;

    @SerializedName("DriverName")
    @Nullable
    String driverName;

    @SerializedName("DriverVersion")
    @Nullable
    String driverVersion;

    @SerializedName("DriverDate")
    @Nullable
    String driverDate;

    @SerializedName("MtuSize")
    @Nullable
    Integer mtuSize;

    @SerializedName("MediaConnectionState")
    @Nullable
    Integer mediaConnectionState;

    @SerializedName("MediaType")
    @Nullable
    String mediaType;

    @SerializedName("PhysicalMediaType")
    @Nullable
    String physicalMediaType;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}
