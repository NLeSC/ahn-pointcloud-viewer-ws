package nl.esciencecenter.ahn.pointcloud.db;

import nl.esciencecenter.ahn.pointcloud.core.Selection;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

public interface RawExtentsDOA {
    @SqlQuery("SELECT SUM(numberpoints) " +
            "FROM extent_raw " +
            "WHERE geom && ST_SetSRID(ST_MakeBox2D(" +
            "   ST_Point(:b.left, :b.bottom)," +
            "   ST_Point(:b.right, :b.top)" +
            "), :srid)")
    long getNumberOfPoints(@BindBean("b") Selection bbox,
                           @Bind("srid") int srid);
}
