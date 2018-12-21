package cassiokf.industrialrenewal.config;

import cassiokf.industrialrenewal.References;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class IRConfigGuiFactory implements IModGuiFactory {

    @Override
    public void initialize(Minecraft minecraftInstance) {
    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new IRConfigGui(parentScreen);
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    public static class IRConfigGui extends GuiConfig {

        public IRConfigGui(GuiScreen parentScreen) {
            super(parentScreen, getConfigElements(), References.MODID, false, false, I18n.format("gui.config.main_title"));
        }

        private static List<IConfigElement> getConfigElements() {
            List<IConfigElement> list = new ArrayList<IConfigElement>();
            list.add(new DummyCategoryElement(I18n.format("gui.config.category.options"), "gui.config.category.options", CategoryEntryOptions.class));
            list.add(new DummyCategoryElement(I18n.format("gui.config.category.recipes"), "gui.config.category.recipes", CategoryEntryRecipes.class));
            return list;
        }

        public static class CategoryEntryOptions extends GuiConfigEntries.CategoryEntry {

            public CategoryEntryOptions(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
                super(owningScreen, owningEntryList, configElement);
            }

            @Override
            protected GuiScreen buildChildScreen() {
                Configuration config = IRConfig.getConfig();
                ConfigElement categoryBlocks = new ConfigElement(config.getCategory(IRConfig.CATEGORY_NAME_OPTIONS));
                List<IConfigElement> propertyOnScreen = categoryBlocks.getChildElements();
                String windowTitle = I18n.format("gui.config.category.options");
                return new GuiConfig(owningScreen, propertyOnScreen, owningScreen.modID, this.configElement.requiresWorldRestart() || owningScreen.allRequireWorldRestart, this.configElement.requiresMcRestart() || owningScreen.allRequireMcRestart, windowTitle);
            }
        }

        public static class CategoryEntryRecipes extends GuiConfigEntries.CategoryEntry {

            public CategoryEntryRecipes(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
                super(owningScreen, owningEntryList, configElement);
            }

            @Override
            protected GuiScreen buildChildScreen() {
                Configuration config = IRConfig.getConfig();
                ConfigElement categoryBlocks = new ConfigElement(config.getCategory(IRConfig.CATEGORY_NAME_RECIPES));
                List<IConfigElement> propertyOnScreen = categoryBlocks.getChildElements();
                String windowTitle = I18n.format("gui.config.category.recipes");
                return new GuiConfig(owningScreen, propertyOnScreen, owningScreen.modID, this.configElement.requiresWorldRestart() || owningScreen.allRequireWorldRestart, this.configElement.requiresMcRestart() || owningScreen.allRequireMcRestart, windowTitle);
            }
        }

    }


}
