CREATE TABLE doctor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    timezone VARCHAR(50) NOT NULL
);

CREATE INDEX idx_doctor_first_name ON doctor(first_name);
CREATE INDEX idx_doctor_last_name ON doctor(last_name);

CREATE TABLE patient (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL
);

CREATE INDEX idx_patient_first_name ON patient(first_name);
CREATE INDEX idx_patient_last_name ON patient(last_name);


CREATE TABLE visit (
    id INT AUTO_INCREMENT PRIMARY KEY,
    start_date_time DATETIME NOT NULL,
    end_date_time DATETIME NOT NULL,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    CONSTRAINT fk_visit_patient FOREIGN KEY (patient_id) REFERENCES patient(id),
    CONSTRAINT fk_visit_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(id)
);

CREATE INDEX idx_visit_doctor_id_start ON visit(doctor_id, start_date_time);
CREATE INDEX idx_visit_patient_id_start ON visit(patient_id, start_date_time);
CREATE INDEX idx_visit_doctor_patient_start ON visit(doctor_id, patient_id, start_date_time);
