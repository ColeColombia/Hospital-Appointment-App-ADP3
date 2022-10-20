package za.ac.cput.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.domain.*;
import za.ac.cput.factory.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class PatientGenderControllerTest {
    @LocalServerPort
    private int port;
    private String SECURITY_USERNAME = "admin";
    private String SECURITY_PASSWORD = "passwordAdmin";

    @Autowired
    private TestRestTemplate restTemplate;

    private static Patient patient= PatientFactory.createPatient("Rhegan", "Fortuin", "19 August 2000", "password");
    private static Gender gender =  GenderFactory.createGender("Rhegan", "Born a male on the 19th of August in the year 2000", "Male");
    private static PatientGender patientGender = PatientGenderFactory.createPatientGender(patient, gender);

    private static String baseUrl;

    @BeforeEach
    void setUp() {
        String urlPatient = "http://localhost:"+ this.port + "/hospital_appointment_management-db/patient/save";
        ResponseEntity<Patient> responsePatient = this.restTemplate.withBasicAuth(SECURITY_USERNAME, SECURITY_PASSWORD).postForEntity(urlPatient, this.patient, Patient.class);
        String urlGender = "http://localhost:"+ this.port + "/hospital_appointment_management-db/gender/save";
        ResponseEntity<Gender> responseGender = this.restTemplate.withBasicAuth(SECURITY_USERNAME, SECURITY_PASSWORD).postForEntity(urlGender, this.gender, Gender.class);
        String urlPatientGender = "http://localhost:"+ this.port + "/hospital_appointment_management-db/patientGender/save";
        ResponseEntity<PatientGender> responsePatientGender = this.restTemplate.withBasicAuth(SECURITY_USERNAME, SECURITY_PASSWORD).postForEntity(urlPatientGender, this.patientGender, PatientGender.class);


        this.baseUrl = "http://localhost:"+ this.port + "/hospital_appointment_management-db/patientGender/";
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void a_save() {
        String url = baseUrl + "save";
        System.out.println(url);
        ResponseEntity<PatientGender> response = this.restTemplate
                .withBasicAuth(SECURITY_USERNAME, SECURITY_PASSWORD)
                .postForEntity(url, this.patientGender, PatientGender.class);
        System.out.println(response);
        assertAll(
                ()-> assertEquals(HttpStatus.OK, response.getStatusCode()),
                ()-> assertNotNull(response.getBody())
        );
    }

    @Test
    void b_read() {
        String url = baseUrl+"read/" + this.patientGender.getPatientGenderID();
        System.out.println(url);
        ResponseEntity<PatientGender> response = this.restTemplate
                .withBasicAuth(SECURITY_USERNAME, SECURITY_PASSWORD)
                .getForEntity(url, PatientGender.class);
        assertAll(
                ()-> assertEquals(HttpStatus.OK, response.getStatusCode()),
                ()-> assertNotNull(response.getBody())
        );
    }

    @Test
    void c_getAll() {
        String url = baseUrl + "all";
        System.out.println(url);
        ResponseEntity<PatientGender[]> response = this.restTemplate
                .withBasicAuth(SECURITY_USERNAME, SECURITY_PASSWORD)
                .getForEntity(url, PatientGender[].class);
        System.out.println(Arrays.asList(response.getBody()));
        assertAll(
                ()-> assertEquals(HttpStatus.OK, response.getStatusCode()),
                ()-> assertTrue(response.getBody().length == 1)
        );
    }

    @Test
    void d_delete() {
        String url = baseUrl + "delete/" + this.patientGender.getPatientGenderID();
        this.restTemplate
                .withBasicAuth(SECURITY_USERNAME, SECURITY_PASSWORD)
                .delete(url);
    }
}