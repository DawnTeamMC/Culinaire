package com.hugman.culinaire.init;

import com.google.common.reflect.Reflection;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hugman.culinaire.Culinaire;
import com.hugman.culinaire.objects.tea.TeaFlavor;
import com.hugman.culinaire.objects.tea.effect.TeaEffectType;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class CulinaireRegistries {
	public static final SimpleRegistry<TeaFlavor> TEA_FLAVOR = FabricRegistryBuilder.createSimple(TeaFlavor.class, Culinaire.MOD_DATA.id("tea_flavor")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final SimpleRegistry<TeaEffectType> TEA_FLAVOR_EFFECT = FabricRegistryBuilder.createSimple(TeaEffectType.class, Culinaire.MOD_DATA.id("tea_flavor_effect")).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	public static void register() {
		Reflection.initialize(TeaEffectType.class);
		ResourceManagerHelper serverData = ResourceManagerHelper.get(ResourceType.SERVER_DATA);
		registerListener(serverData, TEA_FLAVOR, TeaFlavor.CODEC, "tea_flavors");
	}

	private static <P> void registerListener(ResourceManagerHelper serverData, SimpleRegistry<P> registry, Codec<P> codec, String registryId) {
		serverData.registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return Culinaire.MOD_DATA.id(registryId);
			}

			@Override
			public void reload(ResourceManager manager) {
				registry.clearTags();

				for(Identifier completeId : manager.findResources(registryId, path -> path.endsWith(".json"))) {
					try(Reader reader = new BufferedReader(new InputStreamReader(manager.getResource(completeId).getInputStream()))) {
						JsonElement json = JsonParser.parseReader(reader);

						String path = completeId.getPath();
						path = path.substring((registryId + "/").length(), path.length() - ".json".length());
						Identifier identifier = new Identifier(completeId.getNamespace(), path);

						DataResult<P> result = codec.decode(JsonOps.INSTANCE, json).map(Pair::getFirst);

						result.result().ifPresent(module -> Registry.register(registry, identifier, module));
						result.error().ifPresent(error -> Culinaire.LOGGER.error("Failed to decode " + registryId + " at {}: {}", completeId, error.toString()));
					} catch(IOException e) {
						Culinaire.LOGGER.error("Failed to read " + registryId + " at {}", completeId, e);
					}
				}
			}
		});
		Culinaire.LOGGER.info("Registered " + registryId + " reload listener");
	}
}
