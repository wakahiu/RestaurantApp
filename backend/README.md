# Restaurant Microservice

##brief description

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

### 3. Launch:
```sh
  $ node app.js
```

##### Some HTTP result codes. 

  - 200 - “OK”.
  - 201 - “Created” (Used with POST).
  - 400 - “Bad Request” (Perhaps missing required parameters).
  - 401 - “Unauthorized” (Missing authentication parameters).
  - 403 - “Forbidden” (You were authenticated but lacking required privileges).
  - 404 - “Not Found”.