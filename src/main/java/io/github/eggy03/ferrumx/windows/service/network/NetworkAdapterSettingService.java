package io.github.eggy03.ferrumx.windows.service.network;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.CimQuery;
import io.github.eggy03.ferrumx.windows.entity.network.NetworkAdapter;
import io.github.eggy03.ferrumx.windows.entity.network.NetworkAdapterConfiguration;
import io.github.eggy03.ferrumx.windows.entity.network.NetworkAdapterSetting;
import io.github.eggy03.ferrumx.windows.mapping.network.NetworkAdapterSettingMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;

import java.util.List;

/**
 * Service class for fetching the association between a Network Adapter, and it's Configuration from the system.
 * <p>
 * This class executes the {@link CimQuery#NETWORK_ADAPTER_SETTING_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link NetworkAdapterSetting} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * NetworkAdapterSettingService nasService = new NetworkAdapterSettingService();
 * List<NetworkAdapterSetting> nas = nasService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     NetworkAdapterSettingService nasService = new NetworkAdapterSettingService();
 *     List<NetworkAdapterSetting> nas = nasService.get(session);
 * }
 * }</pre>
 * @since 2.3.0
 * @author Egg-03
 */
public class NetworkAdapterSettingService implements CommonServiceInterface<NetworkAdapterSetting> {

    /**
     * Retrieves a list of {@link NetworkAdapterSetting} entities present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link NetworkAdapterSetting} objects representing the association between
     * a {@link NetworkAdapter} and it's {@link NetworkAdapterConfiguration}. Returns an empty list if none are detected.
     */
    @Override
    public List<NetworkAdapterSetting> get() {
        PowerShellResponse powerShellResponse = PowerShell.executeSingleCommand(CimQuery.NETWORK_ADAPTER_SETTING_QUERY.getQuery());
        return new NetworkAdapterSettingMapper().mapToList(powerShellResponse.getCommandOutput(), NetworkAdapterSetting.class);
    }

    /**
     * Retrieves a list of {@link NetworkAdapterSetting} entities using the caller's {@link PowerShell} session.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link NetworkAdapterSetting} objects representing the association between
     * a {@link NetworkAdapter} and it's {@link NetworkAdapterConfiguration}. Returns an empty list if none are detected.
     */
    @Override
    public List<NetworkAdapterSetting> get(PowerShell powerShell) {
        PowerShellResponse powerShellResponse = powerShell.executeCommand(CimQuery.NETWORK_ADAPTER_SETTING_QUERY.getQuery());
        return new NetworkAdapterSettingMapper().mapToList(powerShellResponse.getCommandOutput(), NetworkAdapterSetting.class);
    }
}