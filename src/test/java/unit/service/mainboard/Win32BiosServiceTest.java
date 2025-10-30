package unit.service.mainboard;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.mainboard.Win32Bios;
import io.github.eggy03.ferrumx.windows.service.mainboard.Win32BiosService;
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

class Win32BiosServiceTest {

    private Win32BiosService biosService;

    private static String jsonBios;

    @BeforeAll
    static void setupJson() {
        JsonObject bios = new JsonObject();
        bios.addProperty("Caption", "BIOS");
        bios.addProperty("Description", "BIOS Description");
        bios.addProperty("Manufacturer", "Dell");
        bios.addProperty("Name", "MyPC");
        bios.addProperty("SMBIOSBIOSVersion", "1.0");
        bios.addProperty("Version", "1.0.0");
        bios.addProperty("ReleaseDate", "20230101000000.000000+000");

        jsonBios = new Gson().toJson(bios);
    }

    @BeforeEach
    void setUp() {
        biosService = new Win32BiosService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(jsonBios);

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32Bios> bios = biosService.get();
            assertFalse(bios.isEmpty());
            assertEquals("BIOS", bios.get(0).getCaption());
            assertEquals("1.0.0", bios.get(0).getVersion());
        }
    }

    @Test
    void test_get_emptyJson_returnsEmpty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32Bios> bios = biosService.get();
            assertTrue(bios.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> biosService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(jsonBios);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32Bios> bios = biosService.get(mockShell);
            assertFalse(bios.isEmpty());
            assertEquals("BIOS", bios.get(0).getCaption());
            assertEquals("1.0.0", bios.get(0).getVersion());
        }
    }

    @Test
    void test_getWithSession_emptyJson_returnsEmpty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32Bios> bios = biosService.get(mockShell);
            assertTrue(bios.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);
            assertThrows(JsonSyntaxException.class, () -> biosService.get(mockShell));
        }
    }
}
