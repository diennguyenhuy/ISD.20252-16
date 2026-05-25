/**
 * Cohesion: Communicational Cohesion (Entities) & Functional Cohesion (Builders)<br>
 * Reason: Entities act as highly encapsulated Abstract Data Types (ADTs). All attributes are tightly protected, and the nested Builder classes focus entirely on a single, well-defined task: validating and constructing a strictly valid domain state.<br>
 * Coupling:
 * - Data coupling with external modules, as the builders only accept primitive types, strings, and specific domain enumerations (e.g., CoverType, ProductStatus) to assemble the entity.
 * - The entities are completely self-contained. They do not depend on any services, repositories, controllers, or external infrastructure frameworks.
 * Design Strength:
 * Enforces strict encapsulation and data integrity through the nested Builder Pattern. It guarantees that a product cannot be instantiated in an invalid state, providing a highly robust, testable, and safe foundation for the domain layer.
 */

package com.hust.soict.ict.aims.models.entities.product;