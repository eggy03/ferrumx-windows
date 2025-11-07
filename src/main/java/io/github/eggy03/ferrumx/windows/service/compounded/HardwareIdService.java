package io.github.eggy03.ferrumx.windows.service.compounded;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.PowerShellScriptPath;
import io.github.eggy03.ferrumx.windows.entity.compounded.HardwareId;
import io.github.eggy03.ferrumx.windows.mapping.compounded.HardwareIdMapper;
import io.github.eggy03.ferrumx.windows.service.OptionalCommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class HardwareIdService implements OptionalCommonServiceInterface<HardwareId> {

    @Override
    @NotNull
    public Optional<HardwareId> get() {
        try(PowerShell shell = PowerShell.openSession()){
            PowerShellResponse response = shell.executeScript(PowerShellScriptPath.HWID_SCRIPT.getPath());
            return new HardwareIdMapper().mapToObject(response.getCommandOutput(), HardwareId.class);
        }
    }

    @Override
    @NotNull
    public Optional<HardwareId> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeScript(PowerShellScriptPath.HWID_SCRIPT.getPath());
        return new HardwareIdMapper().mapToObject(response.getCommandOutput(), HardwareId.class);
    }
}