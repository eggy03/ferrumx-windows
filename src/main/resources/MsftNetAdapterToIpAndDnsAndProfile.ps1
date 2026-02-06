$adapters = Get-NetAdapter
$ips = Get-NetIPAddress
$dns = Get-DnsClientServerAddress
$connectionProfile = Get-NetConnectionProfile

$result = foreach ($adapter in $adapters)
{
    $interfaceIndex = $adapter.InterfaceIndex

    [PSCustomObject]@{
        InterfaceIndex = $interfaceIndex
        NetworkAdapter = $adapter
        IPAddresses = @($ips | Where-Object { $_.InterfaceIndex -eq $interfaceIndex })
        DNSServers = @($dns | Where-Object { $_.InterfaceIndex -eq $interfaceIndex })
        Profile = @($connectionProfile | Where-Object { $_.InterfaceIndex -eq $interfaceIndex })
    }
}

$result | ConvertTo-Json -Depth 5