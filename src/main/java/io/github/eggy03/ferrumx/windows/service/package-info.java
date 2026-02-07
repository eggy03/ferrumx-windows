/**
 * Contains classes responsible for executing PowerShell queries and retrieving their output.
 * <p>
 * Most service classes in this package implement either
 * {@link io.github.eggy03.ferrumx.windows.service.CommonServiceInterface} or
 * {@link io.github.eggy03.ferrumx.windows.service.OptionalCommonServiceInterface}
 * to provide a unified mechanism for fetching and mapping PowerShell results.
 * Some services may define custom logic which does not use or require implementation of a common interface.
 * </p>
 * <p>Service classes in this package typically:</p>
 * <ul>
 *   <li>Query the PowerShell using the query constants from the {@link io.github.eggy03.ferrumx.windows.constant} package</li>
 *   <li>Map the result using mappers defined in the {@link io.github.eggy03.ferrumx.windows.mapping} package</li>
 *   <li>Into instances of entity classes defined in the {@link io.github.eggy03.ferrumx.windows.entity} package</li>
 * </ul>
 *
 * @author Egg-03
 * @since 2.0.0
 */
package io.github.eggy03.ferrumx.windows.service;