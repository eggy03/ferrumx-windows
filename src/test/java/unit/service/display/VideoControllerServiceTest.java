package unit.service.display;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.display.VideoController;
import io.github.eggy03.ferrumx.windows.service.display.VideoControllerService;
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

class VideoControllerServiceTest {

    private VideoControllerService videoControllerService;

    private static String json;

    @BeforeAll
    static void setupJson() {
        JsonArray videos = new JsonArray();

        JsonObject video1 = new JsonObject();
        video1.addProperty("DeviceID", "Video1");
        video1.addProperty("Name", "NVIDIA RTX 3080");
        video1.addProperty("CurrentHorizontalResolution", 2560);
        video1.addProperty("CurrentVerticalResolution", 1440);

        JsonObject video2 = new JsonObject();
        video2.addProperty("DeviceID", "Video2");
        video2.addProperty("Name", "Intel UHD Graphics 630");
        video2.addProperty("CurrentHorizontalResolution", 1920);
        video2.addProperty("CurrentVerticalResolution", 1080);

        videos.add(video1);
        videos.add(video2);

        json = new Gson().toJson(videos);
    }

    @BeforeEach
    void setUp() {
        videoControllerService = new VideoControllerService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)){
            mockedPowershell.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<VideoController> videoControllers = videoControllerService.get();
            assertFalse(videoControllers.isEmpty());
            assertEquals("Video1", videoControllers.get(0).getDeviceId());
            assertEquals("NVIDIA RTX 3080", videoControllers.get(0).getName());
            assertEquals("Video2", videoControllers.get(1).getDeviceId());
            assertEquals("Intel UHD Graphics 630", videoControllers.get(1).getName());
        }
    }

    @Test
    void test_get_emptyJson_returnsEmptyList() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> mockedPowerShell = mockStatic(PowerShell.class)) {
            mockedPowerShell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<VideoController> controllers = videoControllerService.get();
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

            List<VideoController> videoControllers = videoControllerService.get(mockShell);
            assertFalse(videoControllers.isEmpty());
            assertEquals("Video1", videoControllers.get(0).getDeviceId());
            assertEquals("NVIDIA RTX 3080", videoControllers.get(0).getName());
            assertEquals("Video2", videoControllers.get(1).getDeviceId());
            assertEquals("Intel UHD Graphics 630", videoControllers.get(1).getName());
        }
    }

    @Test
    void test_getWithSession_emptyJson_returnsEmptyList() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)){
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<VideoController> videoControllers = videoControllerService.get(mockShell);
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
