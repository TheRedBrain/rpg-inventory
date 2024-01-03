package com.github.theredbrain.betteradventuremode.components;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface ILongComponent extends Component {
    long getValue();
    void setValue(long value);
}
