package io.github.eggy03.ferrumx.windows.constant;

import com.profesorfalken.jpowershell.PowerShell;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing the predefined WMI (CIM) queries used in the system.
 * <p>
 * Each constant holds a PowerShell query that queries a specific WMI class
 * and returns the result in JSON format. These queries are typically executed
 * using {@link PowerShell} and mapped to
 * corresponding Java objects.
 * </p>
 * @since 2.0.0
 * @author Egg-03
 */
@RequiredArgsConstructor
@Getter
public enum CimQuery {

    /**
     * Query to fetch the properties of {@code Win32_Battery} class
     * @since 2.0.0
     */
    BATTERY_QUERY("Get-CimInstance Win32_Battery | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_DesktopMonitor} class
     * @since 2.0.0
     */
    MONITOR_QUERY("Get-CimInstance Win32_DesktopMonitor | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_VideoController} class
     * @since 2.0.0
     */
    VIDEO_CONTROLLER_QUERY("Get-CimInstance Win32_VideoController | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_Processor} class
     * @since 2.0.0
     */
    PROCESSOR_QUERY("Get-CimInstance Win32_Processor | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_CacheMemory} class
     * @since 2.0.0
     */
    PROCESSOR_CACHE_QUERY("Get-CimInstance Win32_CacheMemory | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_AssociatedProcessorMemory} class in a custom object
     * @since 2.3.0
     */
    ASSOCIATED_PROCESSOR_MEMORY_QUERY("Get-CimInstance Win32_AssociatedProcessorMemory | ForEach-Object { [PSCustomObject]@{ CacheMemoryDeviceID = $_.Antecedent.DeviceID; ProcessorDeviceID = $_.Dependent.DeviceID } } | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_BIOS} class
     * @since 2.0.0
     */
    BIOS_QUERY("Get-CimInstance Win32_BIOS | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_Baseboard} class
     * @since 2.0.0
     */
    MAINBOARD_QUERY("Get-CimInstance Win32_Baseboard | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_PortConnector} class
     * @since 2.0.0
     */
    MAINBOARD_PORT_QUERY("Get-CimInstance Win32_PortConnector | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_PhysicalMemory} class
     * @since 2.0.0
     */
    PHYSICAL_MEMORY_QUERY("Get-CimInstance Win32_PhysicalMemory | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_NetworkAdapter} class
     * @since 2.0.0
     */
    NETWORK_ADAPTER_QUERY("Get-CimInstance Win32_NetworkAdapter | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_NetworkAdapterConfiguration} class
     * @since 2.0.0
     */
    NETWORK_ADAPTER_CONFIGURATION_QUERY("Get-CimInstance Win32_NetworkAdapterConfiguration | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_NetworkAdapterSetting} in a custom object
     * @since 2.3.0
     */
    NETWORK_ADAPTER_SETTING_QUERY("Get-CimInstance Win32_NetworkAdapterSetting | ForEach-Object { [PSCustomObject]@{ NetworkAdapterDeviceID = $_.Element.DeviceID; NetworkAdapterConfigurationIndex = $_.Setting.Index } } | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_OperatingSystem} class
     * @since 2.0.0
     */
    OPERATING_SYSTEM_QUERY("Get-CimInstance Win32_OperatingSystem | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_DiskDrive} class
     * @since 2.0.0
     */
    DISK_QUERY("Get-CimInstance Win32_DiskDrive | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_DiskPartition} class
     * @since 2.0.0
     */
    DISK_PARTITION_QUERY("Get-CimInstance Win32_DiskPartition | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_LogicalDisk} class
     * @since 2.3.0
     */
    LOGICAL_DISK_QUERY("Get-CimInstance Win32_LogicalDisk | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_DiskDriveToDiskPartition} class in a custom object
     * @since 2.3.0
     */
    DISK_DRIVE_TO_DISK_PARTITION_QUERY("Get-CimInstance Win32_DiskDriveToDiskPartition | ForEach-Object { [PSCustomObject]@{ DiskDriveDeviceID = $_.Antecedent.DeviceID; DiskPartitionDeviceID = $_.Dependent.DeviceID } } | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_ComputerSystemProduct} class
     * @since 2.0.0
     */
    COMPUTER_SYSTEM_PRODUCT("Get-CimInstance -ClassName Win32_ComputerSystemProduct | Select-Object * | ConvertTo-Json");

    private final String query;
}
