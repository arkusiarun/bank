# bank

Steps to Manully Run

	1) Start Docker (systemctl start docker)
	2) docker network create bank-mysql
	3) docker container run --name mysqldb --network bank-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=bank -d mysql:8
	4) docker image build -t bank-jdbc .
	5) docker container run --network bank-mysql --name bank-jdbc-container -p 8080:8080 -d bank-jdbc

Stack Used:
	1) Java, Spring Boot (Microservice Core).
	2) MYSQL (DataBase)
	3) Flyway (Database Migration and Initial Schema Creation)
	4) MyBatis (JPA)

Post Execution Steps:
	1) No need to create Schema, Flyway is Integrated which will take care of initial schema creation.
	2) Create User.
	3) Create Account.
	4) Post Create Account User can Deposit, Withdraw, Check Balance, View Account Summary, Check Transactions.

Initial Denominations are as Below:
name    value count deposit_limit disburse_limit
FIFTY    50    35      100    		10
TWENTY   20    64      100 	 		30
TEN      10    52      100 	 		30
FIVE      5    41      100 	 		20


count --> Remaing Count in ATM
deposit_limit  --> Maximum notes of a denomination ATM can Store.
disburse_limit --> Maximum notes of a Denominations that can be disbursed

When a user Deposit Cash or Withdraw Cash Count in Denomination Changes.


API's aand Responses:

Create User:

curl -X POST "http://localhost:8087/bank/create/user" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"password\": \"test-user\", \"userName\": \"password\"}"

Response: 
{
  "success": true,
  "body": {
    "userId": 2843
  }
}


Create Account:

curl -X POST "http://localhost:8087/bank/create/account" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"customerId\": 2843, \"customerName\": \"Arun\", \"depositAmount\": 1005}"

Response:
{
  "success": true,
  "body": {
    "customerId": 2843,
    "accountNumber": 31148
  }
}

View Account Summary:

curl -X GET "http://localhost:8087/bank/view/account/summary?customerId=2843" -H "accept: */*"

Response:

{
  "success": true,
  "body": {
    "customerId": 2843,
    "accounts": [
      {
        "accountNo": 31148,
        "customerId": 2843,
        "customerName": "Arun",
        "status": "ACTIVE",
        "clearBalance": 1005,
        "unclearBalance": 0
      }
    ]
  }
}

Add Denominations to ATM

curl -X POST "http://localhost:8087/bank/atm/add/denominations" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"fifties\": 10, \"fives\": 10, \"tens\": 10, \"twenties\": 10}"

Response:
{
  "success": true
}


ATM CASH WITHDRAWAL

curl -X POST "http://localhost:8087/bank/atm/transaction" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"accountNo\": 31148, \"customerId\": 2843, \"transactionAmount\": 205, \"transactionType\": \"CASH_WITHDRAWAL\"}"

Response:
{
  "success": true,
  "body": {
    "transactionId": "c4b85ad7-3052-40a1-94e4-b8bded3c526a",
    "accountNo": 31148,
    "transactionType": "Cash Withdrawal",
    "transactionAmount": 205,
    "remainingBalance": 800,
    "denominations": {
      "fifties": 4,
      "twenties": null,
      "tens": null,
      "fives": 1
    }
  }
}

ATM BALANCE ENQUIRY

curl -X POST "http://localhost:8087/bank/atm/transaction" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"accountNo\": 31148, \"customerId\": 2843, \"transactionType\": \"BALANCE_CHECK\"}"

Response:
{
  "success": true,
  "body": {
    "transactionId": "1740a09f-a60b-4cce-b2eb-9deda6d57480",
    "accountNo": 31148,
    "transactionType": "Balance Check",
    "transactionAmount": 0,
    "remainingBalance": 800
  }
}


ATM CASH DEPOSIT

curl -X POST "http://localhost:8087/bank/atm/transaction" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"accountNo\": 31148, \"customerId\": 2843, \"denominations\": { \"fifties\": 3, \"fives\": 2, \"tens\": 1, \"twenties\": 2 }, \"transactionAmount\": 210, \"transactionType\": \"CASH_DEPOSIT\"}"

Response:
{
  "success": true,
  "body": {
    "transactionId": "1b6a314c-f207-4fdc-bb31-13e13b0095ee",
    "accountNo": 31148,
    "transactionType": "Cash Deposit",
    "transactionAmount": 210,
    "remainingBalance": 1010
  }
}


VIEW Account Summary

curl -X GET "http://localhost:8087/bank/view/transaction/summary?accountNo=31148" -H "accept: */*"

Response:
{
  "success": true,
  "body": {
    "accountNo": 31148,
    "transactionList": [
      {
        "id": 1,
        "accountNo": 31148,
        "transactionType": "Cash Withdrawal",
        "transactionId": "aac8195e-0eb1-4780-9e2e-c1fea1c4c9c4",
        "currentBalance": 1005,
        "transactionAmount": 205,
        "clearBalance": 800,
        "status": "COMPLETED",
        "transactionDate": "2022-09-26",
        "remarks": "Cash Withdrawal Success"
      },
      {
        "id": 2,
        "accountNo": 31148,
        "transactionType": "Cash Deposit",
        "transactionId": "7a94b23e-0291-42f1-8846-742e00518934",
        "currentBalance": 800,
        "transactionAmount": 210,
        "clearBalance": 1010,
        "status": "COMPLETED",
        "transactionDate": "2022-09-26",
        "remarks": "Cash Deposit Success"
      }
    ]
  }
}