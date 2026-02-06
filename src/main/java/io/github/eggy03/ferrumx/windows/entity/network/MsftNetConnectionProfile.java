/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.entity.network;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of a connection profile for a particular network adapter on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code MSFT_NetConnectionProfile} class in the
 * {@code root/StandardCimv2} namespace.
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
 *
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 * @see <a href="https://wutils.com/wmi/root/standardcimv2/msft_netconnectionprofile/">MSFT_NetConnectionProfile Documentation</a>
 * @since 3.0.0
 */
@Value
@Builder(toBuilder = true)
public class MsftNetConnectionProfile {

    /**
     * The interface index of the network interface on which the profile is connected.
     */
    @SerializedName("InterfaceIndex")
    @Nullable
    Long interfaceIndex;

    /**
     * The name of the network interface on which the profile is connected.
     * <p>Example: "Ethernet0"</p>
     */
    @SerializedName("InterfaceAlias")
    @Nullable
    String interfaceAlias;

    /**
     * The network category of the connected profile.
     * <p>Data type: uint32</p>
     * <p>Access type: Read-only</p>
     * <p>Possible values:</p>
     * <ul>
     *     <li>0 - Public</li>
     *     <li>1 - Private</li>
     *     <li>2 - DomainAuthenticated</li>
     * </ul>
     */
    @SerializedName("NetworkCategory")
    @Nullable
    Long networkCategory;

    /**
     * Indicates the domain authentication kind associated with the profile.
     * <p>WARNING: No existing documentation found about this field</p>
     */
    @SerializedName("DomainAuthenticationKind")
    @Nullable
    Long domainAuthenticationKind;

    /**
     * The IPv4 connectivity status of the connected profile.
     * <p>Possible values:</p>
     * <ul>
     *     <li>0 - Disconnected</li>
     *     <li>1 - NoTraffic</li>
     *     <li>2 - Subnet</li>
     *     <li>3 - LocalNetwork</li>
     *     <li>4 - Internet</li>
     * </ul>
     */
    @SerializedName("IPv4Connectivity")
    @Nullable
    Long ipv4Connectivity;

    /**
     * The IPv6 connectivity status of the connected profile.
     * <p>Possible values:</p>
     * <ul>
     *     <li>0 - Disconnected</li>
     *     <li>1 - NoTraffic</li>
     *     <li>2 - Subnet</li>
     *     <li>3 - LocalNetwork</li>
     *     <li>4 - Internet</li>
     * </ul>
     */
    @SerializedName("IPv6Connectivity")
    @Nullable
    Long ipv6Connectivity;

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
