package br.ufms.facom.lpoo.rpg.ui;

import br.ufms.facom.lpoo.rpg.personagem.Personagem;
import br.ufms.facom.lpoo.rpg.personagem.Posicao;

enum EstadoSelecao {

	DESOCUPADO,

	PERSONAGEM,

	POSICAO
}

class SelecaoTabuleiro {

	EstadoSelecao estado;

	Personagem personagem;

	Posicao pos;

	SelecaoTabuleiro() {
		estado = EstadoSelecao.DESOCUPADO;
	}
}