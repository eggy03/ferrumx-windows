package io.github.eggy03.ferrumx.windows.entity.network;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of IPv4 and IPv6 address configuration for a Network Adapter on a Windows system.
 * <p>
 *      Fields correspond to properties retrieved from the {@code MSFT_NetIPAddress} class in the
 *      {@code root/StandardCimv2} namespace.
 * </p>
 * <p>
 *     Together, with {@link MsftDnsClientServerAddress}, this class aims to be a
 *     replacement for {@link Win32NetworkAdapterConfiguration}
 * </p>
 * <p>Instances of this class are thread-safe.</p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * MsftNetIpAddress address = MsftNetIpAddress.builder()
 *     .interfaceIndex(1)
 *     .addressFamily(0)
 *     .ipv4Address("192.168.0.210")
 *     .ipv6Address("fe80::abed:1234:5678:9abc")
 *     .build();
 *
 * // Create a modified copy
 * MsftNetIpAddress updated = address.toBuilder()
 *     .ipv4Address("192.168.0.255")
 *     .build();
 * }</pre>
 *
 * <p>See {@link MsftNetAdapter}, for network adapter information.</p>
 * <p>See {@link MsftDnsClientServerAddress}, for information regarding the connected DNS servers of a network adapter.</p>
 * <p>See {@link MsftNetConnectionProfile}, for information regarding the current profile of a network adapter.</p>
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/fwp/wmi/nettcpipprov/msft-netipaddress">MSFT_NetIPAddress Documentation</a>
 * @since 3.0.0
 * @author Egg-03
 */
@Value
@Builder(toBuilder = true)
public class MsftNetIpAddress {

    @SerializedName("InterfaceIndex")
    @Nullable
    Long interfaceIndex;

    @SerializedName("InterfaceAlias")
    @Nullable
    String interfaceAlias;

    @SerializedName("AddressFamily")
    @Nullable
    Long addressFamily;

    @SerializedName("IPAddress")
    @Nullable
    String ipAddress;

    @SerializedName("IPv4Address")
    @Nullable
    String ipv4Address;

    @SerializedName("IPv6Address")
    @Nullable
    String ipv6Address;

    @SerializedName("Type")
    @Nullable
    String type;

    @SerializedName("PrefixOrigin")
    @Nullable
    Long prefixOrigin;

    @SerializedName("SuffixOrigin")
    @Nullable
    Long suffixOrigin;

    @SerializedName("PrefixLength")
    @Nullable
    Long prefixLength;

    @SerializedName("PreferredLifetime")
    @Nullable
    Datetime preferredLifetime;

    @SerializedName("ValidLifetime")
    @Nullable
    Datetime validLifeTime;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }

    /**
     * Lifetime over which the address is preferred. The default value is infinite.
     */
    @Value
    @Builder(toBuilder = true)
    public static class Datetime {

        @SerializedName("Days")
        @Nullable
        Long days;

        @SerializedName("Hours")
        @Nullable
        Long hours;

        @SerializedName("Minutes")
        @Nullable
        Long minutes;

        @SerializedName("Seconds")
        @Nullable
        Long seconds;

        @Override
        public String toString() {
            return new GsonBuilder()
                    .serializeNulls()
                    .setPrettyPrinting()
                    .create()
                    .toJson(this);
        }
    }
}
