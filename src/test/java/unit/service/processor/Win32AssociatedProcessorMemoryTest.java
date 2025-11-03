package unit.service.processor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.processor.Win32AssociatedProcessorMemory;
import io.github.eggy03.ferrumx.windows.service.processor.Win32AssociatedProcessorMemoryService;
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

class Win32AssociatedProcessorMemoryTest {

    private Win32AssociatedProcessorMemoryService apmService;

    private static Win32AssociatedProcessorMemory expectedAssoc1;
    private static Win32AssociatedProcessorMemory expectedAssoc2;

    private static String json;

    @BeforeAll
    static void setAssociations() {
        expectedAssoc1 = Win32AssociatedProcessorMemory.builder()
                .cacheMemoryDeviceId("CacheMemory0")
                .processorDeviceId("CPU0")
                .build();

        expectedAssoc2 = Win32AssociatedProcessorMemory.builder()
                .cacheMemoryDeviceId("CacheMemory1")
                .processorDeviceId("CPU1")
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonArray associations = new JsonArray();

        JsonObject assoc1 = new JsonObject();
        assoc1.addProperty("CacheMemoryDeviceID", "CacheMemory0");
        assoc1.addProperty("ProcessorDeviceID", "CPU0");

        JsonObject assoc2 = new JsonObject();
        assoc2.addProperty("CacheMemoryDeviceID", "CacheMemory1");
        assoc2.addProperty("ProcessorDeviceID", "CPU1");

        associations.add(assoc1);
        associations.add(assoc2);

        json = new GsonBuilder().serializeNulls().create().toJson(associations);
    }


    @BeforeEach
    void setApmService() {
        apmService = new Win32AssociatedProcessorMemoryService();
    }

    @Test
    void test_get_success() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try(MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)){
            powerShellMock.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32AssociatedProcessorMemory> apmList = apmService.get();
            assertEquals(2, apmList.size());

            assertThat(apmList.get(0)).usingRecursiveComparison().isEqualTo(expectedAssoc1);
            assertThat(apmList.get(1)).usingRecursiveComparison().isEqualTo(expectedAssoc2);
        }

    }

    @Test
    void test_get_emptyJson_emptyResult() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMockedStatic = mockStatic(PowerShell.class)){
            powerShellMockedStatic.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);
            List<Win32AssociatedProcessorMemory> apmList = apmService.get();
            assertTrue(apmList.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try (MockedStatic<PowerShell> powerShellMockedStatic = mockStatic(PowerShell.class)){
            powerShellMockedStatic.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);
            assertThrows(JsonSyntaxException.class, ()-> apmService.get());
        }
    }

    @Test
    void test_getWithSession_success() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try(PowerShell mockedShell = mock(PowerShell.class)){
            when(mockedShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32AssociatedProcessorMemory> apmList = apmService.get(mockedShell);
            assertEquals(2, apmList.size());

            assertThat(apmList.get(0)).usingRecursiveComparison().isEqualTo(expectedAssoc1);
            assertThat(apmList.get(1)).usingRecursiveComparison().isEqualTo(expectedAssoc2);
        }
    }

    @Test
    void test_getWithSession_emptyJson_emptyResult() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try(PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);
            List<Win32AssociatedProcessorMemory> apmList = apmService.get(mockShell);
            assertTrue(apmList.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("malformed json");

        try(PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);
            assertThrows(JsonSyntaxException.class, ()-> apmService.get(mockShell));
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
        Field[] declaredClassFields = Win32AssociatedProcessorMemory.class.getDeclaredFields();
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
