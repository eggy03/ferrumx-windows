package io.github.eggy03.ferrumx.windows.entity.compounded;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import io.github.eggy03.ferrumx.windows.entity.network.MsftDnsClientServerAddress;
import io.github.eggy03.ferrumx.windows.entity.network.MsftNetAdapter;
import io.github.eggy03.ferrumx.windows.entity.network.MsftNetConnectionProfile;
import io.github.eggy03.ferrumx.windows.entity.network.MsftNetIpAddress;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class MsftNetAdapterToIpAndDnsAndProfile {

    @SerializedName("InterfaceIndex")
    Long interfaceIndex;

    @SerializedName("NetworkAdapter")
    MsftNetAdapter adapter;

    @SerializedName("IPAddresses")
    List<MsftNetIpAddress> ipAddressList;

    @SerializedName("DNSServers")
    List<MsftDnsClientServerAddress> dnsClientServerAddressList;

    @SerializedName("Profile")
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
