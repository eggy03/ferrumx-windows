package unit.storage;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.storage.DiskDrive;
import io.github.eggy03.ferrumx.windows.service.storage.DiskDriveService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class DiskDriveServiceTest {

    private DiskDriveService diskDriveService;

    private static String json;

    @BeforeAll
    static void setupJson() {
        JsonArray disks = new JsonArray();

        JsonObject disk0 = new JsonObject();
        disk0.addProperty("DeviceID", "Disk0");
        disk0.addProperty("Caption", "Samsung SSD 970");
        disk0.addProperty("Model", "970 EVO Plus");
        disk0.addProperty("Size", 1000204886016L);
        disk0.addProperty("SerialNumber", "S4EMNX0K123456");
        disk0.addProperty("Partitions", 3);
        disk0.addProperty("Status", "OK");
        disk0.addProperty("InterfaceType", "NVMe");

        JsonObject disk1 = new JsonObject();
        disk1.addProperty("DeviceID", "Disk1");
        disk1.addProperty("Caption", "WD Blue HDD");
        disk1.addProperty("Model", "WD10EZEX");
        disk1.addProperty("Size", 1000204886016L);
        disk1.addProperty("SerialNumber", "WD-WCC4E1234567");
        disk1.addProperty("Partitions", 1);
        disk1.addProperty("Status", "OK");
        disk1.addProperty("InterfaceType", "SATA");

        disks.add(disk0);
        disks.add(disk1);

        json = new Gson().toJson(disks);
    }

    @BeforeEach
    void setUp() {
        diskDriveService = new DiskDriveService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<DiskDrive> disks = diskDriveService.get();
            assertFalse(disks.isEmpty());
            assertEquals("Disk0", disks.get(0).getDeviceId());
            assertEquals("Samsung SSD 970", disks.get(0).getCaption());
            assertEquals("Disk1", disks.get(1).getDeviceId());
            assertEquals("WD Blue HDD", disks.get(1).getCaption());
        }
    }

    @Test
    void test_get_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<DiskDrive> disks = diskDriveService.get();
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

            List<DiskDrive> disks = diskDriveService.get(mockShell);
            assertFalse(disks.isEmpty());
            assertEquals("Disk0", disks.get(0).getDeviceId());
            assertEquals("Samsung SSD 970", disks.get(0).getCaption());
            assertEquals("Disk1", disks.get(1).getDeviceId());
            assertEquals("WD Blue HDD", disks.get(1).getCaption());
        }
    }

    @Test
    void test_getWithSession_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<DiskDrive> disks = diskDriveService.get(mockShell);
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