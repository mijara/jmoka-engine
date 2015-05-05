package com.moka.core.contexts;

import com.moka.utils.JMokaException;

public class ReaderContext extends Context
{
    protected final String resources;
    protected final String scene;

    public ReaderContext(String scene, String resources)
    {
        if(scene == null)
        {
            throw new JMokaException("Scene file path cannot be null.");
        }

        this.scene = scene;
        this.resources = resources;
    }

    @Override
    public void onPreLoad()
    {
        if(resources != null)
        {
            define(resources);
        }
    }

    @Override
    public void onCreate()
    {

    }

    @Override
    public void onStop()
    {

    }
}
