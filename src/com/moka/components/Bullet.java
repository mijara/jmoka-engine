package com.moka.components;

import com.moka.core.Moka;
import com.moka.core.entity.Component;
import com.moka.core.ComponentAttribute;
import com.moka.math.Vector2;
import com.moka.utils.Pools;

public class Bullet extends Component
{
	private float speed = 300;

	public Bullet()
	{
		// XML Support.
	}

	@Override
	public void onCreate()
	{

	}

	@Override
	public void onUpdate()
	{
		Vector2 buffer = Pools.vec2.take(0, 0);

		buffer.set(getTransform().getFront(buffer)).mul(speed * Moka.getTime().getDelta());
		getTransform().move(buffer);

		Pools.vec2.put(buffer);
	}

	@ComponentAttribute("Speed")
	public void setSpeed(int speed)
	{
		this.speed = speed;
	}
}
