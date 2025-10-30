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

import java.util.Optional;

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

    private static String jsonMainboard;

    @BeforeAll
    static void setupJson() {
        JsonObject mainboard = new JsonObject();
        mainboard.addProperty("Manufacturer", "ASUS");
        mainboard.addProperty("Model", "ROG STRIX");
        mainboard.addProperty("Version", "1.0.0");
        mainboard.addProperty("SerialNumber", "123456789");

        jsonMainboard = new Gson().toJson(mainboard);
    }

    @BeforeEach
    void setUp() {
        baseboardService = new Win32BaseboardService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(jsonMainboard);

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            Optional<Win32Baseboard> mainboard = baseboardService.get();
            assertTrue(mainboard.isPresent());
            assertEquals("ASUS", mainboard.get().getManufacturer());
            assertEquals("ROG STRIX", mainboard.get().getModel());
        }
    }

    @Test
    void test_get_emptyJson_returnsEmpty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            Optional<Win32Baseboard> mainboard = baseboardService.get();
            assertFalse(mainboard.isPresent());
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
        when(mockResponse.getCommandOutput()).thenReturn(jsonMainboard);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            Optional<Win32Baseboard> mainboard = baseboardService.get(mockShell);
            assertTrue(mainboard.isPresent());
            assertEquals("ASUS", mainboard.get().getManufacturer());
            assertEquals("ROG STRIX", mainboard.get().getModel());
        }
    }

    @Test
    void test_getWithSession_emptyJson_returnsEmpty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            Optional<Win32Baseboard> mainboard = baseboardService.get(mockShell);
            assertFalse(mainboard.isPresent());
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

