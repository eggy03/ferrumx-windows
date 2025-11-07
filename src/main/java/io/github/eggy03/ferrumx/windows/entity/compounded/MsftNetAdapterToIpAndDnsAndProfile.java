package io.github.eggy03.ferrumx.windows.entity.compounded;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import io.github.eggy03.ferrumx.windows.entity.network.MsftDnsClientServerAddress;
import io.github.eggy03.ferrumx.windows.entity.network.MsftNetAdapter;
import io.github.eggy03.ferrumx.windows.entity.network.MsftNetConnectionProfile;
import io.github.eggy03.ferrumx.windows.entity.network.MsftNetIpAddress;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class MsftNetAdapterToIpAndDnsAndProfile {

    @SerializedName("InterfaceIndex")
    @Nullable
    Long interfaceIndex;

    @SerializedName("NetworkAdapter")
    @Nullable
    MsftNetAdapter adapter;

    @SerializedName("IPAddresses")
    @Nullable
    List<MsftNetIpAddress> ipAddressList;

    @SerializedName("DNSServers")
    @Nullable
    List<MsftDnsClientServerAddress> dnsClientServerAddressList;

    @SerializedName("Profile")
    @Nullable
    List<MsftNetConnectionProfile> netConnectionProfileList;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}
