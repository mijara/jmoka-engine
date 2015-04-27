package example.components;

import com.moka.core.entity.Component;
import com.moka.core.Input;
import com.moka.core.xml.XmlAttribute;
import com.moka.math.MathUtil;

public class ShipMovement extends Component
{
    private float speed = 50;
    private float boundLeft = 0;
    private float boundRight = 0;

    public ShipMovement()
    {
        // Empty class for XML Support.
    }

    @Override
    public void onUpdate()
    {
        if (getInput().getKey(Input.KEY_D))
        {
            getTransform().move((float) (speed * getTime().getDelta()), 0);
        }

        if (getInput().getKey(Input.KEY_A))
        {
            getTransform().move(- (float) (speed * getTime().getDelta()), 0);
        }

        getTransform().getPosition().x = MathUtil.clamp(getTransform().getPosition().x, boundLeft, boundRight);
    }

    @XmlAttribute("speed")
    public void setSpeed(float speed)
    {
        this.speed = speed;
    }

    @XmlAttribute("boundLeft")
    public void setBoundLeft(float boundLeft)
    {
        this.boundLeft = boundLeft;
    }

    @XmlAttribute("boundRight")
    public void setBoundRight(float boundRight)
    {
        this.boundRight = boundRight;
    }
}
