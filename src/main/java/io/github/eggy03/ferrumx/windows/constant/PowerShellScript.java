/*
 * © 2024–2025 The ferrumx-windows contributors
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Enum representing the location for some predefined PowerShell script
 * <p>
 * Each constant reads a PowerShell script in a {@link BufferedReader} which, when executed,
 * returns the result in JSON format. These scripts are typically executed
 * using {@link PowerShell#executeScript(BufferedReader)} and mapped to
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
     * Script that returns a JSON which can be deserialized into {@link MsftNetAdapterToIpAndDnsAndProfile}
     */
    MSFT_NET_ADAPTER_TO_IP_AND_DNS_AND_PROFILE_SCRIPT("/MsftNetAdapterToIpAndDnsAndProfile.ps1"),

    /**
     * Script that returns a JSON which can be deserialized into {@link Win32NetworkAdapterToConfiguration}
     */
    WIN32_NETWORK_ADAPTER_TO_CONFIGURATION_SCRIPT("/Win32NetworkAdapterToConfiguration.ps1"),

    /**
     * Script that returns a JSON which can be deserialized into {@link Win32DiskDriveToPartitionAndLogicalDisk}
     */
    WIN32_DISK_DRIVE_TO_PARTITION_AND_LOGICAL_DISK_SCRIPT("/Win32DiskDriveToPartitionAndLogicalDisk.ps1"),

    /**
     * Script that returns a JSON which can be deserialized into {@link Win32DiskPartitionToLogicalDisk}
     */
    WIN32_DISK_PARTITION_TO_LOGICAL_DISK_SCRIPT("/Win32DiskPartitionToLogicalDisk.ps1"),

    /**
     * Script that returns a JSON which can be deserialized into {@link Win32ProcessorToCacheMemory}
     */
    WIN32_PROCESSOR_TO_CACHE_MEMORY_SCRIPT("/Win32ProcessorToCacheMemory.ps1"),

    /**
     * Script that returns a JSON which can be deserialized into {@link HardwareId}
     */
    HWID_SCRIPT("/HardwareID.ps1");

    private final String scriptPath;

    public static InputStream getScriptAsInputStream(String scriptPath){
        return PowerShellScript.class.getResourceAsStream(scriptPath);
    }

    public static BufferedReader getScriptAsBufferedReader(String scriptPath) {
        return new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(PowerShellScript.class.getResourceAsStream(scriptPath))
                        , StandardCharsets.UTF_8
                )
        );
    }

    public static String getScript(String scriptPath) {

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(PowerShellScript.class.getResourceAsStream(scriptPath))
                )
        );

        StringBuilder script = new StringBuilder();
        reader.lines().forEach(line -> script.append(line).append(System.lineSeparator()));
        return script.toString();
    }

}
