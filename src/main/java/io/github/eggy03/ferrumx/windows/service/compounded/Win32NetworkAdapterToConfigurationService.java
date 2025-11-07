package io.github.eggy03.ferrumx.windows.service.compounded;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.PowerShellScriptPath;
import io.github.eggy03.ferrumx.windows.entity.compounded.Win32NetworkAdapterToConfiguration;
import io.github.eggy03.ferrumx.windows.mapping.compounded.Win32NetworkAdapterToConfigurationMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Win32NetworkAdapterToConfigurationService implements CommonServiceInterface<Win32NetworkAdapterToConfiguration> {

    @Override
    @NotNull
    public List<Win32NetworkAdapterToConfiguration> get() {
        try(PowerShell shell = PowerShell.openSession()){
            PowerShellResponse response = shell.executeScript(PowerShellScriptPath.WIN32_NETWORK_ADAPTER_TO_CONFIGURATION_SCRIPT.getPath());
            return new Win32NetworkAdapterToConfigurationMapper().mapToList(response.getCommandOutput(), Win32NetworkAdapterToConfiguration.class);
        }
    }

    @Override
    @NotNull
    public List<Win32NetworkAdapterToConfiguration> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeScript(PowerShellScriptPath.WIN32_NETWORK_ADAPTER_TO_CONFIGURATION_SCRIPT.getPath());
        return new Win32NetworkAdapterToConfigurationMapper().mapToList(response.getCommandOutput(), Win32NetworkAdapterToConfiguration.class);
    }
}
