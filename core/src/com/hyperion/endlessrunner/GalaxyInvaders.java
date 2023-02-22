package com.hyperion.endlessrunner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.hyperion.endlessrunner.pantallas.MainScreen;

public class GalaxyInvaders extends Game {


    @Override
    public void create() {
        this.setScreen(new MainScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    public void cambiaPantalla(Screen screen) {
        this.setScreen(screen);
    }
}
