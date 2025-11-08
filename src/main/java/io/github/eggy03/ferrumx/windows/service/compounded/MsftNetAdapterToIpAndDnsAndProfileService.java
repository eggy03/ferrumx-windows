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
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching net adapter, ip, dns and profile configuration information from the system.
 * <p>
 * This class executes the {@link PowerShellScript#MSFT_NET_ADAPTER_TO_IP_AND_DNS_AND_PROFILE_SCRIPT} script
 * and maps the resulting JSON into a list of {@link MsftNetAdapterToIpAndDnsAndProfile} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * MsftNetAdapterToIpAndDnsAndProfileService compoundAdapterService = new MsftNetAdapterToIpAndDnsAndProfileService();
 * List<MsftNetAdapterToIpAndDnsAndProfile> compoundAdapters = compoundAdapterService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     List<MsftNetAdapterToIpAndDnsAndProfile> compoundAdapters = compoundAdapterService.get(session);
 * }
 * }</pre>
 * @see MsftNetAdapterService
 * @see MsftNetIpAddressService
 * @see MsftDnsClientServerAddressService
 * @see MsftNetConnectionProfileService
 * @see Win32NetworkAdapterToConfigurationService
 * @since 3.0.0
 * @author Egg-03
 */
public class MsftNetAdapterToIpAndDnsAndProfileService implements CommonServiceInterface<MsftNetAdapterToIpAndDnsAndProfile> {

    /**
     * Retrieves a list of adapters and their configs connected to the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link MsftNetAdapterToIpAndDnsAndProfile} objects representing
     * connected adapters with their configs. Returns an empty list if no data is found.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<MsftNetAdapterToIpAndDnsAndProfile> get() {
        try(PowerShell shell = PowerShell.openSession()){
            PowerShellResponse response = shell.executeScript(PowerShellScript.MSFT_NET_ADAPTER_TO_IP_AND_DNS_AND_PROFILE_SCRIPT.getPath());
            return new MsftNetAdapterToIpAndDnsAndProfileMapper().mapToList(response.getCommandOutput(), MsftNetAdapterToIpAndDnsAndProfile.class);
        }
    }

    /**
     * Retrieves a list of adapters and their configs connected to the system using the caller's
     * {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link MsftNetAdapterToIpAndDnsAndProfile} objects representing connected adapters
     * and their configs. Returns an empty list if no data is found.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<MsftNetAdapterToIpAndDnsAndProfile> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeScript(PowerShellScript.MSFT_NET_ADAPTER_TO_IP_AND_DNS_AND_PROFILE_SCRIPT.getPath());
        return new MsftNetAdapterToIpAndDnsAndProfileMapper().mapToList(response.getCommandOutput(), MsftNetAdapterToIpAndDnsAndProfile.class);
    }
}
