package nl.esciencecenter.ahn.pointcloud.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
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
        LazRequest request = new LazRequest(1.0, 2.0, 3.0, 4.0, "someone@example.com", 10);

        assertThat(request.getEmail(), is("someone@example.com"));
    }

    @Test
    public void testGetLevel() throws Exception {
        LazRequest request = new LazRequest(1.0, 2.0, 3.0, 4.0, "someone@example.com", 10);

        assertThat(request.getLevel(), is(10));
    }

    @Test
    public void testToJobArguments() throws Exception {
        LazRequest request = new LazRequest(1.0, 2.0, 3.0, 4.0, "someone@example.com", 10);

        String[] expected = {"1.0", "2.0", "3.0", "4.0", "someone@example.com", "10"};

        assertThat(request.toJobArguments(), equalTo(expected));
    }

    @Test
    public void deserializesFromJSON() throws Exception {
        final LazRequest result = MAPPER.readValue(fixture("fixtures/lazrequest.json"), LazRequest.class);

        final LazRequest expected = new LazRequest(124931.360, 484567.840, 126241.760, 485730.400, "someone@example.com", 10);
        assertThat(result, equalTo(expected));
    }

    @Test
    public void testValidation_levelmustbeset_invalid() {
        LazRequest request = new LazRequest(1.0, 2.0, 3.0, 4.0, "someone@example.com", null);

        Set<ConstraintViolation<LazRequest>> violations = validator.validate(request);

        assertThat(violations.size(), equalTo(1));
        ConstraintViolation<LazRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage(), is("may not be null"));
        assertThat(violation.getPropertyPath().toString(), is("level"));
    }
}