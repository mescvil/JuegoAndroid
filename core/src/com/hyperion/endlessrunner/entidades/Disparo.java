package com.hyperion.endlessrunner.entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Disparo {

    private final static int VELOCIDAD_BALA = 1700;
    private Vector2 posicion;
    public boolean activo;
    public final Sprite sprite;

    public Disparo(Texture texturaBala) {
        posicion = new Vector2();
        sprite = new Sprite(texturaBala);
        sprite.rotate(90);
        activo = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Disparo disparo = (Disparo) o;

        return posicion.equals(disparo.posicion);
    }

    @Override
    public int hashCode() {
        return posicion.hashCode();
    }

    public void setPosicion(float x, float y) {
        activo = true;
        posicion.x = x;
        posicion.y = y;
    }

    public boolean actualizaPosicion() {
        posicion.y += VELOCIDAD_BALA * Gdx.graphics.getDeltaTime();
        return !(posicion.y > Gdx.graphics.getHeight() + 100);
    }

    public void dibuja(SpriteBatch batch) {
        sprite.setPosition(posicion.x, posicion.y);
        sprite.draw(batch);
    }
}
