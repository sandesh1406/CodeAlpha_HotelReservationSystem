ELITE STAY | Hotel Property Management System

A professional Java Swing desktop application for managing hotel rooms, guests, reservations, payments, availability, and administrative operations.

Developed as part of the CodeAlpha Java Programming Internship – Task 4.

📸 Application Screenshots

Login



Dashboard



Admin



📌 Project Overview

ELITE STAY is a desktop-based Hotel Property Management System built with Java Swing and Object-Oriented Programming principles.

The application provides a centralized interface for hotel staff to:

Manage hotel room inventory

Search and filter rooms

Create and manage reservations

Manage guest information

Process simulated payments

Track room availability

Check guests in and out

View booking and payment details

Monitor hotel operations through a dashboard

Access administrative controls and reports

Save and load application data using Java serialization

The system is designed as a local PMS, making it suitable for demonstrating core hotel-management workflows without requiring an external database.

🎯 CodeAlpha Task 4

This project is implemented to satisfy the major requirements of CodeAlpha Java Programming Internship – Task 4, including:

Hotel room search and booking

Reservation management

Reservation cancellation

Room categorization

Payment simulation

Booking details

Guest management

OOP principles

File I/O / Java Serialization for persistence

Administrative room and reservation management

✨ Key Features

🏨 Room Management

Standard, Deluxe, and Suite room categories

Room pricing and guest capacity

Room inventory management

Availability tracking

Category-based room filtering

Date-based availability checking

Prevention of overlapping bookings

📅 Reservation Management

Create new reservations

Generate unique booking IDs

Guest details collection

Check-in and check-out dates

Guest capacity validation

Automatic total calculation based on nights and room price

Reservation status tracking

Reservation cancellation

Check-in and check-out workflow

Detailed booking view

👤 Guest Management

Guest name

Phone number

Email address

Unique guest ID

Centralized customer listing

Guest history through reservations

💳 Payment Management

Payment processing simulation

Payment transaction IDs

Payment methods

Payment status tracking

Paid and pending payment states

Payment history

Receipt/booking confirmation support

📊 Dashboard

The dashboard provides an operational overview including:

Total rooms

Available rooms

Occupied rooms

Maintenance status

Active bookings

Today's check-ins

Today's check-outs

Total revenue

Room status overview

Recent reservations

🛡️ Authentication & Administration

Login authentication

Role-based application access

Administrator profile

Online session status

Admin Center

User management

Administrative navigation

Reports and operational overview

💾 Data Persistence

The application uses Java File I/O and Serialization to preserve application data locally.

Supported data includes:

Rooms

Reservations

Guests

Payments

Transaction information

🔄 Reservation Workflow

Login
  │
  ▼
Dashboard
  │
  ▼
New Reservation
  │
  ├── Guest Details
  │
  ├── Room Category
  │
  ├── Available Room
  │
  ├── Number of Guests
  │
  ├── Check-in Date
  │
  └── Check-out Date
  │
  ▼
Reservation Created
  │
  ▼
Payment
  │
  ├── Pay Now
  │
  └── Pay Later
  │
  ▼
Booking Details
  │
  ▼
Receipt / Reservation Management

🔎 Room Search & Availability

The system supports date-based room availability checking.

Search criteria include:

Check-in date

Check-out date

Room category

Maximum guest capacity

The application validates:

Correct date format

Check-out after check-in

Room capacity

Existing reservation overlap

Room availability for the requested stay

Cancelled reservations release the room for the applicable dates.

📋 Reservation Lifecycle

Reservations can move through the operational lifecycle:

CONFIRMED
    │
    ├── CHECKED_IN
    │       │
    │       └── CHECKED_OUT
    │
    └── CANCELLED

Cancelled reservations remain available in reservation history for record keeping.

Payments remain associated with reservations as historical transaction data.

🧮 Dynamic Booking Calculation

The reservation amount is calculated from the room's nightly price and stay duration.

Total Amount = Room Price × Number of Nights

This amount is then used during the payment workflow.

🛠️ Technology Stack

Technology

Usage

Java

Core application language

Java Swing

Desktop GUI

OOP

Application architecture and business models

Java Collections

Runtime data management

Java File I/O

Local persistence

Java Serialization

Saving and loading application data

AWT / Swing Printing

Receipt printing

🧱 Project Architecture

The application separates the user interface, business logic, and data models.

┌─────────────────────────────────────┐
│          Java Swing GUI             │
│     HotelReservationGUI             │
│            LoginPanel               │
└──────────────────┬──────────────────┘
                   │
                   ▼
┌─────────────────────────────────────┐
│        Business Logic Layer         │
│                                     │
│ RoomManager                         │
│ ReservationManager                  │
│ PaymentManager                      │
│ AuthService                         │
│ AuthSession                         │
└──────────────────┬──────────────────┘
                   │
                   ▼
┌─────────────────────────────────────┐
│             Data Models             │
│                                     │
│ Room                                │
│ Guest                               │
│ Reservation                         │
│ Payment                             │
│ User                                │
└──────────────────┬──────────────────┘
                   │
                   ▼
┌─────────────────────────────────────┐
│       Local File Persistence        │
│       Java Serialization            │
└─────────────────────────────────────┘

📁 Project Structure

CodeAlpha_HotelReservationSystem/
│
├── src/
│   ├── Main.java
│   ├── HotelReservationGUI.java
│   ├── LoginPanel.java
│   │
│   ├── AuthService.java
│   ├── AuthSession.java
│   ├── User.java
│   │
│   ├── Room.java
│   ├── RoomManager.java
│   ├── RoomCategory.java
│   │
│   ├── Guest.java
│   │
│   ├── Reservation.java
│   ├── ReservationManager.java
│   ├── ReservationStatus.java
│   │
│   ├── Payment.java
│   ├── PaymentManager.java
│   ├── PaymentMethod.java
│   └── PaymentStatus.java
│
├── data/
│   └── Local application data
│
├── screenshots/
│   ├── Login.png
│   ├── Dashboard.png
│   └── Admin.png
│
├── .gitignore
└── README.md

▶️ How to Run

1. Clone the repository

git clone https://github.com/YOUR_USERNAME/CodeAlpha_HotelReservationSystem.git
cd CodeAlpha_HotelReservationSystem

2. Compile the project

javac -d out src/*.java

3. Run the application

If Main.java is the project entry point:

java -cp out src.Main

If your environment uses HotelReservationGUI directly:

java -cp out src.HotelReservationGUI

🔐 Authentication

The application starts with a login screen and authenticates users through the application's AuthService.

After successful authentication, the user enters the main Hotel Property Management System.

Administrative users can access:

Admin Center

Reports

Users

Room management

Reservation management

Payment management

🧪 Validation & Testing

The project includes validation around important hotel-management operations such as:

Room availability

Reservation dates

Guest capacity

Reservation status

Payment processing

Cancellation

Check-in / check-out

Data persistence

The project was also validated with the existing project audit tests during development.

🚀 Future Enhancements

Possible future improvements include:

Database integration with MySQL/PostgreSQL

Multi-user network deployment

Online booking portal

Email booking confirmations

QR-code receipts

Advanced revenue analytics

PDF invoice generation

Cloud data synchronization

Automated backup and restore

Advanced user permissions

👨‍💻 Author

Sandesh

Java Programming Internship Project
CodeAlpha – Task 4

📄 License

This project was developed for educational and internship purposes as part of the CodeAlpha Java Programming Internship.