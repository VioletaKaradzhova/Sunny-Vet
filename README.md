# Sunny Vet Clinic Management System 🐾

## Overview
Sunny Vet Clinic is a comprehensive, modern web application designed to manage the daily operations of a veterinary clinic. It handles user registrations, pet profiles, appointment scheduling, and medical treatment records. The system employs a microservice architecture, utilizing a Main Application for user-facing interactions and a dedicated REST Microservice for securely handling medical treatment data.

## Tech Stack
* **Backend Core:** Java 17+, Spring Boot, Spring MVC
* **Security:** Spring Security (Hybrid authentication: stateless JWT for APIs & stateful Cookie/Session for the Web UI)
* **Persistence:** Spring Data JPA, Hibernate, PostgreSQL
* **Frontend:** Thymeleaf, Bootstrap 5, HTML5/CSS3 (with custom SVG aesthetic patterns)
* **Inter-Service Communication:** Spring Cloud OpenFeign

## Integrations
* **Treatment REST Microservice:** A separate Spring Boot application running on port `8081` that strictly handles the `Treatment` domain. The Main Application (port `8080`) integrates with this microservice via **Feign Client** (`MicroserviceClient`) to perform RESTful CRUD operations (POST, PUT, GET, DELETE) across the network.
* **Dual PostgreSQL Databases:**
    * `sunnyvet` - Stores Users, Owners, Doctors, Pets, and Appointments (Accessed by the Main App).
    * `sunnyvet_micro` - Stores Treatment medical records securely (Accessed strictly by the Microservice).

## Supported Features
* **Role-Based Access Control (RBAC):** Distinct roles for `USER` (Client), `DOCTOR`, and `ADMIN`, providing strict authorization rules across the UI and backend endpoints.
* **Smart UI & Dynamic Routing:** Thymeleaf templates that dynamically adapt to the logged-in user's role (e.g., Doctors bypassing strict client ownership rules to manage clinic-wide data).
* **Unified Admin Dashboard:** A centralized control panel for administrators to oversee and manage all users, doctors, pets, and appointments.
* **Robust Validation:** Enforces strict backend constraints, such as future-only appointment dates and secure entity ownership validation to prevent unauthorized URL manipulation.
* **Custom Error Handling:** Beautifully styled custom error pages for `404 Not Found` and `500 Internal Server Error` scenarios, providing a seamless user experience.
* **Single Page Layout Elements:** Smooth-scrolling anchor links combining the Hero, About Us, Doctors, Treatments, and Contact sections on the home page.

## Domain Functionalities
* **Account Management:**
    * Users, Doctors, and Admins can update their profiles (Email, Full Name, Phone Number, Username, Password).
    * Users can request secure account deletion.
    * Admins can create, edit, and delete user accounts.
* **Doctor Roster:**
    * Admins can add new Doctors, simultaneously creating their login credentials and professional profile (Specialization).
    * Dynamic doctor pre-selection when a user clicks "Book Appointment" from the doctor's profile.
* **Pet Management:**
    * Clients can register, edit, and manage their own pets.
    * Doctors and Admins can view, edit, and delete all pets across the clinic.
    * Dedicated "Pet Details" view for comprehensive pet profiles.
    * Pets lists are sorted alphabetically by name.
* **Appointment Scheduling:**
    * Clients can book appointments for their pets with specific doctors.
    * Doctors and Admins can update, reschedule, or cancel any appointment.
    * Appointments are strictly validated for future dates and sorted chronologically (soonest first).
* **Medical Treatments (Microservice):**
    * Doctors and Admins can record new medical treatments (Description, Medication) for a specific pet.
    * Doctors and Admins can update existing treatment records.
    * Treatment history is securely fetched from the microservice and displayed in chronological order on the pet's dedicated medical record page.