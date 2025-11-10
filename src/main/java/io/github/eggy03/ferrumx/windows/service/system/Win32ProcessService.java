/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.service.system;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.Cimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.system.Win32Process;
import io.github.eggy03.ferrumx.windows.mapping.system.Win32ProcessMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;

import java.util.List;

/**
 * Service class for fetching process information from the system.
 * <p>
 * This class executes the {@link Cimv2Namespace#WIN32_PROCESS_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link Win32Process} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32ProcessService processService = new Win32ProcessService();
 * List<Win32Process> processList = processService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     Win32ProcessService processService = new Win32ProcessService();
 *     List<Win32Process> processList = processService.get(session);
 * }
 * }</pre>
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
public class Win32ProcessService implements CommonServiceInterface<Win32Process> {

    /**
     * Retrieves a list of processes present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32Process} objects representing the system's processes.
     *         Returns an empty list if none are detected.
     *
     * @since 3.0.0
     */
    @Override
    public List<Win32Process> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(Cimv2Namespace.WIN32_PROCESS_QUERY.getQuery());
        return new Win32ProcessMapper().mapToList(response.getCommandOutput(), Win32Process.class);
    }

    /**
     * Retrieves a list of processes using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link Win32Process} objects representing the system's processes.
     *         Returns an empty list if none are detected.
     *
     * @since 3.0.0
     */
    @Override
    public List<Win32Process> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(Cimv2Namespace.WIN32_PROCESS_QUERY.getQuery());
        return new Win32ProcessMapper().mapToList(response.getCommandOutput(), Win32Process.class);
    }
}
