package com.hyperion.endlessrunner.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.hyperion.endlessrunner.GalaxyInvaders;

public class FinJuegoScreen implements Screen {

    private GalaxyInvaders juego;
    private Sprite fondo;
    private BitmapFont fuenteOver;
    private String textoOver = "- GAME OVER -";

    public FinJuegoScreen(GalaxyInvaders juego) {
        this.juego = juego;
        fondo = new Sprite(MainScreen.texturaOver);
        fondo.setPosition(185, MainScreen.ANCHO / 1.5f);
        fondo.scale(1.5f);

        FreeTypeFontGenerator generadorFuente
                = new FreeTypeFontGenerator(Gdx.files.internal("fuentes/Pixelmania.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 120;
        parameter.color = Color.RED;
        fuenteOver = generadorFuente.generateFont(parameter);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        MainScreen.spriteBatch.begin();

        fondo.draw(MainScreen.spriteBatch);
        final GlyphLayout layout = new GlyphLayout(fuenteOver, textoOver);

        fuenteOver.draw(MainScreen.spriteBatch, layout,
                (MainScreen.ANCHO - layout.width) / 2f, MainScreen.ALTO / 1.5f);

        MainScreen.spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
