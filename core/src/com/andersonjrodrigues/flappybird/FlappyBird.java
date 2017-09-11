package com.andersonjrodrigues.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlappyBird extends ApplicationAdapter {

	private SpriteBatch batch;
	private Texture[] passaros;
    private Texture fundo;

    //configuração para o app
    private int larguraDispositivo = 0;
    private int alturaDispositivo = 0;
    private int posicaoInicialVertical = 0;

    private float variacao = 0;
    private float velociadadeQueda = 0;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

        fundo = new Texture("fundo.png");

        passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
        passaros[1] = new Texture("passaro2.png");
        passaros[2] = new Texture("passaro3.png");

        larguraDispositivo = Gdx.graphics.getWidth();
        alturaDispositivo = Gdx.graphics.getHeight();
        posicaoInicialVertical = alturaDispositivo / 2;

	}

	@Override
	public void render () {
        variacao += Gdx.graphics.getDeltaTime() * 10;
        velociadadeQueda++;

        if(variacao > 2) {
            variacao = 0;
        }

        if(Gdx.input.justTouched()){
            velociadadeQueda = -10;
        }

        if(posicaoInicialVertical > 0) {
            posicaoInicialVertical -= velociadadeQueda;
        }

		batch.begin();

        batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
		batch.draw(passaros[(int)variacao], 30, posicaoInicialVertical);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
