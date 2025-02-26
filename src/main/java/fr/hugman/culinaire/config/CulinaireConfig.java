package fr.hugman.culinaire.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.culinaire.Culinaire;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public record CulinaireConfig(
        int milkBottlesMaxCount,
        boolean canDrinkMilkBucket
) {
    private static final Path PATH = Paths.get("config/culinaire.json");

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final Codec<CulinaireConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("milk_bottles_max_count").forGetter(CulinaireConfig::milkBottlesMaxCount),
                    Codec.BOOL.fieldOf("can_drink_milk_bucket").forGetter(CulinaireConfig::canDrinkMilkBucket)
            ).apply(instance, CulinaireConfig::new)
    );

    private static CulinaireConfig instance;

    private CulinaireConfig() {
        this(1, false);
    }

    @NotNull
    public static CulinaireConfig get() {
        if (instance == null) {
            instance = initializeConfig();
        }
        return instance;
    }

    private static CulinaireConfig initializeConfig() {
        if (Files.exists(PATH)) {
            return loadConfig();
        } else {
            return createDefaultConfig();
        }
    }

    private static CulinaireConfig loadConfig() {
        try (var input = Files.newInputStream(PATH)) {
            var json = JsonParser.parseReader(new InputStreamReader(input));
            var result = CODEC.decode(JsonOps.INSTANCE, json).map(Pair::getFirst);
            return result.result().orElseGet(CulinaireConfig::new);
        } catch (IOException e) {
            Culinaire.LOGGER.warn("Failed to load Culinaire config", e);
            return new CulinaireConfig();
        }
    }

    private static CulinaireConfig createDefaultConfig() {
        var config = new CulinaireConfig();
        try (var output = Files.newOutputStream(PATH)) {
            var result = CODEC.encodeStart(JsonOps.INSTANCE, config).result();
            if (result.isPresent()) {
                var json = result.get();
                IOUtils.write(GSON.toJson(json), output, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            Culinaire.LOGGER.warn("Failed to create default Culinaire config", e);
        }
        return config;
    }
}
