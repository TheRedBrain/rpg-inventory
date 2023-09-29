package com.github.theredbrain.bamcore.components;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface IStringMapComponent extends Component {
    String getValue(String key);
    void setValue(String key, String value);

    boolean getStatus(String key);
    void setStatus(String key, boolean status);

    void setPair(String key, String value, boolean status);
}
