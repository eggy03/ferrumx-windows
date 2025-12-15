/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.service.network;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.namespace.Cimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.network.Win32NetworkAdapter;
import io.github.eggy03.ferrumx.windows.mapping.network.Win32NetworkAdapterMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import io.github.eggy03.ferrumx.windows.utility.TerminalUtility;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching network adapter information from the system.
 * <p>
 * This class executes the {@link Cimv2Namespace#WIN32_NETWORK_ADAPTER_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link Win32NetworkAdapter} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32NetworkAdapterService adapterService = new Win32NetworkAdapterService();
 * List<Win32NetworkAdapter> adapters = adapterService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     Win32NetworkAdapterService adapterService = new Win32NetworkAdapterService();
 *     List<Win32NetworkAdapter> adapters = adapterService.get(session);
 * }
 * }</pre>
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
@Slf4j
public class Win32NetworkAdapterService implements CommonServiceInterface<Win32NetworkAdapter> {

    /**
     * Retrieves a list of network adapters present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32NetworkAdapter} objects representing the system's network adapters.
     *         Returns an empty list if no adapters are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32NetworkAdapter> get() {

        PowerShellResponse response = PowerShell.executeSingleCommand(Cimv2Namespace.WIN32_NETWORK_ADAPTER_QUERY.getQuery());
        log.trace("PowerShell response for auto-managed session :\n{}", response.getCommandOutput());
        return new Win32NetworkAdapterMapper().mapToList(response.getCommandOutput(), Win32NetworkAdapter.class);
    }

    /**
     * Retrieves a list of network adapters using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link Win32NetworkAdapter} objects representing the system's network adapters.
     *         Returns an empty list if no adapters are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32NetworkAdapter> get(PowerShell powerShell) {

        PowerShellResponse response = powerShell.executeCommand(Cimv2Namespace.WIN32_NETWORK_ADAPTER_QUERY.getQuery());
        log.trace("PowerShell response for self-managed session :\n{}", response.getCommandOutput());
        return new Win32NetworkAdapterMapper().mapToList(response.getCommandOutput(), Win32NetworkAdapter.class);
    }

    @Override
    public List<Win32NetworkAdapter> get(long timeout) {
        String command = Cimv2Namespace.WIN32_NETWORK_ADAPTER_QUERY.getQuery();
        String response = TerminalUtility.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32NetworkAdapterMapper().mapToList(response, Win32NetworkAdapter.class);
    }
}
