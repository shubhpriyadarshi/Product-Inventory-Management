# Problem statement: Product Inventory Management

Create a product inventory management service with the following functionality. You can create Restcontroller, service,
repository in order to save the data in database. Use postman/curl/swagger or any other means to create api collection
for testing.

#### Language: Java

#### Database: Any (MySQL Preferred)

### 1. Create API to add product inventory in stock.

**PUT** `api/v1/product`

Product Information will have following fields:

* Product_Name
* InStock_Quantity

If the same api is executed multiple times with product Id already present in DB, then it should increment the inventory
stock.

The database table should have following information:

* Product name
* Product Id (Unique)
* InStock Quantity

### 2. Create API to onboard a buyer.

**PUT** `api/v1/buyer`

Buyer information should have 2 fields:

* BuyerId
* Name

### 3. Create API to consume the product by a buyer. Take product quantity that is to be ordered in payload.

**POST** `/api/v1/product/{productId}/buyer/{buyerId}/order`

### 4. Create API to display instock quantity for a product Id.

**GET** `/api/v1/product/{productId}/stockInHand`

### 5. Create API to list all buyer and products ordered by them.

**GET** `api/v1/product/order`
