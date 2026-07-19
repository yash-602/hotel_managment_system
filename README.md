# Hotel Management System 🏨

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/javafx-%23FF0000.svg?style=for-the-badge&logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/apache_maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

A comprehensive desktop application built with **Java** and **JavaFX** for managing hotel operations. This system provides a clean, modern user interface to handle room availability, customer bookings, billing, and live analytics.

---

## 🎥 Live Demonstration
*(Upload your GIF here! Simply drag and drop your recorded GIF into the GitHub editor to replace this text).*

---

## ✨ Key Features

* **Dashboard Analytics:** Real-time overview of total revenue, room availability, and a visual grid of all rooms.
* **Room Management:** Restricted admin access to add new rooms, modify prices, and filter by room types (Single, Double, Deluxe, Suite).
* **Booking System:** Allocate rooms to customers, calculate stay durations using `LocalDate`, and offer loyalty discounts and extra amenities.
* **Automated Billing:** Instantly generate complete checkout invoices calculating room costs, amenities, and loyalty discounts.
* **Persistent Storage:** Custom file serialization ensures data is saved securely without the need for an external SQL database.

---

## 📸 Screenshots

### The Main Dashboard
*(Drag and drop a screenshot of the dashboard here)*

### Booking a Room
*(Drag and drop a screenshot of the booking tab here)*

### Generating a Bill
*(Drag and drop a screenshot of the billing receipt here)*

---

## 🛠️ How to Run Locally

If you want to run this application on your own machine:

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yash-602/hotel_managment_system.git
   ```
2. **Navigate to the directory:**
   ```bash
   cd hotel_managment_system
   ```
3. **Run using Maven:**
   ```bash
   mvn clean javafx:run
   ```

---

## 🏗️ Technical Architecture
* **Design Pattern:** Model-View-Controller (MVC) structure for separation of concerns.
* **UI Framework:** Custom-styled JavaFX using programmatic layouts and CSS (No SceneBuilder/FXML used).
* **Data Storage:** Java Object Serialization via `ObjectOutputStream` and `ObjectInputStream`.
