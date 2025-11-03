package unit.service.system;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.system.Win32Environment;
import io.github.eggy03.ferrumx.windows.service.system.Win32EnvironmentService;
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

class Win32EnvironmentTest {

    private Win32EnvironmentService envService;

    private static Win32Environment sysVar;
    private static Win32Environment userVar;
    private static String json;

    @BeforeAll
    static void setEnvironmentVariables() {
        sysVar = Win32Environment.builder()
                .name("PATH")
                .isSystemVariable(true)
                .variableValue("C:\\Windows\\System32")
                .build();

        userVar = Win32Environment.builder()
                .name("TEMP")
                .isSystemVariable(false)
                .variableValue("C:\\Users\\User\\AppData\\Local\\Temp")
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonObject sysEnv = new JsonObject();
        sysEnv.addProperty("Name", "PATH");
        sysEnv.addProperty("SystemVariable", true);
        sysEnv.addProperty("VariableValue", "C:\\Windows\\System32");

        JsonObject userEnv = new JsonObject();
        userEnv.addProperty("Name", "TEMP");
        userEnv.addProperty("SystemVariable", false);
        userEnv.addProperty("VariableValue", "C:\\Users\\User\\AppData\\Local\\Temp");

        JsonArray array = new JsonArray();
        array.add(sysEnv);
        array.add(userEnv);

        json = new GsonBuilder().serializeNulls().create().toJson(array);
    }


    @BeforeEach
    void setUp() {
        envService = new Win32EnvironmentService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32Environment> envList = envService.get();
            assertEquals(2, envList.size());

            assertThat(envList.get(0)).usingRecursiveComparison().isEqualTo(sysVar);
            assertThat(envList.get(1)).usingRecursiveComparison().isEqualTo(userVar);
        }
    }

    @Test
    void test_get_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32Environment> envList = envService.get();
            assertTrue(envList.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> envService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32Environment> envList = envService.get(mockShell);
            assertEquals(2, envList.size());

            assertThat(envList.get(0)).usingRecursiveComparison().isEqualTo(sysVar);
            assertThat(envList.get(1)).usingRecursiveComparison().isEqualTo(userVar);
        }
    }

    @Test
    void test_getWithSession_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32Environment> envList = envService.get(mockShell);
            assertTrue(envList.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> envService.get(mockShell));
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
        Field[] declaredClassFields = Win32Environment.class.getDeclaredFields();
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
