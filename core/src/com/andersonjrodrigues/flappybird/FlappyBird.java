package com.andersonjrodrigues.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

	private SpriteBatch batch;
	private Texture[] passaros;
    private Texture fundo;
    private Texture canoBaixo;
    private Texture canoTopo;
    private Texture gameOver;
    private BitmapFont fonte;
    private BitmapFont mensagem;
    private Circle passaroCirculo;
    private Rectangle retanguloCanoTopo;
    private Rectangle retanguloCanoBaixo;
    //private ShapeRenderer shape;

    private Random numeroRandomico;

    private int estadoJogo = 0; //0: Jogo não iniciado - 1: Jogo Iniciado - 2: Game Over
    private int pontuacao = 0;
    private int variacaoDelta = 0;

    //configuração para o app
    private float larguraDispositivo = 0;
    private float alturaDispositivo = 0;
    private float posicaoInicialVertical = 0;
    private float alturaEntreCanosRandomica = 0;
    private float variacao = 0;
    private float velociadadeQueda = 0;
    private float posicaoMovimentoCanoHorizontal = 0;
    private float espacoEntreCanos = 0;
    private float deltaTime = 0;
    private boolean marcouPonto;
    private boolean alterouVelocidade;

    private OrthographicCamera camera;
    private Viewport viewport;
    private final float VIRTUAL_WIDTH = 768;
    private final float VIRTUAL_HEIGHT = 1024;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
        //shape = new ShapeRenderer();

        fundo = new Texture("fundo.png");
        passaroCirculo = new Circle();
        retanguloCanoTopo = new Rectangle();
        retanguloCanoBaixo = new Rectangle();

        fonte = new BitmapFont();
        fonte.setColor(Color.WHITE);
        fonte.getData().setScale(6);

        mensagem = new BitmapFont();
        mensagem.setColor(Color.WHITE);
        mensagem.getData().setScale(3);

        passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
        passaros[1] = new Texture("passaro2.png");
        passaros[2] = new Texture("passaro3.png");

        canoBaixo = new Texture("cano_baixo.png");
        canoTopo = new Texture("cano_topo.png");
        gameOver = new Texture("game_over.png");

        //Configuraçoes da camera
        camera = new OrthographicCamera();
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

        larguraDispositivo = VIRTUAL_WIDTH;
        alturaDispositivo = VIRTUAL_HEIGHT;
        posicaoInicialVertical = alturaDispositivo / 2;
        posicaoMovimentoCanoHorizontal = larguraDispositivo;
        espacoEntreCanos = 300;
        variacaoDelta = 200;

        numeroRandomico = new Random();
	}

	@Override
	public void render () {
        camera.update();

        //Limpar Frames anteriores
        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_COLOR_BUFFER_BIT);

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

            if (posicaoInicialVertical > 0) {
                posicaoInicialVertical -= velociadadeQueda;
            }

            if(pontuacao >= 7 && pontuacao % 7 == 0){
                if(!alterouVelocidade) {
                    variacaoDelta += 50;
                    alterouVelocidade = true;
                }
            }

            if(estadoJogo == 1){
                posicaoMovimentoCanoHorizontal -= deltaTime * variacaoDelta;

                if (Gdx.input.justTouched()) {
                    velociadadeQueda = -10;
                }

                if (posicaoMovimentoCanoHorizontal < -canoBaixo.getWidth()) {
                    posicaoMovimentoCanoHorizontal = larguraDispositivo;
                    alturaEntreCanosRandomica = numeroRandomico.nextInt(600) - 300;
                    marcouPonto = false;
                    alterouVelocidade = false;
                }

                //verifica pontuação do jogo
                if(posicaoMovimentoCanoHorizontal < 120){
                    if(!marcouPonto) {
                        pontuacao++;
                        marcouPonto = true;
                    }
                }
            }else{//Reinicia o jogo quando Game Over
                if(Gdx.input.justTouched()) {
                    estadoJogo = 0;
                    pontuacao = 0;
                    velociadadeQueda = 0;
                    posicaoInicialVertical = alturaDispositivo / 2;
                    posicaoMovimentoCanoHorizontal = larguraDispositivo;
                    variacaoDelta = 200;
                }
            }
        }

        //Configurando projecao da camera
        batch.setProjectionMatrix(camera.combined);

		batch.begin();
        batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
        batch.draw(canoTopo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + alturaEntreCanosRandomica);
        batch.draw(canoBaixo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaEntreCanosRandomica);
		batch.draw(passaros[(int)variacao], 150, posicaoInicialVertical);
        fonte.draw(batch, String.valueOf(pontuacao), larguraDispositivo / 2, alturaDispositivo - 50);

        if(estadoJogo == 2){
            batch.draw(gameOver, larguraDispositivo / 2 - gameOver.getWidth() / 2, alturaDispositivo / 2);
            mensagem.draw(batch, "Toque para Reiniciar", larguraDispositivo / 2 - 200, alturaDispositivo / 2 - gameOver.getHeight() / 2);
        }

		batch.end();

        passaroCirculo.set(
                150 + passaros[(int)variacao].getWidth() / 2,
                posicaoInicialVertical + passaros[(int)variacao].getHeight() / 2,
                passaros[(int)variacao].getWidth() / 2
        );
        retanguloCanoBaixo = new Rectangle(
                posicaoMovimentoCanoHorizontal,
                alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaEntreCanosRandomica,
                canoBaixo.getWidth(),
                canoBaixo.getHeight()
        );
        retanguloCanoTopo = new Rectangle(
                posicaoMovimentoCanoHorizontal,
                alturaDispositivo / 2 + espacoEntreCanos / 2 + alturaEntreCanosRandomica,
                canoTopo.getWidth(),
                canoTopo.getHeight()
        );

        //Desenha as as formas
        //shape.begin(ShapeRenderer.ShapeType.Filled);
        //shape.circle(passaroCirculo.x, passaroCirculo.y, passaroCirculo.radius);
        //shape.rect(retanguloCanoBaixo.x, retanguloCanoBaixo.y, retanguloCanoBaixo.width, retanguloCanoBaixo.height);
        //shape.rect(retanguloCanoTopo.x, retanguloCanoTopo.y, retanguloCanoTopo.width, retanguloCanoTopo.height);
        //shape.setColor(Color.RED);
        //shape.end();

        //teste de colisao
        if(Intersector.overlaps(passaroCirculo, retanguloCanoBaixo) || Intersector.overlaps(passaroCirculo, retanguloCanoTopo)
                || posicaoInicialVertical <= 0 || posicaoInicialVertical >= alturaDispositivo){
            estadoJogo = 2;
        }
	}

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
	public void dispose () {
        passaros[0].dispose();
        passaros[1].dispose();
        passaros[2].dispose();
        mensagem.dispose();
        fonte.dispose();
        fundo.dispose();
        canoBaixo.dispose();
        canoTopo.dispose();
		batch.dispose();
	}
}
