package io.github.eggy03.ferrumx.windows.entity.compounded;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32DiskPartition;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32LogicalDisk;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
