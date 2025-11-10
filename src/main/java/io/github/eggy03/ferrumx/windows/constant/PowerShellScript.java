/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.constant;

import com.profesorfalken.jpowershell.PowerShell;
import io.github.eggy03.ferrumx.windows.entity.compounded.HardwareId;
import io.github.eggy03.ferrumx.windows.entity.compounded.MsftNetAdapterToIpAndDnsAndProfile;
import io.github.eggy03.ferrumx.windows.entity.compounded.Win32DiskDriveToPartitionAndLogicalDisk;
import io.github.eggy03.ferrumx.windows.entity.compounded.Win32DiskPartitionToLogicalDisk;
import io.github.eggy03.ferrumx.windows.entity.compounded.Win32NetworkAdapterToConfiguration;
import io.github.eggy03.ferrumx.windows.entity.compounded.Win32ProcessorToCacheMemory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

/**
 * Enum representing the location for some predefined PowerShell script
 * <p>
 * Each constant holds a PowerShell script location which, when executed,
 * returns the result in JSON format. These scripts are typically executed
 * using {@link PowerShell#executeScript(String)} and mapped to
 * corresponding Java objects.
 * </p>
 *
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 * @since 3.0.0
 */
@RequiredArgsConstructor
@Getter
public enum PowerShellScript {

    /**
     * Query path for script which returns a JSON which can be deserialized into {@link MsftNetAdapterToIpAndDnsAndProfile}
     */
    MSFT_NET_ADAPTER_TO_IP_AND_DNS_AND_PROFILE_SCRIPT(Objects
            .requireNonNull(PowerShellScript.class
                    .getResource("/MsftNetAdapterToIpAndDnsAndProfile.ps1")).getPath()),

    /**
     * Query path for script which returns a JSON which can be deserialized into {@link Win32NetworkAdapterToConfiguration}
     */
    WIN32_NETWORK_ADAPTER_TO_CONFIGURATION_SCRIPT(Objects
            .requireNonNull(PowerShellScript.class
                    .getResource("/Win32NetworkAdapterToConfiguration.ps1")).getPath()),

    /**
     * Query path for script which returns a JSON which can be deserialized into {@link Win32DiskDriveToPartitionAndLogicalDisk}
     */
    WIN32_DISK_DRIVE_TO_PARTITION_AND_LOGICAL_DISK_SCRIPT(Objects
            .requireNonNull(PowerShellScript.class
                    .getResource("/Win32DiskDriveToPartitionAndLogicalDisk.ps1")).getPath()),

    /**
     * Query path for script which returns a JSON which can be deserialized into {@link Win32DiskPartitionToLogicalDisk}
     */
    WIN32_DISK_PARTITION_TO_LOGICAL_DISK_SCRIPT(Objects
            .requireNonNull(PowerShellScript.class
                    .getResource("/Win32DiskPartitionToLogicalDisk.ps1")).getPath()),

    /**
     * Query path for script which returns a JSON which can be deserialized into {@link Win32ProcessorToCacheMemory}
     */
    WIN32_PROCESSOR_TO_CACHE_MEMORY_SCRIPT(Objects
            .requireNonNull(PowerShellScript.class
                    .getResource("/Win32ProcessorToCacheMemory.ps1")).getPath()),

    /**
     * Query path for script which returns a JSON which can be deserialized into {@link HardwareId}
     */
    HWID_SCRIPT(Objects
            .requireNonNull(PowerShellScript.class
                    .getResource("/HWID.ps1")).getPath());

    private final String path;
}
