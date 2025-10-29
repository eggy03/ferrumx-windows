package io.github.eggy03.ferrumx.windows.entity.network;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable modern representation of a network adapter on a Windows system.
 * <p>
 *      Fields correspond to properties retrieved from the {@code MSFT_NetAdapter} class in the
 *      {@code root/StandardCimv2} namespace. This class is a modern replacement for the now deprecated
 *      {@code Win32_NetworkAdapter} WMI class in the {@code root/cimv2} namespace.
 * </p>
 * <p>
 *     However, the {@link NetworkAdapter} class which represents {@code Win32_NetworkAdapter},
 *     will remain accessible for backwards compatibility will not be marked as deprecated by the library at this time.
 * </p>
 * <p>
 *     Unlike {@link NetworkAdapter} which stores it's configuration in {@link NetworkAdapterConfiguration} and
 *     requires {@link NetworkAdapterSetting} to establish an association between them, {@link MsftNetAdapter} stores configuration
 *     inside {@link MsftNetIpAddress}, {@link MsftDnsClientServerAddress} and {@link MsftNetConnectionProfile}
 *     and all of them are directly linked via the {@code interfaceIndex} field.
 * </p>
 * <p>Instances of this class are thread-safe.</p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * MsftNetAdapter adapter = MsftNetAdapter.builder()
 *     .interfaceName("Ethernet1")
 *     .linkLayerAddress("00:1A:2B:3C:4D:5E")
 *     .linkSpeed("1Gbps)
 *     .build();
 *
 * // Create a modified copy
 * MsftNetAdapter updated = adapter.toBuilder()
 *     .linkSpeed("2.5Gbps")
 *     .build();
 * }</pre>
 *
 * <p>See {@link NetworkAdapter}, the now deprecated equivalent WMI class.</p>
 * <p>See {@link MsftNetIpAddress}, for IP address configuration information of a network adapter.</p>
 * <p>See {@link MsftNetConnectionProfile}, for information regarding the current profile of a network adapter.</p>
 * <p>See {@link MsftDnsClientServerAddress}, for configuration information regarding the DNS servers of a network adapter.</p>
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/fwp/wmi/netadaptercimprov/msft-netadapter">MSFT_NetAdapter Documentation</a>
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
    Long interfaceIndex;

    @SerializedName("InterfaceName")
    @Nullable
    String interfaceName;

    @SerializedName("InterfaceType")
    @Nullable
    Long interfaceType;

    @SerializedName("InterfaceDescription")
    @Nullable
    String interfaceDescription;

    @SerializedName("InterfaceAlias")
    @Nullable
    String interfaceAlias;

    @SerializedName("InterfaceOperationalStatus")
    @Nullable
    Long interfaceOperationalStatus;

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

    @SerializedName("LinkLayerAddress")
    @Nullable
    String linkLayerAddress;

    @SerializedName("LinkSpeed")
    @Nullable
    String linkSpeed;

    @SerializedName("ReceiveLinkSpeed")
    @Nullable
    Long receiveLinkSpeedRaw;

    @SerializedName("TransmitLinkSpeed")
    @Nullable
    Long transmitLinkSpeedRaw;

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
    Long mtuSize;

    @SerializedName("MediaConnectionState")
    @Nullable
    Long mediaConnectionState;

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
