package fr.hugman.culinaire.data.provider;

import fr.hugman.culinaire.item.CulinaireItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Items;
import java.util.concurrent.CompletableFuture;

import static fr.hugman.culinaire.item.CulinaireItems.SANDWICH;
import static fr.hugman.culinaire.tag.CulinaireItemTags.*;

public class CulinaireItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public CulinaireItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        // Culinaire
        valueLookupBuilder(BOWL_FOOD).add(
                Items.MUSHROOM_STEW,
                Items.SUSPICIOUS_STEW,
                Items.RABBIT_STEW,
                Items.BEETROOT_SOUP,
                CulinaireItems.SALAD,
                CulinaireItems.MASHED_POTATOES
        );
        valueLookupBuilder(PIES).add(
                CulinaireItems.APPLE_PIE,
                CulinaireItems.SWEET_BERRY_PIE,
                CulinaireItems.DARK_CHOCOLATE_PIE,
                CulinaireItems.MILK_CHOCOLATE_PIE,
                CulinaireItems.WHITE_CHOCOLATE_PIE
        );

        valueLookupBuilder(SANDWICHES).add(SANDWICH);
        valueLookupBuilder(SANDWICH_BREAD).add(Items.BREAD);
        valueLookupBuilder(SANDWICH_INGREDIENT_BLACKLIST)
                .addTag(SANDWICH_BREAD)
                .addTag(SANDWICHES)
                .addTag(BOWL_FOOD)
                .addTag(PIES);

        valueLookupBuilder(GREEN_TEA_INGREDIENTS).add(Items.SHORT_GRASS, Items.TALL_GRASS, Items.SEAGRASS);
        valueLookupBuilder(WHITE_TEA_INGREDIENTS).add(Items.OXEYE_DAISY);
    }
}