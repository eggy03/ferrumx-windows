$bios = Get-CimInstance Win32_BIOS | Select-Object -Property SMBIOSBIOSVersion, SMBIOSMajorVersion, SMBIOSMinorVersion, SystemBiosMajorVersion, SystemBiosMinorVersion
$baseboard = Get-CimInstance Win32_BaseBoard | Select-Object -Property Manufacturer, Model, OtherIdentifyingInfo, PartNumber, SerialNumber, SKU, Version, Product
$processorIds = Get-CimInstance Win32_Processor | Select-Object -ExpandProperty ProcessorID

$result = @()

foreach($id in $processorIds) {
    if($null -ne $id){
        $result += $id.Trim()
    }
}

foreach($biosObject in $bios) {
    foreach($property in $biosObject.PSObject.Properties){
        $value = $property.Value
        if($null -ne $value){
            $result += $value.ToString().Trim()
        }
    }
}

foreach($baseboardObject in $baseboard){
    foreach($property in $baseboardObject.PSObject.Properties){
        $value = $property.Value
        if($null -ne $value){
            $result += $value.ToString().Trim()
        }
    }
}

# sort
$result = $result | Sort-Object

# convert to string with '|' delimiter
$hwidString = [string]::Join('|', $result)

# hash and add '-' delimiter in the pattern of 8-4-4-12-16-16
$hash = Get-FileHash -InputStream([IO.MemoryStream]::new([Text.Encoding]::UTF8.GetBytes($hwidString))) -Algorithm SHA256 | Select-Object -ExpandProperty Hash
$formatted = ($hash.ToUpperInvariant() -replace '^(.{8})(.{4})(.{4})(.{4})(.{12})(.{16})(.+)$', '$1-$2-$3-$4-$5-$6-$7')

[PSCustomObject]@{
    HWIDRaw  = $hwidString
    HWIDHash = $formatted
} | ConvertTo-Json