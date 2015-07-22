package nl.esciencecenter.ahn.pointcloud.validation;

import nl.esciencecenter.ahn.pointcloud.core.Selection;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

public class ValidSelectionValidatorTest {
    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testIsValid_valid() throws Exception {
        Selection selection = new Selection(1.0, 2.0, 3.0, 4.0);

        Set<ConstraintViolation<Selection>> contraintValidations = validator.validate(selection);

        assertThat(contraintValidations.isEmpty(), is(true));
    }

    @Test
    public void testIsValid_leftbiggerthanright_swappedthem() throws Exception {
        Selection selection = new Selection(3.0, 2.0, 1.0, 4.0);

        Set<ConstraintViolation<Selection>> contraintValidations = validator.validate(selection);

        assertThat(contraintValidations.isEmpty(), is(true));
        Selection expected = new Selection(1.0, 2.0, 3.0, 4.0);
        assertThat(selection, equalTo(expected));
    }

    @Test
    public void testIsValid_topbiggerthanbottom_swappedthem() throws Exception {
        Selection selection = new Selection(1.0, 4.0, 3.0, 2.0);

        Set<ConstraintViolation<Selection>> contraintValidations = validator.validate(selection);

        assertThat(contraintValidations.isEmpty(), is(true));
        Selection expected = new Selection(1.0, 2.0, 3.0, 4.0);
        assertThat(selection, equalTo(expected));
    }

    @Test
    public void testIsValid_null_valid() {
        Selection selection = new Selection(null, null, null, null);

        Set<ConstraintViolation<Selection>> contraintValidations = validator.validate(selection);

        // null violations
        assertThat(contraintValidations.size(), is(4));
    }

}