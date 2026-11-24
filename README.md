# Maison de la Patisserie Bakery Management System

A Java bakery management system for customers and bakery staff.

This project was created for my Object-Oriented Programming module during semester 2 of second year. The aim was to apply object-oriented principles and Java skills, including inheritance, encapsulation, polymorphism, MVC architecture, design patterns and database connectivity, to build a bakery management application from scratch.

## About

This Java application manages customers, bakery products and orders for Maison de la Patisserie. It includes separate functionality for customers and bakery staff through a JavaFX interface.

## Features

- Register and manage customers
- Browse bakery products
- Add products to a cart and place orders
- Track stock quantities and reduce stock after purchase
- Add, update and remove bakery products
- View customer and order information
- Save and load application data
- Store customer information in a MySQL database

## Technology

- Java
- JavaFX
- MySQL and JDBC
- Java serialization
- MVC architecture

## OOP and Design Concepts

- Inheritance and abstract classes
- Encapsulation and polymorphism
- Separation of responsibilities using MVC
- Builder pattern for customer construction
- Singleton pattern for database and serialization management
- DAO-style database access
- Exception handling and input validation

## Main Classes

- `Bakery` - Coordinates customers, products and orders
- `Customer` - Stores customer details
- `Product`, `Bread`, `Cake` and `Pastry` - Represent bakery products
- `Order` - Holds purchased products and calculates totals
- `Admin` - Provides staff management operations
- `Checkout` - Handles cart and order processing
- `Payment` - Handles payment information
- `Database` - Manages MySQL database operations
- `MaisonDeLaPatisserie` - Implements the JavaFX interface
