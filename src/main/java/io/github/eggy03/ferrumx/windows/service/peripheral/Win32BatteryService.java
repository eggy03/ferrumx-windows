package io.github.eggy03.ferrumx.windows.service.peripheral;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.Cimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.peripheral.Win32Battery;
import io.github.eggy03.ferrumx.windows.mapping.peripheral.Win32BatteryMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching battery information from the system.
 * <p>
 * This class executes the {@link Cimv2Namespace#BATTERY_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link Win32Battery} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32BatteryService batteryService = new Win32BatteryService();
 * List<Win32Battery> batteries = batteryService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     List<Win32Battery> batteries = batteryService.get(session);
 * }
 * }</pre>
 * @since 3.0.0
 * @author Egg-03
 */

public class Win32BatteryService implements CommonServiceInterface<Win32Battery> {

    /**
     * Retrieves a list of batteries present on the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32Battery} objects representing the system's batteries.
     *         If no batteries are present, returns an empty list.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32Battery> get() {

        PowerShellResponse response = PowerShell.executeSingleCommand(Cimv2Namespace.BATTERY_QUERY.getQuery());
        return new Win32BatteryMapper().mapToList(response.getCommandOutput(), Win32Battery.class);
    }

    /**
     * Retrieves a list of batteries present on the system using the caller's
     * {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link Win32Battery} objects representing the system's batteries.
     *         If no batteries are present, returns an empty list.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32Battery> get(PowerShell powerShell) {

        PowerShellResponse response = powerShell.executeCommand(Cimv2Namespace.BATTERY_QUERY.getQuery());
        return new Win32BatteryMapper().mapToList(response.getCommandOutput(), Win32Battery.class);
    }
}