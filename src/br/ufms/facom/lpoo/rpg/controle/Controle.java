package br.ufms.facom.lpoo.rpg.controle;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import br.ufms.facom.lpoo.rpg.personagem.Personagem;
import br.ufms.facom.lpoo.rpg.personagem.Posicao;
import br.ufms.facom.lpoo.rpg.personagem.Soldado;
import br.ufms.facom.lpoo.rpg.ui.RolePlayingGame;

public class Controle {

	private RolePlayingGame rpg;
	private Thread threadControle;

	//Array para valor da posição do inimigo 
	private static int MIN = 2;
	int aux[] = {-1, 1};
	int d;
	
	private List<Soldado> soldados = new LinkedList<Soldado>();
	int ali=0, ini=0;

	public Controle(RolePlayingGame rpg) {
		this.rpg = rpg;

		// Cria personagens em um canto do tabuleiro e outro em outro canto.
		soldados.add(new Soldado("Nokia", "Tijolão", 5, RolePlayingGame.MAX_X - 5, RolePlayingGame.MAX_Y - 2));
		soldados.get(0).setAtributos(4, 4, 1, 0);
		soldados.add(new Soldado("Siri", "Siri Robótico", 2, 3, 1));
		soldados.get(1).setAtributos(3, 1, 5, 1);
		soldados.add(new Soldado("Linkedin", "Currículo", 2, RolePlayingGame.MAX_X - 3, RolePlayingGame.MAX_Y - 2));
		soldados.get(2).setAtributos(2, 2, 5, 0);
		soldados.add(new Soldado("Beats", "Super Bass", 3, 5, 1));
		soldados.get(3).setAtributos(2, 3, 4, 1);
		soldados.add(new Soldado("Bill", "Microsoft", 3, RolePlayingGame.MAX_X - 4, RolePlayingGame.MAX_Y - 1));
		soldados.get(4).setAtributos(5, 1, 3, 0);
		soldados.add(new Soldado("Steve", "Apple", 5, 4, 0));
		soldados.get(5).setAtributos(3, 4, 2, 1);
           
		
		// Adiciona os personagens ao tabuleiro.
		rpg.addPersonagem(soldados.get(0));
		rpg.addPersonagem(soldados.get(1));
        rpg.addPersonagem(soldados.get(2));
        rpg.addPersonagem(soldados.get(3));
        rpg.addPersonagem(soldados.get(4));
        rpg.addPersonagem(soldados.get(5));
	}


	public void executaTurno() throws InterruptedException {	
		Posicao pos = new Posicao();
		Personagem p;
		
		//Variável auxiliar para analisar os aliados
		Soldado sAux = new Soldado("Nokia", "Tijolão", 5, 5000, 5000);
		sAux.setAtributos(4, 5, 0, 0);
		int dist[] = new int[3];
		
		for (Soldado s : soldados) {	
			/*
			*	
			*
			*
			*
			Vez dos aliados */
			if(s.getTipo() == 0) {
				rpg.info(String.format("Personagem %s, selecione sua nova posição!", s.getNome()));
				
				pos = rpg.selecionaPosicao();
								
				if (verificaPosicaoValida(pos, s)) {
					s.setX(pos.x);
					s.setY(pos.y);
					
					rpg.atualizaTabuleiro();

					rpg.info(String.format("Personagem %s, selecione um inimigo para atacar!", s.getNome()));
					
					p = rpg.selecionaPersonagem();
					
					
						
					if (p != s) {
						
						if(p.getTipo() == s.getTipo()) {
							rpg.erro("Você não pode atacar um aliado! Perdeu a vez."); 
							rpg.atualizaTabuleiro();	
						} else {
						
						//Arma de corpo a corpo - 100% de chance
						if(Math.abs(p.getX() - s.getX()) == 1 || Math.abs(p.getY() - s.getY()) == 1) {
							p.setVida(p.getVida() - 1);
							rpg.erro(p.getNome() + ": Aii man!");
						}
						//Arma à distância
						else if(calculaProbabilidadeDeAtaque(s, p)) {
							p.setVida(p.getVida() - 1);
							rpg.erro(p.getNome() + ": Essa doeu!");
						}
						//Ataque de arma à distância falhou
						else {
							rpg.erro("Nada aconteceu.");
						}
						
						//Se o inimigo morrer, ele será removido
						if(p.getVida() == 0) {
							rpg.erro("Boom! " + p.getNome() + " morreu! Muahahahahaahah");
							rpg.removePersonagem(p);
							soldados.remove(p);
							ini++;
							rpg.atualizaTabuleiro();
							
							if(ini==3){
								rpg.erro("Você venceu!!!");
								threadControle.interrupt();
							} else
								rpg.info(String.format("Nova rodada iniciada!"));
							return;
							}
						}
					} else {
						rpg.erro("Você não pode atacar você mesmo! Perdeu a vez.");
					}
					rpg.atualizaTabuleiro();
				 }else {
					rpg.erro("Posição inválida! Perdeu a vez.");
				 }
			
			/*
			*	
			*
			*
			*
			Vez dos inimigos */
			} else {
				rpg.info(String.format("Vez do %s!", s.getNome()));
				rpg.atualizaTabuleiro();
				
				//Analisar aliados mais próximos
				int i = 0;
				for (Soldado a : soldados) {
					//Pegar aliado com menor distância
					if(a.getTipo() == 0) {
						dist[i] = Math.abs(a.getX() - s.getX()) + Math.abs(a.getY() - s.getY());
						if(i > 0 && i < 4) {
							if(dist[i] < dist[i-1]) {
								sAux = a;
							}
						} else {
							sAux = a;
						}
						i++;
					}	
					
				}
				
				//Inimigo mover para perto do aliado
				Random rand = new Random();
				 do{
					 d = rand.nextInt(s.getVelocidade()) + 1;
					 pos.x = rand.nextInt(d) + 1;
					 System.out.println(pos.x);
					 pos.y = sAux.getY() + aux[rand.nextInt(MIN)]*(d + sAux.getX() - s.getX());
					 
				} while(verificaPosicaoValida(pos, s) == false);

				s.setX(pos.x);
				s.setY(pos.y);
				
				//Arma de corpo a corpo - 100% de chance
				if(Math.abs(sAux.getX() - s.getX()) == 1 || Math.abs(sAux.getY() - s.getY()) == 1) {
					sAux.setVida(sAux.getVida() - 1);
					rpg.erro(sAux.getNome() + ": Aii man!");
				}
				//Arma à distância
				else if(calculaProbabilidadeDeAtaque(s, sAux)) {
					sAux.setVida(sAux.getVida() - 1);
					rpg.erro(sAux.getNome() + ": Essa doeu!");
				}
				//Ataque de arma à distância falhou
				else {
					rpg.erro("Nada aconteceu.");
				}
						
				//Se o aliado morrer, ele será removido
				if(sAux.getVida() == 0) {
					rpg.erro("Boom! " + sAux.getNome() + " morreu! Muahahahahaahah");
					rpg.removePersonagem(sAux);
					soldados.remove(sAux);
					ini++;
					rpg.atualizaTabuleiro();
							
					if(ini==3){
						rpg.erro("Você venceu!!!");
						threadControle.interrupt();
					} else
						rpg.info(String.format("Nova rodada iniciada!"));
					return;
					}

				rpg.atualizaTabuleiro();
			}
		}
	}
	
	public boolean verificaPosicaoValida(Posicao pos, Personagem p) {
		boolean valido = true;

		//Validar se a distância de Manhatan é menor ou igual à velocidade do personagem
		if(Math.abs(p.getX() - pos.x) + Math.abs(p.getY() - pos.y) > p.getVelocidade()) {
			valido = false;
		}
		
		//Validar se a posição não está ocupada
		for (Soldado s : soldados) {
			if(s.getX() == pos.x && s.getY() == pos.y)
				valido = false;
		}
		
		//Validar se a posição não está fora da tela
		if(pos.x > RolePlayingGame.MAX_X || pos.y > RolePlayingGame.MAX_Y || pos.x < 0 || pos.y < 0) {
			return false;
		}
		return valido;
	}
	
	public boolean calculaProbabilidadeDeAtaque(Personagem aliado, Personagem inimigo) {	
		d = Math.abs(aliado.getX() - inimigo.getX()) + Math.abs(aliado.getY() - inimigo.getY());
		
		//Ataque só será efetivo se estiver no alcance da arma e se for maior que a defesa do inimigo
		if(d <= aliado.getArma().getAlcance() && aliado.getAtaque() > inimigo.getDefesa())
			return true;
		else 
			return false;
	}
}