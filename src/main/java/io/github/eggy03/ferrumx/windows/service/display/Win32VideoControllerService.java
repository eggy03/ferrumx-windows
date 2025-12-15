/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.service.display;

import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.namespace.Cimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.display.Win32VideoController;
import io.github.eggy03.ferrumx.windows.mapping.display.Win32VideoControllerMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import io.github.eggy03.ferrumx.windows.utility.TerminalUtility;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching video controller (GPU) information from the system.
 * <p>
 * This class executes the {@link Cimv2Namespace#WIN32_VIDEO_CONTROLLER_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link Win32VideoController} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32VideoControllerService videoControllerService = new Win32VideoControllerService();
 * List<Win32VideoController> controllers = videoControllerService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     Win32VideoControllerService videoControllerService = new Win32VideoControllerService();
 *     List<Win32VideoController> controllers = videoControllerService.get(session);
 * }
 * }</pre>
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
@Slf4j
public class Win32VideoControllerService implements CommonServiceInterface<Win32VideoController> {

    /**
     * Retrieves a list of video controllers (GPUs) present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32VideoController} objects representing the video controllers.
     *         Returns an empty list if none are detected.
     * @throws JsonSyntaxException if there is an error executing the PowerShell command
     *                          or parsing the output.
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32VideoController> get() {

        PowerShellResponse response = PowerShell.executeSingleCommand(Cimv2Namespace.WIN32_VIDEO_CONTROLLER_QUERY.getQuery());
        log.trace("PowerShell response for auto-managed session :\n{}", response.getCommandOutput());
        return new Win32VideoControllerMapper().mapToList(response.getCommandOutput(), Win32VideoController.class);
    }

    /**
     * Retrieves a list of video controllers (GPUs) present in the system using the caller's
     * {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link Win32VideoController} objects representing the video controllers.
     *         Returns an empty list if none are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32VideoController> get(PowerShell powerShell) {

        PowerShellResponse response = powerShell.executeCommand(Cimv2Namespace.WIN32_VIDEO_CONTROLLER_QUERY.getQuery());
        log.trace("PowerShell response for self-managed session :\n{}", response.getCommandOutput());
        return new Win32VideoControllerMapper().mapToList(response.getCommandOutput(), Win32VideoController.class);
    }

    @Override
    public List<Win32VideoController> get(long timeout) {
        String command = Cimv2Namespace.WIN32_VIDEO_CONTROLLER_QUERY.getQuery();
        String response = TerminalUtility.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32VideoControllerMapper().mapToList(response, Win32VideoController.class);
    }
}