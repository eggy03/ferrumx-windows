/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package unit.service.processor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.processor.Win32Processor;
import io.github.eggy03.ferrumx.windows.service.processor.Win32ProcessorService;
import io.github.eggy03.ferrumx.windows.utility.TerminalUtility;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class Win32ProcessorServiceTest {

    private static Win32Processor expectedProcessor;
    private static String json;
    private Win32ProcessorService service;

    @BeforeAll
    static void setProcessor() {
        expectedProcessor = Win32Processor.builder()
                .deviceId("CPU0")
                .name("Intel(R) Core(TM) i9-13900K")
                .numberOfCores(24)
                .numberOfEnabledCores(24)
                .threadCount(32)
                .numberOfLogicalProcessors(32)
                .manufacturer("GenuineIntel")
                .addressWidth(64)
                .l2CacheSize(2048)
                .l3CacheSize(36864)
                .maxClockSpeed(5300)
                .extClock(100)
                .socketDesignation("LGA1700")
                .version("Model 151 Stepping 2")
                .caption("Intel64 Family 6 Model 151 Stepping 2")
                .family(6)
                .stepping("2")
                .virtualizationFirmwareEnabled(true)
                .processorId("BFEBFBFF000B0671")
                .architecture(9)
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonObject cpu = new JsonObject();
        cpu.addProperty("DeviceID", "CPU0");
        cpu.addProperty("Name", "Intel(R) Core(TM) i9-13900K");
        cpu.addProperty("NumberOfCores", 24);
        cpu.addProperty("NumberOfEnabledCore", 24);
        cpu.addProperty("ThreadCount", 32);
        cpu.addProperty("NumberOfLogicalProcessors", 32);
        cpu.addProperty("Manufacturer", "GenuineIntel");
        cpu.addProperty("AddressWidth", 64);
        cpu.addProperty("L2CacheSize", 2048);
        cpu.addProperty("L3CacheSize", 36864);
        cpu.addProperty("MaxClockSpeed", 5300);
        cpu.addProperty("ExtClock", 100);
        cpu.addProperty("SocketDesignation", "LGA1700");
        cpu.addProperty("Version", "Model 151 Stepping 2");
        cpu.addProperty("Caption", "Intel64 Family 6 Model 151 Stepping 2");
        cpu.addProperty("Family", 6);
        cpu.addProperty("Stepping", "2");
        cpu.addProperty("VirtualizationFirmwareEnabled", true);
        cpu.addProperty("ProcessorId", "BFEBFBFF000B0671");
        cpu.addProperty("Architecture", 9);

        json = new GsonBuilder().serializeNulls().create().toJson(cpu);
    }

    @BeforeEach
    void setUp() {
        service = new Win32ProcessorService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32Processor> processorList = service.get();
            assertEquals(1, processorList.size());
            assertThat(processorList.get(0)).usingRecursiveComparison().isEqualTo(expectedProcessor);
        }
    }

    @Test
    void test_get_emptyJson_empty() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32Processor> processorList = service.get();
            assertTrue(processorList.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> service.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32Processor> processorList = service.get(mockShell);
            assertEquals(1, processorList.size());
            assertThat(processorList.get(0)).usingRecursiveComparison().isEqualTo(expectedProcessor);
        }
    }

    @Test
    void test_getWithSession_emptyJson_empty() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32Processor> processorList = service.get(mockShell);
            assertTrue(processorList.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);
            assertThrows(JsonSyntaxException.class, () -> service.get(mockShell));
        }
    }

    @Test
    void test_getWithTimeout_success() {

        try (MockedStatic<TerminalUtility> mockedTerminal = mockStatic(TerminalUtility.class)) {
            mockedTerminal
                    .when(() -> TerminalUtility.executeCommand(anyString(), anyLong()))
                    .thenReturn(json);

            List<Win32Processor> processorList = service.get(5L);
            assertEquals(1, processorList.size());
            assertThat(processorList.get(0)).usingRecursiveComparison().isEqualTo(expectedProcessor);
        }
    }

    @Test
    void test_getWithTimeout_invalidJson_throwsException() {

        try (MockedStatic<TerminalUtility> mockedTerminal = mockStatic(TerminalUtility.class)) {
            mockedTerminal
                    .when(() -> TerminalUtility.executeCommand(anyString(), anyLong()))
                    .thenReturn("invalid json");

            assertThrows(JsonSyntaxException.class, () -> service.get(5L));
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
        Field[] declaredClassFields = Win32Processor.class.getDeclaredFields();
        Set<String> serializedNames = new HashSet<>();

        for (Field field : declaredClassFields) {
            SerializedName s = field.getAnnotation(SerializedName.class);
            serializedNames.add(s != null ? s.value() : field.getName());
        }

        // Extract JSON keys from the static test JSON
        Set<String> jsonKeys = new Gson().fromJson(json, JsonObject.class).keySet();

        // Validate equality of keys vs serialized names
        assertThat(serializedNames)
                .as("Entity fields and JSON keys must match exactly")
                .containsExactlyInAnyOrderElementsOf(jsonKeys);
    }
}
