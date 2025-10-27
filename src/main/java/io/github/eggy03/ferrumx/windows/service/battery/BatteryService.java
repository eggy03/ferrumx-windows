package io.github.eggy03.ferrumx.windows.service.battery;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.CimQuery;
import io.github.eggy03.ferrumx.windows.entity.battery.Battery;
import io.github.eggy03.ferrumx.windows.mapping.battery.BatteryMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching battery information from the system.
 * <p>
 * This class executes the {@link CimQuery#BATTERY_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link Battery} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * BatteryService batteryService = new BatteryService();
 * List<Battery> batteries = batteryService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     List<Battery> batteries = batteryService.get(session);
 * }
 * }</pre>
 * @since 2.0.0
 * @author Egg-03
 */

public class BatteryService implements CommonServiceInterface<Battery> {

    /**
     * Retrieves a list of batteries present on the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Battery} objects representing the system's batteries.
     *         If no batteries are present, returns an empty list.
     *
     * @since 2.2.0
     */
    @NotNull
    @Override
    public List<Battery> get() {

        PowerShellResponse response = PowerShell.executeSingleCommand(CimQuery.BATTERY_QUERY.getQuery());
        return new BatteryMapper().mapToList(response.getCommandOutput(), Battery.class);
    }

    /**
     * Retrieves a list of batteries present on the system using the caller's
     * {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link Battery} objects representing the system's batteries.
     *         If no batteries are present, returns an empty list.
     *
     * @since 2.2.0
     */
    @NotNull
    @Override
    public List<Battery> get(PowerShell powerShell) {

        PowerShellResponse response = powerShell.executeCommand(CimQuery.BATTERY_QUERY.getQuery());
        return new BatteryMapper().mapToList(response.getCommandOutput(), Battery.class);
    }
}