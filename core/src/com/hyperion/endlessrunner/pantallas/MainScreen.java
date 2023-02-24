package com.hyperion.endlessrunner.pantallas;

import static com.hyperion.endlessrunner.Recursos.texturaAlien;
import static com.hyperion.endlessrunner.Recursos.texturaBala;
import static com.hyperion.endlessrunner.Recursos.texturaBoton;
import static com.hyperion.endlessrunner.Recursos.texturaFondo;
import static com.hyperion.endlessrunner.Recursos.texturaNave;
import static com.hyperion.endlessrunner.Recursos.texturaOver;
import static com.hyperion.endlessrunner.Recursos.texturaWin;
import static com.hyperion.endlessrunner.Recursos.sonidoWin;
import static com.hyperion.endlessrunner.Recursos.sonidoGameOver;
import static com.hyperion.endlessrunner.Recursos.sonidoKill;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.hyperion.endlessrunner.VariablesGlobales;
import com.hyperion.endlessrunner.entidades.Alien;
import com.hyperion.endlessrunner.entidades.Disparo;
import com.hyperion.endlessrunner.entidades.Nave;

public class MainScreen implements Screen {
    private final GalaxyInvaders juego;
    private int puntuacion = 0;
    private int nAliens;
    public static int aliensMuertos = 0;
    private final Nave nave;
    private Alien[][] aliens;
    private final int aliensAlto = 8;
    private final int aliensAncho = 6;
    private long ultimoDisparo;
    private BitmapFont fuentePuntuacion;
    private BitmapFont fuenteAliens;
    public static SpriteBatch spriteBatch;
    private Sprite fondo;
    private Rectangle botonDiaparo;
    private Rectangle colisionInferior;


    public MainScreen(GalaxyInvaders juego) {
        this.juego = juego;
        VariablesGlobales.ALTO = Gdx.graphics.getHeight();
        VariablesGlobales.ANCHO = Gdx.graphics.getWidth();
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
        texturaWin = new Texture(Gdx.files.internal("sprites/fondoWin.png"));
        texturaOver = new Texture(Gdx.files.internal("sprites/fondoOver.png"));

        sonidoKill = Gdx.audio.newSound(Gdx.files.internal("sonidos/kill.wav"));
        sonidoGameOver = Gdx.audio.newSound(Gdx.files.internal("sonidos/gameover.wav"));
        sonidoWin = Gdx.audio.newSound(Gdx.files.internal("sonidos/transicion.wav"));

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
        fondo.setPosition(185, VariablesGlobales.ANCHO / 3f + 450f);
        fondo.scale(1.5f);

        colisionInferior = new Rectangle(0, 0, VariablesGlobales.ANCHO, 400);
    }

    private void configuraBotonDisparo() {
        botonDiaparo = new Rectangle(0f, 0f, VariablesGlobales.ANCHO, 150f);
    }

    private void creaAliens() {
        aliens = new Alien[aliensAlto][aliensAncho];
        for (int i = 0; i < aliensAlto; i++) {
            for (int j = 0; j < aliensAncho; j++) {
                aliens[i][j] = new Alien(texturaAlien,
                        new Vector2(
                                j * 135,
                                (VariablesGlobales.ALTO - texturaAlien.getHeight() - 180) - i * (texturaAlien.getHeight() + 50)));
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

        fuentePuntuacion.draw(spriteBatch, "Score: " + puntuacion, 50, VariablesGlobales.ALTO - 50);
        fuenteAliens.draw(spriteBatch, "Aliens: " + nAliens, 50, VariablesGlobales.ALTO - 100);
        spriteBatch.end();

        nave.actualizaPosicion();
        checkBotonDisparo();
        checkColisiones();
        moviemientoAliens();
        checkEstadoPartida();
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
                            Alien.velocidad += aliensMuertos * 0.1f;
                            disparo.setPosicion(0, 10000000);
                            break;
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


                if (toque.y <= VariablesGlobales.ALTO && toque.y >= posicionBoton)
                    if (toque.x >= 0 && toque.x <= VariablesGlobales.ANCHO) {
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

    private void checkEstadoPartida() {
        /* Fail state */
        for (Alien[] aliensAncho : aliens) {
            for (Alien alien : aliensAncho) {
                if (alien.sprite.getBoundingRectangle().overlaps(colisionInferior) && alien.vivo) {
                    juego.cambiaPantalla(new GameOverScreen(juego));
                }
            }
        }

        /* Win state */
        if (nAliens <= 0) {
            juego.cambiaPantalla(new WinScreen(juego));
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
        texturaBala.dispose();
        texturaWin.dispose();
        spriteBatch.dispose();
    }
}
