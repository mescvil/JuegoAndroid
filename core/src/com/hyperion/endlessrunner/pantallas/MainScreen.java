package com.hyperion.endlessrunner.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.hyperion.endlessrunner.GalaxyInvaders;
import com.hyperion.endlessrunner.entidades.Alien;
import com.hyperion.endlessrunner.entidades.Disparo;
import com.hyperion.endlessrunner.entidades.Nave;

public class MainScreen implements Screen {

    public static int ANCHO;
    public static int ALTO;
    private GalaxyInvaders juego;
    private int puntuacion = 0;
    private int nAliens;
    public static int aliensMuertos = 0;
    private Nave nave;
    private Alien[][] aliens;
    private final int aliensAlto = 8;
    private final int aliensAncho = 6;
    private long ultimoDisparo;
    private Texture texturaNave;
    public Texture texturaFondo;
    public static Texture texturaOver;
    private Texture texturaBoton;
    private Texture texturaBala;
    private Texture texturaAlien;
    private Sound sonidoKill;
    private BitmapFont fuentePuntuacion;
    private BitmapFont fuenteAliens;
    public static SpriteBatch spriteBatch;
    private Sprite fondo;
    private Rectangle botonDiaparo;


    public MainScreen(GalaxyInvaders juego) {
        this.juego = juego;
        ALTO = Gdx.graphics.getHeight();
        ANCHO = Gdx.graphics.getWidth();
        spriteBatch = new SpriteBatch();

        cargaRecursos();
        configuraBotonDisparo();
        configuraFondo();
        creaAliens();

        nave = new Nave(texturaNave, texturaBala, botonDiaparo.getHeight());
    }

    private void cargaRecursos() {
        texturaNave = new Texture(Gdx.files.internal("sprites/nave.png"));
        texturaBoton = new Texture(Gdx.files.internal("sprites/boton.png"));
        texturaBala = new Texture(Gdx.files.internal("sprites/disparo_pequenio.png"));
        texturaAlien = new Texture(Gdx.files.internal("sprites/alien.png"));
        texturaOver = new Texture(Gdx.files.internal("sprites/fondoOver.png"));

        sonidoKill = Gdx.audio.newSound(Gdx.files.internal("sonidos/kill.wav"));

        FreeTypeFontGenerator generadorFuente = new FreeTypeFontGenerator(Gdx.files.internal("fuentes/Pixelmania.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.color = Color.RED;
        fuentePuntuacion = generadorFuente.generateFont(parameter);

        parameter.size = 40;
        parameter.color = Color.GREEN;
        fuenteAliens = generadorFuente.generateFont(parameter);
    }

    private void configuraFondo() {
        texturaFondo = new Texture(Gdx.files.internal("sprites/fondo.png"));
        fondo = new Sprite(texturaFondo);
        fondo.setPosition(185, ANCHO / 3f + 450f);
        fondo.scale(1.5f);
    }

    private void configuraBotonDisparo() {
        botonDiaparo = new Rectangle(0f, 0f, ANCHO, 150f);
    }

    private void creaAliens() {
        aliens = new Alien[aliensAlto][aliensAncho];
        for (int i = 0; i < aliensAlto; i++) {
            for (int j = 0; j < aliensAncho; j++) {
                aliens[i][j] = new Alien(texturaAlien,
                        new Vector2(
                                j * 135,
                                (ALTO - texturaAlien.getHeight() - 180) - i * (texturaAlien.getHeight() + 50)));
            }
        }
        Alien.velocidad = 200;
        nAliens = aliensAlto * aliensAncho;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();

        fondo.draw(spriteBatch);
        nave.dibuja(spriteBatch);

        for (int i = 0; i < aliensAlto; i++) {
            for (int j = 0; j < aliensAncho; j++) {
                Alien alien = aliens[i][j];
                if (alien.vivo) {
                    alien.dibuja(spriteBatch);
                }
            }
        }

        spriteBatch.draw(texturaBoton, botonDiaparo.x - 20f, botonDiaparo.y,
                botonDiaparo.getWidth() + 40f, botonDiaparo.getHeight());

        fuentePuntuacion.draw(spriteBatch, "Score: " + puntuacion, 50, ALTO - 50);
        fuenteAliens.draw(spriteBatch, "Aliens: " + nAliens, 50, ALTO - 100);
        spriteBatch.end();

        nave.actualizaPosicion();
        checkBotonDisparo();
        checkColisiones();
        moviemientoAliens();
    }

    private void checkColisiones() {
        for (Disparo disparo : nave.disparos) {
            if (disparo.activo)
                for (int i = 0; i < aliensAlto; i++) {
                    for (int j = 0; j < aliensAncho; j++) {
                        Alien alien = aliens[i][j];
                        if (alien.vivo
                                &&
                                disparo.sprite.getBoundingRectangle().overlaps(alien.sprite.getBoundingRectangle())) {

                            alien.vivo = false;
                            sonidoKill.play(1);
                            puntuacion += 100;
                            nAliens--;
                            aliensMuertos++;
                            Alien.velocidad += aliensMuertos * 0.3f;
                            disparo.setPosicion(0, 10000000);
                            j++;
                        }
                    }
                }
        }
    }

    // Comprobamos que el boton de disparo ha sido pulsado
    private void checkBotonDisparo() {
        if (TimeUtils.nanoTime() - ultimoDisparo > 1000000000 / 3) {
            if (Gdx.input.isTouched()) {
                Vector3 toque = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                float posicionBoton = Gdx.graphics.getHeight() - botonDiaparo.getHeight();


                if (toque.y <= ALTO && toque.y >= posicionBoton)
                    if (toque.x >= 0 && toque.x <= ANCHO) {
                        juego.cambiaPantalla(new FinJuegoScreen(juego));

                        nave.dispara();
                        ultimoDisparo = TimeUtils.nanoTime();
                    }
            }
        }
    }

    private void moviemientoAliens() {
        for (Alien[] alienArray : aliens) {
            for (Alien alien : alienArray) {
                if (alien.vivo)
                    alien.actualizaPosicion();
            }
        }

        if (Alien.cambio) {
            for (Alien[] alienArray : aliens) {
                for (Alien alien : alienArray) {
                    alien.actualizaAltura();
                }
            }
            Alien.cambio = false;
        }
    }

    @Override
    public void show() {
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
        texturaNave.dispose();
        texturaAlien.dispose();
        texturaBoton.dispose();
        texturaOver.dispose();
        spriteBatch.dispose();
    }
}
