package hugman.ce_foodstuffs.objects.item.tea;

import hugman.ce_foodstuffs.CEFoodstuffs;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class TeaType {
	private final Strength strength;
	private final Flavor flavor;

	public TeaType(String strength, String flavor) {
		this(Strength.byName(strength), Flavor.byName(flavor));
	}

	public TeaType(Strength strength, Flavor flavor) {
		this.strength = strength;
		this.flavor = flavor;
	}

	public boolean isCorrect() {
		return this.getStrength() != null && this.getFlavor() != null;
	}

	public Strength getStrength() {
		return strength;
	}

	public Flavor getFlavor() {
		return flavor;
	}

	public Tag<Item> getTag() {
		return TagRegistry.item(new Identifier(CEFoodstuffs.MOD_ID, "tea_ingredients/" + getFlavor().getName() + "/" + getStrength().getName()));
	}

	public enum Strength {
		WEAK("weak", 1),
		NORMAL("normal", 2),
		STRONG("strong", 3);

		private final String name;
		private final int potency;

		Strength(String name, int potency) {
			this.name = name;
			this.potency = potency;
		}

		public static Strength byName(String name) {
			for(Strength strength : Strength.values()) {
				if(strength.getName().equals(name)) {
					return strength;
				}
			}
			return null;
		}

		public static Strength byPotency(int potency) {
			for(Strength strength : Strength.values()) {
				if(strength.getPotency() == potency) {
					return strength;
				}
			}
			return null;
		}

		public String getName() {
			return name;
		}

		public int getPotency() {
			return potency;
		}
	}

	public enum Flavor {
		SWEET("sweet", 0.25f, 0.5f, 0.75f),
		UMAMI("umami", 0.2f, 0.6f, 0.8f),
		SALTY("salty", 0.25f, 0.5f, 0.75f),
		SOUR("sour", 0.2f, 0.6f, 0.8f),
		BITTER("bitter", 0.2f, 0.5f, 0.8f),
		SHINING("shining", 1f),
		GLOOPY("gloopy", 1f);

		private final String name;
		private final float weakRatio;
		private final float normalRatio;
		private final float strongRatio;

		Flavor(String name, float ratio) {
			this(name, ratio, ratio, ratio);
		}

		Flavor(String name, float weakRatio, float normalRatio, float strongRatio) {
			this.name = name;
			this.weakRatio = weakRatio;
			this.normalRatio = normalRatio;
			this.strongRatio = strongRatio;
		}

		public static Flavor byName(String name) {
			for(Flavor flavor : Flavor.values()) {
				if(flavor.getName().equals(name)) {
					return flavor;
				}
			}
			return null;
		}

		public String getName() {
			return name;
		}
	}
}
