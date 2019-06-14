var TweakerUtils = Java.type('io.github.cottonmc.cotton.tweaker.TweakerUtils');
var RecipeTweaker = Java.type('io.github.cottonmc.cotton.tweaker.RecipeTweaker');

//I *really* hope I can find a way to get rid of using a tweaker in a jar,
// but for some reason vanilla has higher priority over Epicurean.
RecipeTweaker.removeRecipe("minecraft:cooked_salmon_from_smoking");
RecipeTweaker.addSmoking("minecraft:salmon", TweakerUtils.createItemStack("epicurean:smoked_salmon", 1), 800, 0.35);