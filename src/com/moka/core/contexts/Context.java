package com.moka.core.contexts;

import com.moka.components.Camera;
import com.moka.components.Sprite;
import com.moka.core.SubEngine;
import com.moka.core.entity.Entity;
import com.moka.graphics.Shader;
import com.moka.graphics.Texture;
import com.moka.utils.JMokaException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Base class of a game, takes care of internal core usage.
 */
public abstract class Context extends SubEngine
{
    private HashMap<String, Entity> nameRelations;
    private ArrayList<ArrayList<Entity>> layers;
    private String secondaryPackage;
    private ExecutorService service;

    /**
     * Used to store all layers.
     */
    private final ArrayList<Entity> allEntities = new ArrayList<>();
    private final ArrayList<Entity> groupEntities = new ArrayList<>();

    public Context()
    {
        nameRelations = new HashMap<>();
        layers = new ArrayList<>();
        service = Executors.newFixedThreadPool(3);
    }

    public final void update()
    {
        for (int l = layers.size() - 1; l >= 0; l--)
        {
            ArrayList<Entity> layer = layers.get(l);

            for (int i = layer.size() - 1; i >= 0; i--)
            {
                /*
                Runnable runnable = Threading.runnable(ActionDelegator.update, layer.get(i));
                service.execute(runnable);
                */
                layer.get(i).update();
            }
        }
    }

    public final void create()
    {
        for (ArrayList<Entity> layer : layers)
        {
            // iterate over entities.
            for (int i = layer.size() - 1; i >= 0; i--)
            {
                layer.get(i).create();
            }
        }
    }

    public final void render(Shader shader)
    {
        for (ArrayList<Entity> layer : layers)
        {
            for (Entity entity : layer)
            {
                if (entity.hasSprite() && entity.getSprite().isEnabled())
                {
                    entity.getSprite().render(shader);
                }
            }
        }
    }

    public void postUpdate()
    {
        for (ArrayList<Entity> layer : layers)
        {
            for (Entity entity : layer)
            {
                entity.postUpdate();
            }
        }
    }

    public void clean()
    {
        for (ArrayList<Entity> layer : layers)
        {
            for (int i = layer.size() - 1; i >= 0; i--)
            {
                if (layer.get(i).isDestroyed())
                {
                    layer.get(i).dispose();
                    layer.remove(i);
                }
            }
        }
    }

    public void hardReset()
    {
        layers.clear();
        nameRelations.clear();
    }

    /**
     * Adds an entity to a given layer.
     *
     * @param entity the entity to be added.
     * @param layer  the layer at which the context will add the entity.
     * @return the same entity given.
     */
    public final Entity addEntity(Entity entity, int layer)
    {
        // if the layer doesn't exists...
        while (layers.size() <= layer)
        {
            layers.add(new ArrayList<Entity>());
        }

        layers.get(layer).add(entity);
        return entity;
    }

    /**
     * Constructs a new {@link Entity} and add it to the hierarchy.
     *
     * @return the new {@link Entity}.
     */
    public final Entity newEntity(String name, int layer)
    {
        // throw exception if the name already exists.
        if (nameRelations.containsKey(name))
        {
            throw new JMokaException("Entity with name " + name + " already exists.");
        }

        // create and add the entity to the game.
        Entity entity = new Entity(name, this);
        addEntity(entity, layer);

        // register the name only if it has a name.
        if (name != null)
        {
            nameRelations.put(name, entity);
        }

        return entity;
    }

    /**
     * Constructs a new {@link Entity} with a sprite component and add it to the hierarchy.
     *
     * @return the new {@link Entity} with a sprite.
     */
    public final Entity newEntity(String name, Texture texture, int layer)
    {
        Entity entity = newEntity(name, layer);
        entity.addComponent(new Sprite(texture));
        return entity;
    }

    /**
     * Creates a new entity with a camera on it.
     *
     * @param name    name for the entity.
     * @param current sets this camera as the current one.
     * @return the entity.
     */
    public Entity newCamera(String name, boolean current)
    {
        Entity entity = newEntity(name, 0);
        Camera camera = new Camera(0, getDisplay().getWidth(), 0, getDisplay().getHeight());

        entity.addComponent(camera);

        if (current)
        {
            camera.setAsCurrent();
        }

        return entity;
    }

    /**
     * Finds an Entity on the game. Raises an exception if it is not found.
     *
     * @param name entity's unique tag.
     * @return the entity if found.
     */
    public final Entity findEntity(String name)
    {
        Entity entity = nameRelations.get(name);

        if (entity == null)
        {
            throw new JMokaException("There's no entity with name " + name + ".");
        }

        return entity;
    }

    /**
     * Gets all entities across all layers.
     *
     * @return all entities as an array list.
     */
    public ArrayList<Entity> getAllEntities()
    {
        allEntities.clear();

        for (ArrayList<Entity> layer : layers)
        {
            for (Entity entity : layer)
            {
                allEntities.add(entity);
            }
        }

        return allEntities;
    }

    /**
     * Gets all entities from a group.
     *
     * @return all entities from that group as an array list or null if the list is empty.
     */
    public List<Entity> getEntitiesFromGroup(String group)
    {
        groupEntities.clear();

        for (ArrayList<Entity> layer : layers)
        {
            for (Entity entity : layer)
            {
                if (entity.belongsTo(group))
                {
                    groupEntities.add(entity);
                }
            }
        }

        return groupEntities.isEmpty()? null : (List<Entity>) groupEntities.clone();
    }

    public void dispose()
    {
        service.shutdown();
    }

    /**
     * Called every update frame, is not necessary for most games do.
     */
    public void onUpdate()
    {

    }

    /**
     * Creates the game context, this is intended to start everything the game needs.
     */
    public abstract void onCreate();

    /**
     * Been capable to send an error code.
     */
    public abstract void onStop();

}

