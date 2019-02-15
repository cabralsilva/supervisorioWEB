package model;

public enum TipoRota {
	LINEAR("Linear"),
	CIRCULAR("Circular");
	
	private String descricao;
	
	private TipoRota(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
	
}
