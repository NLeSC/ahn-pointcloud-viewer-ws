package nl.esciencecenter.ahn.pointcloud;


import com.google.common.collect.ImmutableMap;
import io.dropwizard.db.DataSourceFactory;
import nl.esciencecenter.ahn.pointcloud.job.SchedulerConfiguration;
import nl.esciencecenter.ahn.pointcloud.job.XenonConfiguration;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ViewerConfigurationTest {
    private ViewerConfiguration config;
    private XenonConfiguration xenon;
    private DataSourceFactory database;
    private ScriptConfiguration scriptConfig;

    @Before
    public void setUp() {
        ImmutableMap<String, String> props = ImmutableMap.of("somepropkey", "somepropvalue");
        SchedulerConfiguration scheduler = new SchedulerConfiguration("ssh", "someone@somewhere:2222", "multi", props);
        xenon = new XenonConfiguration(scheduler, props);
        database = new DataSourceFactory();
        scriptConfig = new ScriptConfiguration(28992, "/bin/echo", "ahn2", "/data/jobs", "http://localhost/jobs");
        config = new ViewerConfiguration(5, database, xenon, scriptConfig);
    }

    @Test
    public void testGetMaximumNumberOfPoints() throws Exception {
        assertThat(config.getPointsLimit(), is(5L));
    }

    @Test
    public void testGetSrid() throws Exception {
        assertThat(config.getSrid(), is(28992));
    }

    @Test
    public void testGetDatabase() throws Exception {
        assertThat(config.getDatabase(), is(database));
    }

    @Test
    public void testGetXenon() throws Exception {
        assertThat(config.getXenon(), is(xenon));
    }

    @Test
    public void testGetScript() throws Exception {
        assertThat(config.getScript(), is(scriptConfig));
    }
}