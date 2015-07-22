package nl.esciencecenter.ahn.pointcloud.db;


import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

public interface PotreeDistancesDOA {
    @SqlQuery("SELECT level " +
            "FROM potree_dist " +
            "WHERE :numpoints_raw*ratio <= :maxpoints " +
            "ORDER BY POWER(:numpoints_raw * ratio - :maxpoints, 2)" +
            "LIMIT 1")
    int getLevel(@Bind("numpoints_raw") long numpoints_raw, @Bind("maxpoints") long maxpoints);

    @SqlQuery("SELECT MAX(level) FROM potree_dist")
    int getMaxLevel();
}
