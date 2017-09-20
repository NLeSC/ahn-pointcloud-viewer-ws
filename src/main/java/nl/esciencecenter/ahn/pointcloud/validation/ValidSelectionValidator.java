package nl.esciencecenter.ahn.pointcloud.validation;

import nl.esciencecenter.ahn.pointcloud.core.Selection;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidSelectionValidator implements ConstraintValidator<ValidSelection, Selection> {


    @Override
    public void initialize(ValidSelection constraintAnnotation) {
        // Do nothing, because validator does not need initialization
    }

    @Override
    public boolean isValid(Selection value, ConstraintValidatorContext context) {
        // OK if left < right + bottom < top
        if (value.getBottom() == null || value.getLeft() == null || value.getRight() == null || value.getTop() == null) {
            // unable to validate ranges with nulls, let field validations trigger violations
            return true;
        }

        // swap corners of selection if needed
        if (value.getLeft() > value.getRight()) {
            Double oldRight = value.getRight();
            value.setRight(value.getLeft());
            value.setLeft(oldRight);
        }
        if (value.getBottom() > value.getTop()) {
            Double oldTop = value.getTop();
            value.setTop(value.getBottom());
            value.setBottom(oldTop);
        }

        return true;
    }
}
