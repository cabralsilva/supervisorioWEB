package model;

public enum Sentido {
	TOP("TOP"),
	RIGHT("RIGHT"),
	BOTTOM("BOTTOM"),
	LEFT("LEFT"),
	FRONT("FRONT"),;
	
	private String descricao;
	
	private Sentido(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
	
}
