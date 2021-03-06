package com.moka.components;

import com.moka.core.Moka;
import com.moka.math.Vector2;
import com.moka.scene.entity.Component;
import com.moka.scene.entity.ComponentAttribute;
import com.moka.time.StopWatch;
import com.moka.utils.Pools;

public class Debugger extends Component
{
    public enum Options
    {
        NONE,
        POSITION,
        ROTATION,
        SIZE,
    }

    private Options selection = Options.NONE;

    private double frequency = 1;
    private StopWatch stopWatch;

    @Override
    public void onCreate()
    {
        stopWatch = Moka.getTime().newStopWatch();
    }

    @Override
    public void onUpdate()
    {
        if (stopWatch.isGreaterThan(frequency))
            stopWatch.restart();
        else
            return;

        switch (selection)
        {
            case POSITION:
                log("position: " + getTransform().getPosition());
                break;

            case ROTATION:
                Vector2 buffer = Pools.vec2.take();
                log("rotation: " + getTransform().getFront(buffer).angle());
                Pools.vec2.put(buffer);
                break;

            case SIZE:
                log("size: " + getTransform().getSize());
                break;
        }
    }

    @ComponentAttribute("Option")
    public void setSelection(Options selection)
    {
        this.selection = selection;
    }

    @ComponentAttribute("Frequency")
    public void setFrequency(double frequency)
    {
        this.frequency = frequency;
    }
}
