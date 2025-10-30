package io.github.eggy03.ferrumx.windows.entity.storage;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import io.github.eggy03.ferrumx.windows.constant.Cimv2Namespace;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of a {@link Win32DiskPartition} association with {@link Win32LogicalDisk}.
 * <p>
 * Fields <b>indirectly</b> correspond to properties retrieved from the {@code Win32_LogicalDiskToPartition} WMI class
 * and represent an association between {@code Win32_DiskPartition} and {@code Win32_LogicalDisk}.
 * </p>
 * <p>Associates {@link Win32DiskPartition} with {@link Win32LogicalDisk} via their device IDs</p>
 * <p>This class has the following two fields:</p>
 * <ul>
 *     <li>{@code diskPartitionDeviceId} - contains the {@code deviceId} field of {@link Win32DiskPartition}</li>
 *     <li>{@code logicalDiskDeviceId} - contains the {@code deviceId} field of {@link Win32LogicalDisk}</li>
 * </ul>
 *
 * <p>
 *     <b>Extra Notes:</b> The {@code Win32_LogicalDiskToPartition} WMI class itself does not directly expose
 *     the {@code DeviceID} (from {@code Win32_DiskPartition}) or the {@code DeviceID}
 *     (from {@code Win32_LogicalDisk}) as standalone properties.
 *     Instead, these values are nested within its references: {@code Antecedent} and {@code Dependent}.
 * </p>
 * <p>
 *     To simplify data mapping, the PowerShell query defined in
 *     {@link Cimv2Namespace#LOGICAL_DISK_TO_PARTITION_QUERY} constructs a custom {@code PSObject}
 *     that maps {@code Antecedent.DeviceID} to {@code diskPartitionDeviceId} and {@code Dependent.DeviceID} to {@code logicalDiskDeviceId}
 *     and the resulting JSON returned is deserialized into this entity class.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Build a new instance
 * Win32LogicalDiskToPartition ldt = Win32LogicalDiskToPartition.builder()
 *     .diskPartitionDeviceId("Disk #0 Partition #1")
 *     .logicalDiskDeviceId("C:")
 *     .build();
 * // Create a modified copy using the builder
 * LWin32LogicalDiskToPartition updated = ldt.toBuilder()
 *     .logicalDiskDeviceId("D:")
 *     .build();
 * }</pre>
 *
 * <p>See {@link Win32DiskPartition} for related partitions on a physical disk.</p>
 * <p>See {@link Win32LogicalDisk} for partition info for partitions on a physical disk</p>
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-logicaldisktopartition">Win32_LogicalDiskToPartition Documentation</a>
 * @since 3.0.0
 * @author Egg-03
 */
@Value
@Builder(toBuilder = true)
public class Win32LogicalDiskToPartition {

    @SerializedName("DiskPartitionDeviceID")
    @Nullable
    String diskPartitionDeviceId;

    @SerializedName("LogicalDiskDeviceID")
    @Nullable
    String logicalDiskDeviceId;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}
