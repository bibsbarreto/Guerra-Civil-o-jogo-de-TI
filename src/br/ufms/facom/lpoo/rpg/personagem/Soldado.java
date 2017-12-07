package br.ufms.facom.lpoo.rpg.personagem;
import br.ufms.facom.lpoo.rpg.arma.Arma;
import br.ufms.facom.lpoo.rpg.arma.Armamento;

public class Soldado implements Personagem {

	private Arma arma;
	private int vida;
	private String nome;
	private Posicao posicao;
	
	//Atributos
	private int ataque;
	private int defesa;
	private int velocidade;
	private int tipo; // 0 = Aliados / 1 = Inimigos;

	public Soldado(String nome) {
		this.nome = nome;
		vida = 5;
		posicao = new Posicao();
	}

	public Soldado(String nome, String nomeDaArma, int alcance, int x, int y) {
		this(nome);
		arma = new Armamento(nomeDaArma, alcance);
		posicao = new Posicao(x, y);
	}

	@Override
	public void setAtributos(int ataque, int defesa, int velocidade, int tipo) {
		this.defesa = defesa;
		this.ataque = ataque;
		this.velocidade = velocidade;
		this.tipo = tipo;
	}
	
	@Override
	public int getDefesa() {
		return defesa;
	}

	@Override
	public int getAtaque() {
		return ataque;
	}

	@Override
	public int getVelocidade() {
		return velocidade;
	}
	
	@Override
	public int getVida() {
		return vida;
	}

	@Override
	public Arma getArma() {
		return arma;
	}

	@Override
	public String getNome() {
		return nome;
	}

	@Override
	public int getX() {
		return posicao.x;
	}

	@Override
	public int getY() {
		return posicao.y;
	}

	@Override
	public void setX(int x) {
		posicao.x = x;
	}

	@Override
	public void setY(int y) {
		posicao.y = y;
	}

	@Override
	public void setVida(int vida) {
		this.vida = vida;
	}
	
	public int getTipo() {
		return tipo;
	}

}
