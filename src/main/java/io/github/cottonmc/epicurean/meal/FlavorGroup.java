package io.github.cottonmc.epicurean.meal;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.StringRepresentable;

public enum FlavorGroup implements StringRepresentable {
	SPICY("spicy", 6, StatusEffects.RESISTANCE),
	UMAMI("umami", 5, StatusEffects.SATURATION),
	//TODO: decide final effect
	ACIDIC("acidic", 4, StatusEffects.FIRE_RESISTANCE),
	SWEET("sweet", 3, StatusEffects.SPEED),
	//TODO: decide final effect
	BITTER("bitter", 2, StatusEffects.HASTE),
	//TODO: decide final effect
	FILLING("filling", 1, StatusEffects.ABSORPTION);

	private final String name;
	private final int impact;
	private final StatusEffect effect;

	FlavorGroup(String name, int impact, StatusEffect effect) {
		this.name=name;
		this.impact = impact;
		this.effect = effect;
	}

	@Override
	public String asString() {
		return name;
	}

	public static FlavorGroup forName(String name) {
		for (FlavorGroup value : FlavorGroup.values()) {
			if (name.equals(value.asString())) {
				return value;
			}
		}
		return FlavorGroup.FILLING;
	}

	public int getImpact() {
		return impact;
	}

	public static FlavorGroup forImpact(int impact) {
		for (FlavorGroup value : FlavorGroup.values()) {
			if (impact == value.getImpact()) {
				return value;
			}
		}
		return FlavorGroup.FILLING;
	}

	public StatusEffect getEffect() {
		return effect;
	}

}
