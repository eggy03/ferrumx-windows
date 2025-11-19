/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.constant.namespace;

import com.profesorfalken.jpowershell.PowerShell;
import io.github.eggy03.ferrumx.windows.entity.display.Win32DesktopMonitor;
import io.github.eggy03.ferrumx.windows.entity.display.Win32VideoController;
import io.github.eggy03.ferrumx.windows.entity.mainboard.Win32Baseboard;
import io.github.eggy03.ferrumx.windows.entity.mainboard.Win32Bios;
import io.github.eggy03.ferrumx.windows.entity.mainboard.Win32PortConnector;
import io.github.eggy03.ferrumx.windows.entity.memory.Win32PhysicalMemory;
import io.github.eggy03.ferrumx.windows.entity.network.Win32NetworkAdapter;
import io.github.eggy03.ferrumx.windows.entity.network.Win32NetworkAdapterConfiguration;
import io.github.eggy03.ferrumx.windows.entity.peripheral.Win32Battery;
import io.github.eggy03.ferrumx.windows.entity.peripheral.Win32Printer;
import io.github.eggy03.ferrumx.windows.entity.peripheral.Win32SoundDevice;
import io.github.eggy03.ferrumx.windows.entity.processor.Win32CacheMemory;
import io.github.eggy03.ferrumx.windows.entity.processor.Win32Processor;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32DiskDrive;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32DiskPartition;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32LogicalDisk;
import io.github.eggy03.ferrumx.windows.entity.system.Win32ComputerSystem;
import io.github.eggy03.ferrumx.windows.entity.system.Win32Environment;
import io.github.eggy03.ferrumx.windows.entity.system.Win32OperatingSystem;
import io.github.eggy03.ferrumx.windows.entity.system.Win32PnPEntity;
import io.github.eggy03.ferrumx.windows.entity.system.Win32Process;
import io.github.eggy03.ferrumx.windows.entity.user.Win32UserAccount;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static io.github.eggy03.ferrumx.windows.constant.PowerShellCmdlets.CONVERT_TO_JSON;
import static io.github.eggy03.ferrumx.windows.constant.PowerShellCmdlets.SELECT_OBJECT_PROPERTY;
import static io.github.eggy03.ferrumx.windows.utility.ReflectionUtility.getFromSerializedNames;

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
    WIN32_BATTERY_QUERY("Get-CimInstance Win32_Battery" +
            SELECT_OBJECT_PROPERTY.getCmdlet() + getFromSerializedNames(Win32Battery.class) +
            CONVERT_TO_JSON.getCmdlet()),

    /**
     * Query to fetch the properties of {@code Win32_DesktopMonitor} class
     *
     * @since 2.0.0
     */
    WIN32_DESKTOP_MONITOR_QUERY("Get-CimInstance Win32_DesktopMonitor" +
            SELECT_OBJECT_PROPERTY.getCmdlet() + getFromSerializedNames(Win32DesktopMonitor.class) +
            CONVERT_TO_JSON.getCmdlet()),

    /**
     * Query to fetch the properties of {@code Win32_VideoController} class
     *
     * @since 2.0.0
     */
    WIN32_VIDEO_CONTROLLER_QUERY("Get-CimInstance Win32_VideoController" +
            SELECT_OBJECT_PROPERTY.getCmdlet() + getFromSerializedNames(Win32VideoController.class) +
            CONVERT_TO_JSON.getCmdlet()),

    /**
     * Query to fetch the properties of {@code Win32_Processor} class
     *
     * @since 2.0.0
     */
    WIN32_PROCESSOR_QUERY("Get-CimInstance Win32_Processor" +
            SELECT_OBJECT_PROPERTY.getCmdlet() + getFromSerializedNames(Win32Processor.class) +
            CONVERT_TO_JSON.getCmdlet()),

    /**
     * Query to fetch the properties of {@code Win32_CacheMemory} class
     *
     * @since 2.0.0
     */
    WIN32_CACHE_MEMORY_QUERY("Get-CimInstance Win32_CacheMemory" +
            SELECT_OBJECT_PROPERTY.getCmdlet() + getFromSerializedNames(Win32CacheMemory.class) +
            CONVERT_TO_JSON.getCmdlet()),

    /**
     * Query to fetch the properties of {@code Win32_AssociatedProcessorMemory} class in a custom object
     *
     * @since 3.0.0
     */
    WIN32_ASSOCIATED_PROCESSOR_MEMORY_QUERY("Get-CimInstance Win32_AssociatedProcessorMemory " +
            "| ForEach-Object { [PSCustomObject]@{ CacheMemoryDeviceID = $_.Antecedent.DeviceID; ProcessorDeviceID = $_.Dependent.DeviceID } } " +
            "| ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_BIOS} class
     *
     * @since 2.0.0
     */
    WIN32_BIOS_QUERY("Get-CimInstance Win32_BIOS" +
            SELECT_OBJECT_PROPERTY.getCmdlet() + getFromSerializedNames(Win32Bios.class) +
            CONVERT_TO_JSON.getCmdlet()),

    /**
     * Query to fetch the properties of {@code Win32_Baseboard} class
     *
     * @since 2.0.0
     */
    WIN32_BASEBOARD_QUERY("Get-CimInstance Win32_Baseboard" +
            SELECT_OBJECT_PROPERTY.getCmdlet() + getFromSerializedNames(Win32Baseboard.class) +
            CONVERT_TO_JSON.getCmdlet()),

    /**
     * Query to fetch the properties of {@code Win32_PortConnector} class
     *
     * @since 2.0.0
     */
    WIN32_PORT_CONNECTOR_QUERY("Get-CimInstance Win32_PortConnector" +
            SELECT_OBJECT_PROPERTY.getCmdlet() + getFromSerializedNames(Win32PortConnector.class) +
            CONVERT_TO_JSON.getCmdlet()),

    /**
     * Query to fetch the properties of {@code Win32_PhysicalMemory} class
     *
     * @since 2.0.0
     */
    WIN32_PHYSICAL_MEMORY_QUERY("Get-CimInstance Win32_PhysicalMemory" +
            SELECT_OBJECT_PROPERTY.getCmdlet() + getFromSerializedNames(Win32PhysicalMemory.class) +
            CONVERT_TO_JSON.getCmdlet()),

    /**
     * Query to fetch the properties of {@code Win32_NetworkAdapter} class
     *
     * @since 2.0.0
     */
    WIN32_NETWORK_ADAPTER_QUERY("Get-CimInstance Win32_NetworkAdapter" +
            SELECT_OBJECT_PROPERTY.getCmdlet() + getFromSerializedNames(Win32NetworkAdapter.class) +
            CONVERT_TO_JSON.getCmdlet()),

    /**
     * Query to fetch the properties of {@code Win32_NetworkAdapterConfiguration} class
     *
     * @since 2.0.0
     */
    WIN32_NETWORK_ADAPTER_CONFIGURATION_QUERY("Get-CimInstance Win32_NetworkAdapterConfiguration" +
            SELECT_OBJECT_PROPERTY.getCmdlet() + getFromSerializedNames(Win32NetworkAdapterConfiguration.class) +
            CONVERT_TO_JSON.getCmdlet()),

    /**
     * Query to fetch the properties of {@code Win32_NetworkAdapterSetting} in a custom object
     *
     * @since 3.0.0
     */
    WIN32_NETWORK_ADAPTER_SETTING_QUERY("Get-CimInstance Win32_NetworkAdapterSetting " +
            "| ForEach-Object { [PSCustomObject]@{ NetworkAdapterDeviceID = $_.Element.DeviceID; NetworkAdapterConfigurationIndex = $_.Setting.Index } } " +
            "| ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_OperatingSystem} class
     *
     * @since 2.0.0
     */
    WIN32_OPERATING_SYSTEM_QUERY("Get-CimInstance Win32_OperatingSystem" +
            SELECT_OBJECT_PROPERTY.getCmdlet() + getFromSerializedNames(Win32OperatingSystem.class) +
            CONVERT_TO_JSON.getCmdlet()),

    /**
     * Query to fetch the properties of {@code Win32_DiskDrive} class
     *
     * @since 2.0.0
     */
    WIN32_DISK_DRIVE_QUERY("Get-CimInstance Win32_DiskDrive" +
            SELECT_OBJECT_PROPERTY.getCmdlet() + getFromSerializedNames(Win32DiskDrive.class) +
            CONVERT_TO_JSON.getCmdlet()),

    /**
     * Query to fetch the properties of {@code Win32_DiskPartition} class
     *
     * @since 2.0.0
     */
    WIN32_DISK_PARTITION_QUERY("Get-CimInstance Win32_DiskPartition" +
            SELECT_OBJECT_PROPERTY.getCmdlet() + getFromSerializedNames(Win32DiskPartition.class) +
            CONVERT_TO_JSON.getCmdlet()),

    /**
     * Query to fetch the properties of {@code Win32_LogicalDisk} class
     *
     * @since 3.0.0
     */
    WIN32_LOGICAL_DISK_QUERY("Get-CimInstance Win32_LogicalDisk" +
            SELECT_OBJECT_PROPERTY.getCmdlet() + getFromSerializedNames(Win32LogicalDisk.class) +
            CONVERT_TO_JSON.getCmdlet()),

    /**
     * Query to fetch the properties of {@code Win32_DiskDriveToDiskPartition} class in a custom object
     *
     * @since 3.0.0
     */
    WIN32_DISK_DRIVE_TO_DISK_PARTITION_QUERY("Get-CimInstance Win32_DiskDriveToDiskPartition " +
            "| ForEach-Object { [PSCustomObject]@{ DiskDriveDeviceID = $_.Antecedent.DeviceID; DiskPartitionDeviceID = $_.Dependent.DeviceID } } " +
            "| ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_LogicalDiskToPartition} class in a custom object
     *
     * @since 3.0.0
     */
    WIN32_LOGICAL_DISK_TO_PARTITION_QUERY("Get-CimInstance Win32_LogicalDiskToPartition " +
            "| ForEach-Object { [PSCustomObject]@{ DiskPartitionDeviceID = $_.Antecedent.DeviceID; LogicalDiskDeviceID = $_.Dependent.DeviceID } } " +
            "| ConvertTo-Json"),

    /**
     * Query to fetch the properties of {@code Win32_ComputerSystem} class
     *
     * @since 3.0.0
     */
    WIN32_COMPUTER_SYSTEM_QUERY("Get-CimInstance -ClassName Win32_ComputerSystem" +
            SELECT_OBJECT_PROPERTY.getCmdlet() + getFromSerializedNames(Win32ComputerSystem.class) +
            CONVERT_TO_JSON.getCmdlet()),

    /**
     * Query to fetch the properties of {@code Win32_Environment} class
     *
     * @since 3.0.0
     */
    WIN32_ENVIRONMENT_QUERY("Get-CimInstance -ClassName Win32_Environment" +
            SELECT_OBJECT_PROPERTY.getCmdlet() + getFromSerializedNames(Win32Environment.class) +
            CONVERT_TO_JSON.getCmdlet()),

    /**
     * Query to fetch the properties of {@code Win32_Printer} class
     *
     * @since 3.0.0
     */
    WIN32_PRINTER_QUERY("Get-CimInstance -ClassName Win32_Printer" +
            SELECT_OBJECT_PROPERTY.getCmdlet() + getFromSerializedNames(Win32Printer.class) +
            CONVERT_TO_JSON.getCmdlet()),

    /**
     * Query to fetch the properties of {@code Win32_UserAccount} class
     *
     * @since 3.0.0
     */
    WIN32_USER_ACCOUNT_QUERY("Get-CimInstance -ClassName Win32_UserAccount" +
            SELECT_OBJECT_PROPERTY.getCmdlet() + getFromSerializedNames(Win32UserAccount.class) +
            CONVERT_TO_JSON.getCmdlet()),

    /**
     * Query to fetch some select properties of {@code Win32_Process} class
     *
     * @since 3.0.0
     */
    WIN32_PROCESS_QUERY("Get-CimInstance -ClassName Win32_Process" +
            SELECT_OBJECT_PROPERTY.getCmdlet() + getFromSerializedNames(Win32Process.class) +
            CONVERT_TO_JSON.getCmdlet()),

    /**
     * Query to fetch the properties of {@code Win32_SoundDevice} class
     *
     * @since 3.0.0
     */
    WIN32_SOUND_DEVICE_QUERY("Get-CimInstance -ClassName Win32_SoundDevice" +
            SELECT_OBJECT_PROPERTY.getCmdlet() + getFromSerializedNames(Win32SoundDevice.class) +
            CONVERT_TO_JSON.getCmdlet()),

    /**
     * Query to fetch the properties of {@code Win32_PnPEntity} class
     */
    WIN32_PNP_ENTITY_QUERY("Get-CimInstance -ClassName Win32_PnPEntity" +
            SELECT_OBJECT_PROPERTY.getCmdlet() + getFromSerializedNames(Win32PnPEntity.class) +
            CONVERT_TO_JSON.getCmdlet());

    private final String query;

}
