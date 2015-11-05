# Restaurant Microservice
(brief description)

<br /> 
<br /> 
### 1. INSTALLING DEPENDENCIES
##### Dependencies (see package.json for versions):
  - cookie-parser
  - body-parser
  - method-override
  - morgan
  - errorhandler
  - mongoose
  - nconf

##### Dependencies Installation:
```sh
  $ npm install
```

<br /> 

<br /> 

### 2. CONFIGURATION
All configurable parameters are accessible the following JSON file:
   - /courses_service/config/config.json

<br /> 

##### A. Server Configurables
  -  mongo.host : <host> {type: string}
  -  mongo.port : <port> {type: integer}

<br /> 

##### B. Schema Configurables
  -  courseAttributes : <key(s)> {type: string array}
  -  Attributes can also be added and removed via API (see Section 4 under 'MODIFY THE SCHEMA')

<br /> 

<br /> 

### 3. Launch:
```sh
  $ nodemon app.js
```