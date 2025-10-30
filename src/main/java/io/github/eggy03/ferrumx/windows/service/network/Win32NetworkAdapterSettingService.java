package io.github.eggy03.ferrumx.windows.service.network;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.Cimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.network.Win32NetworkAdapter;
import io.github.eggy03.ferrumx.windows.entity.network.Win32NetworkAdapterConfiguration;
import io.github.eggy03.ferrumx.windows.entity.network.Win32NetworkAdapterSetting;
import io.github.eggy03.ferrumx.windows.mapping.network.Win32NetworkAdapterSettingMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;

import java.util.List;

/**
 * Service class for fetching the association between a Network Adapter, and it's Configuration from the system.
 * <p>
 * This class executes the {@link Cimv2Namespace#NETWORK_ADAPTER_SETTING_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link Win32NetworkAdapterSetting} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32NetworkAdapterSettingService nasService = new Win32NetworkAdapterSettingService();
 * List<Win32NetworkAdapterSetting> nas = nasService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     Win32NetworkAdapterSettingService nasService = new Win32NetworkAdapterSettingService();
 *     List<Win32NetworkAdapterSetting> nas = nasService.get(session);
 * }
 * }</pre>
 * @since 2.3.0
 * @author Egg-03
 */
public class Win32NetworkAdapterSettingService implements CommonServiceInterface<Win32NetworkAdapterSetting> {

    /**
     * Retrieves a list of {@link Win32NetworkAdapterSetting} entities present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32NetworkAdapterSetting} objects representing the association between
     * a {@link Win32NetworkAdapter} and it's {@link Win32NetworkAdapterConfiguration}. Returns an empty list if none are detected.
     *
     * @since 2.3.0
     */
    @Override
    public List<Win32NetworkAdapterSetting> get() {
        PowerShellResponse powerShellResponse = PowerShell.executeSingleCommand(Cimv2Namespace.NETWORK_ADAPTER_SETTING_QUERY.getQuery());
        return new Win32NetworkAdapterSettingMapper().mapToList(powerShellResponse.getCommandOutput(), Win32NetworkAdapterSetting.class);
    }

    /**
     * Retrieves a list of {@link Win32NetworkAdapterSetting} entities using the caller's {@link PowerShell} session.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32NetworkAdapterSetting} objects representing the association between
     * a {@link Win32NetworkAdapter} and it's {@link Win32NetworkAdapterConfiguration}. Returns an empty list if none are detected.
     *
     * @since 2.3.0
     */
    @Override
    public List<Win32NetworkAdapterSetting> get(PowerShell powerShell) {
        PowerShellResponse powerShellResponse = powerShell.executeCommand(Cimv2Namespace.NETWORK_ADAPTER_SETTING_QUERY.getQuery());
        return new Win32NetworkAdapterSettingMapper().mapToList(powerShellResponse.getCommandOutput(), Win32NetworkAdapterSetting.class);
    }
}