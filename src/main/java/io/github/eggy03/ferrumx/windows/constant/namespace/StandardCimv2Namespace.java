/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.constant.namespace;

import com.profesorfalken.jpowershell.PowerShell;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing the predefined WMI (CIM) queries for the classes available in the {@code root/StandardCimv2} namespace.
 * <p>
 * Each constant holds a PowerShell query that queries a specific class in the namespace
 * and returns the result in JSON format. These queries are typically executed
 * using {@link PowerShell} and mapped to
 * corresponding Java objects.
 * </p>
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
@RequiredArgsConstructor
@Getter
public enum StandardCimv2Namespace {

    /**
     * Query to fetch the properties of the {@code MSFT_NetAdapter} class
     * <p>Will not show hidden physical or logical network adapters unless explicitly stated</p>
     * @since 3.0.0
     */
    MSFT_NET_ADAPTER_QUERY("Get-NetAdapter | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of the {@code MSFT_NetIPAddress} class
     * @since 3.0.0
     */
    MSFT_NET_IP_ADDRESS_QUERY("Get-NetIPAddress | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of the {@code MSFT_NetDNSClientServerAddress} class
     * @since 3.0.0
     */
    MSFT_NET_DNS_CLIENT_SERVER_ADDRESS_QUERY("Get-DNSClientServerAddress | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of the {@code MSFT_NetConnectionProfile} class
     * @since 3.0.0
     */
    MSFT_NET_CONNECTION_PROFILE_QUERY("Get-NetConnectionProfile | Select-Object * | ConvertTo-Json");

    private final String query;
}
