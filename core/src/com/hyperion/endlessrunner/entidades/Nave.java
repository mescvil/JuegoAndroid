package com.hyperion.endlessrunner.entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.hyperion.endlessrunner.entidades.Disparo;

public class Nave {

    public Vector2 posicionNave;
    private float offset;
    private final Sprite spriteNave;
    private final int velocidadNave = 800;

    private int nDisparos;
    private final int nTotalDisparos = 5;
    public Disparo[] disparos;

    private Sound sonidoDisparo;

    public Nave(Texture texturaNave, Texture texturaBala, float offset) {
        this.spriteNave = new Sprite(texturaNave);
        this.offset = offset;

        // posicion incial de la nave con un pequeño offset del boton
        this.posicionNave = new Vector2(
                Gdx.graphics.getWidth() / 2f - spriteNave.getWidth() / 2,
                offset + 20f);

        configuraDisparos(texturaBala);
    }

    private void configuraDisparos(Texture texturaBala) {
        nDisparos = 0;
        disparos = new Disparo[nTotalDisparos];
        for (int i = 0; i < disparos.length; i++) {
            disparos[i] = new Disparo(texturaBala);
        }

        sonidoDisparo = Gdx.audio.newSound(Gdx.files.internal("sonidos/tiro.wav"));
    }

    public void actualizaPosicion() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        if (Gdx.input.getAccelerometerX() < -0.25f)
            if (posicionNave.x + (spriteNave.getWidth()) + velocidadNave * deltaTime < Gdx.graphics.getWidth())
                posicionNave.x += velocidadNave * deltaTime;

        if (Gdx.input.getAccelerometerX() > 0.25f) {
            if (posicionNave.x - (spriteNave.getWidth()) - velocidadNave * deltaTime > -spriteNave.getWidth())
                posicionNave.x -= velocidadNave * deltaTime; // Movimiento a izquierda
        }


        if (Gdx.input.isTouched()) {
            Vector3 toque = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            float alturaBoton = Gdx.graphics.getHeight() - offset;

            // Detectamos si es derecha o izquierda el movimiento
            if (toque.x >= Gdx.graphics.getWidth() / 2f && toque.y < alturaBoton) { // Movimiento a derecha
                if (posicionNave.x + (spriteNave.getWidth()) + velocidadNave * deltaTime < Gdx.graphics.getWidth())
                    posicionNave.x += velocidadNave * deltaTime;

            }
            if (toque.x < Gdx.graphics.getWidth() / 2f && toque.y < alturaBoton) {
                if (posicionNave.x - (spriteNave.getWidth()) - velocidadNave * deltaTime > -spriteNave.getWidth())
                    posicionNave.x -= velocidadNave * deltaTime; // Movimiento a izquierda
            }
        }

        for (Disparo disparo : disparos) {
            if (disparo.activo) {
                if (!disparo.actualizaPosicion()) {
                    nDisparos--;
                    disparo.activo = false;
                }
            }
        }
    }

    public void dispara() {
        if (nDisparos < nTotalDisparos) {
            for (Disparo disparo : disparos) {
                if (!disparo.activo) {
                    disparo.setPosicion(spriteNave.getX() + 37, spriteNave.getY() + 23);
                    sonidoDisparo.play();
                    break;
                }
            }
            nDisparos++;
        }
    }

    public void dibuja(SpriteBatch batch) {
        for (Disparo disparo : disparos) {
            if (disparo.activo)
                disparo.dibuja(batch);
        }

        spriteNave.setPosition(posicionNave.x, posicionNave.y);
        spriteNave.draw(batch);
    }
}
