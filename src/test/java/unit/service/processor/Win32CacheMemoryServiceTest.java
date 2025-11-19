/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package unit.service.processor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.processor.Win32CacheMemory;
import io.github.eggy03.ferrumx.windows.service.processor.Win32CacheMemoryService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                .cacheType(3)
                .level(3)
                .installedSize(256L)
                .associativity(5)
                .location(0)
                .errorCorrectType(5)
                .availability(3)
                .status("OK")
                .statusInfo(3)
                .build();

        expectedL2Cache = Win32CacheMemory.builder()
                .deviceId("CPU0_L2")
                .purpose("Unified")
                .cacheType(5)
                .level(4)
                .installedSize(2048L)
                .associativity(7)
                .location(0)
                .errorCorrectType(5)
                .availability(3)
                .status("OK")
                .statusInfo(3)
                .build();

        expectedL3Cache = Win32CacheMemory.builder()
                .deviceId("CPU0_L3")
                .purpose("Unified")
                .cacheType(5)
                .level(5)
                .installedSize(16384L)
                .associativity(8)
                .location(0)
                .errorCorrectType(5)
                .availability(3)
                .status("OK")
                .statusInfo(3)
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonArray caches = new JsonArray();

        JsonObject l1 = new JsonObject();
        l1.addProperty("DeviceID", "CPU0_L1");
        l1.addProperty("Purpose", "Instruction");
        l1.addProperty("CacheType", 3);
        l1.addProperty("Level", 3);
        l1.addProperty("InstalledSize", 256);
        l1.addProperty("Associativity", 5);
        l1.addProperty("Location", 0);
        l1.addProperty("ErrorCorrectType", 5);
        l1.addProperty("Availability", 3);
        l1.addProperty("Status", "OK");
        l1.addProperty("StatusInfo", 3);

        JsonObject l2 = new JsonObject();
        l2.addProperty("DeviceID", "CPU0_L2");
        l2.addProperty("Purpose", "Unified");
        l2.addProperty("CacheType", 5);
        l2.addProperty("Level", 4);
        l2.addProperty("InstalledSize", 2048);
        l2.addProperty("Associativity", 7);
        l2.addProperty("Location", 0);
        l2.addProperty("ErrorCorrectType", 5);
        l2.addProperty("Availability", 3);
        l2.addProperty("Status", "OK");
        l2.addProperty("StatusInfo", 3);

        JsonObject l3 = new JsonObject();
        l3.addProperty("DeviceID", "CPU0_L3");
        l3.addProperty("Purpose", "Unified");
        l3.addProperty("CacheType", 5);
        l3.addProperty("Level", 5);
        l3.addProperty("InstalledSize", 16384);
        l3.addProperty("Associativity", 8);
        l3.addProperty("Location", 0);
        l3.addProperty("ErrorCorrectType", 5);
        l3.addProperty("Availability", 3);
        l3.addProperty("Status", "OK");
        l3.addProperty("StatusInfo", 3);

        caches.add(l1);
        caches.add(l2);
        caches.add(l3);

        json = new GsonBuilder().serializeNulls().create().toJson(caches);
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

    /*
     * This test ensures that the test JSON has keys matching all @SerializedName
     * (or raw field names if not annotated) declared in the entity class.
     *
     * The test fails if:
     * - any field is added or removed in the entity without updating the test JSON
     * - any @SerializedName value changes without updating the test JSON
     */
    @Test
    void test_entityFieldParity_withTestJson() {

        // get the serialized name for each field, in a set
        // store the field name in case no serialized names are found
        Field[] declaredClassFields = Win32CacheMemory.class.getDeclaredFields();
        Set<String> serializedNames = new HashSet<>();

        for(Field field: declaredClassFields){
            SerializedName s = field.getAnnotation(SerializedName.class);
            serializedNames.add(s!=null ? s.value() : field.getName());
        }

        // Extract JSON keys from the static test JSON
        Set<String> jsonKeys = new Gson().fromJson(json, JsonArray.class)
                .get(0).getAsJsonObject().keySet();

        // Validate equality of keys vs serialized names
        assertThat(serializedNames)
                .as("Entity fields and JSON keys must match exactly")
                .containsExactlyInAnyOrderElementsOf(jsonKeys);
    }
}