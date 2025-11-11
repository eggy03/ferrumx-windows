/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.entity.compounded;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32DiskDrive;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32DiskDriveToDiskPartition;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32DiskPartition;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32LogicalDisk;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32LogicalDiskToPartition;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

import java.util.List;
/**
 * Immutable representation of a {@link Win32DiskDrive} and its associated
 * {@code 1:N} relationships with {@link Win32DiskPartition} and {@link Win32LogicalDisk}
 * in a Windows system.
 * <p>
 * Each instance represents a single physical {@code DiskDrive} identified by {@link #deviceId},
 * and maintains a one-to-many mapping with its corresponding {@code DiskPartitions} and {@code LogicalDisks}.
 * </p>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * Win32DiskDriveToPartitionAndLogicalDisk diskInfo = Win32DiskDriveToPartitionAndLogicalDisk.builder()
 *     .deviceId("\\\\.\\PHYSICALDRIVE0")
 *     .diskDrive(drive)
 *     .diskPartitionList(partitions)
 *     .logicalDiskList(logicalDisks)
 *     .build();
 * }</pre>
 *
 * <p>
 * <b>NOTE: </b>While a {@code DiskDrive} has {@code 1:N} relationship with {@code DiskPartitions} and {@code LogicalDisks},
 * a single instance of a {@code DiskPartition} may also have {@code 1:N} relationship with {@code LogicalDisks}.
 * This class does not represent such a relationship between them ({@link Win32DiskPartition} and {@link Win32LogicalDisk})
 * since it's primary focus is to link both of them with {@link Win32DiskDrive}.
 * To fetch such a relation, check out the custom class {@link Win32DiskPartitionToLogicalDisk}.
 * </p>
 * <p>
 * This is purely a convenience class designed to help users of this library retrieve
 * drive–partition–logical disk associations in a single function call.
 * Using this class eliminates the need for calling the associated classes that are used
 * to fetch an association between the {@code DiskDrive}, {@code DiskPartition} and {@code LogicalDisk} classes,
 * namely {@link Win32DiskDriveToDiskPartition} and {@link Win32LogicalDiskToPartition}
 * However, they will remain available for use if you prefer to map contents yourself.
 * </p>
 *
 * <p>
 * <b>NOTE:</b> I have tried keeping this document concise, but I do realize that the one-to-many
 * association information may feel overwhelming, which is why I would recommend checking out the
 * documentation of the individual classes first (linked towards the end of this class documentation).
 * Each documentation for the individual classes also has a link for the official Microsoft documentation,
 * which I would recommend reading and experimenting with, to have a better understanding
 * of the classes and their associations with each other.
 * </p>
 *
 * @see Win32DiskDrive
 * @see Win32DiskPartition
 * @see Win32LogicalDisk
 * @see Win32DiskDriveToDiskPartition
 * @see Win32LogicalDiskToPartition
 *
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */

@Value
@Builder(toBuilder = true)
public class Win32DiskDriveToPartitionAndLogicalDisk {

    /**
     * The unique identifier for the {@link Win32DiskDrive} instance.
     * <p>
     * This corresponds to the physical disk ID such as {@code "\\\\.\\PHYSICALDRIVE0"}.
     * </p>
     */
    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    /**
     * The {@link Win32DiskDrive} entity associated with the {@link #deviceId}.
     * <p>
     * Represents the physical disk drive identified by the specified {@link #deviceId}.
     * </p>
     */
    @SerializedName("DiskDrive")
    @Nullable
    Win32DiskDrive diskDrive;

    /**
     * A list of {@link Win32DiskPartition} entities associated with the {@link #deviceId}.
     * <p>
     * Represents all partitions physically present on the referenced {@link #diskDrive}.
     * </p>
     */
    @SerializedName("Partitions")
    @Nullable
    List<Win32DiskPartition> diskPartitionList;

    /**
     * A list of {@link Win32LogicalDisk} entities associated with the {@link #deviceId}.
     */
    @SerializedName("LogicalDisks")
    @Nullable
    List<Win32LogicalDisk> logicalDiskList;

    /**
     * Retrieves the entity in a JSON pretty-print formatted string
     * @return the {@link String} value of the object in JSON pretty-print format
     */
    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}
