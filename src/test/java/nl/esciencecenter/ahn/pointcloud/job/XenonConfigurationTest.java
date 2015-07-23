package nl.esciencecenter.ahn.pointcloud.job;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Map;

public class XenonConfigurationTest {
    private XenonConfiguration config;
    private SchedulerConfiguration scheduler;
    private ImmutableMap<String,String> props;

    @Before
    public void setUp() {
        props = ImmutableMap.of("somepropkey", "somepropvalue");
        scheduler = new SchedulerConfiguration("ssh", "someone@somewhere:2222", "multi", props);
        config = new XenonConfiguration(scheduler, props);
    }

    @Test
    public void testGetScheduler() throws Exception {
        assertThat(config.getScheduler(), equalTo(scheduler));
    }

    @Test
    public void testGetProperties() throws Exception {
        assertThat(config.getProperties(), equalTo((Map<String, String>) props));
    }
}