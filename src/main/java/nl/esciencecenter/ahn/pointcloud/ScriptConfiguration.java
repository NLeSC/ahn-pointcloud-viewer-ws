package nl.esciencecenter.ahn.pointcloud;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ScriptConfiguration extends Configuration {
    @Valid
    @NotNull
    @JsonProperty
    private int srid;

    @NotEmpty
    @JsonProperty
    private String executable;

    @NotEmpty
    @JsonProperty
    private String dataset;

    @NotEmpty
    @JsonProperty
    private String basePath;

    @NotEmpty
    @JsonProperty
    private String baseUrl;

    public ScriptConfiguration() {
    }

    public ScriptConfiguration(int srid, String executable, String dataset, String basePath, String baseUrl) {
        this.srid = srid;
        this.executable = executable;
        this.dataset = dataset;
        this.basePath = basePath;
        this.baseUrl = baseUrl;
    }

    public int getSrid() {
        return srid;
    }

    public String getExecutable() {
        return executable;
    }

    public String getDataset() {
        return dataset;
    }

    public String getBasePath() {
        return basePath;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
