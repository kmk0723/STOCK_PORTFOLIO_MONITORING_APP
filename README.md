# STOCK_PORTFOLIO_MONITORING_APP

This is a Spring Boot-based Stock Portfolio Management System that allows users to manage their investments, track their holdings, and get real-time updates. The project integrates with Swagger for seamless API documentation and testing.

---

## Project Objectives
- Manage user registration and authentication .
- Track stock holdings and portfolio performance.
- Enable buying and selling of stocks through the API.
- Set price alerts for specific stock symbols.
- Document and test APIs using Swagger UI.

---

## Features

- User Registration & Login
- Portfolio Management (Buy/Sell stocks)
- Alert System for stock price changes
- Database integration using Spring Data JPA
- Unit testing with JUnit
- Scheduled background tasks
- API documentation with Swagger UI

---

## Project Flow

1. *User* interacts via Swagger UI .
2. *Controller Layer* handles HTTP requests.
3. *Service Layer* processes business logic.
4. *Repository Layer* interacts with the database.
5. *Model Layer* maps to DB tables.
6. *DTOs* carry structured data.
7. *Exception Handling* ensures clear errors.
8. *Swagger Config* provides UI for API testing.

---

## Tech Stack

| Layer       | Technology                     |
|-------------|--------------------------------|
| Backend     | Spring Boot                    |
| ORM         | Spring Data JPA (Hibernate)    |
| DB          | MySQL (configurable)      |
| Docs        | Swagger (Springfox)            |
| Build Tool  | Maven                          |
| Testing     | JUnit                          |

---

## Project Structure

```
StockPortfolioMonitoringApp/
├── src/
│   ├── main/java/com/StockPortfolioMonitoringApp/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── exception/
│   │   ├── model/
│   │   ├── repository/
│   │   ├── service/
│   │   └── resources/
├── test/
│   └── java/com/StockPortfolioMonitoringApp/
├── pom.xml
└── README.md

```

---

## API Documentation

Once the application is running, access the Swagger UI at:

```
http://localhost:8080/swagger-ui/index.html

```

1. *User Controller*
```
POST /api/users/register – Register user
```
```
POST /api/users/register-admin – Register an admin
```
```
POST /api/users/login – User login
```
```
POST /api/users/{admin}/holdings – Admin view of all user holdings
```
```
POST /api/users/{admin}/users – Admin view of all users
```

2. *PortfolioController*
```
PUT /api/portfolio/sell – Sell stock
```
```
POST /api/portfolio/buy – Buy stock
```
```
GET /api/portfolio/{username} – Get portfolio by username
```
```
GET /api/portfolio/showStocks – View available stocks
```

3. *Alert Controller*
```
PUT /api/alerts/{id} – Update alert by ID
```
```
GET /api/alerts – View all alerts
```
```
POST /api/alerts – Create a new alert
```
```
GET /api/alerts/logs – View alert logs
```

4. *ReportController*
```
GET /api/reports/portfolio-summary – Portfolio summary report
```
```
GET /api/reports/export – Export report data
```


## How to Run

1. *Clone the repository*
```
   bash
   git clone https://github.com/kmk0723/STOCK_PORTFOLIO_MONITORING_APP
   cd STOCK_PORTFOLIO_MONITORING_APP
```   

2. *Build the project*
```
   bash
   ./mvnw clean install
``` 

3. *Run the app*
```
   bash
   ./mvnw spring-boot:run
```  

---

## Running Tests

Use your choice IDE, such as Eclipse or IntelliJ, to run the JUnit test cases included in the src/test/java directory.

---


## License

This project is for educational purposes only. Kindly contact the authors before any Commercial use. 
---

## Authors
- [Kalakonda Mani Karthikeya](https://github.com/kmk0723)
- [Bandaru Sai Sri Veer](https://github.com/SAISRIVEER)
- [Chenna Reddy Nithin Kumar](https://github.com/nithinkumar986)
- [Sanjeev Konisetty](https://github.com/sanjeevknsty)
- [Vishnu Vimal Kumar](https://github.com/VishnuVimal24)
- [Bevin Joshua Rajesh](https://github.com/bevinjoshua)


