/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.entity.network;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Immutable representation of a network adapter configuration on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_NetworkAdapterConfiguration} WMI class.
 * </p>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Create a new configuration using the builder
 * Win32NetworkAdapterConfiguration config = Win32NetworkAdapterConfiguration.builder()
 *     .index(1)
 *     .description("Ethernet Adapter")
 *     .ipEnabled(true)
 *     .ipAddress(List.of("192.168.1.100"))
 *     .dnsServerSearchOrder(List.of("8.8.8.8", "8.8.4.4"))
 *     .build();
 *
 * // Create a modified copy using the builder
 * Win32NetworkAdapterConfiguration updated = config.toBuilder()
 *     .description("Updated Ethernet Adapter")
 *     .build();
 *
 * }</pre>
 *
 * See {@link Win32NetworkAdapter} for the corresponding adapter entity
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-networkadapterconfiguration">Win32_NetworkAdapterConfiguration Documentation</a>
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */

@Value
@Builder(toBuilder = true)
public class Win32NetworkAdapterConfiguration {

    /**
     * Index number of the Windows network adapter configuration.
     * Used when multiple configurations exist.
     */
    @SerializedName("Index")
    @Nullable
    Integer index;

    /**
     * Textual description of the network adapter configuration.
     */
    @SerializedName("Description")
    @Nullable
    String description;

    /**
     * Short textual caption describing the object.
     */
    @SerializedName("Caption")
    @Nullable
    String caption;

    /**
     * Unique identifier by which the configuration instance is known.
     */
    @SerializedName("SettingID")
    @Nullable
    String settingId;

    /**
     * Indicates whether TCP/IP is bound and enabled on this adapter.
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("IPEnabled")
    @Nullable
    Boolean ipEnabled;
    public @Nullable Boolean isIPEnabled() {return ipEnabled;}

    /**
     * List of IP addresses associated with this network adapter.
     * May contain IPv4 and/or IPv6 addresses.
     */
    @SerializedName("IPAddress")
    @Nullable
    List<String> ipAddress;

    /**
     * Subnet masks associated with each IP address on this adapter.
     */
    @SerializedName("IPSubnet")
    @Nullable
    List<String> ipSubnet;

    /**
     * List of default gateway IP addresses used by this system.
     */
    @SerializedName("DefaultIPGateway")
    @Nullable
    List<String> defaultIpGateway;

    /**
     * Indicates whether DHCP is enabled for this adapter.
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("DHCPEnabled")
    @Nullable
    Boolean dhcpEnabled;
    public @Nullable Boolean isDHCPEnabled() {return dhcpEnabled;}

    /**
     * IP address of the DHCP server that assigned this configuration.
     */
    @SerializedName("DHCPServer")
    @Nullable
    String dhcpServer;

    /**
     * Date and time when the DHCP lease was obtained.
     */
    @SerializedName("DHCPLeaseObtained")
    @Nullable
    String dhcpLeaseObtained;

    /**
     * Date and time when the DHCP lease expires.
     */
    @SerializedName("DHCPLeaseExpires")
    @Nullable
    String dhcpLeaseExpires;

    /**
     * Host name used to identify this computer on the network.
     */
    @SerializedName("DNSHostName")
    @Nullable
    String dnsHostName;

    /**
     * List of DNS server IP addresses used for name resolution.
     */
    @SerializedName("DNSServerSearchOrder")
    @Nullable
    List<String> dnsServerSearchOrder;

    /**
     * Retrieves the entity in a JSON pretty-print formatted string
     * @return the {@link String} value of the object in JSON pretty-print format
     */
    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}