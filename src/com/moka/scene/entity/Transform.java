package com.moka.scene.entity;

import com.moka.components.Sprite;
import com.moka.core.Moka;
import com.moka.math.Matrix3;
import com.moka.math.Vector2;
import com.moka.utils.CalcUtil;
import com.moka.utils.JMokaException;
import com.moka.utils.Pools;

/**
 * The transform indicates all about the position, rotation and size of an getEntity,
 * every getEntity has a transform, and only one transform. The position, rotation and
 * size vectors are marked as final, so fell free to store them to keep track of them
 * in the future.
 *
 * @author shelo
 */
public class Transform
{
    /**
     * Entity that has this transform.
     */
    private final Entity entity;

    private final Vector2 position;
    private final Matrix3 rotation;
    private final Vector2 size;

    /**
     * If we should use our size or an sprite size.
     */
    private boolean useOwnSize;

    /**
     * Save a previous state in order to check changes.
     */
    private final Transform prev;

    public Transform(Entity entity)
    {
        this.entity = entity;

        rotation = new Matrix3().toIdentity();
        position = Pools.vec2.take();
        size = Pools.vec2.take();

        prev = new Transform();
    }

    /**
     * This constructor is used internally to create a previous state transform.
     */
    public Transform()
    {
        this.entity = null;

        rotation = new Matrix3().toIdentity();
        position = new Vector2();
        size = new Vector2();

        prev = null;
    }

    /**
     * Updates the previous transform in order to catch up.
     */
    public void update()
    {
        prev.set(this);
    }

    public void move(float x, float y)
    {
        position.add(x, y);
    }

    /**
     * Moves the transform multiplying the values by the delta time.
     *
     * @param x     the x's distance not multiplied by delta.
     * @param y     the y's distance not multiplied by delta.
     */
    public void moveDelta(float x, float y)
    {
        move(x * Moka.getTime().getDelta(), y * Moka.getTime().getDelta());
    }

    public void move(Vector2 distance)
    {
        position.add(distance);
    }

    public void rotate(float radians)
    {
        CalcUtil.rotateMatrix(rotation, radians);
    }

    private void set(Transform other)
    {
        position.set(other.getPosition());
        rotation.set(other.getRotation());
        size.set(other.getSize());
        useOwnSize = other.useOwnSize;
    }

    public void setSize(float width, float height)
    {
        useOwnSize = true;
        this.size.set(width, height);
    }

    public void setSize(Vector2 size)
    {
        if (size == null)
        {
            useOwnSize = false;
            return;
        }

        this.size.set(size);
        useOwnSize = true;
    }

    public void setPosition(float x, float y)
    {
        this.position.set(x, y);
    }

    public void setPosition(Vector2 position)
    {
        this.position.set(position);
    }

    public void setRotation(float degrees)
    {
        this.rotation.toRotation(degrees * 0.01745329252f);
    }

    public void setRotationRadians(float radians)
    {
        this.rotation.toRotation(radians);
    }

    /**
     * Returns the vector that points front in the direction of the transform.
     *
     * @param result where we will store the result.
     * @return the result with the forward direction.
     */
    public Vector2 getFront(final Vector2 result)
    {
        return rotation.mul(Vector2.RIGHT, result).nor();
    }

    /**
     * Returns the vector that points back in the direction of the transform.
     *
     * @param result where we will store the result.
     * @return the result with the backward direction.
     */
    public Vector2 getBack(final Vector2 result)
    {
        return rotation.mul(Vector2.LEFT, result).nor();
    }

    /**
     * Returns the vector that points to the right in the direction of the transform.
     *
     * @param result where we will store the result.
     * @return the result with the right direction.
     */
    public Vector2 getRight(final Vector2 result)
    {
        return rotation.mul(Vector2.DOWN, result).nor();
    }

    /**
     * Returns the vector that points to the left in the direction of the transform.
     *
     * @param result where we will store the result.
     * @return the result with the left direction.
     */
    public Vector2 getLeft(final Vector2 result)
    {
        return rotation.mul(Vector2.UP, result).nor();
    }

    public float getLookAngle()
    {
        return CalcUtil.calcFrontAngle(this);
    }

    /**
     * Gets the size that the transform is using at the time, if no size
     * was specified before, then this method will return the sprite's size,
     * if any.
     *
     * @return the size used by the transform.
     */
    public Vector2 getSize()
    {
        Vector2 rSize;

        if (!useOwnSize)
        {
            if (entity.hasDrawable() && entity.getDrawable() instanceof Sprite)
            {
                rSize = ((Sprite) entity.getDrawable()).getSize();

                if (rSize == null)
                    throw new JMokaException("Entity has no dimensions!");
            }
            else
            {
                rSize = size;
            }
        }
        else
        {
            rSize = size;
        }

        return rSize;
    }

    /**
     * Returns the rotation matrix used at this moment.
     *
     * @return the rotation matrix.
     */
    public Matrix3 getRotation()
    {
        return rotation;
    }

    /**
     * Returns the position vector used at this moment.
     *
     * @return the position vector.
     */
    public Vector2 getPosition()
    {
        return position;
    }

    /**
     * Gets the getEntity that has this transform. That getEntity will never
     * change, since the getEntity is declared as a final.
     *
     * @return the getEntity.
     */
    public Entity getEntity()
    {
        return entity;
    }

    public boolean hasRotated()
    {
        return !prev.getRotation().equals(rotation);
    }

    public boolean hasMoved()
    {
        return !prev.getPosition().equals(position);
    }

    public boolean hasChanged()
    {
        return hasRotated() || hasMoved();
    }

    public void moveX(float distance)
    {
        position.add(distance, 0);
    }

    public void moveY(float distance)
    {
        position.add(0, distance);
    }

    public void dispose()
    {
        Pools.vec2.put(position);
        Pools.vec2.put(size);
    }

    public void scale(float value)
    {
        size.mul(value);
    }

    public void lookAt(Vector2 target)
    {
        Vector2 buffer = Pools.vec2.take(0, 0);
        buffer.set(target);
        buffer.sub(position);
        setRotationRadians(buffer.angle());
        Pools.vec2.put(buffer);
    }
}
