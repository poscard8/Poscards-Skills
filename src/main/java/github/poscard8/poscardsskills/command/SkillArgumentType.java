package github.poscard8.poscardsskills.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.skill.Skill;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Argument type that suggests skills.
 */
public class SkillArgumentType implements ArgumentType<String> {

    private static final SimpleCommandExceptionType NO_SKILL_FOUND = new SimpleCommandExceptionType(Component.translatable("command.github.poscard8.poscardsskills.no_skill_found"));

    private SkillArgumentType() {}

    public static SkillArgumentType of() { return new SkillArgumentType(); }

    /**
     * Commands already have a similar method. However {@link ResourceLocation} does not accept the character {@code '#'}.
     * This method enables the parser to read {@code '#all'}.
     */
    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {

        int start = reader.getCursor();
        ResourceLocation location = ResourceLocation.read(reader);
        Optional<Skill> optional = PoscardsSkills.getSkillHandler().byKey(location);

        reader.setCursor(start);

        while(reader.canRead() && !Objects.equals(reader.peek(), ' ')) { reader.skip(); }
        String s = reader.getString().substring(start, reader.getCursor());

        if (optional.isPresent()) {

            return location.toString();

        } else if (Objects.equals(s, "#all")) {

            return s;

        } else {

            reader.setCursor(start);
            throw NO_SKILL_FOUND.create();
        }
    }

    /**
     * Suggests all the registered skills and {@code '#all'}.
     */
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder suggestionsBuilder) {

        Collection<String> keySet = PoscardsSkills.getSkillHandler().getKeys().stream().map(ResourceLocation::toString).collect(Collectors.toSet());
        keySet.add("#all");
        return SharedSuggestionProvider.suggest(keySet, suggestionsBuilder);
    }

}
