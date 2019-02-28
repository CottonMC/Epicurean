package io.github.cottonmc.epicurean.item;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.StringRepresentable;

public enum FlavorGroup implements StringRepresentable {
	SPICY("spicy", 6, StatusEffects.FIRE_RESISTANCE), UMAMI("umami", 5, StatusEffects.STRENGTH), ACIDIC("acidic", 4, StatusEffects.ABSORPTION), SWEET("sweet", 3, StatusEffects.SPEED), BITTER("bitter", 2, StatusEffects.RESISTANCE), FILLING("filling", 1, StatusEffects.SATURATION);

	private final String name;
	private final int weight;
	private final StatusEffect effect;

	FlavorGroup(String name, int weight, StatusEffect effect) {
		this.name=name;
		this.weight = weight;
		this.effect = effect;
	}

	@Override
	public String asString() {
		return name;
	}

	public static FlavorGroup forName(String s) {
		for (FlavorGroup value : FlavorGroup.values()) {
			if (s.equals(value.asString())) {
				return value;
			}
		}
		return FlavorGroup.FILLING;
	}

	public int getWeight() {
		return weight;
	}

	public boolean outweighs(FlavorGroup group) {
		return this.weight > group.weight;
	}

	public StatusEffect getEffect() {
		return effect;
	}

}
