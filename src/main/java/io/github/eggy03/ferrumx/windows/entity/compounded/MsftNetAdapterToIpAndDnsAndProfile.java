/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.entity.compounded;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import io.github.eggy03.ferrumx.windows.entity.network.MsftDnsClientServerAddress;
import io.github.eggy03.ferrumx.windows.entity.network.MsftNetAdapter;
import io.github.eggy03.ferrumx.windows.entity.network.MsftNetConnectionProfile;
import io.github.eggy03.ferrumx.windows.entity.network.MsftNetIpAddress;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

import java.util.List;
/**
 * Immutable representation of a {@link MsftNetAdapter} and its {@code 1:N} relationships
 * with {@link MsftNetIpAddress}, {@link MsftDnsClientServerAddress},
 * and {@link MsftNetConnectionProfile} configurations in a Windows system.
 * <p>
 * Each instance represents a single {@code NetAdapter} identified by {@link #interfaceIndex},
 * and maintains a one-to-many mapping with related
 * {@code IpAddress}, {@code DnsClientServerAddress},
 * and {@code ConnectionProfile} entities.
 * </p>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 * <h2>Usage example</h2>
 * <pre>{@code
 * MsftNetAdapterToIpAndDnsAndProfile adapterInfo = MsftNetAdapterToIpAndDnsAndProfile.builder()
 *     .interfaceIndex(12L)
 *     .adapter(msftNetAdapter)
 *     .ipAddressList(ipAddresses)
 *     .dnsClientServerAddressList(dnsServers)
 *     .netConnectionProfileList(connectionProfiles)
 *     .build();
 * }</pre>
 *
 * <p>
 * This is purely a convenience class designed to simplify data retrieval
 * for all related network configuration entities through a single call.
 * The individual entity classes remain accessible for direct use if you need to map everything by yourself.
 * </p>
 * <p>
 * An equivalent convenience class {@link Win32NetworkAdapterToConfiguration} is also available for use, although
 * {@code Win32_NetworkAdapter} is deprecated by Microsoft in favor of the MSFT classes.
 * </p>
 *
 * @see MsftNetAdapter
 * @see MsftNetIpAddress
 * @see MsftDnsClientServerAddress
 * @see MsftNetConnectionProfile
 *
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */

@Value
@Builder(toBuilder = true)
public class MsftNetAdapterToIpAndDnsAndProfile {

    @SerializedName("InterfaceIndex")
    @Nullable
    Long interfaceIndex;

    @SerializedName("NetworkAdapter")
    @Nullable
    MsftNetAdapter adapter;

    @SerializedName("IPAddresses")
    @Nullable
    List<MsftNetIpAddress> ipAddressList;

    @SerializedName("DNSServers")
    @Nullable
    List<MsftDnsClientServerAddress> dnsClientServerAddressList;

    @SerializedName("Profile")
    @Nullable
    List<MsftNetConnectionProfile> netConnectionProfileList;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}