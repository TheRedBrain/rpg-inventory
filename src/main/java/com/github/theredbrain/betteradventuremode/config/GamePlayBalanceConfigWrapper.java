package com.github.theredbrain.betteradventuremode.config;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

@Config(
        name = "betteradventuremode"
)
public class GamePlayBalanceConfigWrapper extends PartitioningSerializer.GlobalData {
    @ConfigEntry.Category("server")
    @ConfigEntry.Gui.Excluded
    public GamePlayBalanceConfig gamePlayBalance = new GamePlayBalanceConfig();

    public GamePlayBalanceConfigWrapper() {
    }
}
