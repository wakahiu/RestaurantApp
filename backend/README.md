# Restaurant Microservice

##brief description
In this application, we implement a restaurant check-in & check-out service for a group of two or more people. With this application on a smartphone, a group of customers can “check-in” anywhere, order food, pay, and rate. The use of Cloud Computing to store vital information, data, and pictures will allow for pleasant experience while the use of IPSec will provide essential protection and a peace of mind to the users of the application.

<br /> 
<br /> 
### 1. INSTALLING DEPENDENCIES
##### Dependencies (see package.json for versions):
  - express
  - body-parser
  - cookie-parser
  - multer
  - mongodb

##### Dependencies Installation:
```sh
  $ npm install
```

<br /> 

### 2. Launch:
```sh
  $ node app.js
```

### 3. RESOURCES
##### 1. Tutorials

  - <a href="https://www.mongodb.com/blog/post/building-your-first-application-mongodb-creating-rest-api-using-mean-stack-part-1?jmp=docs&_ga=1.41398059.864639056.1446152580" target="_blank">Node.js and MEAN stack</a>

##### 2. Functional Requirements for the REST API
Users need to be able to do the following:

###### Patrons
  - Create an account.
  - Check in into a restataurant.
  - Create or join a table upon invite.
  - See the menu items.
  - Place an order.
  - Recieve a bill and pay.

Additionally, users should be able to reset their password.

###### Waiters
  - Create an account.
  - See tables
  - Bill a table or individual
  - Receive payment

Additionally, a users should be able to reset their password.

The following table shows how these operations can be mapped to HTTP routes and verbs.

| First Header  | Second Header |
| ------------- | ------------- |
| Content Cell  | Content Cell  |
| Content Cell  | Content Cell  |

##### 3. Some HTTP result codes. 

  - 200 - “OK”.
  - 201 - “Created” (Used with POST).
  - 400 - “Bad Request” (Perhaps missing required parameters).
  - 401 - “Unauthorized” (Missing authentication parameters).
  - 403 - “Forbidden” (You were authenticated but lacking required privileges).
  - 404 - “Not Found”.