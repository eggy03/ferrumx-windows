package unit.service.processor;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.processor.Processor;
import io.github.eggy03.ferrumx.windows.service.processor.ProcessorService;
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

class ProcessorServiceTest {

    private ProcessorService processorService;

    private static String jsonProcessorArray;

    @BeforeAll
    static void prepareJson() {
        JsonArray jsonArray = new JsonArray();

        JsonObject cpu0 = new JsonObject();
        cpu0.addProperty("DeviceID", "CPU0");
        cpu0.addProperty("Name", "Intel(R) Core(TM) i7-7700HQ CPU @ 2.80GHz");

        JsonObject cpu1 = new JsonObject();
        cpu1.addProperty("DeviceID", "CPU1");
        cpu1.addProperty("Name", "AMD Ryzen 3 1200 @ 3.10GHz");

        jsonArray.add(cpu0);
        jsonArray.add(cpu1);

        jsonProcessorArray = new Gson().toJson(jsonArray);
    }

    @BeforeEach
    void setUp() {
        processorService = new ProcessorService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(jsonProcessorArray);

        try(MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Processor> processorList = processorService.get();
            assertFalse(processorList.isEmpty());
            assertEquals("CPU0", processorList.get(0).getDeviceId());
            assertEquals("Intel(R) Core(TM) i7-7700HQ CPU @ 2.80GHz", processorList.get(0).getName());
            assertEquals("CPU1", processorList.get(1).getDeviceId());
            assertEquals("AMD Ryzen 3 1200 @ 3.10GHz", processorList.get(1).getName());
        }
    }

    @Test
    void test_get_emptyJson_empty() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try(MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Processor> processorList = processorService.get();
            assertTrue(processorList.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try(MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, ()-> processorService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(jsonProcessorArray);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Processor> processorList = processorService.get(mockShell);
            assertFalse(processorList.isEmpty());
            assertEquals("CPU0", processorList.get(0).getDeviceId());
            assertEquals("Intel(R) Core(TM) i7-7700HQ CPU @ 2.80GHz", processorList.get(0).getName());
            assertEquals("CPU1", processorList.get(1).getDeviceId());
            assertEquals("AMD Ryzen 3 1200 @ 3.10GHz", processorList.get(1).getName());
        }
    }

    @Test
    void test_getWithSession_emptyJson_empty() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Processor> processorList = processorService.get(mockShell);
            assertTrue(processorList.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);
            assertThrows(JsonSyntaxException.class, ()-> processorService.get(mockShell));
        }
    }
}
