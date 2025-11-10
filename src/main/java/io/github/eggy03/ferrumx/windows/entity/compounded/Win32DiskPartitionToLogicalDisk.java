/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.entity.compounded;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32DiskDrive;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32DiskPartition;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32LogicalDisk;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32LogicalDiskToPartition;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Immutable representation of a {@link Win32DiskPartition} and its
 * {@code 1:N} relationship with {@link Win32LogicalDisk} in a Windows system.
 * <p>
 * Each instance represents a single disk partition identified by {@link #partitionId},
 * and maintains a one-to-many mapping with its corresponding logical disks.
 * </p>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 * <p>
 * This class is purely a convenience class designed to eliminate the need for using
 * {@link Win32LogicalDiskToPartition} when
 * fetching a relation between {@link Win32DiskPartition} and {@link Win32LogicalDisk}
 * </p>
 * <p>
 * A class for representing{@code 1:N} mappings of
 * {@link Win32DiskDrive} with {@link Win32DiskPartition} and {@link Win32LogicalDisk}
 * is also available by the name of {@link Win32DiskDriveToPartitionAndLogicalDisk}.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * Win32DiskPartitionToLogicalDisk partitionInfo = Win32DiskPartitionToLogicalDisk.builder()
 *     .partitionId("Disk #0, Partition #1")
 *     .diskPartition(partition)
 *     .logicalDiskList(logicalDisks)
 *     .build();
 * }</pre>
 *
 * @see Win32DiskPartition
 * @see Win32LogicalDisk
 * @see Win32LogicalDiskToPartition
 *
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */

@Value
@Builder(toBuilder = true)
public class Win32DiskPartitionToLogicalDisk {

    @SerializedName("PartitionID")
    @Nullable
    String partitionId;

    @SerializedName("Partition")
    @Nullable
    Win32DiskPartition diskPartition;

    @SerializedName("LogicalDisks")
    @Nullable
    List<Win32LogicalDisk> logicalDiskList;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}
