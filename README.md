# 🧾 QR Check Scanner

This is a Spring Boot web application that allows users to upload an image of a QR code (usually from a purchase receipt), decode it, and retrieve a list of purchased items by making a request to [proverkacheka.com](https://proverkacheka.com) API.

---

## 🚀 Features

- Upload a QR code image via a simple web interface.
- Decode QR code using ZXing and OpenCV (fallback).
- Send decoded data to the API and fetch check details.
- Display parsed information (total amount, item list) to the user.
- Save check and item details in a PostgreSQL database.

---

## 🛠️ Tech Stack

- **Kotlin** + **Spring Boot**
- **Thymeleaf** for HTML rendering
- **ZXing** for basic QR decoding
- **JavaCV / OpenCV** for complex QR recognition
- **PostgreSQL** as the database
- **JPA / Hibernate** for persistence
- **Bootstrap 5** for frontend styling

---

## 📷 How it works

1. User uploads a receipt photo containing a QR code.
2. The app decodes the QR (ZXing first, OpenCV as backup).
3. A request is sent to the [proverkacheka.com API](https://proverkacheka.com/api/v1/check/get).
4. Response is parsed and shown on a result page.
5. Check and items are stored in the database for later use.

---

## 🧪 How to Run and Test Locally

1. **Clone the repository**:

   ```bash
   git clone https://github.com/Thunder22020/ProductScannerQR
   ```
2. Set up your database (PostgreSQL recommended):
 Create a new database, e.g. qrchecker
3. Configure your credentials in `src/main/resources/application.properties`
4. Build and run the application
5. Open in browser (http://localhost:8080)

---
## 📁 Project Structure
```
scannerqr/
├── controllers/      → REST endpoints
├── services/         → Business logic, QR decoding, API calls
├── models/           → DTOs and JPA Entities
├── repositories/     → Spring Data JPA Repositories
├── utils/            → QR decoding and DTO conversion
└── templates/        → HTML pages (index, results)

```

