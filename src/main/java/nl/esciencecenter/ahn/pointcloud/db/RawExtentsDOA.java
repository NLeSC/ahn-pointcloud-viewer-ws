package nl.esciencecenter.ahn.pointcloud.db;

import nl.esciencecenter.ahn.pointcloud.core.Selection;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

public interface RawExtentsDOA {
    @SqlQuery("SELECT "
            + "  FLOOR(SUM(LEAST(numberpoints, (numberpoints * (ST_Area(qgeom) /ST_Area(geom)))))) AS numpoints_raw "
            + "FROM "
            + "  extent_raw, "
            + "  (SELECT ST_SetSRID(ST_MakeBox2D(ST_Point(:b.left, :b.bottom),ST_Point(:b.right, :b.top)), :srid) AS qgeom) AS B "
            + "WHERE geom && qgeom")
    long getNumberOfPoints(@BindBean("b") Selection bbox,
                           @Bind("srid") int srid);
}
