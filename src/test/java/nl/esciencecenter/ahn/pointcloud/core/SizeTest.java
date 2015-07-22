package nl.esciencecenter.ahn.pointcloud.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.hamcrest.CoreMatchers.equalTo;

import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;

public class SizeTest {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void serializesToJSON() throws Exception {
        final Size input = new Size(10193813L, 9234324L, 8);

        final String result = MAPPER.writeValueAsString(input);

        final String expected = MAPPER.writeValueAsString(MAPPER.readValue(fixture("fixtures/size.json"), Size.class));
        assertThat(result, equalTo(expected));
    }

    @Test
    public void getCoverage() {
        final Size size = new Size(10193813L, 9234324L, 8);

        assertThat((double) size.getCoverage(), closeTo(0.9058, 0.0001));
    }

    @Test
    public void getCoverage_zeropoints_fullcoverage() {
        final Size size = new Size(0, 0, 13);

        assertThat((double) size.getCoverage(), closeTo(1, 0.0001));
    }

    @Test
    public void toStringTest() {
        final Size size = new Size(10193813L, 9234324L, 8);

        String expected = "Size{rawPoints=10193813, returnedPoints=9234324, level=8}";
        assertThat(size.toString(), equalTo(expected));
    }
}