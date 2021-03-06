package com.moka.components;

import com.moka.core.Moka;
import com.moka.prefabs.Prefab;
import com.moka.scene.entity.Component;
import com.moka.scene.entity.ComponentAttribute;
import com.moka.triggers.Trigger;

public class Shooting extends Component
{
    private Trigger<Prefab> trigger;
    private Prefab bulletPrefab;
    private String button;

    @Override
    public void onUpdate()
    {
        if (Moka.getInput().getButtonDown(button))
        {
            bulletPrefab.setPosition(getTransform().getPosition());
            bulletPrefab.setRotation(getTransform().getLookAngle());
            onFire();
        }
    }

    protected void onFire()
    {
        if (trigger != null)
            trigger.trigger(this, bulletPrefab);
        else
            bulletPrefab.newEntity(null);
    }

    @ComponentAttribute("Trigger")
    public final void setTrigger(Trigger<Prefab> trigger)
    {
        this.trigger = trigger;
    }

    @ComponentAttribute(value = "BulletPrefab", required = true)
    public final void setBulletPrefab(Prefab bulletPrefab)
    {
        this.bulletPrefab = bulletPrefab;
    }

    @ComponentAttribute(value = "Button", required = true)
    public void setButton(String button)
    {
        this.button = button;
    }
}
