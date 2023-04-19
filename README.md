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
#### 1.4. Editing an existing patient
* PUT request to "/patients"
* request body should containt the patient id and name, id of the GP and boolean value, showing if the patient is insured
#### 1.5. Deleting an existing patient
* DELETE request to "/patients/{id}" where id is the id of the patient

### 2. Doctors
#### 2.1. Getting all doctors
* GET request to "/doctors"
#### 2.2. Getting information about a specific doctor
* GET request to "/doctors/{id}"
#### 2.3. Creating a new doctor
* POST request to "/doctors"
* request body should contain name, birthdate and set of specialties of the doctor
#### 2.4. Editing an existing doctor
* PUT request to "/doctors"
* request body should contain id, name and set of specialties of the doctor
#### 2.5. Deleting an existing doctor
* DELETE request to "/doctors/{id}" where id is the id of the doctor

### 3. Visits
#### 3.1. Getting all visits
* GET request to "/visits"
#### 3.2. Get information about a specific visit
* GET request to "/visits/{id}"
#### 3.3. Creating a new visit
* POST request to "/visits"
* request body should contain id of the patient, id of the doctor and a set of diagnosis IDs
#### 3.4. Editing an existing visit
* PUT request to "/visits"
* request body should contain id of the diagnosis, id of the patient, id of the doctor and a set of diagnosis IDs
#### 3.5. Deleting an existing visit
* DELETE request to "/visits/{id}" where id is the id of the visit

## III. Other functionalities
### 1. Getting list of all insured patients
### 2. Getting the percentage of all non-insured patients
### 3. Getting the total income of all visits
### 4. Getting the income of visit fees of a particular doctor
### 5. Getting the number of visits of a particular patient
### 6. Getting the number of visits by diagnosis
### 7. Getting the number of doctors who have total income greater than a given one
### 8. Getting the total income of all visits by diagnosis
### 9. Getting the total income of visit fees of non-insured patients
### 10. Getting the total income of visit fees of a particular doctor of non-insured patients
