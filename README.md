![Screenshot (13)](https://github.com/user-attachments/assets/9892c291-0bb0-474e-80f2-062d91627d81)

---

# E-commerce Application

This project is a fully functional e-commerce application that I developed using Spring Boot, MySQL, and Elasticsearch. It showcases how to build a scalable and flexible online shopping platform with both relational and non-relational data storage systems.

## Project Overview

The application provides a seamless e-commerce experience, allowing users to browse products, manage their shopping cart, and place orders. It integrates Spring Boot for creating a robust backend, MySQL for structured data storage, and Elasticsearch for enhanced search functionality. Spring Boot serves as the backbone of the application, offering a modular, RESTful architecture that handles complex operations like user management, product cataloging, and order processing.

## Key Features

- **Spring Boot Backend**: Provides REST APIs for the frontend, handling requests for user authentication, product listing, cart management, and order processing.
- **User Management**: Allows users to register, log in, and manage their profiles, with role-based access for different types of users.
- **Product Management**: Supports a product catalog with attributes like name, description, price, and stock. Spring Boot integrates with MySQL for data persistence and with Elasticsearch for fast and flexible product searches.
- **Shopping Cart**: Users can add items to their shopping cart, adjust quantities, and proceed to checkout.
- **Order Processing**: Generates orders based on the cart's content, tracks them, and saves them for historical data.
- **Address Management**: Allows users to manage multiple addresses organized by district, city, and county.
- **Role-Based Access**: Different roles are defined within the application to control access permissions.

## Technology Stack

- **Spring Boot**: Acts as the core of the application, providing a RESTful API layer, managing dependencies, and enabling rapid development with Java.
- **MySQL**: Used as the primary relational database, storing structured data such as user details, product information, and orders.
- **Elasticsearch**: Integrated to handle search functionality, allowing efficient and flexible searching capabilities across the product catalog.

## ER Diagram

The data schema manages complex relationships, including users, products, carts, orders, addresses, and roles. Each table is structured to ensure efficient data handling and retrieval, leveraging Spring Boot’s JPA support to map database entities seamlessly.

---

