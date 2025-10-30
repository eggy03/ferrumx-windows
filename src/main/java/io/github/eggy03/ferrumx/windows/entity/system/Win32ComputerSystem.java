package io.github.eggy03.ferrumx.windows.entity.system;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Immutable representation of a computer system running Windows.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_ComputerSystem} WMI class.
 * </p>
 * <p>
 * Instances are thread-safe and may be safely cached or shared across threads.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Build a new instance
 * Win32ComputerSystem system = Win32ComputerSystem.builder()
 *     .caption("Workstation PC")
 *     .description("High-end office workstation")
 *     .name("User-PC")
 *     .chassisSKUNumber("PROD-1234")
 *     .systemSKUNumber("Default String")
 *     .build();
 *
 * // Create a modified copy
 * Win32ComputerSystem updated = system.toBuilder()
 *     .name("Admin-PC")
 *     .build();
 *
 * }</pre>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-computersystem">Win32_ComputerSystem Documentation</a>
 * @since 3.0.0
 * @author Egg-03
 */
@Value
@Builder(toBuilder = true)
public class Win32ComputerSystem {

    // Password Status
    @SerializedName("AdminPasswordStatus")
    @Nullable
    Integer adminPasswordStatus;

    @SerializedName("KeyboardPasswordStatus")
    @Nullable
    Integer keyboardPasswordStatus;

    @SerializedName("PowerOnPasswordStatus")
    @Nullable
    Integer powerOnPasswordStatus;


    // Boot
    @SerializedName("BootupState")
    @Nullable
    String bootupState;

    @SerializedName("BootStatus")
    @Nullable
    List<Integer> bootStatus;

    @SerializedName("AutomaticResetBootOption")
    @Nullable
    Boolean automaticResetBootOption;


    // Power
    @SerializedName("PowerState")
    @Nullable
    Integer powerState;

    @SerializedName("PowerSupplyState")
    @Nullable
    Integer powerSupplyState;

    @SerializedName("PowerManagementCapabilities")
    @Nullable
    List<Integer> powerManagementCapabilities;

    @SerializedName("PowerManagementSupported")
    @Nullable
    Boolean powerManagementSupported;


    // Reset
    @SerializedName("ResetCapability")
    @Nullable
    Integer resetCapability;

    @SerializedName("ResetCount")
    @Nullable
    Integer resetCount;

    @SerializedName("ResetLimit")
    @Nullable
    Integer resetLimit;

    @SerializedName("FrontPanelResetStatus")
    @Nullable
    Integer frontPanelResetStatus;

    @SerializedName("AutomaticResetCapability")
    @Nullable
    Boolean automaticResetCapability;


    // General info
    @SerializedName("Name")
    @Nullable
    String name;

    @SerializedName("Caption")
    @Nullable
    String caption;

    @SerializedName("Description")
    @Nullable
    String description;

    @SerializedName("Manufacturer")
    @Nullable
    String manufacturer;

    @SerializedName("Model")
    @Nullable
    String model;

    @SerializedName("PrimaryOwnerName")
    @Nullable
    String primaryOwnerName;

    @SerializedName("PrimaryOwnerContact")
    @Nullable
    String primaryOwnerContact;

    @SerializedName("Roles")
    @Nullable
    List<String> roles;

    @SerializedName("ChassisSKUNumber")
    @Nullable
    String chassisSKUNumber;

    @SerializedName("SystemSKUNumber")
    @Nullable
    String systemSKUNumber;

    @SerializedName("SystemFamily")
    @Nullable
    String systemFamily;

    @SerializedName("SystemType")
    @Nullable
    String systemType;

    @SerializedName("UserName")
    @Nullable
    String userName;

    @SerializedName("Workgroup")
    @Nullable
    String workgroup;

    @SerializedName("OEMStringArray")
    @Nullable
    List<String> oemStringArray;

    @SerializedName("NumberOfProcessors")
    @Nullable
    Long numberOfProcessors;

    @SerializedName("NumberOfLogicalProcessors")
    @Nullable
    Long numberOfLogicalProcessors;

    @SerializedName("TotalPhysicalMemory")
    @Nullable
    Long totalPhysicalMemory;


    // Uncategorized
    @SerializedName("AutomaticManagedPagefile")
    @Nullable
    Boolean automaticManagedPagefile;

    @SerializedName("InfraredSupported")
    @Nullable
    Boolean infraredSupported;

    @SerializedName("NetworkServerModeEnabled")
    @Nullable
    Boolean networkServerModeEnabled;

    @SerializedName("HypervisorPresent")
    @Nullable
    Boolean hypervisorPresent;

    @SerializedName("ThermalState")
    @Nullable
    Integer thermalState;


    // Time
    @SerializedName("CurrentTimeZone")
    @Nullable
    Integer currentTimeZone;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}
