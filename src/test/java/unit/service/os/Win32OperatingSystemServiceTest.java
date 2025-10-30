package unit.service.os;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.os.Win32OperatingSystem;
import io.github.eggy03.ferrumx.windows.service.os.Win32OperatingSystemService;
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

class Win32OperatingSystemServiceTest {

    private Win32OperatingSystemService operatingSystemService;

    private static String json;

    @BeforeAll
    static void setupJson() {
        JsonObject windows = new JsonObject();
        windows.addProperty("Name", "Windows 11 Pro");
        windows.addProperty("Caption", "Microsoft Windows 11 Pro");
        windows.addProperty("InstallDate", "20230915090000.000000+000");
        windows.addProperty("CSName", "DESKTOP-1234");
        windows.addProperty("LastBootUpTime", "20250920070000.000000+000");
        windows.addProperty("NumberOfUsers", 1);
        windows.addProperty("Version", "11.0.22000");
        windows.addProperty("OSArchitecture", "64-bit");

        json = new Gson().toJson(windows);
    }

    @BeforeEach
    void setUp() {
        operatingSystemService = new Win32OperatingSystemService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32OperatingSystem> os = operatingSystemService.get();
            assertFalse(os.isEmpty());
            assertEquals("Windows 11 Pro", os.get(0).getName());
            assertEquals("Microsoft Windows 11 Pro", os.get(0).getCaption());
            assertEquals("DESKTOP-1234", os.get(0).getCsName());
            assertEquals("11.0.22000", os.get(0).getVersion());
        }
    }

    @Test
    void test_get_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32OperatingSystem> os = operatingSystemService.get();
            assertTrue(os.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> operatingSystemService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32OperatingSystem> os = operatingSystemService.get(mockShell);
            assertFalse(os.isEmpty());
            assertEquals("Windows 11 Pro", os.get(0).getName());
            assertEquals("Microsoft Windows 11 Pro", os.get(0).getCaption());
            assertEquals("DESKTOP-1234", os.get(0).getCsName());
            assertEquals("11.0.22000", os.get(0).getVersion());
        }
    }

    @Test
    void test_getWithSession_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32OperatingSystem> os = operatingSystemService.get(mockShell);
            assertTrue(os.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> operatingSystemService.get(mockShell));
        }
    }
}