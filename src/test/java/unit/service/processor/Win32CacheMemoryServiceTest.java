package unit.service.processor;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.processor.Win32CacheMemory;
import io.github.eggy03.ferrumx.windows.service.processor.Win32CacheMemoryService;
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

class Win32CacheMemoryServiceTest {

    private Win32CacheMemoryService processorCacheService;

    private static Win32CacheMemory expectedL1Cache;
    private static Win32CacheMemory expectedL2Cache;
    private static Win32CacheMemory expectedL3Cache;

    private static String json;

    @BeforeAll
    static void setCaches() {
        expectedL1Cache = Win32CacheMemory.builder()
                .deviceId("CPU0_L1")
                .purpose("Instruction")
                .installedSize(256L)
                .associativity(4)
                .build();

        expectedL2Cache = Win32CacheMemory.builder()
                .deviceId("CPU0_L2")
                .purpose("Unified")
                .installedSize(2048L)
                .associativity(8)
                .build();

        expectedL3Cache = Win32CacheMemory.builder()
                .deviceId("CPU0_L3")
                .purpose("Unified")
                .installedSize(16384L)
                .associativity(8)
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonArray caches = new JsonArray();

        JsonObject l1 = new JsonObject();
        l1.addProperty("DeviceID", "CPU0_L1");
        l1.addProperty("Purpose", "Instruction");
        l1.addProperty("InstalledSize", 256);
        l1.addProperty("Associativity", 4);

        JsonObject l2 = new JsonObject();
        l2.addProperty("DeviceID", "CPU0_L2");
        l2.addProperty("Purpose", "Unified");
        l2.addProperty("InstalledSize", 2048);
        l2.addProperty("Associativity", 8);

        JsonObject l3 = new JsonObject();
        l3.addProperty("DeviceID", "CPU0_L3");
        l3.addProperty("Purpose", "Unified");
        l3.addProperty("InstalledSize", 16384);
        l3.addProperty("Associativity", 8);

        caches.add(l1);
        caches.add(l2);
        caches.add(l3);

        json = new Gson().toJson(caches);
    }


    @BeforeEach
    void setUp() {
        processorCacheService = new Win32CacheMemoryService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try(MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32CacheMemory> cache = processorCacheService.get();
            assertEquals(3, cache.size());

            assertThat(cache.get(0)).usingRecursiveComparison().isEqualTo(expectedL1Cache);
            assertThat(cache.get(1)).usingRecursiveComparison().isEqualTo(expectedL2Cache);
            assertThat(cache.get(2)).usingRecursiveComparison().isEqualTo(expectedL3Cache);
        }
    }

    @Test
    void test_get_emptyJson_empty() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try(MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32CacheMemory> cache = processorCacheService.get();
            assertTrue(cache.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try(MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, ()-> processorCacheService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32CacheMemory> cache = processorCacheService.get(mockShell);
            assertEquals(3, cache.size());

            assertThat(cache.get(0)).usingRecursiveComparison().isEqualTo(expectedL1Cache);
            assertThat(cache.get(1)).usingRecursiveComparison().isEqualTo(expectedL2Cache);
            assertThat(cache.get(2)).usingRecursiveComparison().isEqualTo(expectedL3Cache);
        }
    }

    @Test
    void test_getWithSession_emptyJson_empty() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32CacheMemory> cache = processorCacheService.get(mockShell);
            assertTrue(cache.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, ()-> processorCacheService.get(mockShell));
        }
    }
}