package github.poscard8.poscardsskills.module;

import net.minecraftforge.eventbus.api.IEventBus;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class PSModules {

    private PSModules() {}

    static List<Module> VALUES = new ArrayList<>();

    public static final BaseModule BASE = new BaseModule();
    public static final DecorativeBlocksModule DECORATIVE_BLOCKS = new DecorativeBlocksModule();
    public static final BrilliantUtilitiesModule BRILLIANT_UTILITIES = new BrilliantUtilitiesModule();
    public static final BrilliantGearModule BRILLIANT_GEAR = new BrilliantGearModule();

    public static void onSetup(IEventBus bus) { VALUES.forEach(module -> module.onSetup(bus)); }

}
