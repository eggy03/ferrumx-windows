package io.github.eggy03.ferrumx.windows.entity.storage;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of a partition in a <b>physical</b> disk on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_DiskPartition} WMI class.
 * </p>
 * <p>
 * Instances are thread-safe and may be safely cached or shared across threads.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Build a new instance
 * Win32DiskPartition partition = Win32DiskPartition.builder()
 *     .deviceId("Disk0\\Partition1")
 *     .name("System Reserved")
 *     .description("EFI System Partition")
 *     .blockSize(512L)
 *     .numberOfBlocks(131072L)
 *     .bootable(true)
 *     .primaryPartition(true)
 *     .bootPartition(true)
 *     .diskIndex(0)
 *     .size(67108864L)
 *     .type("EFI")
 *     .build();
 *
 * // Create a modified copy
 * Win32DiskPartition resizedPartition = partition.toBuilder()
 *     .size(134217728L)
 *     .build();
 *
 * }</pre>
 *
 * <p>See {@link Win32DiskDrive} for information about physical disks.</p>
 * <p>See {@link Win32LogicalDisk} for information about the logical volumes on a physical disk.</p>
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-diskpartition">Win32_DiskPartition Documentation</a>
 * @since 2.0.0
 * @author Egg-03
 */

@Value
@Builder(toBuilder = true)
public class Win32DiskPartition {

    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    @SerializedName("Name")
    @Nullable
    String name;

    @SerializedName("Description")
    @Nullable
    String description;

    @SerializedName("BlockSize")
    @Nullable
    Long blockSize;

    @SerializedName("NumberOfBlocks")
    @Nullable
    Long numberOfBlocks;

    @SerializedName("Bootable")
    @Nullable
    Boolean bootable;

    @SerializedName("PrimaryPartition")
    @Nullable
    Boolean primaryPartition;

    @SerializedName("BootPartition")
    @Nullable
    Boolean bootPartition;

    @SerializedName("DiskIndex")
    @Nullable
    Integer diskIndex;

    @SerializedName("Size")
    @Nullable
    Long size;

    @SerializedName("Type")
    @Nullable
    String type;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}