$cpus = Get-CimInstance Win32_Processor

$result = foreach ($cpu in $cpus)
{

    $cache = Get-CimAssociatedInstance -InputObject $cpu -ResultClassName Win32_CacheMemory

    [PSCustomObject]@{
        DeviceID = $cpu.DeviceID
        Processor = $cpu
        CacheMemory = @($cache)
    }
}

$result | ConvertTo-Json -Depth 5