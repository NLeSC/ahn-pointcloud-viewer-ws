package nl.esciencecenter.ahn.pointcloud;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import nl.esciencecenter.ahn.pointcloud.job.XenonConfiguration;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ViewerConfiguration extends Configuration {

    @Valid
    @Range(min=1)
    @JsonProperty
    private long pointsLimit;

    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    @JsonProperty
    private XenonConfiguration xenon;

    @Valid
    @NotNull
    @JsonProperty
    private ScriptConfiguration script;

    private ViewerConfiguration() {
    }

    public ViewerConfiguration(long pointsLimit, DataSourceFactory database, XenonConfiguration xenon, ScriptConfiguration script) {
        this.pointsLimit = pointsLimit;
        this.database = database;
        this.xenon = xenon;
        this.script = script;
    }

    public long getPointsLimit() {
        return pointsLimit;
    }

    public int getSrid() {
        return script.getSrid();
    }

    public DataSourceFactory getDatabase() {
        return database;
    }

    public XenonConfiguration getXenon() {
        return xenon;
    }

    public ScriptConfiguration getScript() {
        return script;
    }
}
