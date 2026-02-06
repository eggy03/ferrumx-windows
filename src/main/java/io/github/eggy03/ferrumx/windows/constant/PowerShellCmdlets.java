/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.constant;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PowerShellCmdlets {

    SELECT_OBJECT_PROPERTY(" | Select-Object -Property "),

    CONVERT_TO_JSON(" | ConvertTo-Json");

    @NonNull
    private final String cmdlet;
}
