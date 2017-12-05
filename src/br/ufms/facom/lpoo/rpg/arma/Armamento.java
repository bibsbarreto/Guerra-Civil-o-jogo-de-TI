package br.ufms.facom.lpoo.rpg.arma;

/**
 * Implementa uma arma: faca.
 * <p>
 * Esta implementação é apenas um exemplo que não segue as especificações do
 * trabalho.
 * 
 * @author eraldo
 *
 */
public class Armamento implements Arma {
        
	public String nome;
    public int alcance;
    
    public Armamento(String nome, int alcance) {
    	this.nome = nome;
    	this.alcance = alcance;
    }
    
    @Override
    public void setAlcance() {
    	this.alcance = alcance;
    }
        
	@Override
	public int getAlcance() {
		return alcance;
	}
        
    @Override
    public void setNome() {
        this.nome = nome;
    }

    @Override
    public String getNome() {
        return nome;
    }

}
