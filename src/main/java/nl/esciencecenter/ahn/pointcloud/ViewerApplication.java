package nl.esciencecenter.ahn.pointcloud;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import nl.esciencecenter.ahn.pointcloud.db.PointCloudStore;
import nl.esciencecenter.ahn.pointcloud.job.XenonSubmitter;
import nl.esciencecenter.ahn.pointcloud.resources.LazResource;
import nl.esciencecenter.ahn.pointcloud.resources.SizeResource;
import nl.esciencecenter.xenon.XenonException;
import org.skife.jdbi.v2.DBI;

public class ViewerApplication extends Application<ViewerConfiguration> {
    public static void main(String[] args) throws Exception {
        new ViewerApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ViewerConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
            new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                new EnvironmentVariableSubstitutor()
            )
        );
    }

    @Override
    public void run(ViewerConfiguration configuration, Environment environment) throws Exception {
        final PointCloudStore store = createStores(configuration, environment);

        registerResources(configuration, environment, store);
    }

    private PointCloudStore createStores(ViewerConfiguration configuration, Environment environment) {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDatabase(), "postgresql");
        return new PointCloudStore(jdbi, configuration.getSrid(), configuration.getPointsLimit());
    }

    private void registerResources(ViewerConfiguration configuration, Environment environment, PointCloudStore store) throws XenonException {
        final SizeResource sizeResource = new SizeResource(store);
        environment.jersey().register(sizeResource);

        final XenonSubmitter submitter = new XenonSubmitter(configuration.getXenon());
        final ScriptConfiguration scriptConfig = configuration.getScript();
        final LazResource lazResource = new LazResource(store, submitter, scriptConfig);
        environment.jersey().register(lazResource);
    }
}
