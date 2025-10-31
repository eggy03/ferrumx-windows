package unit.service.system;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.system.Win32Environment;
import io.github.eggy03.ferrumx.windows.service.system.Win32EnvironmentService;
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

class Win32EnvironmentTest {

    private Win32EnvironmentService envService;

    private static String json;

    @BeforeAll
    static void setupJson() {
        JsonArray env = new JsonArray();

        JsonObject env0 = new JsonObject();
        env0.addProperty("Name", "PROCESSOR_ARCHITECTURE");
        env0.addProperty("SystemVariable", true);
        env0.addProperty("VariableValue", "AMD64");

        JsonObject env1 = new JsonObject();
        env1.addProperty("Name", "NUMBER_OF_PROCESSORS");
        env1.addProperty("SystemVariable", true);
        env1.addProperty("VariableValue", "12");

        env.add(env0);
        env.add(env1);

        json = new Gson().toJson(env);
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
            assertFalse(envList.isEmpty());

            assertEquals("PROCESSOR_ARCHITECTURE", envList.get(0).getName());
            assertTrue(Boolean.TRUE, String.valueOf(envList.get(0).getIsSystemVariable()));
            assertEquals("AMD64", envList.get(0).getVariableValue());

            assertEquals("NUMBER_OF_PROCESSORS", envList.get(1).getName());
            assertTrue(Boolean.TRUE, String.valueOf(envList.get(1).getIsSystemVariable()));
            assertEquals("12", envList.get(1).getVariableValue());
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
            assertFalse(envList.isEmpty());

            assertEquals("PROCESSOR_ARCHITECTURE", envList.get(0).getName());
            assertTrue(Boolean.TRUE, String.valueOf(envList.get(0).getIsSystemVariable()));
            assertEquals("AMD64", envList.get(0).getVariableValue());

            assertEquals("NUMBER_OF_PROCESSORS", envList.get(1).getName());
            assertTrue(Boolean.TRUE, String.valueOf(envList.get(1).getIsSystemVariable()));
            assertEquals("12", envList.get(1).getVariableValue());
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
}
