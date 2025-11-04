package io.github.eggy03.ferrumx.windows.service.user;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.Cimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.user.Win32UserAccount;
import io.github.eggy03.ferrumx.windows.mapping.user.Win32UserAccountMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;

import java.util.List;

/**
 * Service class for fetching information about User Accounts in a Windows System.
 * <p>
 * This class executes the {@link Cimv2Namespace#WIN32_USER_ACCOUNT_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link Win32UserAccount} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32UserAccountService uaService = new Win32UserAccountService();
 * List<Win32UserAccount> ua = uaService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     Win32UserAccountService uaService = new Win32UserAccountService();
 *     List<Win32UserAccount> ua = uaService.get(session);
 * }
 * }</pre>
 * @since 3.0.0
 * @author Egg-03
 */
public class Win32UserAccountService implements CommonServiceInterface<Win32UserAccount> {

    /**
     * Retrieves a non-null list of user accounts present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32UserAccount} objects representing the user accounts.
     *         Returns an empty list if no user accounts are detected.
     *
     * @since 3.0.0
     */
    @Override
    public List<Win32UserAccount> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(Cimv2Namespace.WIN32_USER_ACCOUNT_QUERY.getQuery());
        return new Win32UserAccountMapper().mapToList(response.getCommandOutput(), Win32UserAccount.class);
    }

    /**
     * Retrieves a non-null list of user accounts using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link Win32UserAccount} objects representing the user accounts.
     *         Returns an empty list if no user accounts are detected.
     *
     * @since 3.0.0
     */
    @Override
    public List<Win32UserAccount> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(Cimv2Namespace.WIN32_USER_ACCOUNT_QUERY.getQuery());
        return new Win32UserAccountMapper().mapToList(response.getCommandOutput(), Win32UserAccount.class);
    }
}