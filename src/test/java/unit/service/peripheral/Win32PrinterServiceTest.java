package unit.service.peripheral;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.peripheral.Win32Printer;
import io.github.eggy03.ferrumx.windows.service.peripheral.Win32PrinterService;
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

class Win32PrinterServiceTest {

    private Win32PrinterService printerService;

    private static String json;

    @BeforeAll
    static void loadJson() {
        JsonArray printers = new JsonArray();

        JsonObject printerOne = new JsonObject();
        printerOne.addProperty("DeviceID", "PR1");
        printerOne.addProperty("PrintProcessor", "win_print");

        JsonObject printerTwo = new JsonObject();
        printerTwo.addProperty("DeviceID", "PR2");
        printerTwo.addProperty("PrintProcessor", "win_print");

        printers.add(printerOne);
        printers.add(printerTwo);

        json = new Gson().toJson(printers);
    }

    @BeforeEach
    void setUp() {
        printerService = new Win32PrinterService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32Printer> printers = printerService.get();
            assertFalse(printers.isEmpty());
            assertEquals("PR1", printers.get(0).getDeviceId());
            assertEquals("win_print", printers.get(0).getPrintProcessor());
            assertEquals("PR2", printers.get(1).getDeviceId());
            assertEquals("win_print", printers.get(1).getPrintProcessor());
        }
    }

    @Test
    void test_get_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32Printer> printers = printerService.get();
            assertTrue(printers.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> printerService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockSession = mock(PowerShell.class)) {
            when(mockSession.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32Printer> printers = printerService.get(mockSession);
            assertFalse(printers.isEmpty());
            assertEquals("PR1", printers.get(0).getDeviceId());
            assertEquals("win_print", printers.get(0).getPrintProcessor());
            assertEquals("PR2", printers.get(1).getDeviceId());
            assertEquals("win_print", printers.get(1).getPrintProcessor());
        }
    }

    @Test
    void test_getWithSession_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockSession = mock(PowerShell.class)) {
            when(mockSession.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32Printer> printers = printerService.get(mockSession);
            assertTrue(printers.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (PowerShell mockSession = mock(PowerShell.class)) {
            when(mockSession.executeCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> printerService.get(mockSession));
        }
    }
}
