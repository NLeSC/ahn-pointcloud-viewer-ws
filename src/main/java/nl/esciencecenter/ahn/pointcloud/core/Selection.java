package nl.esciencecenter.ahn.pointcloud.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import nl.esciencecenter.ahn.pointcloud.validation.ValidSelection;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@ValidSelection
public class Selection {
    @NotNull
    @JsonProperty
    @ApiModelProperty(value="Most left or minimum x coordinate", example = "124931.360")
    private Double left;

    @NotNull
    @JsonProperty
    @ApiModelProperty(value="Most bottom or minimum y coordinate", example = "484567.840")
    private Double bottom;

    @NotNull
    @JsonProperty
    @ApiModelProperty(value="Most right or maximum x coordinate", example = "126241.760")
    private Double right;

    @NotNull
    @JsonProperty
    @ApiModelProperty(value="Most top or maximum x coordinate", example = "485730.400")
    private Double top;

    public Selection(Double left, Double bottom, Double right, Double top) {
        this.left = left;
        this.bottom = bottom;
        this.right = right;
        this.top = top;
    }

    protected Selection() {
    }

    public Double getLeft() {
        return left;
    }

    public Double getBottom() {
        return bottom;
    }

    public Double getRight() {
        return right;
    }

    public Double getTop() {
        return top;
    }

    public void setLeft(Double left) {
        this.left = left;
    }

    public void setBottom(Double bottom) {
        this.bottom = bottom;
    }

    public void setRight(Double right) {
        this.right = right;
    }

    public void setTop(Double top) {
        this.top = top;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        Selection selection = (Selection) o;
        return Objects.equals(getLeft(), selection.getLeft()) &&
                Objects.equals(getBottom(), selection.getBottom()) &&
                Objects.equals(getRight(), selection.getRight()) &&
                Objects.equals(getTop(), selection.getTop());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLeft(), getBottom(), getRight(), getTop());
    }
}
