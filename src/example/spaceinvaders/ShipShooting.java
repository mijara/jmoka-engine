package example.spaceinvaders;

import com.moka.components.Component;
import com.moka.core.Prefab;
import com.moka.core.triggers.Trigger;
import com.moka.core.triggers.TriggerEvent;
import com.moka.core.xml.XmlAttribute;
import org.lwjgl.glfw.GLFW;

public class ShipShooting extends Component
{
    private Trigger<Prefab> trigger;
    private Prefab bulletPrefab;

    public ShipShooting()
    {
        // Empty class to support xml.
    }

    @Override
    public void onUpdate()
    {
        if (getInput().getKeyDown(GLFW.GLFW_KEY_SPACE))
        {
            if (trigger != null)
            {
                trigger.onTrigger(new TriggerEvent<>(this, bulletPrefab));
            }
        }
    }

    @XmlAttribute("trigger")
    public void setTrigger(Trigger<Prefab> trigger)
    {
        this.trigger = trigger;
    }

    @XmlAttribute("bulletPrefab")
    public void setBulletPrefab(Prefab bulletPrefab)
    {
        this.bulletPrefab = bulletPrefab;
    }
}
