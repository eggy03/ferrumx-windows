package unit.service.mainboard;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.mainboard.Win32Baseboard;
import io.github.eggy03.ferrumx.windows.service.mainboard.Win32BaseboardService;
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

class Win32BaseboardServiceTest {

    private Win32BaseboardService baseboardService;

    private static String jsonBaseboard;

    @BeforeAll
    static void setupJson() {
        JsonObject baseboard = new JsonObject();
        baseboard.addProperty("Manufacturer", "ASUS");
        baseboard.addProperty("Model", "ROG STRIX");
        baseboard.addProperty("Version", "1.0.0");
        baseboard.addProperty("SerialNumber", "123456789");

        jsonBaseboard = new Gson().toJson(baseboard);
    }

    @BeforeEach
    void setUp() {
        baseboardService = new Win32BaseboardService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(jsonBaseboard);

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32Baseboard> baseboardList = baseboardService.get();
            assertFalse(baseboardList.isEmpty());
            assertEquals("ASUS", baseboardList.get(0).getManufacturer());
            assertEquals("ROG STRIX", baseboardList.get(0).getModel());
        }
    }

    @Test
    void test_get_emptyJson_returnsEmpty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32Baseboard> baseboardListList = baseboardService.get();
            assertTrue(baseboardListList.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> baseboardService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(jsonBaseboard);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32Baseboard> baseboardList = baseboardService.get(mockShell);
            assertFalse(baseboardList.isEmpty());
            assertEquals("ASUS", baseboardList.get(0).getManufacturer());
            assertEquals("ROG STRIX", baseboardList.get(0).getModel());
        }
    }

    @Test
    void test_getWithSession_emptyJson_returnsEmpty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32Baseboard> baseboardList = baseboardService.get(mockShell);
            assertTrue(baseboardList.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> baseboardService.get(mockShell));
        }
    }
}

