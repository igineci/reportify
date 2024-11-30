# Reportify

This project automates the generation of reports based on data stored in a realtional database. The system connects to a PostgreSQL database, retrieves customer, product, sales and stock data, processes the data and then generates JSON reports. These reports are subsequently sent to an external API.

## Project Overview

The primary goal of this project is to generate and send five types of reports. Each report provides crucial data for different business operations and is sent in JSON format to an external API. The reports are:
- **Customer Data**
- **Product Data**
- **Sales Data** for the last 14 days
- **Sales Data** for the last 35 days
- **Stock Data**

## Features

- **Command-Line Interface**: Provides a simple CLI for getting parameters for establishing connection to the database and parameter for selecting which reports to generate and send.
  
| Argument | Short | Description                    | Required |
| ------- | ------- | -------                    | ------- |
|host      |-h     |The host of the database server.|Yes       |
|port|-p|The port number of the database server.|Yes|
|database|-d|The name of the database to connect to.|Yes|
|username|-U|The username used for authentication with the database.|Yes|
|password|-P|The password associated with the username for database access.|Yes|
|reports|-r|Reports that are going to be generated and sent to an external API.|Yes|

- **Database Connectivity**: Connects to a PostgreSQL database to fetch data required for report generation.
- **Report Generation**: Generates Customer, Product, Sales and Stock reports based on input parameters:
- **API Communication**: Sends reports to an external API using the POST method in JSON format.

## Technologies Used

- **Java** JDK  or higher
- **Apache HttpClient** for handling HTTP requests
- **Gson** for generating and parsing JSON
- **PostgreSQL** as the relational database to store data.
- **Command-Line arguments** for input parameters.

# Getting Started

## Instalation

1. **Clone the project**:

 ```bash
    git clone https://github.com/igineci/reportify.git
```
2. **Navigate to the project Directory**:

```bash
    cd reportify
```
3. **Build the Project**:
If you use Maven:
```bash
    mvn clean install
```
If you use Gradle:
```bash
    gradle build
```
4. **Run the Application**:
   After building the project you will be able to run the application with the following command:
```bash
    java -jar target/reportify.jar  -p 17710 -d b1 -U user -P password123* -h localhost -r cus,sto,sal,pro
```
This above command will generate and send Customer(cus), Product(pro), Sales(sal) and Stock(sto) reports.
This is just an example, replace parameters with the appropriate values for your database.

## Report Documentation

1. **Customer Data Report**
The CUS report updates customer data and includes critical information such as delivery locations and payers.

**Required Fields for this report**:

|Field Name|Description|Data Type|Mandatory|Additional Information|
| ------- | ------- | ------- | ------- | ------- |
|BRANCH_ID|Customer's branch/store identifier.|CHR (50)|Yes||
|CUSTOMER_ID|Unique customer or delivery location identifier.|CHR (50)|Yes||
|PAYER_ID|Payer's identifier (if different from the customer).|CHR (50)|Yes|If no separate payer exists, this is the same as CUSTOMER_ID.|
|CUSTOMER_NAME_1|Full customer name.	|CHR (500)|Yes||
|CUSTOMER_NAME_2|Short name or identifier for the customer.|CHR (200)|Yes||
|COUNTRY_ID|Country code (ISO-3166-1).|CHR (10)|Da||
|CITY|Customer's city.|CHR (100)|Yes||
|POSTAL_CODE|Customer's postal code.|CHR (20)|Yes||
|STREET|Customer's street address.|CHR (100)|Yes||
|TIN|Tax Identification Number (VAT number).|CHR (50)|Yes|Must contain 9 or 13 digits, and follow certain rules.|
|ACTIVITY|Customer status..|INT|Yes| 0 - inactive, 1 - active.|


2. **Product Data Report**
The Product Report contains detailed data about products, including both active and inactive products.

**Required Fields for this report**:

|Field Name|Description|Data Type|Mandatory|Additional Information|
| ------- | ------- | ------- | ------- | ------- |
|BRANCH_ID|Branch/office identifier of the customer.|CHR (50)|Yes||
|PRODUCT_ID|Product identifier.|CHR (50)|Yes||
|PRODUCER_PRODUCT_ID|Serial number of the product in the sales system branch.	CHAR |CHR (50)|Yes||
|EAN|Barcode.|CHR (15)|Yes||
|PRODUCT_NAME_1|Full product name, description, and features.|CHR (100)|Yes||
|PRODUCT_NAME_2|Short product name, code, identifier.|CHR (200)|Yes||
|MEASURE_UNIT_ID|ID of the smallest sales unit.|CHR (50)|Yes||
|MEASURE_UNIT|Name of the measurement unit.|CHR (100)|Yes||
|PRODUCER_ID|Manufacturer's product identifier.|CHR (50)|Yes||
|PRODUCER_NAME|Manufacturer's name.|CHR (100)|Yes||
|ACTIVITY|Product status.|INT|Yes|0 - deleted or not present in the PRO message.
1 - active, available for sale.|
|MULTIPLIER|Conversion factor from supplier's unit to manufacturer's unit.|DEC (19,8)|Yes||

3. **Sales Data Report**
The Sales Report contains sales documents for the registered manufacturer's assortment. It reports the quantity of sold products based on sales documents and their corrections*. 
*Corrections refer to invoice corrections, returns, and credit invoices/notes (all documents related to a reduction in sales quantity). 
For each SAL message, the data range is defined in the fields @FROM_DATE@ and @TO_DATE@. The data range should not be shorter than 14 calendar days, with an additional sales report sent for 35 days on the 5th day of the month.

**Required Fields for this report**:

|Field Name|Description|Data Type|Mandatory|Additional Information|
| ------- | ------- | ------- | ------- | ------- |
|FROM_DATE|Start date of the reporting period.|DATE|Yes||
|TO_DATE|End date of the reporting period.|DATE|Yes||
|TYPE_DATE|Type of date used for selecting documents.|CHR (10)|Yes||
|TYPE_ID|Document type.|CHR (3)|Yes||
|BRANCH_ID|Distributor branch identifier.|CHR (50)|Yes||
|DOC_ID|Document identifier.|CHR (50)|Yes||
|ISSUE_DATE|Document issue date.|DATE|Yes||
|SALE_DATE|Sale date from the document. If correction, it is the return issue date.|DATE|Da|If not provided, the document issue date is used.|
|CUSTOMER_ID|Customer identifier for the distributor branch.|CHR (50)|Yes||
|CUR_DOC_ID|Identifier for the correction document.|CHR (50)|Yes, for invoice corrections or returns|If it is a sales document, it is EMPTY VALUE.|
|CUR_DOC_DATE|Date of the document error for corrections.|DATE|Yes, for invoice corrections or returns|If it is a sales document, it is EMPTY VALUE.|
|TYPE_SALE_ID|Type of sale.|CHR (3)|Yes||
|FIELD_1|WHS BD code.|CHR (200)|Yes|If it is a sales document, it is EMPTY VALUE.|
|FIELD_2|WHS BD code.|CHR (200)|Yes|If it is a sales document, it is EMPTY VALUE.|
|ITEM|Unique item in the document.|INT|Yes|In the case of a correction, it aligns with the item in the correction document.|
|PRODUCT_ID|Product identifier for the distributor branch.|CHR (50)|Yes||
|MEASURE_UNIT_ID|Identifier for the smallest sales unit.|CHR (50)|Yes||
|PRODUCER_ID|Manufacturer's product identifier.|CHR (50)|Yes||
|QUANTITY|Quantity of the sold product expressed in the measurement unit (MEASURE_UNIT_ID).|DEC (19,4)|Da|If it is a correction document, the quantity is AFTER correction.|
|QUANTITY_WAS|Quantity of the sold product expressed in the measurement unit (MEASURE_UNIT_ID) BEFORE correction.|DEC (19,4)|Yes, for invoice corrections or returns|If it is a sales document, it is EMPTY VALUE.|

4. Stock Data Report

The Stock Report sends the current stock/warehouse status for the registered products from the distributor.

**Required Fields for this report**:

|Field Name|Description|Data Type|Mandatory|
| ------- | ------- | ------- | ------- | 
|BRANCH_ID|Distributor branch identifier.|CHR (50)|Yes|
|PRODUCT_ID|Product identifier for the distributor branch.|CHR (50)|Yes|
|STOCK_DATE|Date of the reported stock status.|DATE|No|
|STOCK|Stock status.|DEC (19,4)|Yes|
|MEASURE_UNIT_ID|Identifier for the smallest sales unit.|CHR (50)|Yes|
|PRODUCER_ID|Manufacturer's product identifier.|CHR (50)|Yes|
