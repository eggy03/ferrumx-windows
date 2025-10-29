package unit.service.mainboard;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.mainboard.MainboardPort;
import io.github.eggy03.ferrumx.windows.service.mainboard.MainboardPortService;
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

class MainboardPortServiceTest {

    private MainboardPortService mainboardPortService;

    private static String jsonMainboardPort;

    @BeforeAll
    static void setupJson() {
        JsonArray ports = new JsonArray();

        JsonObject port1 = new JsonObject();
        port1.addProperty("Tag", "Port1");
        port1.addProperty("ExternalReferenceDesignator", "External1");
        port1.addProperty("InternalReferenceDesignator", "Internal1");

        JsonObject port2 = new JsonObject();
        port2.addProperty("Tag", "Port2");
        port2.addProperty("ExternalReferenceDesignator", "External2");
        port2.addProperty("InternalReferenceDesignator", "Internal2");

        ports.add(port1);
        ports.add(port2);

        jsonMainboardPort = new Gson().toJson(ports);
    }

    @BeforeEach
    void setUp() {
        mainboardPortService = new MainboardPortService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(jsonMainboardPort);

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<MainboardPort> mainboardPort = mainboardPortService.get();
            assertFalse(mainboardPort.isEmpty());
            assertEquals("Port1", mainboardPort.get(0).getTag());
            assertEquals("External1", mainboardPort.get(0).getExternalReferenceDesignator());
            assertEquals("Internal2", mainboardPort.get(1).getInternalReferenceDesignator());
        }
    }

    @Test
    void test_get_emptyJson_returnsEmpty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<MainboardPort> mainboardPort = mainboardPortService.get();
            assertTrue(mainboardPort.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> mainboardPortService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(jsonMainboardPort);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<MainboardPort> mainboardPort = mainboardPortService.get(mockShell);
            assertFalse(mainboardPort.isEmpty());
            assertEquals("Port1", mainboardPort.get(0).getTag());
            assertEquals("External1", mainboardPort.get(0).getExternalReferenceDesignator());
            assertEquals("Internal2", mainboardPort.get(1).getInternalReferenceDesignator());
        }
    }

    @Test
    void test_getWithSession_emptyJson_returnsEmpty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<MainboardPort> mainboardPort = mainboardPortService.get(mockShell);
            assertTrue(mainboardPort.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);
            assertThrows(JsonSyntaxException.class, () -> mainboardPortService.get(mockShell));
        }
    }
}