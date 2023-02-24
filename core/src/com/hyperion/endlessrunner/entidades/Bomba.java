package com.hyperion.endlessrunner.entidades;

import static com.hyperion.endlessrunner.Recursos.sonidoBomba;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Bomba {

    private static final float VELOCIDAD = 750;
    public Vector2 posicion;
    public Sprite sprite;
    public boolean activa;


    public Bomba(Texture textura, Vector2 posicion) {
        this.posicion = posicion;
        sprite = new Sprite(textura);
        activa = false;
    }

    public void activaBomba(Vector2 posicion) {
        activa = true;
        sonidoBomba.play();
        this.posicion = posicion;
    }

    public void actulizaPosicion() {
        float delta = Gdx.graphics.getDeltaTime();
        posicion.y -= delta * VELOCIDAD;
    }

    public void dibuja(SpriteBatch spriteBatch) {
        sprite.setPosition(posicion.x, posicion.y);
        sprite.draw(spriteBatch);
    }
}
