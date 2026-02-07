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
 * Immutable representation of IPv4 and IPv6 address configuration for a Network Adapter on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code MSFT_NetIPAddress} class in the
 * {@code root/StandardCimv2} namespace.
 * </p>
 * <p>
 * Together, with {@link MsftDnsClientServerAddress}, this class aims to be a
 * replacement for {@link Win32NetworkAdapterConfiguration}
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
 *
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/fwp/wmi/nettcpipprov/msft-netipaddress">MSFT_NetIPAddress Documentation</a>
 * @since 3.0.0
 */
@Value
@Builder(toBuilder = true)
public class MsftNetIpAddress {

    /**
     * Index of the network interface associated with this IP configuration.
     */
    @SerializedName("InterfaceIndex")
    @Nullable
    Long interfaceIndex;

    /**
     * User-friendly name of the network interface.
     */
    @SerializedName("InterfaceAlias")
    @Nullable
    String interfaceAlias;

    /**
     * Address family of the IP address.
     * <ul>
     *     <li>2 - IPv4</li>
     *     <li>23 - IPv6</li>
     * </ul>
     */
    @SerializedName("AddressFamily")
    @Nullable
    Long addressFamily;

    /**
     * The IP address assigned to the interface (can be IPv4 or IPv6 or both).
     */
    @SerializedName("IPAddress")
    @Nullable
    String ipAddress;

    /**
     * The IPv4 address assigned to the interface, if applicable.
     */
    @SerializedName("IPv4Address")
    @Nullable
    String ipv4Address;

    /**
     * The IPv6 address assigned to the interface, if applicable.
     */
    @SerializedName("IPv6Address")
    @Nullable
    String ipv6Address;

    /**
     * Type of IP address.
     * <ul>
     *     <li>1 - Unicast</li>
     *     <li>2 - Anycast</li>
     * </ul>
     */
    @SerializedName("Type")
    @Nullable
    Integer type;

    /**
     * Source of the prefix for this IP Address.
     * <ul>
     *     <li>0 - Other</li>
     *     <li>1 - Manual</li>
     *     <li>2 - WellKnown</li>
     *     <li>3 - DHCP</li>
     *     <li>4 - RouterAdvertisement</li>
     * </ul>
     */
    @SerializedName("PrefixOrigin")
    @Nullable
    Long prefixOrigin;

    /**
     * Source of the suffix for the IP address.
     * <ul>
     *     <li>0 - Other</li>
     *     <li>1 - Manual</li>
     *     <li>2 - WellKnown</li>
     *     <li>3 - DHCP</li>
     *     <li>4 - Link</li>
     *     <li>5 - Random</li>
     * </ul>
     */
    @SerializedName("SuffixOrigin")
    @Nullable
    Long suffixOrigin;

    /**
     * Length of the network prefix, in bits.
     */
    @SerializedName("PrefixLength")
    @Nullable
    Long prefixLength;

    /**
     * Lifetime during which the address is preferred for use.
     * The default value is infinite.
     */
    @SerializedName("PreferredLifetime")
    @Nullable
    Datetime preferredLifetime;

    /**
     * Total lifetime during which the address is valid.
     * The default value is infinite.
     */
    @SerializedName("ValidLifetime")
    @Nullable
    Datetime validLifeTime;

    /**
     * Prints the entity in a JSON pretty-print format
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
}
