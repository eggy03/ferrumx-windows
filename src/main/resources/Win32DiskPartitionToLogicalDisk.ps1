$partitions = Get-CimInstance Win32_DiskPartition

$result = foreach($partition in $partitions) {

    $logicalDisks = Get-CimAssociatedInstance -InputObject $partition -ResultClassName Win32_LogicalDisk

    [PSCustomObject]@{
        PartitionID = $partition.DeviceID
        Partition = $partition
        LogicalDisks = @($logicalDisks)
    }
}

$result | ConvertTo-Json -Depth 5