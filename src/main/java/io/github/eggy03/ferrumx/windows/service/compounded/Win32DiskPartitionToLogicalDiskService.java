package io.github.eggy03.ferrumx.windows.service.compounded;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.PowerShellScriptPath;
import io.github.eggy03.ferrumx.windows.entity.compounded.Win32DiskPartitionToLogicalDisk;
import io.github.eggy03.ferrumx.windows.mapping.compounded.Win32DiskPartitionToLogicalDiskMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Win32DiskPartitionToLogicalDiskService implements CommonServiceInterface<Win32DiskPartitionToLogicalDisk> {

    @Override
    @NotNull
    public List<Win32DiskPartitionToLogicalDisk> get() {
        try(PowerShell shell = PowerShell.openSession()){
            PowerShellResponse response = shell.executeScript(PowerShellScriptPath.WIN32_DISK_PARTITION_TO_LOGICAL_DISK_SCRIPT.getPath());
            return new Win32DiskPartitionToLogicalDiskMapper().mapToList(response.getCommandOutput(), Win32DiskPartitionToLogicalDisk.class);
        }
    }

    @Override
    @NotNull
    public List<Win32DiskPartitionToLogicalDisk> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeScript(PowerShellScriptPath.WIN32_DISK_PARTITION_TO_LOGICAL_DISK_SCRIPT.getPath());
        return new Win32DiskPartitionToLogicalDiskMapper().mapToList(response.getCommandOutput(), Win32DiskPartitionToLogicalDisk.class);
    }
}
