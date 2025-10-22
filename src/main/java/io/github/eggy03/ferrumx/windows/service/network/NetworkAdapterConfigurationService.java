package io.github.eggy03.ferrumx.windows.service.network;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.CimQuery;
import io.github.eggy03.ferrumx.windows.entity.network.NetworkAdapterConfiguration;
import io.github.eggy03.ferrumx.windows.mapping.network.NetworkAdapterConfigurationMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching network adapter configuration information from the system.
 * <p>
 * This class executes the {@link CimQuery#NETWORK_ADAPTER_CONFIGURATION_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link NetworkAdapterConfiguration} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * NetworkAdapterConfigurationService configService = new NetworkAdapterConfigurationService();
 * List<NetworkAdapterConfiguration> configs = configService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     NetworkAdapterConfigurationService configService = new NetworkAdapterConfigurationService();
 *     List<NetworkAdapterConfiguration> configs = configService.get(session);
 * }
 * }</pre>
 * @since 2.0.0
 * @author Egg-03
 */

public class NetworkAdapterConfigurationService implements CommonServiceInterface<NetworkAdapterConfiguration> {

    /**
     * Retrieves a list of network adapter configurations present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link NetworkAdapterConfiguration} objects representing the system's network adapters.
     *         Returns an empty list if no configurations are detected.
     */
    @NotNull
    @Override
    public List<NetworkAdapterConfiguration> get() {

        PowerShellResponse response = PowerShell.executeSingleCommand(CimQuery.NETWORK_ADAPTER_CONFIGURATION_QUERY.getQuery());
        return new NetworkAdapterConfigurationMapper().mapToList(response.getCommandOutput(), NetworkAdapterConfiguration.class);
    }

    /**
     * Retrieves a list of network adapter configurations using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link NetworkAdapterConfiguration} objects representing the system's network adapters.
     *         Returns an empty list if no configurations are detected.
     */
    @NotNull
    @Override
    public List<NetworkAdapterConfiguration> get(PowerShell powerShell) {

        PowerShellResponse response = powerShell.executeCommand(CimQuery.NETWORK_ADAPTER_CONFIGURATION_QUERY.getQuery());
        return new NetworkAdapterConfigurationMapper().mapToList(response.getCommandOutput(), NetworkAdapterConfiguration.class);
    }

}
