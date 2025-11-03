package unit.service.storage;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32DiskDrive;
import io.github.eggy03.ferrumx.windows.service.storage.Win32DiskDriveService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class Win32DiskDriveServiceTest {

    private Win32DiskDriveService diskDriveService;

    private static Win32DiskDrive expectedDiskDrive1;
    private static Win32DiskDrive expectedDiskDrive2;
    private static String json;

    @BeforeAll
    static void setDiskDrives() {
        expectedDiskDrive1 = Win32DiskDrive.builder()
                .deviceId("\\\\.\\PHYSICALDRIVE0")
                .caption("Samsung SSD 970 EVO")
                .model("MZ-V7E1T0")
                .size(1000204886016L)
                .firmwareRevision("2B2QEXM7")
                .serialNumber("S4EVNX0M123456")
                .partitions(3L)
                .status("OK")
                .interfaceType("NVMe")
                .pnpDeviceId("PCI\\VEN_144D&DEV_A808&SUBSYS_0A0E144D&REV_01\\4&1A2B3C4D&0&000000")
                .build();

        expectedDiskDrive2 = Win32DiskDrive.builder()
                .deviceId("\\\\.\\PHYSICALDRIVE1")
                .caption("Seagate BarraCuda 2TB")
                .model("ST2000DM008")
                .size(2000398934016L)
                .firmwareRevision("CC26")
                .serialNumber("ZFL123ABC456")
                .partitions(2L)
                .status("OK")
                .interfaceType("SATA")
                .pnpDeviceId("PCI\\VEN_8086&DEV_A102&SUBSYS_85C41043&REV_31\\3&11583659&0&FA")
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonObject disk1 = new JsonObject();
        disk1.addProperty("DeviceID", "\\\\.\\PHYSICALDRIVE0");
        disk1.addProperty("Caption", "Samsung SSD 970 EVO");
        disk1.addProperty("Model", "MZ-V7E1T0");
        disk1.addProperty("Size", 1000204886016L);
        disk1.addProperty("FirmwareRevision", "2B2QEXM7");
        disk1.addProperty("SerialNumber", "S4EVNX0M123456");
        disk1.addProperty("Partitions", 3L);
        disk1.addProperty("Status", "OK");
        disk1.addProperty("InterfaceType", "NVMe");
        disk1.addProperty("PNPDeviceID", "PCI\\VEN_144D&DEV_A808&SUBSYS_0A0E144D&REV_01\\4&1A2B3C4D&0&000000");

        JsonObject disk2 = new JsonObject();
        disk2.addProperty("DeviceID", "\\\\.\\PHYSICALDRIVE1");
        disk2.addProperty("Caption", "Seagate BarraCuda 2TB");
        disk2.addProperty("Model", "ST2000DM008");
        disk2.addProperty("Size", 2000398934016L);
        disk2.addProperty("FirmwareRevision", "CC26");
        disk2.addProperty("SerialNumber", "ZFL123ABC456");
        disk2.addProperty("Partitions", 2L);
        disk2.addProperty("Status", "OK");
        disk2.addProperty("InterfaceType", "SATA");
        disk2.addProperty("PNPDeviceID", "PCI\\VEN_8086&DEV_A102&SUBSYS_85C41043&REV_31\\3&11583659&0&FA");

        JsonArray array = new JsonArray();
        array.add(disk1);
        array.add(disk2);

        json = new Gson().toJson(array);
    }

    @BeforeEach
    void setUp() {
        diskDriveService = new Win32DiskDriveService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32DiskDrive> disks = diskDriveService.get();
            assertEquals(2, disks.size());

            assertThat(disks.get(0)).usingRecursiveComparison().isEqualTo(expectedDiskDrive1);
            assertThat(disks.get(1)).usingRecursiveComparison().isEqualTo(expectedDiskDrive2);
        }
    }

    @Test
    void test_get_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32DiskDrive> disks = diskDriveService.get();
            assertTrue(disks.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> diskDriveService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32DiskDrive> disks = diskDriveService.get(mockShell);
            assertEquals(2, disks.size());

            assertThat(disks.get(0)).usingRecursiveComparison().isEqualTo(expectedDiskDrive1);
            assertThat(disks.get(1)).usingRecursiveComparison().isEqualTo(expectedDiskDrive2);
        }
    }

    @Test
    void test_getWithSession_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32DiskDrive> disks = diskDriveService.get(mockShell);
            assertTrue(disks.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> diskDriveService.get(mockShell));
        }
    }
}