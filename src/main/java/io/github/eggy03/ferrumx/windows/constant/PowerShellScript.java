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

    /**
     * Loads a PowerShell script from the classpath as an {@link InputStream}.
     *
     * <p>This method is useful when the caller wants to handle stream processing
     * manually or pass the stream directly as a reference.</p>
     *
     * @param scriptPath the absolute classpath location of the script (e.g. {@code "/script.ps1"})
     * @return an {@link InputStream} for the requested script, or {@code null}
     *         if the resource cannot be found
     */
    public static InputStream getScriptAsInputStream(String scriptPath){
        return PowerShellScript.class.getResourceAsStream(scriptPath);
    }

    /**
     * Loads a PowerShell script from the classpath and wraps it in a {@link BufferedReader}.
     *
     * <p>The script is read using UTF-8 encoding.</p>
     *
     * @param scriptPath the absolute classpath location of the script (e.g. {@code "/script.ps1"})
     * @return a {@link BufferedReader} for the requested script
     * @throws NullPointerException if the script resource cannot be found
     */
    public static BufferedReader getScriptAsBufferedReader(String scriptPath) {
        return new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(PowerShellScript.class.getResourceAsStream(scriptPath))
                        , StandardCharsets.UTF_8
                )
        );
    }

    /**
     * Loads a PowerShell script from the classpath and returns its full contents as a {@link String}.
     *
     * <p>Each line of the script is preserved and separated using
     * {@link System#lineSeparator()}.</p>
     *
     * @param scriptPath the absolute classpath location of the script (e.g. {@code "/script.ps1"})
     * @return the complete script contents as a {@link String}
     * @throws NullPointerException if the script resource cannot be found
     */
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
