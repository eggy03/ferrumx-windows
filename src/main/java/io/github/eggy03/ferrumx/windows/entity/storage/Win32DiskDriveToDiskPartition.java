package io.github.eggy03.ferrumx.windows.entity.storage;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import io.github.eggy03.ferrumx.windows.constant.Cimv2Namespace;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of a {@link Win32DiskDrive} association with {@link Win32DiskPartition}.
 * <p>
 * Fields <b>indirectly</b> correspond to properties retrieved from the {@code Win32_DiskDriveToDiskPartition} WMI class
 * and represent an association between {@code Win32_DiskDrive} and {@code Win32_DiskPartition}.
 * </p>
 * <p>Associates {@link Win32DiskDrive} with {@link Win32DiskPartition} via their device IDs</p>
 * <p>This class has the following two fields:</p>
 * <ul>
 *     <li>{@code diskDriveDeviceId} - contains the {@code deviceId} field of {@link Win32DiskDrive}</li>
 *     <li>{@code diskPartitionDeviceId} - contains the {@code deviceId} field of {@link Win32DiskPartition}</li>
 * </ul>
 *
 * <p>
 *     <b>Extra Notes:</b> The {@code Win32_DiskDriveToDiskPartition} WMI class itself does not directly expose
 *     the {@code DeviceID} (from {@code Win32_DiskDrive}) or the {@code DeviceID}
 *     (from {@code Win32_DiskPartition}) as standalone properties.
 *     Instead, these values are nested within its references: {@code Antecedent} and {@code Dependent}.
 * </p>
 * <p>
 *     To simplify data mapping, the PowerShell query defined in
 *     {@link Cimv2Namespace#DISK_DRIVE_TO_DISK_PARTITION_QUERY} constructs a custom {@code PSObject}
 *     that maps {@code Antecedent.DeviceID} to {@code diskDriveDeviceId} and {@code Dependent.DeviceID} to {@code diskPartitionDeviceId}
 *     and the resulting JSON returned is deserialized into this entity class.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Build a new instance
 * Win32DiskDriveToDiskPartition ddt = Win32DiskDriveToDiskPartition.builder()
 *     .diskDriveDeviceId("////.//PHYSICALDRIVE0")
 *     .diskPartitionDeviceId("Disk #0 Partition #1")
 *     .build();
 * // Create a modified copy using the builder
 * Win32DiskDriveToDiskPartition updated = ddt.toBuilder()
 *     .diskPartitionDeviceId("Disk #0 Partition #2")
 *     .build();
 * }</pre>
 *
 * <p>See {@link Win32DiskDrive} for related physical disk info.</p>
 * <p>See {@link Win32DiskPartition} for related partitions on a physical disk.</p>
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-diskdrivetodiskpartition">Win32_DiskDriveToDiskPartition Documentation</a>
 * @since 2.3.0
 * @author Egg-03
 */
@Value
@Builder(toBuilder = true)
public class Win32DiskDriveToDiskPartition {

    @SerializedName("DiskDriveDeviceID")
    @Nullable
    String diskDriveDeviceId;

    @SerializedName("DiskPartitionDeviceID")
    @Nullable
    String diskPartitionDeviceId;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}
