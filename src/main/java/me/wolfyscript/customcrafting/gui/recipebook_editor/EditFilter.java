package me.wolfyscript.customcrafting.gui.recipebook_editor;

import me.wolfyscript.customcrafting.CustomCrafting;
import me.wolfyscript.customcrafting.data.CCCache;
import me.wolfyscript.utilities.api.inventory.gui.GuiCluster;
import me.wolfyscript.utilities.api.inventory.gui.GuiUpdate;

public class EditFilter extends EditCategorySetting {

    public EditFilter(GuiCluster<CCCache> cluster, CustomCrafting customCrafting) {
        super(cluster, "filter", customCrafting);
    }

    @Override
    public void onInit() {
        super.onInit();


    }

    @Override
    public void onUpdateAsync(GuiUpdate<CCCache> update) {
        super.onUpdateAsync(update);


    }
}