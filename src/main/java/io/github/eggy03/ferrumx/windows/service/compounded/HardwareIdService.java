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
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * HardwareIdService hwidService = new HardwareIdService();
 * Optional<HardwareId> hwid = hwidService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     HardwareIdService hwidService = new HardwareIdService();
 *     Optional<HardwareId> hwid = hwidService.get(session);
 * }
 * }</pre>
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
    public Optional<HardwareId> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeScript(PowerShellScript.getScriptAsBufferedReader(PowerShellScript.HWID_SCRIPT.getScriptPath()));
        log.trace("PowerShell response for self-managed session :\n{}", response.getCommandOutput());
        return new HardwareIdMapper().mapToObject(response.getCommandOutput(), HardwareId.class);
    }

    @Override
    public Optional<HardwareId> get(long timeout) {

        String script = PowerShellScript.getScript(PowerShellScript.HWID_SCRIPT.getScriptPath());
        String response = TerminalUtility.executeCommand(script, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new HardwareIdMapper().mapToObject(response, HardwareId.class);
    }
}