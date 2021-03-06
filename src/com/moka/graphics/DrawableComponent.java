package com.moka.graphics;

import com.moka.scene.entity.Component;

public abstract class DrawableComponent extends Component
{
    public abstract void render(Renderer renderer);

    public abstract boolean shouldBatch();
}
