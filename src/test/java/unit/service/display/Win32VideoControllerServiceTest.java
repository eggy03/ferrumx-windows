package unit.service.display;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.display.Win32VideoController;
import io.github.eggy03.ferrumx.windows.service.display.Win32VideoControllerService;
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

class Win32VideoControllerServiceTest {

    private Win32VideoControllerService videoControllerService;

    private static Win32VideoController expectedGpu1;
    private static Win32VideoController expectedGpu2;

    private static String json;

    @BeforeAll
    static void setGpus() {
        expectedGpu1 = Win32VideoController.builder()
                .deviceId("GPU1")
                .name("NVIDIA GeForce RTX 4090")
                .pnpDeviceId("PCI\\VEN_10DE&DEV_2684&SUBSYS_409010DE&REV_A1")
                .currentBitsPerPixel(32)
                .currentHorizontalResolution(3840)
                .currentVerticalResolution(2160)
                .currentRefreshRate(144)
                .maxRefreshRate(240)
                .minRefreshRate(60)
                .adapterDacType("Integrated RAMDAC")
                .adapterRam(24000000000L)
                .driverDate("2024-09-12")
                .driverVersion("552.22")
                .videoProcessor("AD102")
                .build();

        expectedGpu2 = Win32VideoController.builder()
                .deviceId("GPU2")
                .name("AMD Radeon RX 7900 XTX")
                .pnpDeviceId("PCI\\VEN_1002&DEV_744C&SUBSYS_79001002&REV_C8")
                .currentBitsPerPixel(32)
                .currentHorizontalResolution(2560)
                .currentVerticalResolution(1440)
                .currentRefreshRate(165)
                .maxRefreshRate(240)
                .minRefreshRate(60)
                .adapterDacType("Internal DAC")
                .adapterRam(20000000000L)
                .driverDate("2024-07-05")
                .driverVersion("24.7.1")
                .videoProcessor("Navi 31 XTX")
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonArray gpus = new JsonArray();

        JsonObject gpu1 = new JsonObject();
        gpu1.addProperty("DeviceID", "GPU1");
        gpu1.addProperty("Name", "NVIDIA GeForce RTX 4090");
        gpu1.addProperty("PNPDeviceID", "PCI\\VEN_10DE&DEV_2684&SUBSYS_409010DE&REV_A1");
        gpu1.addProperty("CurrentBitsPerPixel", 32);
        gpu1.addProperty("CurrentHorizontalResolution", 3840);
        gpu1.addProperty("CurrentVerticalResolution", 2160);
        gpu1.addProperty("CurrentRefreshRate", 144);
        gpu1.addProperty("MaxRefreshRate", 240);
        gpu1.addProperty("MinRefreshRate", 60);
        gpu1.addProperty("AdapterDACType", "Integrated RAMDAC");
        gpu1.addProperty("AdapterRAM", 24000000000L);
        gpu1.addProperty("DriverDate", "2024-09-12");
        gpu1.addProperty("DriverVersion", "552.22");
        gpu1.addProperty("VideoProcessor", "AD102");

        JsonObject gpu2 = new JsonObject();
        gpu2.addProperty("DeviceID", "GPU2");
        gpu2.addProperty("Name", "AMD Radeon RX 7900 XTX");
        gpu2.addProperty("PNPDeviceID", "PCI\\VEN_1002&DEV_744C&SUBSYS_79001002&REV_C8");
        gpu2.addProperty("CurrentBitsPerPixel", 32);
        gpu2.addProperty("CurrentHorizontalResolution", 2560);
        gpu2.addProperty("CurrentVerticalResolution", 1440);
        gpu2.addProperty("CurrentRefreshRate", 165);
        gpu2.addProperty("MaxRefreshRate", 240);
        gpu2.addProperty("MinRefreshRate", 60);
        gpu2.addProperty("AdapterDACType", "Internal DAC");
        gpu2.addProperty("AdapterRAM", 20000000000L);
        gpu2.addProperty("DriverDate", "2024-07-05");
        gpu2.addProperty("DriverVersion", "24.7.1");
        gpu2.addProperty("VideoProcessor", "Navi 31 XTX");

        gpus.add(gpu1);
        gpus.add(gpu2);

        json = new Gson().toJson(gpus);
    }

    @BeforeEach
    void setUp() {
        videoControllerService = new Win32VideoControllerService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)){
            mockedPowershell.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32VideoController> videoControllers = videoControllerService.get();
            assertEquals(2, videoControllers.size());

            assertThat(videoControllers.get(0)).usingRecursiveComparison().isEqualTo(expectedGpu1);
            assertThat(videoControllers.get(1)).usingRecursiveComparison().isEqualTo(expectedGpu2);
        }
    }

    @Test
    void test_get_emptyJson_returnsEmptyList() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> mockedPowerShell = mockStatic(PowerShell.class)) {
            mockedPowerShell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32VideoController> controllers = videoControllerService.get();
            assertTrue(controllers.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not valid json");

        try (MockedStatic<PowerShell> mockedPowerShell = mockStatic(PowerShell.class)) {
            mockedPowerShell.when(() -> PowerShell.executeSingleCommand(anyString()))
                    .thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> videoControllerService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockShell = mock(PowerShell.class)){
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32VideoController> videoControllers = videoControllerService.get(mockShell);
            assertEquals(2, videoControllers.size());

            assertThat(videoControllers.get(0)).usingRecursiveComparison().isEqualTo(expectedGpu1);
            assertThat(videoControllers.get(1)).usingRecursiveComparison().isEqualTo(expectedGpu2);
        }
    }

    @Test
    void test_getWithSession_emptyJson_returnsEmptyList() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)){
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32VideoController> videoControllers = videoControllerService.get(mockShell);
            assertTrue(videoControllers.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not valid json");

        try (PowerShell mockShell = mock(PowerShell.class)){
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> videoControllerService.get(mockShell));
        }
    }
}
