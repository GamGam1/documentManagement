# documentManagement

# Backend
A RESTful API built with Spring Boot for managing documents. Supports creating, updating, deleting, and filtering document data.

## Features
* CRUD operations

* Filtering documents by multiple categories, upload date, document name, and file extension.

* Input Validation

* JSON-based API requests and responses

* uploading and deleting to an AWS S3 bucket

* User registration and login with JWT authentication

* Secure endpoints using Spring Security

* User data isolation (users only access their own data)

## Tech Stack
* Java 24
* Spring Boot
* Spring Data JPA
* Postgres SQL
* Maven
* AWS S3
* Postman (API Testing)
* Spring Security + JWT

## Starting Guide
* PreReqs
    * Java 24
    * Maven
    * Intellji (or ide of your choice, but instructions will assume you are using Intellji)
    * Database set up 
    * AWS S3 Account
* Connecting to Database
    * Make an `application.properties` file in the resource `backend/src/main/resources` folder, and copy the lines provided in the `template-AppProp.txt` file which is located in the `template` folder. Fill in the blanks.
    *  Run the application to make sure it is all working
    * 
## Security

- Passwords are securely hashed using BCrypt.
- JWTs are used to authenticate and authorize users.
- Only authenticated users can access protected endpoints.
- Users can only interact with their own data.
- Admin role can be extended to manage users or access analytics (planned).

## Database Information

### Models
The `DocumentData` Entity, the main database

| Name            | Type      | Description                                                           |
|-----------------|-----------|-----------------------------------------------------------------------|
| `documentId`    | Long      | unique id of the expense                                              |
| `userId`        | Long      | userId that the document belongs too                                  |
| `documentName`  | String    | Name of Document                                                      |
| `uploadDate`    | TimeStamp | Date of when the document was uploaded                                |
| `fileSize`      | String    | Size of document, in KB or MB                                         |
| `contentType`   | String    | content Type of the document                                          |
| `category`      | String    | category of the document                                              |
| `s3Key`         | String    | s3key of the document, used to find the document in the AWS S3 Bucket |
| `favorite`      | Boolean   | If True that means the document is favorite                           |
| `fileExtension` | String    | The file extension of the document                                    |

* **Validation**
    * id: unique and non null
    * userId: cannot be empty 
    * documentName: cannot be empty and must be not contain any window path characters
    * category: no window path characters
  
The `UpdateDocumentData` class, helps with data transferring when it comes to updating a document's metadata

| Name           | Type    | Description                      |
|----------------|---------|----------------------------------|
| `documentName` | String  | The updated document name        |
| `favorite`     | Boolean | Updated status of favorite       |
| `category`     | String  | Updated category of the document |


* **Validation**
    * documentName: no window characters
    * category: no window characters

The `UploadDocumentData` class, helps with data transferring when it comes to updating a document's metadata

| Name       | Type    | Description              |
|------------|---------|--------------------------|
| `favorite` | Boolean | status of favorite       |
| `category` | String  | category of the document |


* **Validation**
    * category: no window characters and must not be null

The `UserInfo` class is the database that stores users.

| Name       | Type   | Description                             |
|------------|--------|-----------------------------------------|
| `username` | String | username of the user                    |
| `password` | String | password of the user (encrypted)        |
| `role`     | String | role of the user, "USER" is the default |
| `userId`   | Long   | Id of user                              |



* **Validation**
  * username: unique

## API Endpoints

### AUTH
* `localhost:8080/api/auth/register`
  * given a `username` and `password` in the json body it registers that user to the `UserInfo` database.
  * ```
      {
      "username": "jonh doe",
      "password": "password123!"
      }
* `localhost:8080/api/auth/login`
  * given a `username` and `password` in the json body, it checks if the credentials are correct. If they are correct it returns a JWT token that is needed to use the rest of the endpoints.
  * ```
      {
      "username": "jonh doe",
      "password": "password123!"
      }

* Note: for the following end points they expect a valid JWT Token so the `userId` can be extracted.
### GET

* `localhost:8080/api/documents/getDocuments`
    * gets all document of the user, based on the JWT Token
* `localhost:8080/api/documents/filter?categories=...&fileExtensions=&...&categories=...&fileExtensions=&...&maxDate=...&minDate=...&documentName=...&favorite=...`
    * This will return documents of the user filtering out multiple conditions 
    * note for multiple `categories` or `fileExtensions` filters you need to do separate calls as shown above
    * note the api endpoint does not expect either query parameters shown above
    * will throw an error if no documents are found
* `localhost:8080/api/documents/download/{documentId}`
    * gives a pre-signed url to download the document from the AWS S3 bucket
    * will throw an error if the document is not found in database
* `localhost:8080/api/documents/share/{documentId}?duration=...`
    * gives a pre-signed url to share with a duration of `duration` mins
    * will throw an error if the document is not found in database

### POST

* `localhost:8080/api/documents/upload`
    * saves an uploaded file alongside a `uploadedDocumentData`jsn body
    * ```
      {
      "category": "study",
      "favorite": true
      }
  
    * note: the key name expecting for the file is 'file' and `uploadData` for the json
### PUT

* `localhost:8080/api/documents/update/{documentId}`
    * given a `documentId` and an `UpdatedDocumentData` it updates either `documentName`, `category`, `favorite`, or all 3 depending on the json body
    * ```` 
      {
      "documentName": "dmv_docs",
      "favorite": true,
      "category": "study",
      }
* note: the above shows all three attributes but if only some are being updated you just need to input those attributes into the json body
* will throw an error if the document is not in the database

### DELETE

* `localhost:8080/api/documents/delete/{documentId}`
    * given a `documentId` it will delete that document from the database and the Amazon S3 bucket
    * will throw an error if document is not found in database

## Future Improvements

### Backend

*  add role based actions

### Frontend

* adding in frontend integration via react or other