package com.hyperion.endlessrunner.entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Alien {

    public static boolean derecha;
    public static boolean cambio;
    public static float velocidad;
    public Sprite sprite;
    private Vector2 posicion;
    public boolean vivo;

    public Alien(Texture texturaAlien, Vector2 posicion) {
        this.posicion = posicion;
        sprite = new Sprite(texturaAlien);
        vivo = true;
        derecha = true;
        cambio = false;
    }

    public void actualizaPosicion() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        if (derecha) {
            if (posicion.x + (sprite.getWidth()) + velocidad * deltaTime < Gdx.graphics.getWidth())
                posicion.x += velocidad * deltaTime;
            else {
                cambio = true;
                derecha = false;
            }
        } else {
            if (posicion.x - (sprite.getWidth()) - velocidad * deltaTime > -sprite.getWidth())
                posicion.x -= velocidad * deltaTime;
            else {
                cambio = true;
                derecha = true;
            }
        }
    }

    public void actualizaAltura() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        posicion.y -= deltaTime * 2000;
    }

    public void dibuja(SpriteBatch batch) {
        sprite.setPosition(posicion.x, posicion.y);
        sprite.draw(batch);
    }

}
