package nl.esciencecenter.ahn.pointcloud;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.google.common.collect.ImmutableMap;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.setup.Environment;
import nl.esciencecenter.ahn.pointcloud.job.SchedulerConfiguration;
import nl.esciencecenter.ahn.pointcloud.job.XenonConfiguration;
import nl.esciencecenter.ahn.pointcloud.resources.AbstractResource;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.mockito.Mockito.*;

public class ViewerApplicationTest {
    private Environment env;
    private ViewerApplication app;
    private ViewerConfiguration config;

    @Before
    public void setUp() throws MalformedURLException {
        config = getViewerConfiguration();
        env = getEnvironment();
        app = new ViewerApplication();
    }

    private Environment getEnvironment() {
        Environment environment = mock(Environment.class);
        JerseyEnvironment jersey = mock(JerseyEnvironment.class);
        when(environment.jersey()).thenReturn(jersey);
        LifecycleEnvironment lifecycle = mock(LifecycleEnvironment.class);
        when(environment.lifecycle()).thenReturn(lifecycle);
        HealthCheckRegistry healthchecks = mock(HealthCheckRegistry.class);
        when(environment.healthChecks()).thenReturn(healthchecks);
        return environment;
    }

    private ViewerConfiguration getViewerConfiguration() throws MalformedURLException {
        ImmutableMap<String, String> props = ImmutableMap.of();
        SchedulerConfiguration scheduler = new SchedulerConfiguration("local", "/", "multi", props);
        XenonConfiguration xenon = new XenonConfiguration(scheduler, props);
        DataSourceFactory database = new DataSourceFactory();

        ScriptConfiguration scriptConfig = new ScriptConfiguration(28992, "/bin/echo", "ahn2", "/data/jobs", "http://localhost/jobs");
        return new ViewerConfiguration(5, database, xenon, scriptConfig);
    }

    @Test
    public void testRun() throws Exception {
        app.run(config, env);

        verify(env.jersey(), times(2)).register(any(AbstractResource.class));
    }
}