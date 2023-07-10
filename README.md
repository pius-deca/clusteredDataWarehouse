
# CLUSTERED DATA WAREHOUSE

This is to analyze fx deals by accepting deals and persisting in DB. Validation is on fields to ensure correct data are persisted in the data.


##  Run - Docker
```sh
docker-compose up
```

## Project Run
#### How to run the project
- Go to application.yml in resources package and input your username and password for MYSQL

- Run this command
```sh
 mvn clean spring-boot:run
```

## PROJECT DOCUMENTATION

#### Technology Used
- SPRINGBOOT
- MYSQL
- DOCKER
- MAPSTRUCT
- LOMBOK

# Project Packages
#### Resource
- POST - /deal - Create a deal
- GET - /deals - Retrieve list of deals
- GET - /deal/{id} - Retrieve a deal by id

### Service and Impl
- The business logic is in the impl package. The service class which is an interface implements the impl class.
- The DealDTO is also in the service package

#### Model
- id - Unique id with Datatype: Long
- fromCurrency - ISO Currency from deal with Datatype: Currency
- toCurrency - ISO Currency to deal with Datatype: Currency
- createdAt - Instantaneous time with Datatype: Instant
- amount - Amount with Datatype: BigDecimal

#### Logging
- Logging used to log errors for controllers, services and repositories, spring ASPECT was used for cross-cutting concerns

### Request body
```json
{
    "fromCurrency": "NGN",
    "toCurrency": "USD",
    "amount": 4345.24
}
```

### Success Response
```json
{
  "status": "Success",
  "message": "Payload Successful",
  "data": {
    "id": 61,
    "fromCurrency": "NGN",
    "toCurrency": "USD",
    "amount": 4345.24,
    "createdAt": "2023-07-10T16:10:26.741829Z"
  }
}
```

### Error Response
if "fromCurrency is null"
```json
{
  "status": "Failed",
  "message": "An error has occurred at entity",
  "data": {
    "statusCode": 422,
    "message": [
      "Deal from_currency is not valid."
    ]
  }
}
```

TEST
- Unit test in the test folder
