/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.service.compounded;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.PowerShellScript;
import io.github.eggy03.ferrumx.windows.entity.compounded.HardwareId;
import io.github.eggy03.ferrumx.windows.mapping.compounded.HardwareIdMapper;
import io.github.eggy03.ferrumx.windows.service.OptionalCommonServiceInterface;
import io.github.eggy03.ferrumx.windows.utility.TerminalUtility;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Service class for fetching the HWID information from a system running Windows.
 * <p>
 * This class executes the {@link PowerShellScript#HWID_SCRIPT} PowerShell script
 * and maps the resulting JSON into an {@link Optional} {@link HardwareId} object.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * HardwareIdService service = new HardwareIdService();
 * Optional<HardwareId> hwid = service.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *       HardwareIdService service = new HardwareIdService();
 *       Optional<HardwareId> hwid = service.get(session);
 * }
 *
 * // API with execution timeout (auto-created session is terminated if the timeout is exceeded)
 * HardwareIdService service = new HardwareIdService();
 * Optional<HardwareId> hwid = service.get(10);
 * }</pre>
 *
 * <h2>Execution models and concurrency</h2>
 * <p>
 * This service supports multiple PowerShell execution strategies:
 * </p>
 *
 * <ul>
 *   <li>
 *     <b>jPowerShell-based execution</b> via {@link #get()} and
 *     {@link #get(PowerShell)}:
 *     <br>
 *     These methods rely on {@code jPowerShell} sessions. Due to internal
 *     global configuration of {@code jPowerShell}, the PowerShell sessions
 *     launched by it is <b>not safe to use concurrently across multiple
 *     threads or executors</b>. Running these methods in parallel may result
 *     in runtime exceptions.
 *   </li>
 *
 *   <li>
 *     <b>Isolated PowerShell execution</b> via {@link #get(long timeout)}:
 *     <br>
 *     This method doesn't rely on {@code jPowerShell} and instead, launches a
 *     standalone PowerShell process per invocation using
 *     {@link TerminalUtility}. Each call is fully isolated and
 *     <b>safe to use in multithreaded and executor-based environments</b>.
 *   </li>
 * </ul>
 *
 * <p>
 * For concurrent or executor-based workloads, prefer {@link #get(long timeout)}.
 * </p>
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
@Slf4j
public class HardwareIdService implements OptionalCommonServiceInterface<HardwareId> {

    /**
     * Retrieves an {@link Optional} containing the HWID information.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return an {@link Optional} of {@link HardwareId} representing
     *         the HWID. Returns {@link Optional#empty()} if no information is detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public Optional<HardwareId> get() {
        try(PowerShell shell = PowerShell.openSession()){
            PowerShellResponse response = shell.executeScript(PowerShellScript.getScriptAsBufferedReader(PowerShellScript.HWID_SCRIPT.getScriptPath()));
            log.trace("PowerShell response for auto-managed session :\n{}", response.getCommandOutput());
            return new HardwareIdMapper().mapToObject(response.getCommandOutput(), HardwareId.class);
        }
    }

    /**
     * Retrieves an {@link Optional} containing the HWID information
     * using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return an {@link Optional} of {@link HardwareId} representing
     *         the HWID. Returns {@link Optional#empty()} if no information is detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public Optional<HardwareId> get(@NonNull PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeScript(PowerShellScript.getScriptAsBufferedReader(PowerShellScript.HWID_SCRIPT.getScriptPath()));
        log.trace("PowerShell response for self-managed session :\n{}", response.getCommandOutput());
        return new HardwareIdMapper().mapToObject(response.getCommandOutput(), HardwareId.class);
    }

    /**
     * Retrieves an {@link Optional} containing the HWID information
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an {@link Optional} of {@link HardwareId} representing
     *         the HWID. Returns {@link Optional#empty()} if no information
     *         is detected.
     *
     * @since 3.1.0
     */
    @NotNull
    @Override
    public Optional<HardwareId> get(long timeout) {

        String script = PowerShellScript.getScript(PowerShellScript.HWID_SCRIPT.getScriptPath());
        String response = TerminalUtility.executeCommand(script, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new HardwareIdMapper().mapToObject(response, HardwareId.class);
    }
}