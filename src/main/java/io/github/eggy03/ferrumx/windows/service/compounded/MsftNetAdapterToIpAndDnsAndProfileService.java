/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.service.compounded;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.PowerShellScript;
import io.github.eggy03.ferrumx.windows.entity.compounded.MsftNetAdapterToIpAndDnsAndProfile;
import io.github.eggy03.ferrumx.windows.mapping.compounded.MsftNetAdapterToIpAndDnsAndProfileMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import io.github.eggy03.ferrumx.windows.service.network.MsftDnsClientServerAddressService;
import io.github.eggy03.ferrumx.windows.service.network.MsftNetAdapterService;
import io.github.eggy03.ferrumx.windows.service.network.MsftNetConnectionProfileService;
import io.github.eggy03.ferrumx.windows.service.network.MsftNetIpAddressService;
import io.github.eggy03.ferrumx.windows.utility.TerminalUtility;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching net adapter, ip, dns and profile configuration information from the system.
 * <p>
 * This class executes the {@link PowerShellScript#MSFT_NET_ADAPTER_TO_IP_AND_DNS_AND_PROFILE_SCRIPT} script
 * and maps the resulting JSON into an immutable list of {@link MsftNetAdapterToIpAndDnsAndProfile} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * MsftNetAdapterToIpAndDnsAndProfileService service = new MsftNetAdapterToIpAndDnsAndProfileService();
 * List<MsftNetAdapterToIpAndDnsAndProfile> compoundAdapters = service.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     List<MsftNetAdapterToIpAndDnsAndProfile> compoundAdapters = service.get(session);
 * }
 *
 * // API with execution timeout (auto-created session is terminated if the timeout is exceeded)
 * MsftNetAdapterToIpAndDnsAndProfileService service = new MsftNetAdapterToIpAndDnsAndProfileService();
 * List<MsftNetAdapterToIpAndDnsAndProfile> compoundAdapters = service.get(10);
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
 * @see MsftNetAdapterService
 * @see MsftNetIpAddressService
 * @see MsftDnsClientServerAddressService
 * @see MsftNetConnectionProfileService
 * @see Win32NetworkAdapterToConfigurationService
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
@Slf4j
public class MsftNetAdapterToIpAndDnsAndProfileService implements CommonServiceInterface<MsftNetAdapterToIpAndDnsAndProfile> {

    /**
     * Retrieves an immutable list of adapters and their configs connected to the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return an immutable list of {@link MsftNetAdapterToIpAndDnsAndProfile} objects representing
     * connected adapters with their configs. Returns an empty list if no data is found.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<MsftNetAdapterToIpAndDnsAndProfile> get() {
        try(PowerShell shell = PowerShell.openSession()){
            PowerShellResponse response = shell.executeScript(PowerShellScript.getScriptAsBufferedReader(PowerShellScript.MSFT_NET_ADAPTER_TO_IP_AND_DNS_AND_PROFILE_SCRIPT.getScriptPath()));
            log.trace("PowerShell response for auto-managed session :\n{}", response.getCommandOutput());
            return new MsftNetAdapterToIpAndDnsAndProfileMapper().mapToList(response.getCommandOutput(), MsftNetAdapterToIpAndDnsAndProfile.class);
        }
    }

    /**
     * Retrieves an immutable list of adapters and their configs connected to the system using the caller's
     * {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return an immutable list of {@link MsftNetAdapterToIpAndDnsAndProfile} objects representing connected adapters
     * and their configs. Returns an empty list if no data is found.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<MsftNetAdapterToIpAndDnsAndProfile> get(@NonNull PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeScript(PowerShellScript.getScriptAsBufferedReader(PowerShellScript.MSFT_NET_ADAPTER_TO_IP_AND_DNS_AND_PROFILE_SCRIPT.getScriptPath()));
        log.trace("PowerShell response for self-managed session :\n{}", response.getCommandOutput());
        return new MsftNetAdapterToIpAndDnsAndProfileMapper().mapToList(response.getCommandOutput(), MsftNetAdapterToIpAndDnsAndProfile.class);
    }

    /**
     * Retrieves an immutable list of adapters and their configs connected to the system
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an immutable list of {@link MsftNetAdapterToIpAndDnsAndProfile} objects representing connected adapters
     * and their configs. Returns an empty list if no data is found.
     *
     * @since 3.1.0
     */
    @NotNull
    @Override
    public List<MsftNetAdapterToIpAndDnsAndProfile> get(long timeout) {

        String script = PowerShellScript.getScript(PowerShellScript.MSFT_NET_ADAPTER_TO_IP_AND_DNS_AND_PROFILE_SCRIPT.getScriptPath());
        String response = TerminalUtility.executeCommand(script, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new MsftNetAdapterToIpAndDnsAndProfileMapper().mapToList(response, MsftNetAdapterToIpAndDnsAndProfile.class);
    }
}
