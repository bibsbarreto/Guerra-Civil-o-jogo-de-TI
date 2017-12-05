package br.ufms.facom.lpoo.rpg.controle;

import java.util.LinkedList;
import java.util.List;
import br.ufms.facom.lpoo.rpg.personagem.Personagem;
import br.ufms.facom.lpoo.rpg.personagem.Posicao;
import br.ufms.facom.lpoo.rpg.personagem.Soldado;
import br.ufms.facom.lpoo.rpg.ui.RolePlayingGame;

public class Controle {

	private RolePlayingGame rpg;
	private List<Soldado> soldados = new LinkedList<Soldado>();


	public Controle(RolePlayingGame rpg) {
		this.rpg = rpg;

		// Cria personagens em um canto do tabuleiro e outro em outro canto.
		soldados.add(new Soldado("Nokia", "Tijolão", 5, RolePlayingGame.MAX_X - 5, RolePlayingGame.MAX_Y - 2));
		soldados.get(0).setAtributos(4, 5, 0);
		soldados.add(new Soldado("Linkedin", "Currículo", 2, RolePlayingGame.MAX_X - 3, RolePlayingGame.MAX_Y - 2));
		soldados.get(1).setAtributos(2, 2, 5);
		soldados.add(new Soldado("Siri", "Siri Robótico", 2, 3, 1));
		soldados.get(2).setAtributos(3, 1, 5);
		soldados.add(new Soldado("Beats", "Super Bass", 3, 5, 1));
		soldados.get(3).setAtributos(2, 3, 4);
		soldados.add(new Soldado("Steve", "Apple", 5, 4, 0));
		soldados.get(4).setAtributos(2, 5, 2);
		soldados.add(new Soldado("Bill", "Microsoft", 3, RolePlayingGame.MAX_X - 4, RolePlayingGame.MAX_Y - 1));
		soldados.get(5).setAtributos(5, 1, 3);
           
		
		// Adiciona os personagens ao tabuleiro.
		rpg.addPersonagem(soldados.get(0));
		rpg.addPersonagem(soldados.get(1));
        rpg.addPersonagem(soldados.get(2));
        rpg.addPersonagem(soldados.get(3));
        rpg.addPersonagem(soldados.get(4));
        rpg.addPersonagem(soldados.get(5));
	}


	public void executaTurno() throws InterruptedException {	
		Posicao pos;
		Personagem p;
		
		for (Soldado s : soldados) {
			if(s.getNome().equals("Bill") || s.getNome().equals("Nokia") || s.getNome().equals("Linkedin")) {
				rpg.info(String.format("Personagem %s, selecione sua nova posição!", s.getNome()));
				pos = rpg.selecionaPosicao();
								
				if (verificaPosicaoValida(pos, s)) {
					s.setX(pos.x);
					s.setY(pos.y);
					
					rpg.atualizaTabuleiro();

					rpg.info(String.format("Personagem %s, selecione um inimigo para atacar!", s.getNome()));
					p = rpg.selecionaPersonagem();
					if (p != s) {
						//Arma de corpo a corpo - 100% de chance
						if(Math.abs(p.getX() - s.getX()) == 1 || Math.abs(p.getY() - s.getY()) == 1)
							p.setVida(p.getVida() - 1);
						//Arma à distância
						else if(calculaProbabilidadeDeAtaque(s, p))
							p.setVida(p.getVida() - 1);
						//Ataque de arma à distância falhou
						else
							rpg.erro("Nada aconteceu.");
						
						//Se o inimigo morrer, ele será removido
						if(p.getVida() == 0) {
							rpg.erro("Boom! " + p.getNome() + " morreu! Muahahahahaahah");
							rpg.removePersonagem(p);
							soldados.remove(p);
							rpg.atualizaTabuleiro();
							break;
						}
					} else
						rpg.erro("Você não pode atacar você mesmo! Perdeu a vez.");

					rpg.atualizaTabuleiro();
				} else
					rpg.erro("Posição inválida! Perdeu a vez.");
			}
		}
	}
	
	public boolean verificaPosicaoValida(Posicao pos, Personagem p) {
		boolean valido = true;

		if(Math.abs(p.getX() - pos.x) > 1 || Math.abs(p.getY() - pos.y) > 1) {
			valido = false;
		}
		
		for (Soldado s : soldados) {
			if(s.getX() == pos.x && s.getY() == pos.y)
				valido = false;
		}
		return valido;
	}
	
	public boolean calculaProbabilidadeDeAtaque(Personagem aliado, Personagem inimigo) {	
		int d = Math.abs(aliado.getX() - inimigo.getX()) + Math.abs(aliado.getY() - inimigo.getY());
		
		double mAliado = (aliado.getAtaque()*10 + aliado.getDefesa()*1 + d*1);
		System.out.println(mAliado);
		double mInimigo = (inimigo.getAtaque()*1 + inimigo.getDefesa()*4 + d*7);
		System.out.println(mInimigo);
		
		if(mAliado >= mInimigo)
			return true;
		else 
			return false;
	}
}