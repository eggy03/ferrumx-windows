/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.service.network;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.namespace.StandardCimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.network.MsftDnsClientServerAddress;
import io.github.eggy03.ferrumx.windows.mapping.network.MsftDnsClientServerAddressMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching DNS Client and Server information for a network adapter.
 * <p>
 * This class executes the {@link StandardCimv2Namespace#MSFT_NET_DNS_CLIENT_SERVER_ADDRESS_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link MsftDnsClientServerAddress} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * MsftDnsClientServerAddressService dnsService = new MsftDnsClientServerAddressService();
 * List<MsftDnsClientServerAddress> dns = dnsService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     MsftDnsClientServerAddressService dnsService = new MsftDnsClientServerAddressService();
 *     List<MsftDnsClientServerAddress> dns = dnsService.get(session);
 * }
 * }</pre>
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
@Slf4j
public class MsftDnsClientServerAddressService implements CommonServiceInterface<MsftDnsClientServerAddress> {

    /**
     * Retrieves a list of DNS Server and Client configuration for all network adapters present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link MsftDnsClientServerAddress} objects representing the DNS configs.
     *         Returns an empty list if no configs are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<MsftDnsClientServerAddress> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(StandardCimv2Namespace.MSFT_NET_DNS_CLIENT_SERVER_ADDRESS_QUERY.getQuery());
        log.trace("Powershell response for auto-managed session :\n{}", response.getCommandOutput());
        return new MsftDnsClientServerAddressMapper().mapToList(response.getCommandOutput(), MsftDnsClientServerAddress.class);
    }

    /**
     * Retrieves a list of DNS Server and Client configuration for all network adapters
     * present in the system using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link MsftDnsClientServerAddress} objects representing the DNS configs.
     *         Returns an empty list if no configs are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<MsftDnsClientServerAddress> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(StandardCimv2Namespace.MSFT_NET_DNS_CLIENT_SERVER_ADDRESS_QUERY.getQuery());
        log.trace("Powershell response for self-managed session :\n{}", response.getCommandOutput());
        return new MsftDnsClientServerAddressMapper().mapToList(response.getCommandOutput(), MsftDnsClientServerAddress.class);
    }
}
