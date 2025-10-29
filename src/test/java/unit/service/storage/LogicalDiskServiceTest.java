package unit.service.storage;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.storage.LogicalDisk;
import io.github.eggy03.ferrumx.windows.service.storage.LogicalDiskService;
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

class LogicalDiskServiceTest {
    
    private LogicalDiskService logicalDiskService;

    private static String json;

    @BeforeAll
    static void setupJson() {
        JsonArray volumes = new JsonArray();

        JsonObject volume0 = new JsonObject();
        volume0.addProperty("DeviceID", "C:");
        volume0.addProperty("FileSystem", "NTFS");

        JsonObject volume1 = new JsonObject();
        volume1.addProperty("DeviceID", "D:");
        volume1.addProperty("FileSystem", "ReFS");

        volumes.add(volume0);
        volumes.add(volume1);

        json = new Gson().toJson(volumes);
    }

    @BeforeEach
    void setUp() {
        logicalDiskService = new LogicalDiskService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<LogicalDisk> disks = logicalDiskService.get();
            assertFalse(disks.isEmpty());
            assertEquals("C:", disks.get(0).getDeviceId());
            assertEquals("NTFS", disks.get(0).getFileSystem());
            assertEquals("D:", disks.get(1).getDeviceId());
            assertEquals("ReFS", disks.get(1).getFileSystem());
        }
    }

    @Test
    void test_get_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<LogicalDisk> disks = logicalDiskService.get();
            assertTrue(disks.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> logicalDiskService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<LogicalDisk> disks = logicalDiskService.get(mockShell);
            assertFalse(disks.isEmpty());
            assertEquals("C:", disks.get(0).getDeviceId());
            assertEquals("NTFS", disks.get(0).getFileSystem());
            assertEquals("D:", disks.get(1).getDeviceId());
            assertEquals("ReFS", disks.get(1).getFileSystem());
        }
    }

    @Test
    void test_getWithSession_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<LogicalDisk> disks = logicalDiskService.get(mockShell);
            assertTrue(disks.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> logicalDiskService.get(mockShell));
        }
    }
}
