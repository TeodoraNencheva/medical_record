insert into doctor (name, birth_date)
values ("Ivan Ivanov", '1980-10-31');

insert into general_practitioner (id)
values (1);

insert into specialty (name)
values ("Cardiologist"),
       ("Paediatrics"),
       ("Oncology"),
       ("Pathology");

insert into doctors_specialties (doctor_id, specialty_id)
values (1, 1);

insert into patient (name, gp_id, insured)
values ("Petya Petrova", 1, 1);

insert into fee_history (price, effective_from)
values (2.90, '2000-01-01');

insert into diagnosis (name, description)
values ("healty", "patient is healthy"),
       ("broken leg", "broken leg"),
       ("broken arm", "broken arm");