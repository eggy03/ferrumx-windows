package io.github.eggy03.ferrumx.windows.entity.compounded;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import io.github.eggy03.ferrumx.windows.entity.processor.Win32CacheMemory;
import io.github.eggy03.ferrumx.windows.entity.processor.Win32Processor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class Win32ProcessorToCacheMemory {

    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    @SerializedName("Processor")
    @Nullable
    Win32Processor processor;

    @SerializedName("CacheMemory")
    @Nullable
    List<Win32CacheMemory> cacheMemoryList;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}