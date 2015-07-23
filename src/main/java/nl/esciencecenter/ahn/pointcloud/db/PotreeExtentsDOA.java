package nl.esciencecenter.ahn.pointcloud.db;


import nl.esciencecenter.ahn.pointcloud.core.Selection;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

public interface PotreeExtentsDOA {
    @SqlQuery("SELECT "
            + "  FLOOR(SUM(LEAST(numberpoints, (numberpoints * (ST_Area(qgeom) /ST_Area(geom)))))) AS numpoints_raw "
            + "FROM "
            + "  extent_potree, "
            + "  (SELECT ST_SetSRID(ST_MakeBox2D(ST_Point(:left, :bottom),ST_Point(:right, :top)), :srid) AS qgeom) AS B "
            + "WHERE level = :level "
            + "AND geom && qgeom")
    long getNumberOfPoints(@Bind("level") int level,
                           @BindBean("b") Selection bbox,
                           @Bind("srid") int srid);
}
