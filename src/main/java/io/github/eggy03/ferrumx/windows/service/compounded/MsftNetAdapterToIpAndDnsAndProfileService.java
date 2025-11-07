package io.github.eggy03.ferrumx.windows.service.compounded;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.PowerShellScriptPath;
import io.github.eggy03.ferrumx.windows.entity.compounded.MsftNetAdapterToIpAndDnsAndProfile;
import io.github.eggy03.ferrumx.windows.mapping.compounded.MsftNetAdapterToIpAndDnsAndProfileMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MsftNetAdapterToIpAndDnsAndProfileService implements CommonServiceInterface<MsftNetAdapterToIpAndDnsAndProfile> {

    @Override
    @NotNull
    public List<MsftNetAdapterToIpAndDnsAndProfile> get() {
        try(PowerShell shell = PowerShell.openSession()){
            PowerShellResponse response = shell.executeScript(PowerShellScriptPath.MSFT_NET_ADAPTER_TO_IP_AND_DNS_AND_PROFILE_SCRIPT.getPath());
            return new MsftNetAdapterToIpAndDnsAndProfileMapper().mapToList(response.getCommandOutput(), MsftNetAdapterToIpAndDnsAndProfile.class);
        }
    }

    @Override
    @NotNull
    public List<MsftNetAdapterToIpAndDnsAndProfile> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeScript(PowerShellScriptPath.MSFT_NET_ADAPTER_TO_IP_AND_DNS_AND_PROFILE_SCRIPT.getPath());
        return new MsftNetAdapterToIpAndDnsAndProfileMapper().mapToList(response.getCommandOutput(), MsftNetAdapterToIpAndDnsAndProfile.class);
    }
}
