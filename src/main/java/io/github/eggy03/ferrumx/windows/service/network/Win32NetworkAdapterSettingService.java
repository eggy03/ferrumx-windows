/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.service.network;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.namespace.Cimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.network.Win32NetworkAdapter;
import io.github.eggy03.ferrumx.windows.entity.network.Win32NetworkAdapterConfiguration;
import io.github.eggy03.ferrumx.windows.entity.network.Win32NetworkAdapterSetting;
import io.github.eggy03.ferrumx.windows.mapping.network.Win32NetworkAdapterSettingMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Service class for fetching the association between a Network Adapter, and it's Configuration from the system.
 * <p>
 * This class executes the {@link Cimv2Namespace#WIN32_NETWORK_ADAPTER_SETTING_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link Win32NetworkAdapterSetting} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32NetworkAdapterSettingService nasService = new Win32NetworkAdapterSettingService();
 * List<Win32NetworkAdapterSetting> nas = nasService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     Win32NetworkAdapterSettingService nasService = new Win32NetworkAdapterSettingService();
 *     List<Win32NetworkAdapterSetting> nas = nasService.get(session);
 * }
 * }</pre>
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
@Slf4j
public class Win32NetworkAdapterSettingService implements CommonServiceInterface<Win32NetworkAdapterSetting> {

    /**
     * Retrieves a list of {@link Win32NetworkAdapterSetting} entities present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32NetworkAdapterSetting} objects representing the association between
     * a {@link Win32NetworkAdapter} and it's {@link Win32NetworkAdapterConfiguration}. Returns an empty list if none are detected.
     *
     * @since 3.0.0
     */
    @Override
    public List<Win32NetworkAdapterSetting> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(Cimv2Namespace.WIN32_NETWORK_ADAPTER_SETTING_QUERY.getQuery());
        log.trace("Powershell response for auto-managed session :\n{}", response.getCommandOutput());
        return new Win32NetworkAdapterSettingMapper().mapToList(response.getCommandOutput(), Win32NetworkAdapterSetting.class);
    }

    /**
     * Retrieves a list of {@link Win32NetworkAdapterSetting} entities using the caller's {@link PowerShell} session.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32NetworkAdapterSetting} objects representing the association between
     * a {@link Win32NetworkAdapter} and it's {@link Win32NetworkAdapterConfiguration}. Returns an empty list if none are detected.
     *
     * @since 3.0.0
     */
    @Override
    public List<Win32NetworkAdapterSetting> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(Cimv2Namespace.WIN32_NETWORK_ADAPTER_SETTING_QUERY.getQuery());
        log.trace("Powershell response for self-managed session :\n{}", response.getCommandOutput());
        return new Win32NetworkAdapterSettingMapper().mapToList(response.getCommandOutput(), Win32NetworkAdapterSetting.class);
    }
}