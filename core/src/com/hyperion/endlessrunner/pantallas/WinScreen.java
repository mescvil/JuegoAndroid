package com.hyperion.endlessrunner.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.TimeUtils;
import com.hyperion.endlessrunner.GalaxyInvaders;

import jdk.tools.jmod.Main;

public class WinScreen implements Screen {

    private final GalaxyInvaders juego;
    private Sprite fondo;
    private BitmapFont fuenteWin;
    private BitmapFont fuenteClick;
    private String textoWin = "> Victoria <";
    private String textoClick = "Pulsa para continuar";
    private long espera;

    public WinScreen(GalaxyInvaders juego) {
        this.juego = juego;
        fondo = new Sprite(MainScreen.texturaWin);
        fondo.setPosition(185, MainScreen.ANCHO / 1.5f);
        fondo.scale(1.5f);

        FreeTypeFontGenerator generadorFuente
                = new FreeTypeFontGenerator(Gdx.files.internal("fuentes/Pixelmania.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 120;
        parameter.color = Color.GREEN;
        fuenteWin = generadorFuente.generateFont(parameter);

        parameter.size = 60;
        parameter.color = Color.WHITE;
        fuenteClick = generadorFuente.generateFont(parameter);

        espera = TimeUtils.nanoTime();
    }

    @Override
    public void show() {
        MainScreen.sonidoWin.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        MainScreen.spriteBatch.begin();

        fondo.draw(MainScreen.spriteBatch);
        final GlyphLayout layoutOver = new GlyphLayout(fuenteWin, textoWin);

        fuenteWin.draw(MainScreen.spriteBatch, layoutOver,
                (MainScreen.ANCHO - layoutOver.width) / 2f, MainScreen.ALTO / 1.5f);

        final GlyphLayout layoutClick = new GlyphLayout(fuenteClick, textoClick);

        fuenteClick.draw(MainScreen.spriteBatch, layoutClick,
                (MainScreen.ANCHO - layoutClick.width) / 2f, MainScreen.ALTO / 1.8f);

        MainScreen.spriteBatch.end();

        if (Gdx.input.isTouched() && TimeUtils.nanoTime() - espera > 500000000) {
            juego.cambiaPantalla(new MainScreen(juego));
        }
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
