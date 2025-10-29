package io.github.eggy03.ferrumx.windows.service.network;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.CimQuery;
import io.github.eggy03.ferrumx.windows.entity.network.MsftNetAdapter;
import io.github.eggy03.ferrumx.windows.mapping.network.MsftNetAdapterMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;

import java.util.List;

public class MsftNetAdapterService implements CommonServiceInterface<MsftNetAdapter> {

    @Override
    public List<MsftNetAdapter> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(CimQuery.MSFT_NET_ADAPTER_QUERY.getQuery());
        return new MsftNetAdapterMapper().mapToList(response.getCommandOutput(), MsftNetAdapter.class);
    }

    @Override
    public List<MsftNetAdapter> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(CimQuery.MSFT_NET_ADAPTER_QUERY.getQuery());
        return new MsftNetAdapterMapper().mapToList(response.getCommandOutput(), MsftNetAdapter.class);
    }
}
