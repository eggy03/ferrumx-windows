package unit.service.mainboard;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class Win32BiosServiceTest {

    private Win32BiosService biosService;

    private static Win32Bios expectedBios1;
    private static Win32Bios expectedBios2;

    private static String json;

    @BeforeAll
    static void setBioses() {
        expectedBios1 = Win32Bios.builder()
                .name("American Megatrends Inc. BIOS")
                .caption("AMI BIOS")
                .manufacturer("American Megatrends Inc.")
                .releaseDate("2024-03-15")
                .smbiosPresent(true)
                .status("OK")
                .version("2.21.1278")
                .currentLanguage("en-US")
                .smbiosBiosVersion("A.10")
                .primaryBios(true)
                .build();

        expectedBios2 = Win32Bios.builder()
                .name("Phoenix Technologies LTD BIOS")
                .caption("Phoenix SecureCore BIOS")
                .manufacturer("Phoenix Technologies LTD")
                .releaseDate("2023-12-10")
                .smbiosPresent(true)
                .status("OK")
                .version("P1.30")
                .currentLanguage("en-US")
                .smbiosBiosVersion("1.30.0")
                .primaryBios(false)
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonArray bioses = new JsonArray();

        JsonObject bios1 = new JsonObject();
        bios1.addProperty("Name", "American Megatrends Inc. BIOS");
        bios1.addProperty("Caption", "AMI BIOS");
        bios1.addProperty("Manufacturer", "American Megatrends Inc.");
        bios1.addProperty("ReleaseDate", "2024-03-15");
        bios1.addProperty("SMBIOSPresent", true);
        bios1.addProperty("Status", "OK");
        bios1.addProperty("Version", "2.21.1278");
        bios1.addProperty("CurrentLanguage", "en-US");
        bios1.addProperty("SMBIOSBIOSVersion", "A.10");
        bios1.addProperty("PrimaryBIOS", true);

        JsonObject bios2 = new JsonObject();
        bios2.addProperty("Name", "Phoenix Technologies LTD BIOS");
        bios2.addProperty("Caption", "Phoenix SecureCore BIOS");
        bios2.addProperty("Manufacturer", "Phoenix Technologies LTD");
        bios2.addProperty("ReleaseDate", "2023-12-10");
        bios2.addProperty("SMBIOSPresent", true);
        bios2.addProperty("Status", "OK");
        bios2.addProperty("Version", "P1.30");
        bios2.addProperty("CurrentLanguage", "en-US");
        bios2.addProperty("SMBIOSBIOSVersion", "1.30.0");
        bios2.addProperty("PrimaryBIOS", false);

        bioses.add(bios1);
        bioses.add(bios2);

        json = new Gson().toJson(bioses);
    }


    @BeforeEach
    void setUp() {
        biosService = new Win32BiosService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32Bios> bios = biosService.get();
            assertEquals(2, bios.size());

            assertThat(bios.get(0)).usingRecursiveComparison().isEqualTo(expectedBios1);
            assertThat(bios.get(1)).usingRecursiveComparison().isEqualTo(expectedBios2);
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
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32Bios> bios = biosService.get(mockShell);
            assertEquals(2, bios.size());

            assertThat(bios.get(0)).usingRecursiveComparison().isEqualTo(expectedBios1);
            assertThat(bios.get(1)).usingRecursiveComparison().isEqualTo(expectedBios2);
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
