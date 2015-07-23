package nl.esciencecenter.ahn.pointcloud.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class Size {
    @NotNull
    @Range(min=0)
    private long rawPoints = 0;

    @NotNull
    @Range(min=0)
    private long returnedPoints = 0;

    @NotNull
    @Range(min=0, max=24)
    private int level;

    private Size() {
    }

    public Size(long rawPoints, long returnedPoints, int level) {
        this.rawPoints = rawPoints;
        this.returnedPoints = returnedPoints;
        this.level = level;
    }

    /**
     * @return Number of points of selection based on 100% of the available points.
     */
    public long getRawPoints() {
        return rawPoints;
    }

    /**
     * @return Number of points which can be returned when maximum of points is taken into account.
     */
    public long getReturnedPoints() {
        return returnedPoints;
    }

    /**
     * @return Level in octree at which the returned points will be taken from.
     */
    public int getLevel() {
        return level;
    }

    /**
     * @return Ratio of returned points vs all points
     */
    @JsonProperty("coverage")
    public float getCoverage() {
        if (rawPoints == 0) {
            return 1;
        }
        return (float) returnedPoints / (float) rawPoints;
    }

    @JsonProperty("coverage")
    public void setCoverage(float coverage) {
        // dummy setter, as it is a computed property
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        Size size = (Size) o;
        return Objects.equals(rawPoints, size.rawPoints) &&
                Objects.equals(returnedPoints, size.returnedPoints)  &&
                Objects.equals(level, size.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rawPoints, returnedPoints, level);
    }


    @Override
    public String toString() {
        return "Size{" +
            "rawPoints=" + rawPoints +
            ", returnedPoints=" + returnedPoints +
            ", level=" + level +
            '}';
    }
}
