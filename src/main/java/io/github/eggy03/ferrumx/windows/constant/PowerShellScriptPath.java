package io.github.eggy03.ferrumx.windows.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
@Getter
public enum PowerShellScriptPath {

    MSFT_NET_ADAPTER_TO_IP_AND_DNS_AND_PROFILE_SCRIPT(Objects
            .requireNonNull(PowerShellScriptPath.class
                    .getResource("/MsftNetAdapterToIpAndDnsAndProfile.ps1")).getPath()),

    WIN32_NETWORK_ADAPTER_TO_CONFIGURATION_SCRIPT(Objects
            .requireNonNull(PowerShellScriptPath.class
                    .getResource("/Win32NetworkAdapterToConfiguration.ps1")).getPath()),

    WIN32_DISK_DRIVE_TO_PARTITION_AND_LOGICAL_DISK_SCRIPT(Objects
            .requireNonNull(PowerShellScriptPath.class
                    .getResource("/Win32DiskDriveToPartitionAndLogicalDisk.ps1")).getPath()),

    WIN32_DISK_PARTITION_TO_LOGICAL_DISK_SCRIPT(Objects
            .requireNonNull(PowerShellScriptPath.class
                    .getResource("/Win32DiskPartitionToLogicalDisk.ps1")).getPath()),

    WIN32_PROCESSOR_TO_CACHE_MEMORY_SCRIPT(Objects
            .requireNonNull(PowerShellScriptPath.class
                    .getResource("/Win32ProcessorToCacheMemory.ps1")).getPath()),

    HWID_SCRIPT(Objects
            .requireNonNull(PowerShellScriptPath.class
                    .getResource("/HWID.ps1")).getPath());

    private final String path;
}
