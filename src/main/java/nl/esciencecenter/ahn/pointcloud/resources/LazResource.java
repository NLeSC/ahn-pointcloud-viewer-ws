package nl.esciencecenter.ahn.pointcloud.resources;

import com.codahale.metrics.annotation.Timed;
import nl.esciencecenter.ahn.pointcloud.ScriptConfiguration;
import nl.esciencecenter.ahn.pointcloud.core.LazRequest;
import nl.esciencecenter.ahn.pointcloud.core.Size;
import nl.esciencecenter.ahn.pointcloud.db.PointCloudStore;
import nl.esciencecenter.ahn.pointcloud.job.XenonSubmitter;
import nl.esciencecenter.xenon.XenonException;

import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("laz")
@Produces(MediaType.APPLICATION_JSON)
public class LazResource extends AbstractResource {
    private final ScriptConfiguration scriptConfig;
    private final XenonSubmitter submitter;

    public LazResource(PointCloudStore store, XenonSubmitter submitter, ScriptConfiguration scriptConfig) {
        super(store);
        this.submitter = submitter;
        this.scriptConfig = scriptConfig;
    }

    @POST
    @Timed
    public Size submitSelection(@Valid LazRequest request) throws XenonException {
        Size size = getStore().getApproximateNumberOfPoints(request);

        // Submit as Xenon job
        submitter.submit(
            request.toJobDescription(size.getLevel(), scriptConfig)
        );

        return size;
    }
}
