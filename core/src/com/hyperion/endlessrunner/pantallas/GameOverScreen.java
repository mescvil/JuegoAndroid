package com.hyperion.endlessrunner.pantallas;

import static com.hyperion.endlessrunner.Recursos.sonidoGameOver;

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
import com.hyperion.endlessrunner.Recursos;
import com.hyperion.endlessrunner.VariablesGlobales;

public class GameOverScreen implements Screen {

    private final GalaxyInvaders juego;
    private Sprite fondo;
    private BitmapFont fuenteOver;
    private BitmapFont fuenteClick;
    private String textoOver = "> GAME OVER <";
    private String textoClick = "Pulsa para continuar";
    private long espera;

    public GameOverScreen(GalaxyInvaders juego) {
        this.juego = juego;
        fondo = new Sprite(Recursos.texturaOver);
        fondo.setPosition(185, VariablesGlobales.ANCHO / 1.5f);
        fondo.scale(1.5f);

        FreeTypeFontGenerator generadorFuente
                = new FreeTypeFontGenerator(Gdx.files.internal("fuentes/Pixelmania.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 120;
        parameter.color = Color.RED;
        fuenteOver = generadorFuente.generateFont(parameter);

        parameter.size = 60;
        parameter.color = Color.WHITE;
        fuenteClick = generadorFuente.generateFont(parameter);

        espera = TimeUtils.nanoTime();
    }

    @Override
    public void show() {
        sonidoGameOver.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        MainScreen.spriteBatch.begin();

        fondo.draw(MainScreen.spriteBatch);
        final GlyphLayout layoutOver = new GlyphLayout(fuenteOver, textoOver);

        fuenteOver.draw(MainScreen.spriteBatch, layoutOver,
                (VariablesGlobales.ANCHO - layoutOver.width) / 2f, VariablesGlobales.ALTO / 1.5f);

        final GlyphLayout layoutClick = new GlyphLayout(fuenteClick, textoClick);

        fuenteClick.draw(MainScreen.spriteBatch, layoutClick,
                (VariablesGlobales.ANCHO - layoutClick.width) / 2f, VariablesGlobales.ALTO / 1.8f);

        MainScreen.spriteBatch.end();

        if (Gdx.input.isTouched() && TimeUtils.nanoTime() - espera > 500000000) {
            juego.cambiaPantalla(new MenuScreen(juego));
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
