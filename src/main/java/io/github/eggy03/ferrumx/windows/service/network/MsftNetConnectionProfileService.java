package io.github.eggy03.ferrumx.windows.service.network;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.StandardCimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.network.MsftNetConnectionProfile;
import io.github.eggy03.ferrumx.windows.mapping.network.MsftNetConnectionProfileMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching the connection profile for a network adapter.
 * <p>
 * This class executes the {@link StandardCimv2Namespace#MSFT_NET_CONNECTION_PROFILE_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link MsftNetConnectionProfile} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * MsftNetConnectionProfileService profileService = new MsftNetConnectionProfileService();
 * List<MsftNetConnectionProfile> profiles = profileService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *    MsftNetConnectionProfileService profileService = new MsftNetConnectionProfileService();
 *     List<MsftNetConnectionProfile> profiles = profileService.get(session);
 * }
 * }</pre>
 * @since 3.0.0
 * @author Egg-03
 */
public class MsftNetConnectionProfileService implements CommonServiceInterface<MsftNetConnectionProfile> {

    /**
     * Retrieves a list of connection profiles for all network adapters present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link MsftNetConnectionProfile} objects representing the connection profiles.
     *         Returns an empty list if no profiles are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<MsftNetConnectionProfile> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(StandardCimv2Namespace.MSFT_NET_CONNECTION_PROFILE_QUERY.getQuery());
        return new MsftNetConnectionProfileMapper().mapToList(response.getCommandOutput(), MsftNetConnectionProfile.class);
    }

    /**
     * Retrieves a list of connection profiles for all network adapters
     * present in the system using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link MsftNetConnectionProfile} objects representing the connection profiles.
     *         Returns an empty list if no profiles are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<MsftNetConnectionProfile> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(StandardCimv2Namespace.MSFT_NET_CONNECTION_PROFILE_QUERY.getQuery());
        return new MsftNetConnectionProfileMapper().mapToList(response.getCommandOutput(), MsftNetConnectionProfile.class);
    }
}
