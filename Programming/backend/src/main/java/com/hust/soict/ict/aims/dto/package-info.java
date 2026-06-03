/**
 * DTO package containing request and response data structures
 * used for communication across the web/API boundary.<br>
 * Cohesion:<br>
 * - Functional Cohesion:
 *   DTO classes exist solely to encapsulate and transfer
 *   structured data between layers.
 * Coupling:<br>
 * - Stamp coupling with entities and controllers/services
 *   because composite request/response objects are exchanged
 *   between modules.
 */
package com.hust.soict.ict.aims.dto;