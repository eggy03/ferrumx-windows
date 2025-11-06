package io.github.eggy03.ferrumx.windows.service.compounded;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.PowerShellScriptPath;
import io.github.eggy03.ferrumx.windows.entity.compounded.Win32ProcessorToCacheMemory;
import io.github.eggy03.ferrumx.windows.mapping.compounded.Win32ProcessorToCacheMemoryMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;

import java.util.List;

public class Win32ProcessorToCacheMemoryService implements CommonServiceInterface<Win32ProcessorToCacheMemory> {

    @Override
    public List<Win32ProcessorToCacheMemory> get() {
        try(PowerShell shell = PowerShell.openSession()){
            PowerShellResponse response = shell.executeScript(PowerShellScriptPath.WIN32_PROCESSOR_TO_CACHE_MEMORY_SCRIPT.getPath());
            return new Win32ProcessorToCacheMemoryMapper().mapToList(response.getCommandOutput(), Win32ProcessorToCacheMemory.class);
        }
    }

    @Override
    public List<Win32ProcessorToCacheMemory> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeScript(PowerShellScriptPath.WIN32_PROCESSOR_TO_CACHE_MEMORY_SCRIPT.getPath());
        return new Win32ProcessorToCacheMemoryMapper().mapToList(response.getCommandOutput(), Win32ProcessorToCacheMemory.class);
    }
}
