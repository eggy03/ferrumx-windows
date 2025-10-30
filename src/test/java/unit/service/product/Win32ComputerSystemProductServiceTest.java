package unit.service.product;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.product.Win32ComputerSystemProduct;
import io.github.eggy03.ferrumx.windows.service.product.Win32ComputerSystemProductService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class Win32ComputerSystemProductServiceTest {

    private Win32ComputerSystemProductService productService;

    private static String jsonProduct;

    @BeforeAll
    static void setupJson() {
        JsonObject product = new JsonObject();
        product.addProperty("Caption", "Computer System Product");
        product.addProperty("Description", "Some workstation");
        product.addProperty("IdentifyingNumber", "12345-67890");
        product.addProperty("Name", "MyPC");
        product.addProperty("SKUNumber", "SKU-001");
        product.addProperty("Vendor", "Dell");
        product.addProperty("Version", "1.0");
        product.addProperty("UUID", "550e8400-e29b-41d4-a716-446655440000");

        jsonProduct = new Gson().toJson(product);
    }

    @BeforeEach
    void setUp() {
        productService = new Win32ComputerSystemProductService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(jsonProduct);

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            Optional<Win32ComputerSystemProduct> product = productService.get();
            assertTrue(product.isPresent());
            assertEquals("MyPC", product.get().getName());
            assertEquals("Dell", product.get().getVendor());
            assertEquals("SKU-001", product.get().getSkuNumber());
        }
    }

    @Test
    void test_get_emptyJson_returnsEmpty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            Optional<Win32ComputerSystemProduct> product = productService.get();
            assertFalse(product.isPresent());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> productService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(jsonProduct);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            Optional<Win32ComputerSystemProduct> product = productService.get(mockShell);
            assertTrue(product.isPresent());
            assertEquals("MyPC", product.get().getName());
            assertEquals("Dell", product.get().getVendor());
            assertEquals("SKU-001", product.get().getSkuNumber());
        }
    }

    @Test
    void test_getWithSession_emptyJson_returnsEmpty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            Optional<Win32ComputerSystemProduct> product = productService.get(mockShell);
            assertFalse(product.isPresent());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> productService.get(mockShell));
        }
    }
}
