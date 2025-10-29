package unit.service.storage;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.storage.DiskPartition;
import io.github.eggy03.ferrumx.windows.service.storage.DiskPartitionService;
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

class DiskPartitionServiceTest {

    private DiskPartitionService diskPartitionService;

    private static String json;

    @BeforeAll
    static void setupJson() {
        JsonArray partitions = new JsonArray();

        JsonObject part1 = new JsonObject();
        part1.addProperty("DeviceID", "Disk0_Part1");
        part1.addProperty("Name", "C:");
        part1.addProperty("Description", "System Partition");
        part1.addProperty("Size", 500107862016L);
        part1.addProperty("Bootable", true);
        part1.addProperty("PrimaryPartition", true);
        part1.addProperty("BootPartition", true);
        part1.addProperty("DiskIndex", 0);

        JsonObject part2 = new JsonObject();
        part2.addProperty("DeviceID", "Disk0_Part2");
        part2.addProperty("Name", "D:");
        part2.addProperty("Description", "Data Partition");
        part2.addProperty("Size", 500107862016L);
        part2.addProperty("Bootable", false);
        part2.addProperty("PrimaryPartition", true);
        part2.addProperty("BootPartition", false);
        part2.addProperty("DiskIndex", 0);

        partitions.add(part1);
        partitions.add(part2);

        json = new Gson().toJson(partitions);
    }

    @BeforeEach
    void setUp() {
        diskPartitionService = new DiskPartitionService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<DiskPartition> partitions = diskPartitionService.get();
            assertFalse(partitions.isEmpty());
            assertEquals("Disk0_Part1", partitions.get(0).getDeviceId());
            assertEquals("C:", partitions.get(0).getName());
            assertEquals("Disk0_Part2", partitions.get(1).getDeviceId());
            assertEquals("D:", partitions.get(1).getName());
        }
    }

    @Test
    void test_get_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<DiskPartition> partitions = diskPartitionService.get();
            assertTrue(partitions.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> diskPartitionService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<DiskPartition> partitions = diskPartitionService.get(mockShell);
            assertFalse(partitions.isEmpty());
            assertEquals("Disk0_Part1", partitions.get(0).getDeviceId());
            assertEquals("C:", partitions.get(0).getName());
            assertEquals("Disk0_Part2", partitions.get(1).getDeviceId());
            assertEquals("D:", partitions.get(1).getName());
        }
    }

    @Test
    void test_getWithSession_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<DiskPartition> partitions = diskPartitionService.get(mockShell);
            assertTrue(partitions.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> diskPartitionService.get(mockShell));
        }
    }
}
