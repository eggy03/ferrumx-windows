/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.service.network;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.namespace.StandardCimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.network.MsftNetAdapter;
import io.github.eggy03.ferrumx.windows.mapping.network.MsftNetAdapterMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import io.github.eggy03.ferrumx.windows.utility.TerminalUtility;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching network adapter information from the system.
 * <p>
 * This class executes the {@link StandardCimv2Namespace#MSFT_NET_ADAPTER_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link MsftNetAdapter} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * MsftNetAdapterService adapterService = new MsftNetAdapterService();
 * List<MsftNetAdapter> adapters = adapterService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     MsftNetAdapterService adapterService = new MsftNetAdapterService();
 *     List<MsftNetAdapter> adapters = adapterService.get(session);
 * }
 * }</pre>
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
@Slf4j
public class MsftNetAdapterService implements CommonServiceInterface<MsftNetAdapter> {

    /**
     * Retrieves a list of network adapters present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link MsftNetAdapter} objects representing the system's network adapters.
     *         Returns an empty list if no adapters are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<MsftNetAdapter> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(StandardCimv2Namespace.MSFT_NET_ADAPTER_QUERY.getQuery());
        log.trace("PowerShell response for auto-managed session :\n{}", response.getCommandOutput());
        return new MsftNetAdapterMapper().mapToList(response.getCommandOutput(), MsftNetAdapter.class);
    }

    /**
     * Retrieves a list of network adapters using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link MsftNetAdapter} objects representing the system's network adapters.
     *         Returns an empty list if no adapters are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<MsftNetAdapter> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(StandardCimv2Namespace.MSFT_NET_ADAPTER_QUERY.getQuery());
        log.trace("PowerShell response for self-managed session :\n{}", response.getCommandOutput());
        return new MsftNetAdapterMapper().mapToList(response.getCommandOutput(), MsftNetAdapter.class);
    }

    @Override
    public List<MsftNetAdapter> get(long timeout) {
        String command = StandardCimv2Namespace.MSFT_NET_ADAPTER_QUERY.getQuery();
        String response = TerminalUtility.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new MsftNetAdapterMapper().mapToList(response, MsftNetAdapter.class);
    }
}
