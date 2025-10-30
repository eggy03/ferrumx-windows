package io.github.eggy03.ferrumx.windows.entity.network;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Immutable representation of a DNS server configuration for a particular network adapter on a Windows system.
 * <p>
 *      Fields correspond to properties retrieved from the {@code MSFT_DNSClientServerAddress} class in the
 *      {@code root/StandardCimv2} namespace.
 * </p>
 * <p>
 *     Together, with {@link MsftNetIpAddress}, this class aims to be
 *     a replacement for {@link Win32NetworkAdapterConfiguration}
 * </p>
 * <p>Instances of this class are thread-safe.</p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * MsftDnsClientServerAddress dns = MsftDnsClientServerAddress.builder()
 *     .interfaceIndex(1)
 *     .interfaceAlias("Ethernet")
 *     .addressFamily(23)
 *     .build();
 *
 * // Create a modified copy
 * MsftDnsClientServerAddress updated = dns.toBuilder()
 *     .addressFamily(2)
 *     .build();
 * }</pre>
 *
 * <p>See {@link MsftNetAdapter}, for network adapter information.</p>
 * <p>See {@link MsftNetConnectionProfile}, for information regarding the current profile of a network adapter.</p>
 * <p>See {@link MsftNetIpAddress}, for IP address configuration information of a network adapter.</p>
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/fwp/wmi/dnsclientcimprov/msft-dnsclientserveraddress">MSFT_DNSClientServerAddress Documentation</a>
 * @since 2.3.0
 * @author Egg-03
 */
@Value
@Builder(toBuilder = true)
public class MsftDnsClientServerAddress {

    @SerializedName("InterfaceIndex")
    @Nullable
    Long interfaceIndex;

    @SerializedName("InterfaceAlias")
    @Nullable
    String interfaceAlias;

    @SerializedName("AddressFamily")
    @Nullable
    Integer addressFamily;

    @SerializedName("ServerAddresses")
    @Nullable
    List<String> dnsServerAddresses;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}
