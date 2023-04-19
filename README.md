# Medical Records - REST API built with Spring Boot
## The application handles the process of registering visits of doctors and patients. Each doctor can have one or more specialties and can be a general practitioner. Each patient can have a GP or have no GP. Visits have a doctor, a patient, a fee and one or more diagnoses.

## I. Database model
![database model](https://user-images.githubusercontent.com/79698998/233052669-d5c776e8-017b-4d2a-943a-8b4dc5412a90.PNG)

## II. CRUD operations - all requests can be tested via the 'Medical Records postman' file in the repository
### 1. Patients
#### 1.1. Getting all patients
* GET request to "/patients"
#### 1.2. Getting information about specific patient
* GET request to "/patients/{id}" where id is the id of the patient
#### 1.3. Creating a new patient
* POST request to "/patients"
* request body should containt patient name, id of the GP and a boolean value, showing if the patient is insured
* request body example: {"name": "John Doe", "gpId": 1, "insured": true}
#### 1.4. Editing an existing patient
* PUT request to "/patients"
* request body should containt the patient id and name, id of the GP and boolean value, showing if the patient is insured
* request body example:  {"id": 1, "name": "John Doe", "gpId": 1, "insured": true}
#### 1.5. Deleting an existing patient
* DELETE request to "/patients/{id}" where id is the id of the patient
