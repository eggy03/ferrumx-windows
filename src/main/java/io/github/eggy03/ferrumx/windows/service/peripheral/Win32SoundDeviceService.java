/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.service.peripheral;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.namespace.Cimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.peripheral.Win32SoundDevice;
import io.github.eggy03.ferrumx.windows.mapping.peripheral.Win32SoundDeviceMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching sound device information from the system.
 * <p>
 * This class executes the {@link Cimv2Namespace#WIN32_SOUND_DEVICE_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link Win32SoundDevice} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of this class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32SoundDeviceService soundService = new Win32SoundDeviceService();
 * List<Win32SoundDevice> devices = soundService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     List<Win32SoundDevice> devices = soundService.get(session);
 * }
 * }</pre>
 *
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
@Slf4j
public class Win32SoundDeviceService implements CommonServiceInterface<Win32SoundDevice> {

    /**
     * Retrieves a list of sound devices present on the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32SoundDevice} objects representing the system's sound devices.
     *         If no sound devices are present, returns an empty list.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32SoundDevice> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(Cimv2Namespace.WIN32_SOUND_DEVICE_QUERY.getQuery());
        log.trace("Powershell response for auto-managed session :\n{}", response.getCommandOutput());
        return new Win32SoundDeviceMapper().mapToList(response.getCommandOutput(), Win32SoundDevice.class);
    }

    /**
     * Retrieves a list of sound devices present on the system using the caller's
     * {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link Win32SoundDevice} objects representing the system's sound devices.
     *         If no sound devices are present, returns an empty list.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32SoundDevice> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(Cimv2Namespace.WIN32_SOUND_DEVICE_QUERY.getQuery());
        log.trace("Powershell response for self-managed session :\n{}", response.getCommandOutput());
        return new Win32SoundDeviceMapper().mapToList(response.getCommandOutput(), Win32SoundDevice.class);
    }
}
