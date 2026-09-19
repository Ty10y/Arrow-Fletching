package com.arrowfletching.menu;

import java.util.function.Supplier;

import com.arrowfletching.ArrowFletching;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Registers the fletching-table menu type. */
public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, ArrowFletching.MOD_ID);

    public static final Supplier<MenuType<FletchingTableMenu>> FLETCHING_TABLE =
            MENUS.register("fletching_table",
                    () -> IMenuTypeExtension.create((windowId, inv, data) -> new FletchingTableMenu(windowId, inv)));

    private ModMenus() {
    }
}
