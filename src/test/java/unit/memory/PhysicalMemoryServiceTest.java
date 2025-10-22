package unit.memory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.memory.PhysicalMemory;
import io.github.eggy03.ferrumx.windows.service.memory.PhysicalMemoryService;
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

class PhysicalMemoryServiceTest {

    private PhysicalMemoryService physicalMemoryService;

    private static String json;

    @BeforeAll
    static void setupJson() {
        JsonArray memories = new JsonArray();

        JsonObject mem0 = new JsonObject();
        mem0.addProperty("Tag", "Physical Memory 0");
        mem0.addProperty("Name", "DIMM0");
        mem0.addProperty("Manufacturer", "Kingston");
        mem0.addProperty("Capacity", 8589934592L);
        mem0.addProperty("Speed", 3200);
        mem0.addProperty("SerialNumber", "12345678");

        JsonObject mem1 = new JsonObject();
        mem1.addProperty("Tag", "Physical Memory 1");
        mem1.addProperty("Name", "DIMM1");
        mem1.addProperty("Manufacturer", "Corsair");
        mem1.addProperty("Capacity", 8589934592L);
        mem1.addProperty("Speed", 3200);
        mem1.addProperty("SerialNumber", "87654321");

        memories.add(mem0);
        memories.add(mem1);

        json = new Gson().toJson(memories);
    }

    @BeforeEach
    void setUp() {
        physicalMemoryService = new PhysicalMemoryService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> mockPowershell = mockStatic(PowerShell.class)) {
            mockPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<PhysicalMemory> memories = physicalMemoryService.get();
            assertFalse(memories.isEmpty());
            assertEquals("Physical Memory 0", memories.get(0).getTag());
            assertEquals("DIMM0", memories.get(0).getName());
            assertEquals("Physical Memory 1", memories.get(1).getTag());
            assertEquals("DIMM1", memories.get(1).getName());
        }
    }

    @Test
    void test_get_emptyJson_returnsEmptyList() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString()))
                    .thenReturn(mockResponse);

            List<PhysicalMemory> memories = physicalMemoryService.get();
            assertTrue(memories.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try (MockedStatic<PowerShell> mockPowershell = mockStatic(PowerShell.class)) {
            mockPowershell.when(() -> PowerShell.executeSingleCommand(anyString()))
                    .thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class,
                    () -> physicalMemoryService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<PhysicalMemory> memories = physicalMemoryService.get(mockShell);
            assertFalse(memories.isEmpty());
            assertEquals("Physical Memory 0", memories.get(0).getTag());
            assertEquals("DIMM0", memories.get(0).getName());
            assertEquals("Physical Memory 1", memories.get(1).getTag());
            assertEquals("DIMM1", memories.get(1).getName());
        }
    }

    @Test
    void test_getWithSession_emptyJson_returnsEmptyList() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<PhysicalMemory> memories = physicalMemoryService.get(mockShell);
            assertTrue(memories.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);
            assertThrows(JsonSyntaxException.class, () -> physicalMemoryService.get(mockShell));
        }
    }
}
