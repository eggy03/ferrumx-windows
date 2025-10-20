package io.github.eggy03.ferrumx.windows.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing the predefined WMI (CIM) queries used in the system.
 * <p>
 * Each constant holds a PowerShell query that queries a specific WMI class
 * and returns the result in JSON format. These queries are typically executed
 * using {@link com.profesorfalken.jpowershell.PowerShell} and mapped to
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
     */
    BATTERY_QUERY("Get-CimInstance Win32_Battery | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_DesktopMonitor} class
     */
    MONITOR_QUERY("Get-CimInstance Win32_DesktopMonitor | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_VideoController} class
     */
    VIDEO_CONTROLLER_QUERY("Get-CimInstance Win32_VideoController | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_Processor} class
     */
    PROCESSOR_QUERY("Get-CimInstance Win32_Processor | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_CacheMemory} class
     */
    PROCESSOR_CACHE_QUERY("Get-CimInstance Win32_CacheMemory | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_AssociatedProcessorMemory} class
     */
    ASSOCIATED_PROCESSOR_MEMORY_QUERY("Get-CimInstance Win32_AssociatedProcessorMemory | ForEach-Object { [PSCustomObject]@{ CacheMemoryID = $_.Antecedent.DeviceID; CPUID = $_.Dependent.DeviceID } } | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_BIOS} class
     */
    BIOS_QUERY("Get-CimInstance Win32_BIOS | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_Baseboard} class
     */
    MAINBOARD_QUERY("Get-CimInstance Win32_Baseboard | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_PortConnector} class
     */
    MAINBOARD_PORT_QUERY("Get-CimInstance Win32_PortConnector | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_PhysicalMemory} class
     */
    PHYSICAL_MEMORY_QUERY("Get-CimInstance Win32_PhysicalMemory | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_NetworkAdapter} class
     */
    NETWORK_ADAPTER_QUERY("Get-CimInstance Win32_NetworkAdapter | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_NetworkAdapterConfiguration} class
     */
    NETWORK_ADAPTER_CONFIGURATION_QUERY("Get-CimInstance Win32_NetworkAdapterConfiguration | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_NetworkAdapterSetting}
     */
    NETWORK_ADAPTER_SETTING_QUERY("Get-CimInstance Win32_NetworkAdapterSetting | ForEach-Object { [PSCustomObject]@{ DeviceID = $_.Element.DeviceID; Index = $_.Setting.Index } } | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_OperatingSystem} class
     */
    OPERATING_SYSTEM_QUERY("Get-CimInstance Win32_OperatingSystem | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_DiskDrive} class
     */
    DISK_QUERY("Get-CimInstance Win32_DiskDrive | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_DiskPartition} class
     */
    DISK_PARTITION_QUERY("Get-CimInstance Win32_DiskPartition | Select-Object * | ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_ComputerSystemProduct} class
     */
    COMPUTER_SYSTEM_PRODUCT("Get-CimInstance -ClassName Win32_ComputerSystemProduct | Select-Object * | ConvertTo-Json");

    private final String query;
}
