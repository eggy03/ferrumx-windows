package io.github.eggy03.ferrumx.windows.service.display;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.CimQuery;
import io.github.eggy03.ferrumx.windows.entity.display.VideoController;
import io.github.eggy03.ferrumx.windows.mapping.display.VideoControllerMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching video controller (GPU) information from the system.
 * <p>
 * This class executes the {@link CimQuery#VIDEO_CONTROLLER_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link VideoController} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * VideoControllerService videoControllerService = new VideoControllerService();
 * List<VideoController> controllers = videoControllerService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     VideoControllerService videoControllerService = new VideoControllerService();
 *     List<VideoController> controllers = videoControllerService.get(session);
 * }
 * }</pre>
 * @since 2.0.0
 * @author Egg-03
 */

public class VideoControllerService implements CommonServiceInterface<VideoController> {

    /**
     * Retrieves a list of video controllers (GPUs) present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link VideoController} objects representing the video controllers.
     *         Returns an empty list if none are detected.
     * @throws com.google.gson.JsonSyntaxException if there is an error executing the PowerShell command
     *                          or parsing the output.
     */
    @NotNull
    @Override
    public List<VideoController> get() {

        PowerShellResponse response = PowerShell.executeSingleCommand(CimQuery.VIDEO_CONTROLLER_QUERY.getQuery());
        return new VideoControllerMapper().mapToList(response.getCommandOutput(), VideoController.class);
    }

    /**
     * Retrieves a list of video controllers (GPUs) present in the system using the caller's
     * {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link VideoController} objects representing the video controllers.
     *         Returns an empty list if none are detected.
     * @throws com.google.gson.JsonSyntaxException if there is an error executing the PowerShell command
     *                          or parsing the output.
     */
    @NotNull
    @Override
    public List<VideoController> get(PowerShell powerShell) {

        PowerShellResponse response = powerShell.executeCommand(CimQuery.VIDEO_CONTROLLER_QUERY.getQuery());
        return new VideoControllerMapper().mapToList(response.getCommandOutput(), VideoController.class);
    }
}