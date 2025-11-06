package io.github.eggy03.ferrumx.windows.entity.compounded;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32DiskDrive;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32DiskPartition;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32LogicalDisk;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class Win32DiskDriveToPartitionAndLogicalDisk {

    @SerializedName("DeviceID")
    String deviceId;

    @SerializedName("DiskDrive")
    Win32DiskDrive diskDrive;

    @SerializedName("Partitions")
    List<Win32DiskPartition> diskPartitionList;

    @SerializedName("LogicalDisks")
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
