package unit.network;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.network.MsftNetConnectionProfile;
import io.github.eggy03.ferrumx.windows.service.network.MsftNetConnectionProfileService;
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

class MsftNetConnectionProfileServiceTest {

    private MsftNetConnectionProfileService msftNetConnectionProfileService;

    private static String json;

    @BeforeAll
    static void setupJson() {
        JsonArray profiles = new JsonArray();

        JsonObject ethernet = new JsonObject();
        ethernet.addProperty("InterfaceIndex", 1);
        ethernet.addProperty("NetworkCategory", 0);
        ethernet.addProperty("IPv4Connectivity", 4);
        ethernet.addProperty("IPv6Connectivity", 1);

        JsonObject wifi = new JsonObject();
        wifi.addProperty("InterfaceIndex", 2);
        wifi.addProperty("NetworkCategory", 1);
        wifi.addProperty("IPv4Connectivity", 1);
        wifi.addProperty("IPv6Connectivity", 4);

        profiles.add(ethernet);
        profiles.add(wifi);

        json = new Gson().toJson(profiles);
    }

    @BeforeEach
    void setUp() {
        msftNetConnectionProfileService = new MsftNetConnectionProfileService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<MsftNetConnectionProfile> profiles = msftNetConnectionProfileService.get();
            assertFalse(profiles.isEmpty());
            assertEquals(1, profiles.get(0).getInterfaceIndex());
            assertEquals(0, profiles.get(0).getNetworkCategory());
            assertEquals(4, profiles.get(0).getIpv4Connectivity());
            assertEquals(1, profiles.get(0).getIpv6Connectivity());

            assertEquals(2, profiles.get(1).getInterfaceIndex());
            assertEquals(1, profiles.get(1).getNetworkCategory());
            assertEquals(1, profiles.get(1).getIpv4Connectivity());
            assertEquals(4, profiles.get(1).getIpv6Connectivity());
        }
    }

    @Test
    void test_get_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<MsftNetConnectionProfile> profiles = msftNetConnectionProfileService.get();
            assertTrue(profiles.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> msftNetConnectionProfileService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<MsftNetConnectionProfile> profiles = msftNetConnectionProfileService.get(mockShell);
            assertFalse(profiles.isEmpty());
            assertEquals(1, profiles.get(0).getInterfaceIndex());
            assertEquals(0, profiles.get(0).getNetworkCategory());
            assertEquals(4, profiles.get(0).getIpv4Connectivity());
            assertEquals(1, profiles.get(0).getIpv6Connectivity());

            assertEquals(2, profiles.get(1).getInterfaceIndex());
            assertEquals(1, profiles.get(1).getNetworkCategory());
            assertEquals(1, profiles.get(1).getIpv4Connectivity());
            assertEquals(4, profiles.get(1).getIpv6Connectivity());
        }
    }

    @Test
    void test_getWithSession_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<MsftNetConnectionProfile> profiles = msftNetConnectionProfileService.get(mockShell);
            assertTrue(profiles.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> msftNetConnectionProfileService.get(mockShell));
        }
    }
}
