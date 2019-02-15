package model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Rota implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private Long id;
	
	@NotEmpty
	@Column(nullable = false, length = 20)
	private String descricao;
	
	private String cor;
	
	@Enumerated(EnumType.STRING)
	private TipoRota tipo;
	
	@Transient
	private List<Ponto> lstPontos;

	public List<Ponto> getLstPontos() {
		return lstPontos;
	}

	public void setLstPontos(List<Ponto> lstPontos) {
		this.lstPontos = lstPontos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
}
