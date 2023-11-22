package com.github.theredbrain.bamcore.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.util.math.BlockPos;

public interface IBlockPosComponent extends Component {
    BlockPos getValue();
    void setValue(BlockPos value);
}
