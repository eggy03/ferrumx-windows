package io.github.eggy03.ferrumx.windows.service.network;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.CimQuery;
import io.github.eggy03.ferrumx.windows.entity.network.Win32NetworkAdapterConfiguration;
import io.github.eggy03.ferrumx.windows.mapping.network.Win32NetworkAdapterConfigurationMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching network adapter configuration information from the system.
 * <p>
 * This class executes the {@link CimQuery#NETWORK_ADAPTER_CONFIGURATION_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link Win32NetworkAdapterConfiguration} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32NetworkAdapterConfigurationService configService = new Win32NetworkAdapterConfigurationService();
 * List<Win32NetworkAdapterConfiguration> configs = configService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     Win32NetworkAdapterConfigurationService configService = new Win32NetworkAdapterConfigurationService();
 *     List<Win32NetworkAdapterConfiguration> configs = configService.get(session);
 * }
 * }</pre>
 * @since 2.0.0
 * @author Egg-03
 */

public class Win32NetworkAdapterConfigurationService implements CommonServiceInterface<Win32NetworkAdapterConfiguration> {

    /**
     * Retrieves a list of network adapter configurations present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32NetworkAdapterConfiguration} objects representing the system's network adapters.
     *         Returns an empty list if no configurations are detected.
     * @since 2.2.0
     */
    @NotNull
    @Override
    public List<Win32NetworkAdapterConfiguration> get() {

        PowerShellResponse response = PowerShell.executeSingleCommand(CimQuery.NETWORK_ADAPTER_CONFIGURATION_QUERY.getQuery());
        return new Win32NetworkAdapterConfigurationMapper().mapToList(response.getCommandOutput(), Win32NetworkAdapterConfiguration.class);
    }

    /**
     * Retrieves a list of network adapter configurations using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link Win32NetworkAdapterConfiguration} objects representing the system's network adapters.
     *         Returns an empty list if no configurations are detected.
     * @since 2.2.0
     */
    @NotNull
    @Override
    public List<Win32NetworkAdapterConfiguration> get(PowerShell powerShell) {

        PowerShellResponse response = powerShell.executeCommand(CimQuery.NETWORK_ADAPTER_CONFIGURATION_QUERY.getQuery());
        return new Win32NetworkAdapterConfigurationMapper().mapToList(response.getCommandOutput(), Win32NetworkAdapterConfiguration.class);
    }

}
