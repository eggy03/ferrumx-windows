package io.github.eggy03.ferrumx.windows.entity.network;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of a connection profile for a particular network adapter on a Windows system.
 * <p>
 *      Fields correspond to properties retrieved from the {@code MSFT_NetConnectionProfile} class in the
 *      {@code root/StandardCimv2} namespace.
 * </p>
 * <p>Instances of this class are thread-safe.</p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * MsftNetConnectionProfile profile = MsftNetConnectionProfile.builder()
 *     .interfaceIndex(1)
 *     .networkCategory(0)
 *     .ipv4Connectivity(4)
 *     .ipv6Connectivity(1)
 *     .build();
 *
 * // Create a modified copy
 * MsftNetConnectionProfile updated = profile.toBuilder()
 *     .networkCategory(1)
 *     .build();
 * }</pre>
 *
 * <p>See {@link MsftNetAdapter}, for network adapter information.</p>
 * <p>See {@link MsftDnsClientServerAddress}, for information regarding the connected DNS servers of a network adapter.</p>
 * <p>See {@link MsftNetIpAddress}, for IP address configuration information of a network adapter.</p>
 * @see <a href="https://wutils.com/wmi/root/standardcimv2/msft_netconnectionprofile/">MSFT_NetConnectionProfile Documentation</a>
 * @since 2.3.0
 * @author Egg-03
 */
@Value
@Builder(toBuilder = true)
public class MsftNetConnectionProfile {

    @SerializedName("InterfaceIndex")
    @Nullable
    Long interfaceIndex;

    @SerializedName("InterfaceAlias")
    @Nullable
    String interfaceAlias;

    @SerializedName("NetworkCategory")
    @Nullable
    Long networkCategory;

    @SerializedName("DomainAuthenticationKind")
    @Nullable
    Long domainAuthenticationKind;

    @SerializedName("IPv4Connectivity")
    @Nullable
    Long ipv4Connectivity;

    @SerializedName("IPv6Connectivity")
    @Nullable
    Long ipv6Connectivity;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}
