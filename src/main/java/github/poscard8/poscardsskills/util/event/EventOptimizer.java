package github.poscard8.poscardsskills.util.event;


import github.poscard8.poscardsskills.event.CommonEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Used to save performance on certain game events. {@link #handle(String, int)}
 * returns false once every <i>n</i> times. If the method returns true, the wrapper method is stopped.
 * <p>See {@link CommonEvents} for usage.</p>
 */
public class EventOptimizer {

    private static final List<EventOptimizer> VALUES = new ArrayList<>();

    final String name;
    final int period;
    int tick;

    private EventOptimizer(String name, int period) {

        this.name = name;
        this.period = period;
        this.tick = 0;
        VALUES.add(this);
    }

    public static boolean handle(String name, int period) {

        boolean stop;

        Optional<EventOptimizer> optional = VALUES.stream().filter(optimizer -> optimizer.name.equals(name) && optimizer.period == period).findFirst();

        if (optional.isPresent()) {

            EventOptimizer optimizer = optional.get();
            optimizer.tick();
            stop = optimizer.tick != 0;

        } else {

            new EventOptimizer(name, period);
            stop = false;
        }
        return stop;
    }

    private void tick() { tick = (tick + 1) % period; }

}
