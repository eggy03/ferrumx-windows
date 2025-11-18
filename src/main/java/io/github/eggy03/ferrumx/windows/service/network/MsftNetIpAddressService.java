/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.service.network;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.namespace.StandardCimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.network.MsftNetIpAddress;
import io.github.eggy03.ferrumx.windows.mapping.network.MsftNetIpAddressMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching the IPv4 and IPv6 configs for a network adapter.
 * <p>
 * This class executes the {@link StandardCimv2Namespace#MSFT_NET_IP_ADDRESS_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link MsftNetIpAddress} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * MsftNetIpAddressService addressService = new MsftNetIpAddressService();
 * List<MsftNetIpAddress> address = addressService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *    MsftNetIpAddressService addressService = new MsftNetIpAddressService();
 *     List<MsftNetIpAddress> address = addressService.get(session);
 * }
 * }</pre>
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
@Slf4j
public class MsftNetIpAddressService implements CommonServiceInterface<MsftNetIpAddress> {

    /**
     * Retrieves a list of IPv4 and IPv6 configs for all network adapters present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link MsftNetIpAddress} objects representing the IPv4 and IPv6 configs.
     *         Returns an empty list if no configs are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<MsftNetIpAddress> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(StandardCimv2Namespace.MSFT_NET_IP_ADDRESS_QUERY.getQuery());
        log.trace("Powershell response for auto-managed session :\n{}", response.getCommandOutput());
        return new MsftNetIpAddressMapper().mapToList(response.getCommandOutput(),MsftNetIpAddress.class);
    }

    /**
     * Retrieves a list of IPv4 and IPv6 configs for all network adapters
     * present in the system using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link MsftNetIpAddress} objects representing the IPv4 and IPv6 configs.
     *         Returns an empty list if no configs are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<MsftNetIpAddress> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(StandardCimv2Namespace.MSFT_NET_IP_ADDRESS_QUERY.getQuery());
        log.trace("Powershell response for self-managed session :\n{}", response.getCommandOutput());
        return new MsftNetIpAddressMapper().mapToList(response.getCommandOutput(),MsftNetIpAddress.class);
    }
}
