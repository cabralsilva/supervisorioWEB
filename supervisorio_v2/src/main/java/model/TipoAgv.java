package model;

public enum TipoAgv {
	REBOCADOR("Rebocador"),
	CARREGADOR("CARREGADOR");
	
	private String descricao;
	
	private TipoAgv(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
	
}
