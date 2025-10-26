package unit.processor;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.processor.AssociatedProcessorMemory;
import io.github.eggy03.ferrumx.windows.service.processor.AssociatedProcessorMemoryService;
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

class AssociatedProcessorMemoryTest {

    private AssociatedProcessorMemoryService apmService;

    private static String json;

    @BeforeAll
    static void setJson() {

        JsonArray jsonArray = new JsonArray();

        JsonObject cacheZero = new JsonObject();
        cacheZero.addProperty("CacheMemoryDeviceID", "Cache Memory 0");
        cacheZero.addProperty("ProcessorDeviceID", "CPU0");

        JsonObject cacheOne = new JsonObject();
        cacheOne.addProperty("CacheMemoryDeviceID", "Cache Memory 1");
        cacheOne.addProperty("ProcessorDeviceID", "CPU0");

        jsonArray.add(cacheZero);
        jsonArray.add(cacheOne);

        json = new Gson().toJson(jsonArray);
    }

    @BeforeEach
    void setApmService() {
        apmService = new AssociatedProcessorMemoryService();
    }

    @Test
    void test_get_success() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try(MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)){
            powerShellMock.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);
            List<AssociatedProcessorMemory> apmList = apmService.get();
            assertFalse(apmList.isEmpty());
            assertEquals(2, apmList.size());
            assertEquals("Cache Memory 0", apmList.get(0).getCacheMemoryDeviceId());
            assertEquals("Cache Memory 1", apmList.get(1).getCacheMemoryDeviceId());
            assertEquals("CPU0", apmList.get(0).getProcessorDeviceId());
            assertEquals("CPU0", apmList.get(1).getProcessorDeviceId());
        }

    }

    @Test
    void test_get_emptyJson_emptyResult() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMockedStatic = mockStatic(PowerShell.class)){
            powerShellMockedStatic.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);
            List<AssociatedProcessorMemory> apmList = apmService.get();
            assertTrue(apmList.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try (MockedStatic<PowerShell> powerShellMockedStatic = mockStatic(PowerShell.class)){
            powerShellMockedStatic.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);
            assertThrows(JsonSyntaxException.class, ()-> apmService.get());
        }
    }

    @Test
    void test_getWithSession_success() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try(PowerShell mockedShell = mock(PowerShell.class)){
            when(mockedShell.executeCommand(anyString())).thenReturn(mockResponse);
            List<AssociatedProcessorMemory> apmList = apmService.get(mockedShell);
            assertFalse(apmList.isEmpty());
            assertEquals(2, apmList.size());
            assertEquals("Cache Memory 0", apmList.get(0).getCacheMemoryDeviceId());
            assertEquals("Cache Memory 1", apmList.get(1).getCacheMemoryDeviceId());
            assertEquals("CPU0", apmList.get(0).getProcessorDeviceId());
            assertEquals("CPU0", apmList.get(1).getProcessorDeviceId());
        }
    }

    @Test
    void test_getWithSession_emptyJson_emptyResult() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try(PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);
            List<AssociatedProcessorMemory> apmList = apmService.get(mockShell);
            assertTrue(apmList.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("malformed json");

        try(PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);
            assertThrows(JsonSyntaxException.class, ()-> apmService.get(mockShell));
        }
    }
}
