package github.poscard8.poscardsskills.module;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * The mod is split into 4 modules for better user configuration.
 * The 4 modules are {@link BaseModule}, {@link DecorativeBlocksModule}, {@link BrilliantUtilitiesModule} and {@link BrilliantGearModule}.
 * The class uses some reflection since there is no simple way to use mixins on Forge classes.
 */
public abstract class Module {

    protected String configKey;

    Module(@Nullable String configKey) {

        this.configKey = configKey;
        PSModules.VALUES.add(this);
    }

    protected static <T> void clearAllEntries(IForgeRegistry<T> iForgeRegistry, DeferredRegister<T> deferredRegister) {

        if (iForgeRegistry instanceof ForgeRegistry<T> forgeRegistry) clearAllEntries(forgeRegistry, deferredRegister);
    }

    protected static <T> void clearAllEntries(ForgeRegistry<T> forgeRegistry, DeferredRegister<T> deferredRegister) {

        Field[] fields = forgeRegistry.getClass().getDeclaredFields();
        Field isModifiable = null;

        for (Field field : fields) {

            if (field.getName().equals("isModifiable")) {

                isModifiable = field;
                break;
            }
        }

        try {

            if (isModifiable != null) {

                isModifiable.setAccessible(true);
                isModifiable.set(forgeRegistry, true);
                isModifiable.setAccessible(false);
            }

        } catch (IllegalAccessException e) { throw new RuntimeException(e); }

        List<ResourceLocation> keySet = deferredRegister.getEntries().stream().map(RegistryObject::getId).toList();
        for (ResourceLocation key : keySet) forgeRegistry.remove(key);
    }

    public void onSetup(IEventBus bus) { if (isPresent()) { whenPresent(bus); } else whenAbsent(bus); }

    public boolean isPresentByDefault() { return true; }

    public boolean isPresent() {

        // illegally read config file
        File configFile = new File(Minecraft.getInstance().gameDirectory, "config/poscardsskills-common.toml");

        if (configFile.exists() && configFile.canRead()) {

            try {

                BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile));

                String line;
                while ((line = bufferedReader.readLine()) != null) {

                    if (!line.contains("#")) {

                        if (line.contains(configKey)) return line.contains("true");
                    }
                }

            } catch (IOException ignored) {}
        }

        return isPresentByDefault();
    }

    protected abstract void whenPresent(IEventBus bus);

    protected abstract void whenAbsent(IEventBus bus);

}
