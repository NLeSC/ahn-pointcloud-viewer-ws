package nl.esciencecenter.ahn.pointcloud.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import nl.esciencecenter.ahn.pointcloud.ScriptConfiguration;
import nl.esciencecenter.xenon.jobs.JobDescription;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LazRequestTest  {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testGetEmail() throws Exception {
        LazRequest request = new LazRequest(1.0, 2.0, 3.0, 4.0, "someone@example.com");

        assertThat(request.getEmail(), is("someone@example.com"));
    }

    @Test
    public void testToJobDescription() throws Exception {
        LazRequest request = new LazRequest(1.0, 2.0, 3.0, 4.0, "someone@example.com");
        ScriptConfiguration scriptConfig = new ScriptConfiguration(28992, "/bin/echo", "ahn2", "/data/jobs", "http://localhost/jobs");

        JobDescription result = request.toJobDescription(8, scriptConfig);

        JobDescription expected = new JobDescription();
        expected.setExecutable("/bin/echo");
        String[] expectedArguments = {
            "-s", "28992", "-e", "someone@example.com", "-l", "8", "-b", "\"1.0 2.0 3.0 4.0\"", "-d", "ahn2", "-f", "/data/jobs", "-w", "http://localhost/jobs"
        };
        expected.setArguments(expectedArguments);
        expected.setStderr("/data/jobs/1.0_2.0_3.0_4.0.err");
        expected.setStdout("/data/jobs/1.0_2.0_3.0_4.0.out");
        assertThat(result, equalTo(expected));
    }

    @Test
    public void deserializesFromJSON() throws Exception {
        final LazRequest result = MAPPER.readValue(fixture("fixtures/lazrequest.json"), LazRequest.class);

        final LazRequest expected = new LazRequest(124931.360, 484567.840, 126241.760, 485730.400, "someone@example.com");
        assertThat(result, equalTo(expected));
    }

    @Test
    public void testValidation_emailmustbefilled_invalid() {
        LazRequest request = new LazRequest(1.0, 2.0, 3.0, 4.0, "");

        Set<ConstraintViolation<LazRequest>> violations = validator.validate(request);

        assertThat(violations.size(), equalTo(1));
        ConstraintViolation<LazRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage(), is("may not be empty"));
        assertThat(violation.getPropertyPath().toString(), is("email"));
    }
}