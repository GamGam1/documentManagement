# documentManagement

# Backend
A RESTful API built with Spring Boot for managing documents. Supports creating, updating, deleting, and filtering document data.

## Features
* CRUD operations

* Filtering documents by multiple categories, upload date, document name, and file extension.

* Input Validation

* JSON-based API requests and responses

* uploading and deleting to an AWS S3 bucket

## Tech Stack
* Java 24
* Spring Boot
* Spring Data JPA
* Postgres SQL
* Maven
* AWS S3
* Postman (API Testing)

## Starting Guide
* PreReqs
    * Java 24
    * Maven
    * Intellji (or ide of your choice, but instructions will assume you are using Intellji)
    * Database set up 
    * AWS S3 Account
* Connecting to Database
    * Make an `application.properties` file in the resource `backend/src/main/resources` folder, and copy the lines provided in the `application-properties-template.txt` file. Fill in the blanks.
    *  Run the application to make sure it is all working

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


## API Endpoints

### GET

* `localhost:8080/api/{userId}/document`
    * gets all document of the user
    * will throw an error if `userId` is not in the database
* `localhost:8080/api/{user}/filter?categories=...&fileExtensions=&...&categories=...&fileExtensions=&...&maxDate=...&minDate=...&documentName=...&favorite=...`
    * This will return documents of the user filtering out multiple conditions 
    * note for multiple `categories` or `fileExtensions` filters you need to do separate calls as shown above
    * note the api endpoint does not expect either query parameters shown above
    * will throw an error if no documents are found
* `localhost:8080/api/{userId}/document/{documentId}/download`
    * gives a pre-signed url to download the document from the AWS S3 bucket
    * will throw an error if the document is not found in database
* `localhost:8080/api/{userId}/document/{documentId}/share?duration=...`
    * gives a pre-signed url to share with a duration of `duration` mins
    * will throw an error if the document is not found in database

### POST

* `localhost:8080/api/{userId}/upload`
    * saves an uploaded file alongside a `uploadedDocumentData`jsn body
    * ```
      {
      "category": "study",
      "favorite": true
      }
  
    * note: the key name expecting for the file is 'file' and `uploadData` for the json
### PUT

* `localhost:8080/api/{userId}/{documentId}/update`
    * given a `userid`, `documentId` and an `UpdatedDocumentData` it updates either `documentName`, `category`, `favorite`, or all 3 depending on the json body
    * ```` 
      {
      "documentName": "dmv_docs",
      "favorite": true,
      "category": "study",
      }
* note: the above shows all three attributes but if only some are being updated you just need to input those attributes into the json body
* will throw an error if the document is not in the database

### DELETE

* `localhost:8080/api/{userId}/document/{documentId}/delete`
    * given an `userId` and `documentId` it will delete that document from the database and the Amazon S3 bucket
    * will throw an error if document is not found in database

## Future Improvements

### Backend

*  add User authentication (JWT)

### Frontend

* adding in frontend integration via react or other