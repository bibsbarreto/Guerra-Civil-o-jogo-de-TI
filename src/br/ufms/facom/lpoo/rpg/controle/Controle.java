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

		// Cria um personagem em um canto do tabuleiro e outro em outro canto.
		soldados.add(new Soldado("Steve Jobs", "Apple", 5, 4, 1));
		soldados.add(new Soldado("Bill Gates", "Microsoft", 3, RolePlayingGame.MAX_X - 4, RolePlayingGame.MAX_Y - 2));
		soldados.add(new Soldado("Nokia", "Tijolão", 5,RolePlayingGame.MAX_X - 5, RolePlayingGame.MAX_Y - 1));
		soldados.add(new Soldado("Linkedin", "Currículo", 2, RolePlayingGame.MAX_X - 3, RolePlayingGame.MAX_Y - 1));
		soldados.add(new Soldado("Siri", "Siri Robótico", 2, 3, 0));
		soldados.add(new Soldado("Beats", "Super Bass", 3, 5, 0));
           
		
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
			if(s.getNome().equals("Bill Gates") || s.getNome().equals("Nokia") || s.getNome().equals("Linkedin")) {
				rpg.info(String.format("Personagem %s, selecione sua nova posição!", s.getNome()));
				pos = rpg.selecionaPosicao();
								
				if (verificaPosicaoValida(pos)) {
					s.setX(pos.x);
					s.setY(pos.y);
					
					rpg.atualizaTabuleiro();

					rpg.info(String.format("Personagem %s, selecione um inimigo para atacar!", s.getNome()));
					p = rpg.selecionaPersonagem();
					if (p != s)
						p.setVida(p.getVida() - 1);
					else
						rpg.erro("Você não pode atacar você mesmo! Perdeu a vez.");

					rpg.atualizaTabuleiro();
				} else
					rpg.erro("Posição inválida! Perdeu a vez.");
			}
		}
	}
	
	public boolean verificaPosicaoValida(Posicao pos) {
		for (Soldado s : soldados) {
			if(s.getX() == pos.x && s.getY() == pos.y)
				return false;
		}
		return true;
	}
}