package io.github.eggy03.ferrumx.windows.entity.user;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of a User Account in system running Windows.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_UserAccount} WMI class.
 * </p>
 * <p>
 * Instances are thread-safe and may be safely cached or shared across threads.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Build a new instance
 * Win32UserAccount systemAccount = Win32UserAccount.builder()
 *     .sid("S-1-5-21-0987654321-1002")
 *     .sidType(1)
 *     .accountType(512)
 *     .caption("User2")
 *     .description("Administrator account")
 *     .domain("WORKGROUP")
 *     .name("Admin")
 *     .build();
 *
 * // Create a modified copy
 * Win32UserAccount updated = systemAccount.toBuilder()
 *     .name("Admin-PC")
 *     .build();
 *
 * }</pre>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-useraccount">Win32_UserAccount Documentation</a>
 * @since 3.0.0
 * @author Egg-03
 */
@Value
@Builder(toBuilder = true)
public class Win32UserAccount {

    @SerializedName("SID")
    String sid;

    @SerializedName("SIDType")
    Integer sidType;

    @SerializedName("AccountType")
    Long accountType;

    @SerializedName("Caption")
    String caption;

    @SerializedName("Description")
    String description;

    @SerializedName("Domain")
    String domain;

    @SerializedName("Name")
    String name;

    @Getter(AccessLevel.NONE)
    @SerializedName("Disabled")
    Boolean disabled;
    public @Nullable Boolean isDisabled() {return disabled;}

    @Getter(AccessLevel.NONE)
    @SerializedName("LocalAccount")
    Boolean localAccount;
    public @Nullable Boolean isLocalAccount() {return localAccount;}

    @Getter(AccessLevel.NONE)
    @SerializedName("Lockout")
    Boolean lockout;
    public @Nullable Boolean isLockedOut() {return lockout;}

    @Getter(AccessLevel.NONE)
    @SerializedName("PasswordRequired")
    Boolean passwordRequired;
    public @Nullable Boolean isPasswordRequired() {return passwordRequired;}

    @Getter(AccessLevel.NONE)
    @SerializedName("PasswordExpires")
    Boolean passwordExpires;
    public @Nullable Boolean doesPasswordExpire() {return passwordExpires;}

    @Getter(AccessLevel.NONE)
    @SerializedName("PasswordChangeable")
    Boolean passwordChangeable;
    public @Nullable Boolean isPasswordChangeable() {return passwordChangeable;}

    @SerializedName("Status")
    String status;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}
