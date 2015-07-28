package nl.esciencecenter.ahn.pointcloud.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Joiner;

import nl.esciencecenter.ahn.pointcloud.ScriptConfiguration;
import nl.esciencecenter.xenon.jobs.JobDescription;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.validation.constraints.NotNull;

public class LazRequest extends Selection {

    @NotNull
    @NotBlank
    @Email
    @JsonProperty
    private String email;

    private LazRequest() {
    }

    public LazRequest(Double left, Double bottom, Double right, Double top, String email) {
        super(left, bottom, right, top);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public JobDescription toJobDescription(int level, ScriptConfiguration scriptConfig) {

        // See https://github.com/NLeSC/Massive-PotreeConverter/blob/master/python/create_user_file.py
        // create_user_file.py -s
        // 28992 -e o.rubi@esciencecenter.nl -l 5 -b
        // 116549.99,399108.01,169138.67,444358.73 -d ahn2 -f /data/ahn2_pc_viewer/user_data -w http://131.180.126.49/user_data

        String[] bbox = {
            String.valueOf(getLeft()),
            String.valueOf(getBottom()),
            String.valueOf(getRight()),
            String.valueOf(getTop())
        };

        String[] arguments = {
            "-s",
            String.valueOf(scriptConfig.getSrid()),
            "-e",
            email,
            "-l",
            String.valueOf(level),
            "-b",
            "\"" + Joiner.on(",").join(bbox) + "\"",
            "-d",
            scriptConfig.getDataset(),
            "-f",
            String.valueOf(scriptConfig.getBasePath()),
            "-w",
            String.valueOf(scriptConfig.getBaseUrl())
        };

        JobDescription description = new JobDescription();
        description.setArguments(arguments);
        description.setExecutable(scriptConfig.getExecutable());
        String stderr = scriptConfig.getBasePath() + "/" + Joiner.on("_").join(bbox) + ".err";
        description.setStderr(stderr);
        String stdout = scriptConfig.getBasePath() + "/" + Joiner.on("_").join(bbox) + ".out";
        description.setStdout(stdout);
        return description;
    }
}
