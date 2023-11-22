package com.github.theredbrain.bamcore.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

public interface IHousingAccessPosComponent extends Component {
    Pair<Pair<String, BlockPos>, Boolean> getValue();
    void setValue(Pair<Pair<String, BlockPos>, Boolean> value);
    void deactivate();
}
