package github.poscard8.poscardsskills.util.event;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Used to save performance on certain game events.
 * See {@link github.poscard8.poscardsskills.event.CommonEvents} for usage.
 */
public class EventOptimizer {

    private static final List<EventOptimizer> VALUES = new ArrayList<>();

    private final String name;
    private final int period;
    private int tick;

    private EventOptimizer(String name, int period) {

        this.name = name;
        this.period = period;
        this.tick = 0;
        VALUES.add(this);
    }

    public static boolean handle(String name, int period) {

        boolean throwsError;

        Optional<EventOptimizer> optional = VALUES.stream().filter(optimizer -> optimizer.name.equals(name) && optimizer.period == period).findFirst();

        if (optional.isPresent()) {

            EventOptimizer optimizer = optional.get();
            optimizer.tick();
            throwsError = optimizer.tick != 0;

        } else {

            new EventOptimizer(name, period);
            throwsError = false;
        }
        return throwsError;
    }

    private void tick() { tick = (tick + 1) % period; }

}
