/**
 * Mapper package containing DTO mapper interfaces used for
 * transforming domain entities into DTOs and vice versa.<br>
 * Cohesion:<br>
 * - Functional Cohesion:
 *   All mapper methods contribute solely to object
 *   transformation between architectural layers.
 * Coupling:<br>
 * - Stamp coupling with entity and DTO classes because
 *   complete objects are transformed between layers.
 */
package com.hust.soict.ict.aims.mapper;