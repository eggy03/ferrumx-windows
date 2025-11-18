/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.constant.namespace;

import com.profesorfalken.jpowershell.PowerShell;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing the predefined WMI (CIM) queries for the classes available in the {@code root/cimv2} namespace.
 * <p>
 * Each constant holds a PowerShell query that queries a specific class in the namespace
 * and returns the result in JSON format. These queries are typically executed
 * using {@link PowerShell} and mapped to
 * corresponding Java objects.
 * </p>
 *
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 * @since 3.0.0
 */
@RequiredArgsConstructor
@Getter
public enum Cimv2Namespace {

    /**
     * Query to fetch the properties of {@code Win32_Battery} class
     *
     * @since 2.0.0
     */
    WIN32_BATTERY_QUERY("Get-CimInstance Win32_Battery | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_DesktopMonitor} class
     *
     * @since 2.0.0
     */
    WIN32_DESKTOP_MONITOR_QUERY("Get-CimInstance Win32_DesktopMonitor | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_VideoController} class
     *
     * @since 2.0.0
     */
    WIN32_VIDEO_CONTROLLER_QUERY("Get-CimInstance Win32_VideoController | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_Processor} class
     *
     * @since 2.0.0
     */
    WIN32_PROCESSOR_QUERY("Get-CimInstance Win32_Processor | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_CacheMemory} class
     *
     * @since 2.0.0
     */
    WIN32_CACHE_MEMORY_QUERY("Get-CimInstance Win32_CacheMemory | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_AssociatedProcessorMemory} class in a custom object
     *
     * @since 3.0.0
     */
    WIN32_ASSOCIATED_PROCESSOR_MEMORY_QUERY("Get-CimInstance Win32_AssociatedProcessorMemory | ForEach-Object { [PSCustomObject]@{ CacheMemoryDeviceID = $_.Antecedent.DeviceID; ProcessorDeviceID = $_.Dependent.DeviceID } } | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_BIOS} class
     *
     * @since 2.0.0
     */
    WIN32_BIOS_QUERY("Get-CimInstance Win32_BIOS | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_Baseboard} class
     *
     * @since 2.0.0
     */
    WIN32_BASEBOARD_QUERY("Get-CimInstance Win32_Baseboard | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_PortConnector} class
     *
     * @since 2.0.0
     */
    WIN32_PORT_CONNECTOR_QUERY("Get-CimInstance Win32_PortConnector | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_PhysicalMemory} class
     *
     * @since 2.0.0
     */
    WIN32_PHYSICAL_MEMORY_QUERY("Get-CimInstance Win32_PhysicalMemory | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_NetworkAdapter} class
     *
     * @since 2.0.0
     */
    WIN32_NETWORK_ADAPTER_QUERY("Get-CimInstance Win32_NetworkAdapter | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_NetworkAdapterConfiguration} class
     *
     * @since 2.0.0
     */
    WIN32_NETWORK_ADAPTER_CONFIGURATION_QUERY("Get-CimInstance Win32_NetworkAdapterConfiguration | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_NetworkAdapterSetting} in a custom object
     *
     * @since 3.0.0
     */
    WIN32_NETWORK_ADAPTER_SETTING_QUERY("Get-CimInstance Win32_NetworkAdapterSetting | ForEach-Object { [PSCustomObject]@{ NetworkAdapterDeviceID = $_.Element.DeviceID; NetworkAdapterConfigurationIndex = $_.Setting.Index } } | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_OperatingSystem} class
     *
     * @since 2.0.0
     */
    WIN32_OPERATING_SYSTEM_QUERY("Get-CimInstance Win32_OperatingSystem | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_DiskDrive} class
     *
     * @since 2.0.0
     */
    WIN32_DISK_DRIVE_QUERY("Get-CimInstance Win32_DiskDrive | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_DiskPartition} class
     *
     * @since 2.0.0
     */
    WIN32_DISK_PARTITION_QUERY("Get-CimInstance Win32_DiskPartition | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_LogicalDisk} class
     *
     * @since 3.0.0
     */
    WIN32_LOGICAL_DISK_QUERY("Get-CimInstance Win32_LogicalDisk | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_DiskDriveToDiskPartition} class in a custom object
     *
     * @since 3.0.0
     */
    WIN32_DISK_DRIVE_TO_DISK_PARTITION_QUERY("Get-CimInstance Win32_DiskDriveToDiskPartition | ForEach-Object { [PSCustomObject]@{ DiskDriveDeviceID = $_.Antecedent.DeviceID; DiskPartitionDeviceID = $_.Dependent.DeviceID } } | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_LogicalDiskToPartition} class in a custom object
     *
     * @since 3.0.0
     */
    WIN32_LOGICAL_DISK_TO_PARTITION_QUERY("Get-CimInstance Win32_LogicalDiskToPartition | ForEach-Object { [PSCustomObject]@{ DiskPartitionDeviceID = $_.Antecedent.DeviceID; LogicalDiskDeviceID = $_.Dependent.DeviceID } } | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_ComputerSystem} class
     *
     * @since 3.0.0
     */
    WIN32_COMPUTER_SYSTEM_QUERY("Get-CimInstance -ClassName Win32_ComputerSystem | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_Environment} class
     *
     * @since 3.0.0
     */
    WIN32_ENVIRONMENT_QUERY("Get-CimInstance -ClassName Win32_Environment | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_Printer} class
     *
     * @since 3.0.0
     */
    WIN32_PRINTER_QUERY("Get-CimInstance -ClassName Win32_Printer | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_UserAccount} class
     *
     * @since 3.0.0
     */
    WIN32_USER_ACCOUNT_QUERY("Get-CimInstance -ClassName Win32_UserAccount | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch some select properties of {@code Win32_Process} class
     *
     * @since 3.0.0
     */
    WIN32_PROCESS_QUERY("Get-CimInstance -ClassName Win32_Process | Select-Object -Property" +
            " Name, Caption, Description," +
            " ExecutablePath, ExecutionState, Handle, HandleCount, Priority, ProcessId, SessionId," +
            " ThreadCount, KernelModeTime, UserModeTime ," +
            " WorkingSetSize, PeakWorkingSetSize," +
            " PrivatePageCount, PageFileUsage, PeakPageFileUsage," +
            " VirtualSize, PeakVirtualSize," +
            " CreationDate, TerminationDate" +
            " | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_SoundDevice} class
     *
     * @since 3.0.0
     */
    WIN32_SOUND_DEVICE_QUERY("Get-CimInstance -ClassName Win32_SoundDevice | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_PnPEntity} class
     */
    WIN32_PNP_ENTITY_QUERY("Get-CimInstance -ClassName Win32_PnPEntity | Select-Object * | ConvertTo-Json");
    private final String query;
}
