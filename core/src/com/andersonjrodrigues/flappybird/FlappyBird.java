package com.andersonjrodrigues.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

	private SpriteBatch batch;
	private Texture[] passaros;
    private Texture fundo;
    private Texture canoBaixo;
    private Texture canoTopo;
    private BitmapFont fonte;

    private Random numeroRandomico;

    //configuração para o app
    private int larguraDispositivo = 0;
    private int alturaDispositivo = 0;
    private int posicaoInicialVertical = 0;
    private int alturaEntreCanosRandomica = 0;
    private int estadoJogo = 0;
    private int pontuacao = 0;

    private float variacao = 0;
    private float velociadadeQueda = 0;
    private float posicaoMovimentoCanoHorizontal = 0;
    private float espacoEntreCanos = 0;
    private float deltaTime = 0;

    private boolean marcouPonto;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

        fundo = new Texture("fundo.png");

        fonte = new BitmapFont();
        fonte.setColor(Color.WHITE);
        fonte.getData().setScale(6);

        passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
        passaros[1] = new Texture("passaro2.png");
        passaros[2] = new Texture("passaro3.png");

        canoBaixo = new Texture("cano_baixo.png");
        canoTopo = new Texture("cano_topo.png");

        larguraDispositivo = Gdx.graphics.getWidth();
        alturaDispositivo = Gdx.graphics.getHeight();
        posicaoInicialVertical = alturaDispositivo / 2;
        posicaoMovimentoCanoHorizontal = larguraDispositivo;
        espacoEntreCanos = 400;

        numeroRandomico = new Random();
	}

	@Override
	public void render () {
        deltaTime = Gdx.graphics.getDeltaTime();
        variacao += deltaTime * 10;

        if (variacao > 2) {
            variacao = 0;
        }

        if(estadoJogo == 0){
            if(Gdx.input.justTouched()){
                estadoJogo = 1;
            }
        } else {
            velociadadeQueda++;
            posicaoMovimentoCanoHorizontal -= deltaTime * 200;

            if (posicaoMovimentoCanoHorizontal < -canoBaixo.getWidth()) {
                posicaoMovimentoCanoHorizontal = larguraDispositivo;
                alturaEntreCanosRandomica = numeroRandomico.nextInt(400) - 200;
                marcouPonto = false;
            }

            if (Gdx.input.justTouched()) {
                velociadadeQueda = -10;
            }

            if (posicaoInicialVertical > 0) {
                posicaoInicialVertical -= velociadadeQueda;
            }

            //verifica pontuação do jogo
            if(posicaoMovimentoCanoHorizontal < 120){
                if(!marcouPonto) {
                    pontuacao++;
                    marcouPonto = true;
                }
            }
        }

		batch.begin();

        batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
        batch.draw(canoTopo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + alturaEntreCanosRandomica);
        batch.draw(canoBaixo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaEntreCanosRandomica);
		batch.draw(passaros[(int)variacao], 150, posicaoInicialVertical);

        fonte.draw(batch, String.valueOf(pontuacao), larguraDispositivo / 2, alturaDispositivo - 50);

		batch.end();
	}
	
	@Override
	public void dispose () {
        passaros[0].dispose();
        passaros[1].dispose();
        passaros[2].dispose();
        fundo.dispose();
        canoBaixo.dispose();
        canoTopo.dispose();
		batch.dispose();
	}
}
