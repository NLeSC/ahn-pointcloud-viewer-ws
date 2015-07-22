package nl.esciencecenter.ahn.pointcloud.db;

import nl.esciencecenter.ahn.pointcloud.core.Selection;
import nl.esciencecenter.ahn.pointcloud.core.Size;
import nl.esciencecenter.ahn.pointcloud.exception.TooManyPoints;
import org.skife.jdbi.v2.DBI;

public class PointCloudStore {
    private final DBI dbi;
    private final long pointsLimit;
    private final int octreeLevels;
    private final int srid;

    public PointCloudStore(DBI dbi, int srid, long pointsLimit) {
        this.dbi = dbi;
        this.srid = srid;
        this.pointsLimit = pointsLimit;
        this.octreeLevels = dbi.onDemand(PotreeExtentsDOA.class).getMaxLevel();
    }

    /**
     * Retrieve approximate number of points within selection.
     *
     * @param selection Selection in a pointcloud
     * @return number of points
     */
    public Size getApproximateNumberOfPoints(Selection selection) throws TooManyPoints {
        RawExtentsDOA tiles = dbi.onDemand(RawExtentsDOA.class);
        long points = tiles.getApproximateNumberOfPoints(
                selection.getLeft(),
                selection.getBottom(),
                selection.getRight(),
                selection.getTop(),
                srid
                );

        // TODO calculate fraction between area of requested selection and area of selected tiles
        // can be used to interpolate a better number of points

        return new Size(points, pointsLimit, octreeLevels);
    }

}
