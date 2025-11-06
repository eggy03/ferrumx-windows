$diskSerials = Get-CimInstance Win32_DiskDrive |
            Where-Object -Property InterfaceType -NE "USB" |
            Select-Object -ExpandProperty SerialNumber

$cspId = Get-CimInstance Win32_ComputerSystemProduct | Select-Object -ExpandProperty UUID
$procIds = Get-CimInstance Win32_Processor | Select-Object -ExpandProperty ProcessorId

$result = @()
# trim and filter nulls
$result += $cspId | Where-Object { $_ } | ForEach-Object {$_.Trim()}
$result += $diskSerials | Where-Object { $_ } | ForEach-Object {$_.Trim()}
$result += $procIds | Where-Object { $_ } | ForEach-Object {$_.Trim()}

# sort and uppercase
$result = $result | Sort-Object
$result = $result | ForEach-Object { $_.ToUpperInvariant() }

# convert to string with '|' delimiter
$hwidString = [string]::Join('|', $result)

# hash and add '-' delimiter in the pattern of 8-4-4-12-16-16
$hash = Get-FileHash -InputStream([IO.MemoryStream]::new([Text.Encoding]::UTF8.GetBytes($hwidString))) -Algorithm SHA256 | Select-Object -ExpandProperty Hash
$formatted = ($hash.ToUpperInvariant() -replace '^(.{8})(.{4})(.{4})(.{4})(.{12})(.{16})(.+)$', '$1-$2-$3-$4-$5-$6-$7')

[PSCustomObject]@{
    HWIDRaw  = $hwidString
    HWIDHash = $formatted
} | ConvertTo-Json

