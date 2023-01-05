package api.errors;

import java.util.ArrayList;
import java.util.List;

public class MultipleFailuresError extends Error {
    private final String heading;
    private final List<Throwable> failures = new ArrayList<>();

    public MultipleFailuresError(String heading, List<? extends Throwable> failures) {
        if (failures == null)
            throw new NullPointerException("failures must not be null");

        this.heading = heading == null || heading.trim().length() == 0 ? "Multiple Failures" : heading.trim();

        for (Throwable failure : failures) {
            if (failure == null)
                throw new NullPointerException("failures must not contain null elements");

            this.failures.add(failure);
        }
    }
}
