package nl.esciencecenter.ahn.pointcloud.db;

import nl.esciencecenter.ahn.pointcloud.core.Selection;
import nl.esciencecenter.ahn.pointcloud.core.Size;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PointCloudStoreTest {
    private DBI dbi;
    private final int srid = 28992;
    private final long pointsLimit = 10000000;
    private PointCloudStore store;
    private RawExtentsDOA rawExtents;
    private PotreeExtentsDOA potreeExtents;
    private PotreeDistancesDOA potreeDistances;

    @Before
    public void setUp() throws Exception {
        dbi = mock(DBI.class);

        // create mocked sql objects
        rawExtents = mock(RawExtentsDOA.class);
        when(dbi.onDemand(RawExtentsDOA.class)).thenReturn(rawExtents);
        potreeExtents = mock(PotreeExtentsDOA.class);
        when(dbi.onDemand(PotreeExtentsDOA.class)).thenReturn(potreeExtents);
        potreeDistances = mock(PotreeDistancesDOA.class);
        when(dbi.onDemand(PotreeDistancesDOA.class)).thenReturn(potreeDistances);

        store = new PointCloudStore(dbi, srid, pointsLimit);
    }

    @Test
    public void testGetApproximateNumberOfPoints_abovelimit_usepotreeextents() {
        int level = 8;
        Selection selection = new Selection(124931.360, 484567.840, 126241.760, 485730.400);

        when(rawExtents.getNumberOfPoints(selection, srid)).thenReturn(10193813L);
        when(potreeDistances.getMaxLevel()).thenReturn(13);
        when(potreeDistances.getLevel(10193813L, pointsLimit)).thenReturn(level);
        when(potreeExtents.getNumberOfPoints(level, selection, srid)).thenReturn(9234324L);

        Size result = store.getApproximateNumberOfPoints(selection);

        Size expected = new Size(10193813L, 9234324L, level);
        assertThat(result, equalTo(expected));
    }

    @Test
    public void testGetApproximateNumberOfPoints_belowlimit_userawextents() {
        Selection selection = new Selection(124931.360, 484567.840, 126241.760, 485730.400);

        when(rawExtents.getNumberOfPoints(selection, srid)).thenReturn(9234324L);
        when(potreeDistances.getMaxLevel()).thenReturn(13);

        Size result = store.getApproximateNumberOfPoints(selection);

        Size expected = new Size(9234324L, 9234324L, 14);
        assertThat(result, equalTo(expected));
    }
}