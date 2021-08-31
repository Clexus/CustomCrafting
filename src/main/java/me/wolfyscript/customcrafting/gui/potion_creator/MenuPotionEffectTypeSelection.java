package me.wolfyscript.customcrafting.gui.potion_creator;

import me.wolfyscript.customcrafting.CustomCrafting;
import me.wolfyscript.customcrafting.data.CCCache;
import me.wolfyscript.customcrafting.data.cache.potions.PotionEffects;
import me.wolfyscript.customcrafting.gui.CCWindow;
import me.wolfyscript.customcrafting.gui.main_gui.ClusterMain;
import me.wolfyscript.utilities.api.inventory.gui.GuiCluster;
import me.wolfyscript.utilities.api.inventory.gui.GuiUpdate;
import me.wolfyscript.utilities.api.inventory.gui.button.ButtonState;
import me.wolfyscript.utilities.api.inventory.gui.button.buttons.ActionButton;
import me.wolfyscript.utilities.util.NamespacedKey;
import me.wolfyscript.utilities.util.inventory.PlayerHeadUtils;
import org.bukkit.potion.PotionEffectType;

public class MenuPotionEffectTypeSelection extends CCWindow {

    public MenuPotionEffectTypeSelection(GuiCluster<CCCache> cluster, CustomCrafting customCrafting) {
        super(cluster, ClusterPotionCreator.POTION_EFFECT_TYPE_SELECTION.getKey(), 54, customCrafting);
    }

    @Override
    public void onInit() {
        registerButton(new ActionButton<>("back", new ButtonState<>(ClusterMain.BACK, PlayerHeadUtils.getViaURL("864f779a8e3ffa231143fa69b96b14ee35c16d669e19c75fd1a7da4bf306c"), (cache, guiHandler, player, inventory, slot, event) -> {
            PotionEffects potionEffectCache = guiHandler.getCustomCache().getPotionEffectCache();
            if (!potionEffectCache.getOpenedFromCluster().isEmpty()) {
                guiHandler.openWindow(new NamespacedKey(potionEffectCache.getOpenedFromCluster(), potionEffectCache.getOpenedFromWindow()));
            } else {
                guiHandler.openWindow(potionEffectCache.getOpenedFromWindow());
            }
            return true;
        })));

        for (PotionEffectType type : PotionEffectType.values()) {
            registerButton(new ButtonPotionEffectTypeSelect(type));
        }
    }

    @Override
    public void onUpdateAsync(GuiUpdate<CCCache> update) {
        super.onUpdateAsync(update);
        update.setButton(0, "back");
        for (int i = 0; i < PotionEffectType.values().length; i++) {
            update.setButton(9 + i, "potion_effect_type_" + PotionEffectType.values()[i].getName().toLowerCase());
        }
    }
}