# bankaccount
System Requirements
------------------------------------------
1. Java 8
2. Intellij Editor
3. MySQL 5.7.26
4. Postman

Database setup
-----------------------------------------
1. Create a new database named "bankaccount";
2. Import the files from the DB_bankaccount folder into the newly created database.

For account details please go through the table mapping_user_account in database. All transactions will be recorded in transaction_details table.

Project Setup
-----------------------------------------
1. Clone the project
2. Open it using the intellij editor
3. Go to application property file available under src->main->resources->application.properties
4. Change the username and password of your local user.
5. Run the application from BankAccountApplication.java available in src->main->java->com.linus.bankaccount->BankaccountApplication.java

Testing
------------------------------------------
1. Open Postman application
2. Provide url as http://localhost:8080/v1/apt/transaction with POST method
3. In header add Content-Type : application/json
4. In Body section select raw format and pass the parameter as 
{
    "fromAccountId":"6789000000225002",
    "toAccountId":"6789000000225002",
    "amount":"10.1"
}




 
