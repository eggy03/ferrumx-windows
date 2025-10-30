package io.github.eggy03.ferrumx.windows.service.product;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.CimQuery;
import io.github.eggy03.ferrumx.windows.entity.product.Win32ComputerSystemProduct;
import io.github.eggy03.ferrumx.windows.mapping.product.Win32ComputerSystemProductMapper;
import io.github.eggy03.ferrumx.windows.service.OptionalCommonServiceInterface;

import java.util.Optional;

/**
 * Service class for fetching the system product information.
 * <p>
 * This class executes the {@link CimQuery#COMPUTER_SYSTEM_PRODUCT} PowerShell command
 * and maps the resulting JSON into an {@link Optional} {@link Win32ComputerSystemProduct} object.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32ComputerSystemProductService productService = new Win32ComputerSystemProductService();
 * Optional<Win32ComputerSystemProduct> product = productService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     Win32ComputerSystemProductService productService = new Win32ComputerSystemProductService();
 *     Optional<Win32ComputerSystemProduct> product = productService.get(session);
 * }
 * }</pre>
 * @since 2.0.0
 * @author Egg-03
 */

public class Win32ComputerSystemProductService implements OptionalCommonServiceInterface<Win32ComputerSystemProduct> {

    /**
     * Retrieves an {@link Optional} containing the computer system product information.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return an {@link Optional} of {@link Win32ComputerSystemProduct} representing
     *         the computer system as a product. Returns {@link Optional#empty()} if no product information is detected.
     *
     * @since 2.2.0
     */
    @Override
    public Optional<Win32ComputerSystemProduct> get() {

        PowerShellResponse response = PowerShell.executeSingleCommand(CimQuery.COMPUTER_SYSTEM_PRODUCT.getQuery());
        return new Win32ComputerSystemProductMapper().mapToObject(response.getCommandOutput(), Win32ComputerSystemProduct.class);
    }

    /**
     * Retrieves an {@link Optional} containing the computer system product information
     * using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return an {@link Optional} of {@link Win32ComputerSystemProduct} representing
     *         the computer system as a product. Returns {@link Optional#empty()} if no product information is detected.
     *
     * @since 2.2.0
     */
    @Override
    public Optional<Win32ComputerSystemProduct> get(PowerShell powerShell) {

        PowerShellResponse response = powerShell.executeCommand(CimQuery.COMPUTER_SYSTEM_PRODUCT.getQuery());
        return new Win32ComputerSystemProductMapper().mapToObject(response.getCommandOutput(), Win32ComputerSystemProduct.class);
    }
}
