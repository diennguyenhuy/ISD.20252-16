/**
 * Repository package containing JPA repository interfaces
 * responsible for entity persistence and retrieval.<br>
 * Cohesion:<br>
 * - Functional Cohesion:
 *   Repository interfaces focus solely on persistence-related
 *   operations and database abstraction.
 * Coupling:<br>
 * - Data coupling with services through repository method calls.
 * - Stamp coupling with entities because repositories manage
 *   complete aggregate objects.
 */
package com.hust.soict.ict.aims.repositories;