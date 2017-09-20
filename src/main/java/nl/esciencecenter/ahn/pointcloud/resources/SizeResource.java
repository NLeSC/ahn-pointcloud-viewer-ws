package nl.esciencecenter.ahn.pointcloud.resources;


import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nl.esciencecenter.ahn.pointcloud.core.Selection;
import nl.esciencecenter.ahn.pointcloud.core.Size;
import nl.esciencecenter.ahn.pointcloud.db.PointCloudStore;
import nl.esciencecenter.ahn.pointcloud.exception.TooManyPoints;

import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("size")
@Produces(MediaType.APPLICATION_JSON)
@Api()
public class SizeResource extends AbstractResource {

    public SizeResource(PointCloudStore store) {
        super(store);
    }

    @POST
    @Timed
    @ApiOperation(value="Retrieve approximate number of points in selection")
    @ApiResponses(value = { @ApiResponse(code = 405, message = "Invalid input") })
    public Size getSizeOfSelection(@Valid Selection selection) throws TooManyPoints {
        return getStore().getApproximateNumberOfPoints(selection);
    }
}
