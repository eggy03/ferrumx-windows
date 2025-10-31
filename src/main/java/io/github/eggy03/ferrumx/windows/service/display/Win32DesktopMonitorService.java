package io.github.eggy03.ferrumx.windows.service.display;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.Cimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.display.Win32DesktopMonitor;
import io.github.eggy03.ferrumx.windows.mapping.display.Win32DesktopMonitorMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import lombok.NonNull;

import java.util.List;

/**
 * Service class for fetching monitor information from the system.
 * <p>
 * This class executes the {@link Cimv2Namespace#WIN32_DESKTOP_MONITOR_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link Win32DesktopMonitor} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32DesktopMonitorService monitorService = new Win32DesktopMonitorService();
 * List<Win32DesktopMonitor> monitors = monitorService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     List<Win32DesktopMonitor> monitors = monitorService.get(session);
 * }
 * }</pre>
 * @since 3.0.0
 * @author Egg-03
 */

public class Win32DesktopMonitorService implements CommonServiceInterface<Win32DesktopMonitor> {

    /**
     * Retrieves a list of monitors connected to the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32DesktopMonitor} objects representing connected monitors.
     *         Returns an empty list if no monitors are detected.
     *
     * @since 3.0.0
     */
    @NonNull
    @Override
    public List<Win32DesktopMonitor> get() {

        PowerShellResponse response = PowerShell.executeSingleCommand(Cimv2Namespace.WIN32_DESKTOP_MONITOR_QUERY.getQuery());
        return new Win32DesktopMonitorMapper().mapToList(response.getCommandOutput(), Win32DesktopMonitor.class);
    }

    /**
     * Retrieves a list of monitors connected to the system using the caller's
     * {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link Win32DesktopMonitor} objects representing connected monitors.
     *         Returns an empty list if no monitors are detected.
     *
     * @since 3.0.0
     */
    @NonNull
    @Override
    public List<Win32DesktopMonitor> get(PowerShell powerShell) {

        PowerShellResponse response = powerShell.executeCommand(Cimv2Namespace.WIN32_DESKTOP_MONITOR_QUERY.getQuery());
        return new Win32DesktopMonitorMapper().mapToList(response.getCommandOutput(), Win32DesktopMonitor.class);
    }
}
