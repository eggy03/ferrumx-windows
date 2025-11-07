package io.github.eggy03.ferrumx.windows.entity.compounded;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import io.github.eggy03.ferrumx.windows.entity.network.Win32NetworkAdapter;
import io.github.eggy03.ferrumx.windows.entity.network.Win32NetworkAdapterConfiguration;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class Win32NetworkAdapterToConfiguration {

    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    @SerializedName("Adapter")
    @Nullable
    Win32NetworkAdapter adapter;

    @SerializedName("Configurations")
    @Nullable
    List<Win32NetworkAdapterConfiguration> configurationList;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}
