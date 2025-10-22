package unit.network;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.network.NetworkAdapterSetting;
import io.github.eggy03.ferrumx.windows.service.network.NetworkAdapterSettingService;
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

class NetworkAdapterSettingServiceTest {

    private NetworkAdapterSettingService service;

    private static String json;

    @BeforeAll
    static void setJson() {
        JsonArray jsonArray = new JsonArray();

        JsonObject settingOne = new JsonObject();
        settingOne.addProperty("DeviceID", "1");
        settingOne.addProperty("Index", 1);

        JsonObject settingTwo = new JsonObject();
        settingTwo.addProperty("DeviceID", "2");
        settingTwo.addProperty("Index", 2);

        jsonArray.add(settingOne);
        jsonArray.add(settingTwo);

        json = new Gson().toJson(jsonArray);
    }

    @BeforeEach
    void setService() {
        service = new NetworkAdapterSettingService();
    }

    @Test
    void test_get_success() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try(MockedStatic<PowerShell> mockShell = mockStatic(PowerShell.class)){

            mockShell.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);
            List<NetworkAdapterSetting> networkAdapterSettingList = service.get();

            assertFalse(networkAdapterSettingList.isEmpty());
            assertEquals("1", networkAdapterSettingList.get(0).getDeviceId());
            assertEquals("2", networkAdapterSettingList.get(1).getDeviceId());
            assertEquals(1, networkAdapterSettingList.get(0).getIndex());
            assertEquals(2, networkAdapterSettingList.get(1).getIndex());
        }
    }

    @Test
    void test_get_emptyJson_emptyResult() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try(MockedStatic<PowerShell> mockShell = mockStatic(PowerShell.class)){

            mockShell.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);
            List<NetworkAdapterSetting> networkAdapterSettingList = service.get();

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
            List<NetworkAdapterSetting> networkAdapterSettingList = service.get(mockShell);

            assertFalse(networkAdapterSettingList.isEmpty());
            assertEquals("1", networkAdapterSettingList.get(0).getDeviceId());
            assertEquals("2", networkAdapterSettingList.get(1).getDeviceId());
            assertEquals(1, networkAdapterSettingList.get(0).getIndex());
            assertEquals(2, networkAdapterSettingList.get(1).getIndex());
        }
    }

    @Test
    void test_getWithSession_emptyJson_emptyResult() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try(PowerShell mockShell = mock(PowerShell.class)){

            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);
            List<NetworkAdapterSetting> networkAdapterSettingList = service.get(mockShell);

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
