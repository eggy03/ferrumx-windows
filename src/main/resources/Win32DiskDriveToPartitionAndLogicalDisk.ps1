$diskDrives = Get-CimInstance Win32_DiskDrive

$result = foreach ($disk in $diskDrives)
{

    $partitions = Get-CimAssociatedInstance -InputObject $disk -ResultClassName Win32_DiskPartition

    $logicalDisks = foreach ($partition in $partitions)
    {
        Get-CimAssociatedInstance -InputObject $partition -ResultClassName Win32_LogicalDisk
    }

    [PSCustomObject]@{
        DeviceID = $disk.DeviceID
        DiskDrive = $disk
        Partitions = @($partitions)
        LogicalDisks = @($logicalDisks)
    }
}

$result | ConvertTo-Json -Depth 5