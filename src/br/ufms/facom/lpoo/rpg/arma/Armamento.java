package br.ufms.facom.lpoo.rpg.arma;

public class Armamento implements Arma {
        
	public String nome;
    public int alcance;
    
    public Armamento(String nome, int alcance) {
    	this.nome = nome;
    	this.alcance = alcance;
    }
    
    @Override
    public void setAlcance(int alcance) {
    	this.alcance = alcance;
    }
        
	@Override
	public int getAlcance() {
		return alcance;
	}
        
    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String getNome() {
        return nome;
    }

	@Override
	public void setAlcance() {

	}

	@Override
	public void setNome() {	
		
	}

}
