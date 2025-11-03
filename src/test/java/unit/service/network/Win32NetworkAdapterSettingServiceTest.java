package unit.service.network;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.network.Win32NetworkAdapterSetting;
import io.github.eggy03.ferrumx.windows.service.network.Win32NetworkAdapterSettingService;
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

class Win32NetworkAdapterSettingServiceTest {

    private Win32NetworkAdapterSettingService service;

    private static Win32NetworkAdapterSetting expectedEthernetSetting;
    private static Win32NetworkAdapterSetting expectedWifiSetting;

    private static String json;

    @BeforeAll
    static void setSettings() {
        expectedEthernetSetting = Win32NetworkAdapterSetting.builder()
                .networkAdapterDeviceId("1")
                .networkAdapterConfigurationIndex(1)
                .build();

        expectedWifiSetting = Win32NetworkAdapterSetting.builder()
                .networkAdapterDeviceId("2")
                .networkAdapterConfigurationIndex(2)
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonArray settings = new JsonArray();

        JsonObject ethernet = new JsonObject();
        ethernet.addProperty("NetworkAdapterDeviceID", "1");
        ethernet.addProperty("NetworkAdapterConfigurationIndex", 1);

        JsonObject wifi = new JsonObject();
        wifi.addProperty("NetworkAdapterDeviceID", "2");
        wifi.addProperty("NetworkAdapterConfigurationIndex", 2);

        settings.add(ethernet);
        settings.add(wifi);

        json = new Gson().toJson(settings);
    }


    @BeforeEach
    void setService() {
        service = new Win32NetworkAdapterSettingService();
    }

    @Test
    void test_get_success() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try(MockedStatic<PowerShell> mockShell = mockStatic(PowerShell.class)){

            mockShell.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32NetworkAdapterSetting> networkAdapterSettingList = service.get();
            assertEquals(2, networkAdapterSettingList.size());

            assertThat(networkAdapterSettingList.get(0)).usingRecursiveComparison().isEqualTo(expectedEthernetSetting);
            assertThat(networkAdapterSettingList.get(1)).usingRecursiveComparison().isEqualTo(expectedWifiSetting);
        }
    }

    @Test
    void test_get_emptyJson_emptyResult() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try(MockedStatic<PowerShell> mockShell = mockStatic(PowerShell.class)){

            mockShell.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);
            List<Win32NetworkAdapterSetting> networkAdapterSettingList = service.get();

            assertTrue(networkAdapterSettingList.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a valid json");

        try(MockedStatic<PowerShell> mockShell = mockStatic(PowerShell.class)) {
            mockShell.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);
            assertThrows(JsonSyntaxException.class, ()-> service.get());
        }
    }

    @Test
    void test_getWithSession_success() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try(PowerShell mockShell = mock(PowerShell.class)){

            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32NetworkAdapterSetting> networkAdapterSettingList = service.get(mockShell);
            assertEquals(2, networkAdapterSettingList.size());

            assertThat(networkAdapterSettingList.get(0)).usingRecursiveComparison().isEqualTo(expectedEthernetSetting);
            assertThat(networkAdapterSettingList.get(1)).usingRecursiveComparison().isEqualTo(expectedWifiSetting);
        }
    }

    @Test
    void test_getWithSession_emptyJson_emptyResult() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try(PowerShell mockShell = mock(PowerShell.class)){

            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);
            List<Win32NetworkAdapterSetting> networkAdapterSettingList = service.get(mockShell);

            assertTrue(networkAdapterSettingList.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try(PowerShell mockShell = mock(PowerShell.class)){

            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);
            assertThrows(JsonSyntaxException.class, ()-> service.get(mockShell));
        }
    }
}
