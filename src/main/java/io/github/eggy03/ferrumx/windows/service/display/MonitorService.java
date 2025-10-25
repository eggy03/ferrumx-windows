package io.github.eggy03.ferrumx.windows.service.display;

import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.CimQuery;
import io.github.eggy03.ferrumx.windows.entity.display.Monitor;
import io.github.eggy03.ferrumx.windows.mapping.display.MonitorMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import lombok.NonNull;

import java.util.List;

/**
 * Service class for fetching monitor information from the system.
 * <p>
 * This class executes the {@link CimQuery#MONITOR_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link Monitor} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * MonitorService monitorService = new MonitorService();
 * List<Monitor> monitors = monitorService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     List<Monitor> monitors = monitorService.get(session);
 * }
 * }</pre>
 * @since 2.0.0
 * @author Egg-03
 */

public class MonitorService implements CommonServiceInterface<Monitor> {

    /**
     * Retrieves a list of monitors connected to the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Monitor} objects representing connected monitors.
     *         Returns an empty list if no monitors are detected.
     *
     * @since 2.2.0
     */
    @NonNull
    @Override
    public List<Monitor> get() {

        PowerShellResponse response = PowerShell.executeSingleCommand(CimQuery.MONITOR_QUERY.getQuery());
        return new MonitorMapper().mapToList(response.getCommandOutput(), Monitor.class);
    }

    /**
     * Retrieves a list of monitors connected to the system using the caller's
     * {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link Monitor} objects representing connected monitors.
     *         Returns an empty list if no monitors are detected.
     *
     * @since 2.2.0
     */
    @NonNull
    @Override
    public List<Monitor> get(PowerShell powerShell) {

        PowerShellResponse response = powerShell.executeCommand(CimQuery.MONITOR_QUERY.getQuery());
        return new MonitorMapper().mapToList(response.getCommandOutput(), Monitor.class);
    }
}
