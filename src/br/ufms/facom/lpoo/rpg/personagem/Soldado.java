package br.ufms.facom.lpoo.rpg.personagem;
import br.ufms.facom.lpoo.rpg.arma.Arma;
import br.ufms.facom.lpoo.rpg.arma.Armamento;

/**
 * Personagem soldado.
 * <p>
 * Este é apenas um exemplo que não respeita os requisitos do trabalho.
 * 
 * @author eraldo
 *
 */
public class Soldado implements Personagem {

	/**
	 * Arma do soldado.
	 */
	private Arma arma;

	/**
	 * Nível de vida do soldado.
	 */
	private int vida;

	/**
	 * Nome do soldado.
	 */
	private String nome;

	/**
	 * Posição do soldado no tabuleiro.
	 */
	private Posicao posicao;

	/**
	 * Cria um soldado com o nome dado.
	 * 
	 * @param nome
	 */
	public Soldado(String nome) {
		this.nome = nome;
		//arma = new Armamento(nomeDaArma, alcance);
		vida = 5;
		posicao = new Posicao();
	}

	/**
	 * Cria um soldado com o nome e posição dados.
	 * 
	 * @param nome
	 * @param x
	 * @param y
	 */
	public Soldado(String nome, String nomeDaArma, int alcance, int x, int y) {
		this(nome);
		arma = new Armamento(nomeDaArma, alcance);
		posicao = new Posicao(x, y);
	}

	@Override
	public int getDefesa() {
		return 0;
	}

	@Override
	public int getAtaque() {
		return 0;
	}

	@Override
	public int getVelocidade() {
		return 0;
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
}
