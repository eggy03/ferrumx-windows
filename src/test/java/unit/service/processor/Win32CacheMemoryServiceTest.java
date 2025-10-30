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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class Win32CacheMemoryServiceTest {

    private Win32CacheMemoryService processorCacheService;

    private static String jsonProcessor;

    @BeforeAll
    static void setupJson() {
        JsonArray caches = new JsonArray();

        JsonObject cache1 = new JsonObject();
        cache1.addProperty("DeviceID", "1");
        cache1.addProperty("Purpose", "Level 1 Cache");
        cache1.addProperty("InstalledSize", 32);
        cache1.addProperty("Associativity", 0);

        JsonObject cache2 = new JsonObject();
        cache2.addProperty("DeviceID", "2");
        cache2.addProperty("Purpose", "Level 2 Cache");
        cache2.addProperty("InstalledSize", 256);
        cache2.addProperty("Associativity", 7);

        caches.add(cache1);
        caches.add(cache2);

        jsonProcessor = new Gson().toJson(caches);
    }

    @BeforeEach
    void setUp() {
        processorCacheService = new Win32CacheMemoryService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(jsonProcessor);

        try(MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32CacheMemory> cache = processorCacheService.get();
            assertFalse(cache.isEmpty());
            assertEquals("1", cache.get(0).getDeviceId());
            assertEquals("2", cache.get(1).getDeviceId());
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
        when(mockResponse.getCommandOutput()).thenReturn(jsonProcessor);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32CacheMemory> cache = processorCacheService.get(mockShell);
            assertFalse(cache.isEmpty());
            assertEquals("1", cache.get(0).getDeviceId());
            assertEquals("2", cache.get(1).getDeviceId());
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