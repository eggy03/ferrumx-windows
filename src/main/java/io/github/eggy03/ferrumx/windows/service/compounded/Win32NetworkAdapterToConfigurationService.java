/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.service.compounded;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.PowerShellScript;
import io.github.eggy03.ferrumx.windows.entity.compounded.Win32NetworkAdapterToConfiguration;
import io.github.eggy03.ferrumx.windows.mapping.compounded.Win32NetworkAdapterToConfigurationMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import io.github.eggy03.ferrumx.windows.service.network.Win32NetworkAdapterConfigurationService;
import io.github.eggy03.ferrumx.windows.service.network.Win32NetworkAdapterService;
import io.github.eggy03.ferrumx.windows.service.network.Win32NetworkAdapterSettingService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching network adapter and related configuration information from the system.
 * <p>
 * This class executes the {@link PowerShellScript#WIN32_NETWORK_ADAPTER_TO_CONFIGURATION_SCRIPT} script
 * and maps the resulting JSON into a list of {@link Win32NetworkAdapterToConfiguration} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32NetworkAdapterToConfigurationService netAdapConService = new Win32NetworkAdapterToConfigurationService();
 * List<Win32NetworkAdapterToConfiguration> netAdapConList = netAdapConService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     List<Win32NetworkAdapterToConfiguration> netAdapConList = netAdapConService.get(session);
 * }
 * }</pre>
 * @see Win32NetworkAdapterService
 * @see Win32NetworkAdapterConfigurationService
 * @see Win32NetworkAdapterSettingService
 * @see MsftNetAdapterToIpAndDnsAndProfileService
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
@Slf4j
public class Win32NetworkAdapterToConfigurationService implements CommonServiceInterface<Win32NetworkAdapterToConfiguration> {

    /**
     * Retrieves a list of network adapters and related configuration connected in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32NetworkAdapterToConfiguration} objects representing connected network adapter and related configuration.
     * Returns an empty list if no network adapter and related configuration are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32NetworkAdapterToConfiguration> get() {
        try(PowerShell shell = PowerShell.openSession()){
            PowerShellResponse response = shell.executeScript(PowerShellScript.WIN32_NETWORK_ADAPTER_TO_CONFIGURATION_SCRIPT.getScript());
            log.trace("Powershell response for auto-managed session :\n{}", response.getCommandOutput());
            return new Win32NetworkAdapterToConfigurationMapper().mapToList(response.getCommandOutput(), Win32NetworkAdapterToConfiguration.class);
        }
    }

    /**
     * Retrieves a list of network adapters and related configuration connected in the system using the caller's
     * {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link Win32NetworkAdapterToConfiguration} objects representing connected network adapter and related configuration.
     * Returns an empty list if no network adapter and related configuration are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32NetworkAdapterToConfiguration> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeScript(PowerShellScript.WIN32_NETWORK_ADAPTER_TO_CONFIGURATION_SCRIPT.getScript());
        log.trace("Powershell response for self-managed session :\n{}", response.getCommandOutput());
        return new Win32NetworkAdapterToConfigurationMapper().mapToList(response.getCommandOutput(), Win32NetworkAdapterToConfiguration.class);
    }
}
