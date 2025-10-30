package io.github.eggy03.ferrumx.windows.service.mainboard;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.CimQuery;
import io.github.eggy03.ferrumx.windows.entity.mainboard.Win32PortConnector;
import io.github.eggy03.ferrumx.windows.mapping.mainboard.Win32PortConnectorMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching mainboard port information from the system.
 * <p>
 * This class executes the {@link CimQuery#MAINBOARD_PORT_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link Win32PortConnector} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32PortConnectorService portService = new Win32PortConnectorService();
 * List<Win32PortConnector> ports = portService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     Win32PortConnectorService portService = new Win32PortConnectorService();
 *     List<Win32PortConnector> ports = portService.get(session);
 * }
 * }</pre>
 * @since 2.0.0
 * @author Egg-03
 */

public class Win32PortConnectorService implements CommonServiceInterface<Win32PortConnector> {

    /**
     * Retrieves a list of mainboard ports present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     * @return a list of {@link Win32PortConnector} objects representing the system's mainboard ports.
     *         Returns an empty list if no ports are detected.
     *
     * @since 2.2.0
     */
    @NotNull
    @Override
    public List<Win32PortConnector> get() {

        PowerShellResponse response = PowerShell.executeSingleCommand(CimQuery.MAINBOARD_PORT_QUERY.getQuery());
        return new Win32PortConnectorMapper().mapToList(response.getCommandOutput(), Win32PortConnector.class);
    }

    /**
     * Retrieves a list of mainboard ports present in the system using the caller's
     * {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link Win32PortConnector} objects representing the system's mainboard ports.
     *         Returns an empty list if no ports are detected.
     *
     * @since 2.2.0
     */
    @NotNull
    @Override
    public List<Win32PortConnector> get(PowerShell powerShell) {

        PowerShellResponse response = powerShell.executeCommand(CimQuery.MAINBOARD_PORT_QUERY.getQuery());
        return new Win32PortConnectorMapper().mapToList(response.getCommandOutput(), Win32PortConnector.class);
    }
}
