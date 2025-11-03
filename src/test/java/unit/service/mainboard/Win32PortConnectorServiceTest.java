package unit.service.mainboard;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.mainboard.Win32PortConnector;
import io.github.eggy03.ferrumx.windows.service.mainboard.Win32PortConnectorService;
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

class Win32PortConnectorServiceTest {

    private Win32PortConnectorService portConnectorService;

    private static Win32PortConnector expectedPort1;
    private static Win32PortConnector expectedPort2;

    private static String json;

    @BeforeAll
    static void setPorts() {
        expectedPort1 = Win32PortConnector.builder()
                .tag("PortConnector1")
                .externalReferenceDesignator("USB3_0")
                .internalReferenceDesignator("JUSB1")
                .build();

        expectedPort2 = Win32PortConnector.builder()
                .tag("PortConnector2")
                .externalReferenceDesignator("HDMI_OUT")
                .internalReferenceDesignator("JHDMI1")
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonArray ports = new JsonArray();

        JsonObject port1 = new JsonObject();
        port1.addProperty("Tag", "PortConnector1");
        port1.addProperty("ExternalReferenceDesignator", "USB3_0");
        port1.addProperty("InternalReferenceDesignator", "JUSB1");

        JsonObject port2 = new JsonObject();
        port2.addProperty("Tag", "PortConnector2");
        port2.addProperty("ExternalReferenceDesignator", "HDMI_OUT");
        port2.addProperty("InternalReferenceDesignator", "JHDMI1");

        ports.add(port1);
        ports.add(port2);

        json = new Gson().toJson(ports);
    }


    @BeforeEach
    void setUp() {
        portConnectorService = new Win32PortConnectorService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32PortConnector> mainboardPort = portConnectorService.get();
            assertEquals(2, mainboardPort.size());

            assertThat(mainboardPort.get(0)).usingRecursiveComparison().isEqualTo(expectedPort1);
            assertThat(mainboardPort.get(1)).usingRecursiveComparison().isEqualTo(expectedPort2);
        }
    }

    @Test
    void test_get_emptyJson_returnsEmpty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32PortConnector> mainboardPort = portConnectorService.get();
            assertTrue(mainboardPort.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> portConnectorService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32PortConnector> mainboardPort = portConnectorService.get(mockShell);
            assertEquals(2, mainboardPort.size());

            assertThat(mainboardPort.get(0)).usingRecursiveComparison().isEqualTo(expectedPort1);
            assertThat(mainboardPort.get(1)).usingRecursiveComparison().isEqualTo(expectedPort2);
        }
    }

    @Test
    void test_getWithSession_emptyJson_returnsEmpty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32PortConnector> mainboardPort = portConnectorService.get(mockShell);
            assertTrue(mainboardPort.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);
            assertThrows(JsonSyntaxException.class, () -> portConnectorService.get(mockShell));
        }
    }
}