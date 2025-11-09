package unit.service.memory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.memory.Win32PhysicalMemory;
import io.github.eggy03.ferrumx.windows.service.memory.Win32PhysicalMemoryService;
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

class Win32PhysicalMemoryServiceTest {

    private Win32PhysicalMemoryService physicalMemoryService;

    private static Win32PhysicalMemory expectedMemory1;
    private static Win32PhysicalMemory expectedMemory2;

    private static String json;

    @BeforeAll
    static void setMemoryModules() {
        expectedMemory1 = Win32PhysicalMemory.builder()
                .tag("PhysicalMemory1")
                .name("Corsair Vengeance LPX DDR4")
                .manufacturer("Corsair")
                .model("CMK16GX4M2B3200C16")
                .otherIdentifyingInfo("DDR4-3200 16GB Module")
                .partNumber("CMK16GX4M2B3200C16")
                .formFactor(8)
                .bankLabel("BANK 0")
                .capacity(16L * 1024 * 1024 * 1024)
                .dataWidth(64)
                .speed(3200L)
                .configuredClockSpeed(3200L)
                .deviceLocator("DIMM_A1")
                .serialNumber("ABC123456789")
                .build();

        expectedMemory2 = Win32PhysicalMemory.builder()
                .tag("PhysicalMemory2")
                .name("G.Skill Trident Z5 DDR5")
                .manufacturer("G.Skill")
                .model("F5-6000J3238F16GX2-TZ5RK")
                .otherIdentifyingInfo("DDR5-6000 16GB Module")
                .partNumber("F5-6000J3238F16GX2-TZ5RK")
                .formFactor(8)
                .bankLabel("BANK 1")
                .capacity(16L * 1024 * 1024 * 1024)
                .dataWidth(64)
                .speed(6000L)
                .configuredClockSpeed(6000L)
                .deviceLocator("DIMM_B1")
                .serialNumber("XYZ987654321")
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonArray modules = new JsonArray();

        JsonObject mem1 = new JsonObject();
        mem1.addProperty("Tag", "PhysicalMemory1");
        mem1.addProperty("Name", "Corsair Vengeance LPX DDR4");
        mem1.addProperty("Manufacturer", "Corsair");
        mem1.addProperty("Model", "CMK16GX4M2B3200C16");
        mem1.addProperty("OtherIdentifyingInfo", "DDR4-3200 16GB Module");
        mem1.addProperty("PartNumber", "CMK16GX4M2B3200C16");
        mem1.addProperty("FormFactor", 8);
        mem1.addProperty("BankLabel", "BANK 0");
        mem1.addProperty("Capacity", 16L * 1024 * 1024 * 1024);
        mem1.addProperty("DataWidth", 64);
        mem1.addProperty("Speed", 3200L);
        mem1.addProperty("ConfiguredClockSpeed", 3200L);
        mem1.addProperty("DeviceLocator", "DIMM_A1");
        mem1.addProperty("SerialNumber", "ABC123456789");

        JsonObject mem2 = new JsonObject();
        mem2.addProperty("Tag", "PhysicalMemory2");
        mem2.addProperty("Name", "G.Skill Trident Z5 DDR5");
        mem2.addProperty("Manufacturer", "G.Skill");
        mem2.addProperty("Model", "F5-6000J3238F16GX2-TZ5RK");
        mem2.addProperty("OtherIdentifyingInfo", "DDR5-6000 16GB Module");
        mem2.addProperty("PartNumber", "F5-6000J3238F16GX2-TZ5RK");
        mem2.addProperty("FormFactor", 8);
        mem2.addProperty("BankLabel", "BANK 1");
        mem2.addProperty("Capacity", 16L * 1024 * 1024 * 1024);
        mem2.addProperty("DataWidth", 64);
        mem2.addProperty("Speed", 6000L);
        mem2.addProperty("ConfiguredClockSpeed", 6000L);
        mem2.addProperty("DeviceLocator", "DIMM_B1");
        mem2.addProperty("SerialNumber", "XYZ987654321");

        modules.add(mem1);
        modules.add(mem2);

        json = new GsonBuilder().serializeNulls().create().toJson(modules);
    }


    @BeforeEach
    void setUp() {
        physicalMemoryService = new Win32PhysicalMemoryService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> mockPowershell = mockStatic(PowerShell.class)) {
            mockPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32PhysicalMemory> memories = physicalMemoryService.get();
            assertEquals(2, memories.size());

            assertThat(memories.get(0)).usingRecursiveComparison().isEqualTo(expectedMemory1);
            assertThat(memories.get(1)).usingRecursiveComparison().isEqualTo(expectedMemory2);
        }
    }

    @Test
    void test_get_emptyJson_returnsEmptyList() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString()))
                    .thenReturn(mockResponse);

            List<Win32PhysicalMemory> memories = physicalMemoryService.get();
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

            List<Win32PhysicalMemory> memories = physicalMemoryService.get(mockShell);
            assertEquals(2, memories.size());

            assertThat(memories.get(0)).usingRecursiveComparison().isEqualTo(expectedMemory1);
            assertThat(memories.get(1)).usingRecursiveComparison().isEqualTo(expectedMemory2);
        }
    }

    @Test
    void test_getWithSession_emptyJson_returnsEmptyList() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32PhysicalMemory> memories = physicalMemoryService.get(mockShell);
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
        Field[] declaredClassFields = Win32PhysicalMemory.class.getDeclaredFields();
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
