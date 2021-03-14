package me.wolfyscript.customcrafting.recipes.types.anvil;

import me.wolfyscript.customcrafting.data.CCCache;
import me.wolfyscript.customcrafting.gui.recipebook.buttons.IngredientContainerButton;
import me.wolfyscript.customcrafting.recipes.RecipeType;
import me.wolfyscript.customcrafting.recipes.Types;
import me.wolfyscript.customcrafting.recipes.types.CustomRecipe;
import me.wolfyscript.customcrafting.utils.ItemLoader;
import me.wolfyscript.customcrafting.utils.recipe_item.Ingredient;
import me.wolfyscript.customcrafting.utils.recipe_item.Result;
import me.wolfyscript.customcrafting.utils.recipe_item.target.SlotResultTarget;
import me.wolfyscript.utilities.api.inventory.custom_items.CustomItem;
import me.wolfyscript.utilities.api.inventory.gui.GuiCluster;
import me.wolfyscript.utilities.api.inventory.gui.GuiHandler;
import me.wolfyscript.utilities.api.inventory.gui.GuiUpdate;
import me.wolfyscript.utilities.api.inventory.gui.GuiWindow;
import me.wolfyscript.utilities.libraries.com.fasterxml.jackson.core.JsonGenerator;
import me.wolfyscript.utilities.libraries.com.fasterxml.jackson.databind.JsonNode;
import me.wolfyscript.utilities.libraries.com.fasterxml.jackson.databind.SerializerProvider;
import me.wolfyscript.utilities.util.NamespacedKey;
import org.bukkit.Material;

import java.io.IOException;
import java.util.*;

public class CustomAnvilRecipe extends CustomRecipe<CustomAnvilRecipe, SlotResultTarget> {

    private boolean blockRepair;
    private boolean blockRename;
    private boolean blockEnchant;

    private Mode mode;
    private int repairCost;
    private boolean applyRepairCost;
    private RepairCostMode repairCostMode;
    private int durability;

    private HashMap<Integer, Ingredient> ingredients;

    public CustomAnvilRecipe(NamespacedKey namespacedKey, JsonNode node) {
        super(namespacedKey, node);
        this.ingredients = new HashMap<>();
        this.blockEnchant = node.path("block_enchant").asBoolean(false);
        this.blockRename = node.path("block_rename").asBoolean(false);
        this.blockRepair = node.path("block_repair").asBoolean(false);
        {
            JsonNode repairNode = node.path("repair_cost");
            this.repairCost = repairNode.path("amount").asInt(1);
            this.applyRepairCost = repairNode.path("apply_to_result").asBoolean(true);
            this.repairCostMode = RepairCostMode.valueOf(repairNode.path("mode").asText("NONE"));
        }
        this.ingredients = new HashMap<>();
        {
            JsonNode modeNode = node.path("mode");
            this.mode = Mode.valueOf(modeNode.get("usedMode").asText("DURABILITY"));
            this.durability = modeNode.path("durability").asInt(0);
            this.result = ItemLoader.loadResult(modeNode.path("result"));
        }
        readInput(0, node);
        readInput(1, node);
    }

    public CustomAnvilRecipe(CustomAnvilRecipe recipe) {
        super(recipe);
        this.ingredients = recipe.ingredients;
        this.result = recipe.result;
        this.mode = recipe.getMode();
        this.durability = recipe.durability;
        this.repairCost = recipe.repairCost;
        this.applyRepairCost = recipe.applyRepairCost;
        this.repairCostMode = recipe.repairCostMode;
        this.blockEnchant = recipe.blockEnchant;
        this.blockRename = recipe.blockRename;
        this.blockRepair = recipe.blockRepair;
    }

    public CustomAnvilRecipe() {
        super();
        this.ingredients = new HashMap<>();
        this.result = new Result<>();
        this.mode = Mode.RESULT;
        this.durability = 0;
        this.repairCost = 1;
        this.applyRepairCost = false;
        this.repairCostMode = RepairCostMode.NONE;
        this.blockEnchant = false;
        this.blockRename = false;
        this.blockRepair = false;
    }

    private void readInput(int slot, JsonNode node) {
        this.ingredients.put(slot, ItemLoader.loadIngredient(node.path("input_" + (slot == 0 ? "left" : "right"))));
    }

    private void writeInput(int slot, JsonGenerator gen) throws IOException {
        gen.writeArrayFieldStart("input_" + (slot == 0 ? "left" : "right"));
        gen.writeObject(this.ingredients.get(slot));
        gen.writeEndArray();
    }

    public int getDurability() {
        return durability;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public int getRepairCost() {
        return repairCost;
    }

    public void setRepairCost(int repairCost) {
        this.repairCost = repairCost;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public Ingredient getInputLeft() {
        return getInput(0);
    }

    public void setInputLeft(Ingredient inputLeft) {
        ingredients.put(0, inputLeft);
    }

    public Ingredient getInputRight() {
        return getInput(1);
    }

    public void setInputRight(Ingredient inputRight) {
        ingredients.put(1, inputRight);
    }

    public void setInput(int slot, Ingredient input) {
        ingredients.put(slot, input);
    }

    public Ingredient getInput(int slot) {
        return ingredients.get(slot);
    }

    public boolean hasInputLeft() {
        return !getInputLeft().isEmpty();
    }

    public boolean hasInputRight() {
        return !getInputRight().isEmpty();
    }

    public boolean isBlockRepair() {
        return blockRepair;
    }

    public void setBlockRepair(boolean blockRepair) {
        this.blockRepair = blockRepair;
    }

    public boolean isBlockRename() {
        return blockRename;
    }

    public void setBlockRename(boolean blockRename) {
        this.blockRename = blockRename;
    }

    public boolean isBlockEnchant() {
        return blockEnchant;
    }

    public void setBlockEnchant(boolean blockEnchant) {
        this.blockEnchant = blockEnchant;
    }

    public boolean isApplyRepairCost() {
        return applyRepairCost;
    }

    public void setApplyRepairCost(boolean applyRepairCost) {
        this.applyRepairCost = applyRepairCost;
    }

    public RepairCostMode getRepairCostMode() {
        return repairCostMode;
    }

    public void setRepairCostMode(RepairCostMode repairCostMode) {
        this.repairCostMode = repairCostMode;
    }

    @Override
    public RecipeType<CustomAnvilRecipe> getRecipeType() {
        return Types.ANVIL;
    }

    @Override
    public CustomAnvilRecipe clone() {
        return new CustomAnvilRecipe(this);
    }

    @Override
    public void writeToJson(JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        super.writeToJson(gen, serializerProvider);
        gen.writeBooleanField("block_enchant", blockEnchant);
        gen.writeBooleanField("block_rename", blockRename);
        gen.writeBooleanField("block_repair", blockRepair);
        {
            gen.writeObjectFieldStart("repair_cost");
            gen.writeNumberField("amount", repairCost);
            gen.writeBooleanField("apply_to_result", applyRepairCost);
            gen.writeStringField("mode", repairCostMode.toString());
            gen.writeEndObject();
        }
        {
            gen.writeObjectFieldStart("mode");
            gen.writeNumberField("durability", durability);
            gen.writeStringField("usedMode", mode.toString());
            gen.writeObjectField("result", result);
            gen.writeEndObject();
        }
        writeInput(0, gen);
        writeInput(1, gen);
    }

    @Override
    public List<CustomItem> getRecipeBookItems() {
        return getMode().equals(CustomAnvilRecipe.Mode.RESULT) ? getResult().getChoices() : hasInputLeft() ? getInputLeft().getChoices() : getInputRight().getChoices();
    }

    @Override
    public void prepareMenu(GuiHandler<CCCache> guiHandler, GuiCluster<CCCache> cluster) {
        Ingredient inputLeft = getInputLeft();
        Ingredient inputRight = getInputRight();
        ((IngredientContainerButton) cluster.getButton("ingredient.container_10")).setVariants(guiHandler, inputLeft);
        ((IngredientContainerButton) cluster.getButton("ingredient.container_13")).setVariants(guiHandler, inputRight);
        if (getMode().equals(CustomAnvilRecipe.Mode.RESULT)) {
            ((IngredientContainerButton) cluster.getButton("ingredient.container_34")).setVariants(guiHandler, getResult());
        } else if (getMode().equals(CustomAnvilRecipe.Mode.DURABILITY)) {
            ((IngredientContainerButton) cluster.getButton("ingredient.container_34")).setVariants(guiHandler, inputLeft);
        }else{
            ((IngredientContainerButton) cluster.getButton("ingredient.container_34")).setVariants(guiHandler, Collections.singletonList(new CustomItem(Material.AIR)));
        }
    }

    @Override
    public void renderMenu(GuiWindow<CCCache> guiWindow, GuiUpdate<CCCache> event) {
        event.setButton(10, new NamespacedKey("recipe_book", "ingredient.container_10"));
        event.setButton(13, new NamespacedKey("recipe_book", "ingredient.container_13"));
        NamespacedKey glass = new NamespacedKey("none", "glass_green");
        event.setButton(19, glass);
        event.setButton(22, glass);
        event.setButton(28, glass);
        event.setButton(29, glass);
        event.setButton(30, glass);
        event.setButton(32, glass);
        event.setButton(33, glass);
        if (getMode().equals(CustomAnvilRecipe.Mode.RESULT)) {
            event.setButton(31, new NamespacedKey("recipe_book", "anvil.result"));
        } else if (getMode().equals(CustomAnvilRecipe.Mode.DURABILITY)) {
            event.setButton(31, new NamespacedKey("recipe_book", "anvil.durability"));
        } else {
            event.setButton(31, new NamespacedKey("recipe_book", "anvil.none"));
        }
        event.setButton(34, new NamespacedKey("recipe_book", "ingredient.container_34"));
    }

    public enum Mode {
        DURABILITY(1), RESULT(2), NONE(0);

        private final int id;

        Mode(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Mode getById(int id) {
            for (Mode mode : Mode.values()) {
                if (mode.getId() == id)
                    return mode;
            }
            return NONE;
        }
    }

    public enum RepairCostMode {
        ADD(), MULTIPLY(), NONE();

        private static final List<RepairCostMode> modes = new ArrayList<>();

        public static List<RepairCostMode> getModes() {
            if (modes.isEmpty()) {
                modes.addAll(Arrays.asList(values()));
            }
            return modes;
        }
    }
}
