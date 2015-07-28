package nl.esciencecenter.ahn.pointcloud.resources;

import nl.esciencecenter.ahn.pointcloud.ScriptConfiguration;
import nl.esciencecenter.ahn.pointcloud.core.LazRequest;
import nl.esciencecenter.ahn.pointcloud.core.Size;
import nl.esciencecenter.ahn.pointcloud.db.PointCloudStore;
import nl.esciencecenter.ahn.pointcloud.job.XenonSubmitter;
import nl.esciencecenter.xenon.XenonException;
import nl.esciencecenter.xenon.jobs.JobDescription;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;


public class LazResourceTest {
    private PointCloudStore store;
    private XenonSubmitter xenon;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        store = mock(PointCloudStore.class);
        xenon = mock(XenonSubmitter.class);
    }

    @Test
    public void submitSelection_jobsubmitted() throws XenonException {
        LazRequest request = new LazRequest(124931.360, 484567.840, 126241.760, 485730.400, "someone@example.com");
        Size size = new Size(10193813L, 9234324L, 8);
        when(store.getApproximateNumberOfPoints(request)).thenReturn(size);
        ScriptConfiguration scriptConfig = new ScriptConfiguration(28992, "/bin/echo", "ahn2", "/data/jobs", "http://localhost/jobs");
        LazResource resource = new LazResource(store, xenon, scriptConfig);

        resource.submitSelection(request);

        ArgumentCaptor<JobDescription> argument = ArgumentCaptor.forClass(JobDescription.class);
        verify(xenon, times(1)).submit(argument.capture());
        JobDescription submittedDescription = argument.getValue();
        assertThat(submittedDescription.getExecutable(), is("/bin/echo"));
        String[] expectedArguments = {
            "-s", "28992", "-e", "someone@example.com", "-l", "8", "-b", "\"124931.36 484567.84 126241.76 485730.4\"", "-d", "ahn2", "-f", "/data/jobs", "-w", "http://localhost/jobs"
        };
        assertThat(submittedDescription.getArguments(), equalTo(Arrays.asList(expectedArguments)));
    }
}