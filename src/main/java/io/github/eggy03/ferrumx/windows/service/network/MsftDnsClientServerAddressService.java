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
import io.github.eggy03.ferrumx.windows.utility.TerminalUtility;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching DNS Client and Server information for a network adapter.
 * <p>
 * This class executes the {@link StandardCimv2Namespace#MSFT_NET_DNS_CLIENT_SERVER_ADDRESS_QUERY} PowerShell command
 * and maps the resulting JSON into an immutable list of {@link MsftDnsClientServerAddress} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * MsftDnsClientServerAddressService service = new MsftDnsClientServerAddressService();
 * List<MsftDnsClientServerAddress> dns = service.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     MsftDnsClientServerAddressService service = new MsftDnsClientServerAddressService();
 *     List<MsftDnsClientServerAddress> dns = service.get(session);
 * }
 *
 * // API with execution timeout (auto-created session is terminated if the timeout is exceeded)
 * MsftDnsClientServerAddressService service = new MsftDnsClientServerAddressService();
 * List<MsftDnsClientServerAddress> dns = service.get(10);
 * }</pre>
 *
 * <h2>Execution models and concurrency</h2>
 * <p>
 * This service supports multiple PowerShell execution strategies:
 * </p>
 *
 * <ul>
 *   <li>
 *     <b>jPowerShell-based execution</b> via {@link #get()} and
 *     {@link #get(PowerShell)}:
 *     <br>
 *     These methods rely on {@code jPowerShell} sessions. Due to internal
 *     global configuration of {@code jPowerShell}, the PowerShell sessions
 *     launched by it is <b>not safe to use concurrently across multiple
 *     threads or executors</b>. Running these methods in parallel may result
 *     in runtime exceptions.
 *   </li>
 *
 *   <li>
 *     <b>Isolated PowerShell execution</b> via {@link #get(long timeout)}:
 *     <br>
 *     This method doesn't rely on {@code jPowerShell} and instead, launches a
 *     standalone PowerShell process per invocation using
 *     {@link TerminalUtility}. Each call is fully isolated and
 *     <b>safe to use in multithreaded and executor-based environments</b>.
 *   </li>
 * </ul>
 *
 * <p>
 * For concurrent or executor-based workloads, prefer {@link #get(long timeout)}.
 * </p>
 *
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 * @since 3.0.0
 */
@Slf4j
public class MsftDnsClientServerAddressService implements CommonServiceInterface<MsftDnsClientServerAddress> {

    /**
     * Retrieves an immutable list of DNS Server and Client configuration for all network adapters present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return an immutable list of {@link MsftDnsClientServerAddress} objects representing the DNS configs.
     * Returns an empty list if no configs are detected.
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<MsftDnsClientServerAddress> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(StandardCimv2Namespace.MSFT_NET_DNS_CLIENT_SERVER_ADDRESS_QUERY.getQuery());
        log.trace("PowerShell response for auto-managed session :\n{}", response.getCommandOutput());
        return new MsftDnsClientServerAddressMapper().mapToList(response.getCommandOutput(), MsftDnsClientServerAddress.class);
    }

    /**
     * Retrieves an immutable list of DNS Server and Client configuration for all network adapters
     * present in the system using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return an immutable list of {@link MsftDnsClientServerAddress} objects representing the DNS configs.
     * Returns an empty list if no configs are detected.
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<MsftDnsClientServerAddress> get(@NonNull PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(StandardCimv2Namespace.MSFT_NET_DNS_CLIENT_SERVER_ADDRESS_QUERY.getQuery());
        log.trace("PowerShell response for self-managed session :\n{}", response.getCommandOutput());
        return new MsftDnsClientServerAddressMapper().mapToList(response.getCommandOutput(), MsftDnsClientServerAddress.class);
    }

    /**
     * Retrieves an immutable list of DNS Server and Client configuration for all network adapters present in the system
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an immutable list of {@link MsftDnsClientServerAddress} objects representing the DNS configs.
     * Returns an empty list if no configs are detected.
     * @since 3.1.0
     */
    @NotNull
    @Override
    public List<MsftDnsClientServerAddress> get(long timeout) {
        String command = StandardCimv2Namespace.MSFT_NET_DNS_CLIENT_SERVER_ADDRESS_QUERY.getQuery();
        String response = TerminalUtility.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new MsftDnsClientServerAddressMapper().mapToList(response, MsftDnsClientServerAddress.class);
    }
}
