/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.constant.namespace;

import com.profesorfalken.jpowershell.PowerShell;
import io.github.eggy03.ferrumx.windows.entity.network.MsftDnsClientServerAddress;
import io.github.eggy03.ferrumx.windows.entity.network.MsftNetAdapter;
import io.github.eggy03.ferrumx.windows.entity.network.MsftNetConnectionProfile;
import io.github.eggy03.ferrumx.windows.entity.network.MsftNetIpAddress;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import static io.github.eggy03.ferrumx.windows.constant.PowerShellCmdlets.CONVERT_TO_JSON;
import static io.github.eggy03.ferrumx.windows.constant.PowerShellCmdlets.SELECT_OBJECT_PROPERTY;
import static io.github.eggy03.ferrumx.windows.utility.ReflectionUtility.getFromSerializedNames;

/**
 * Enum representing the predefined WMI (CIM) queries for the classes available in the {@code root/StandardCimv2} namespace.
 * <p>
 * Each constant holds a PowerShell query that queries a specific class in the namespace
 * and returns the result in JSON format. These queries are typically executed
 * using {@link PowerShell} and mapped to
 * corresponding Java objects.
 * </p>
 *
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 * @since 3.0.0
 */
@RequiredArgsConstructor
@Getter
public enum StandardCimv2Namespace {

    /**
     * Query to fetch the properties of the {@code MSFT_NetAdapter} class
     * <p>Will not show hidden physical or logical network adapters unless explicitly stated</p>
     *
     * @since 3.0.0
     */
    MSFT_NET_ADAPTER_QUERY("Get-NetAdapter" +
            SELECT_OBJECT_PROPERTY.getCmdlet() + getFromSerializedNames(MsftNetAdapter.class) +
            CONVERT_TO_JSON.getCmdlet()),

    /**
     * Query to fetch the properties of the {@code MSFT_NetIPAddress} class
     *
     * @since 3.0.0
     */
    MSFT_NET_IP_ADDRESS_QUERY("Get-NetIPAddress" +
            SELECT_OBJECT_PROPERTY.getCmdlet() + getFromSerializedNames(MsftNetIpAddress.class) +
            CONVERT_TO_JSON.getCmdlet()),

    /**
     * Query to fetch the properties of the {@code MSFT_NetDNSClientServerAddress} class
     *
     * @since 3.0.0
     */
    MSFT_NET_DNS_CLIENT_SERVER_ADDRESS_QUERY("Get-DNSClientServerAddress" +
            SELECT_OBJECT_PROPERTY.getCmdlet() + getFromSerializedNames(MsftDnsClientServerAddress.class) +
            CONVERT_TO_JSON.getCmdlet()),

    /**
     * Query to fetch the properties of the {@code MSFT_NetConnectionProfile} class
     *
     * @since 3.0.0
     */
    MSFT_NET_CONNECTION_PROFILE_QUERY("Get-NetConnectionProfile" +
            SELECT_OBJECT_PROPERTY.getCmdlet() + getFromSerializedNames(MsftNetConnectionProfile.class) +
            CONVERT_TO_JSON.getCmdlet());

    @NonNull
    private final String query;
}
