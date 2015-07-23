package nl.esciencecenter.ahn.pointcloud.db;

import nl.esciencecenter.ahn.pointcloud.core.Selection;
import nl.esciencecenter.ahn.pointcloud.core.Size;
import org.skife.jdbi.v2.DBI;

public class PointCloudStore {
    private final DBI dbi;
    private final long pointsLimit;
    private final int srid;

    public PointCloudStore(DBI dbi, int srid, long pointsLimit) {
        this.dbi = dbi;
        this.srid = srid;
        this.pointsLimit = pointsLimit;
    }

    /**
     * Retrieve approximate number of points within selection.
     *
     * @param selection Selection in a pointcloud
     * @return number of points
     */
    public Size getApproximateNumberOfPoints(Selection selection) {
        RawExtentsDOA rawExtents = dbi.onDemand(RawExtentsDOA.class);
        PotreeExtentsDOA potreeExtents = dbi.onDemand(PotreeExtentsDOA.class);
        PotreeDistancesDOA potreeDistances = dbi.onDemand(PotreeDistancesDOA.class);

        long rawPoints = rawExtents.getNumberOfPoints(selection, srid);
        long returnedPoints = rawPoints;
        int level = potreeDistances.getMaxLevel() + 1;

        if (rawPoints > pointsLimit) {
            level = potreeDistances.getLevel(rawPoints, pointsLimit);
            returnedPoints = potreeExtents.getNumberOfPoints(level, selection, srid);
        }

        return new Size(rawPoints, returnedPoints, level);
    }

}
