package io.github.eggy03.ferrumx.windows.service.compounded;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.PowerShellScript;
import io.github.eggy03.ferrumx.windows.entity.compounded.Win32DiskDriveToPartitionAndLogicalDisk;
import io.github.eggy03.ferrumx.windows.mapping.compounded.Win32DiskDriveToPartitionAndLogicalDiskMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Win32DiskDriveToPartitionAndLogicalDiskService implements CommonServiceInterface<Win32DiskDriveToPartitionAndLogicalDisk> {

    @Override
    @NotNull
    public List<Win32DiskDriveToPartitionAndLogicalDisk> get() {
        try(PowerShell shell = PowerShell.openSession()){
            PowerShellResponse response = shell.executeScript(PowerShellScript.WIN32_DISK_DRIVE_TO_PARTITION_AND_LOGICAL_DISK_SCRIPT.getPath());
            return new Win32DiskDriveToPartitionAndLogicalDiskMapper().mapToList(response.getCommandOutput(), Win32DiskDriveToPartitionAndLogicalDisk.class);
        }
    }

    @Override
    @NotNull
    public List<Win32DiskDriveToPartitionAndLogicalDisk> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeScript(PowerShellScript.WIN32_DISK_DRIVE_TO_PARTITION_AND_LOGICAL_DISK_SCRIPT.getPath());
        return new Win32DiskDriveToPartitionAndLogicalDiskMapper().mapToList(response.getCommandOutput(), Win32DiskDriveToPartitionAndLogicalDisk.class);
    }
}
