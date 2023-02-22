package com.hyperion.endlessrunner.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.TimeUtils;
import com.hyperion.endlessrunner.GalaxyInvaders;

public class MenuScreen implements Screen {

    private final GalaxyInvaders juego;
    private Sprite fondo;
    private Texture texturaFondo;
    private SpriteBatch spriteBatch;
    private BitmapFont fuenteClick;
    private String textoClick = "Pulsa para continuar";
    private long espera;

    public MenuScreen(GalaxyInvaders juego) {
        this.juego = juego;
        spriteBatch = new SpriteBatch();

        texturaFondo = new Texture(Gdx.files.internal("sprites/fondoMenu.png"));
        fondo = new Sprite(texturaFondo);
        fondo.setPosition(185, Gdx.graphics.getHeight() / 4f);
        fondo.scale(1f);

        FreeTypeFontGenerator generadorFuente
                = new FreeTypeFontGenerator(Gdx.files.internal("fuentes/Pixelmania.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 60;
        parameter.color = Color.WHITE;
        fuenteClick = generadorFuente.generateFont(parameter);

        espera = TimeUtils.nanoTime();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();

        fondo.draw(spriteBatch);

        final GlyphLayout layoutClick = new GlyphLayout(fuenteClick, textoClick);

        fuenteClick.draw(spriteBatch, layoutClick,
                (Gdx.graphics.getWidth() - layoutClick.width) / 2f, Gdx.graphics.getHeight() / 1.8f);

        spriteBatch.end();

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
        texturaFondo.dispose();
        spriteBatch.dispose();
    }
}
