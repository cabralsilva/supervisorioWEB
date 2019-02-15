package model;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Agv implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private Long id;
	
	@NotEmpty
	@Column(nullable = false, length = 20)
	private String descricao;
	
	@NotEmpty
	@Column(nullable = false, length = 16)
	private String mac;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private TipoAgv tipoAgv;
	
	@Transient
	private StatusAgv atualStatus;
	
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@Transient
	private Date ultimaAtualizacao;
	
	@Transient
	private Long atualBateria;
	
	@Transient
	private String atualIntensidadeWifi;
	
	@Transient
	private String mensagemAlerta;
	
	@Transient
	private boolean sincronizando;
	
	@Transient
	private boolean sincronizacaoPendente;
	
	@Transient
	private int posicao;

	///////////////////////////////////////////////////////////////////////
	
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

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public TipoAgv getTipoAgv() {
		return tipoAgv;
	}

	public void setTipoAgv(TipoAgv tipoAgv) {
		this.tipoAgv = tipoAgv;
	}

	public StatusAgv getAtualStatus() {
		return atualStatus;
	}

	public void setAtualStatus(StatusAgv atualStatus) {
		this.atualStatus = atualStatus;
	}

	public Date getUltimaAtualizacao() {
		return ultimaAtualizacao;
	}

	public void setUltimaAtualizacao(Date ultimaAtualizacao) {
		this.ultimaAtualizacao = ultimaAtualizacao;
	}

	public Long getAtualBateria() {
		return atualBateria;
	}

	public void setAtualBateria(Long atualBateria) {
		this.atualBateria = atualBateria;
	}

	public String getAtualIntensidadeWifi() {
		return atualIntensidadeWifi;
	}

	public void setAtualIntensidadeWifi(String atualIntensidadeWifi) {
		this.atualIntensidadeWifi = atualIntensidadeWifi;
	}

	public String getMensagemAlerta() {
		return mensagemAlerta;
	}

	public void setMensagemAlerta(String mensagemAlerta) {
		this.mensagemAlerta = mensagemAlerta;
	}

	public boolean isSincronizando() {
		return sincronizando;
	}

	public void setSincronizando(boolean sincronizando) {
		this.sincronizando = sincronizando;
	}

	public boolean isSincronizacaoPendente() {
		return sincronizacaoPendente;
	}

	public void setSincronizacaoPendente(boolean sincronizacaoPendente) {
		this.sincronizacaoPendente = sincronizacaoPendente;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getPosicao() {
		return posicao;
	}

	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}	
	
	
	////////////////////////////////////////////////////////////////////////////


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((atualBateria == null) ? 0 : atualBateria.hashCode());
		result = prime * result + ((atualIntensidadeWifi == null) ? 0 : atualIntensidadeWifi.hashCode());
		result = prime * result + ((atualStatus == null) ? 0 : atualStatus.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((mac == null) ? 0 : mac.hashCode());
		result = prime * result + ((mensagemAlerta == null) ? 0 : mensagemAlerta.hashCode());
		result = prime * result + (sincronizacaoPendente ? 1231 : 1237);
		result = prime * result + (sincronizando ? 1231 : 1237);
		result = prime * result + ((tipoAgv == null) ? 0 : tipoAgv.hashCode());
		result = prime * result + ((ultimaAtualizacao == null) ? 0 : ultimaAtualizacao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Agv other = (Agv) obj;
		if (atualBateria == null) {
			if (other.atualBateria != null)
				return false;
		} else if (!atualBateria.equals(other.atualBateria))
			return false;
		if (atualIntensidadeWifi == null) {
			if (other.atualIntensidadeWifi != null)
				return false;
		} else if (!atualIntensidadeWifi.equals(other.atualIntensidadeWifi))
			return false;
		if (atualStatus == null) {
			if (other.atualStatus != null)
				return false;
		} else if (!atualStatus.equals(other.atualStatus))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (mac == null) {
			if (other.mac != null)
				return false;
		} else if (!mac.equals(other.mac))
			return false;
		if (mensagemAlerta == null) {
			if (other.mensagemAlerta != null)
				return false;
		} else if (!mensagemAlerta.equals(other.mensagemAlerta))
			return false;
		if (sincronizacaoPendente != other.sincronizacaoPendente)
			return false;
		if (sincronizando != other.sincronizando)
			return false;
		if (tipoAgv != other.tipoAgv)
			return false;
		if (ultimaAtualizacao == null) {
			if (other.ultimaAtualizacao != null)
				return false;
		} else if (!ultimaAtualizacao.equals(other.ultimaAtualizacao))
			return false;
		return true;
	}

	
	
	//////////////////////////////////////////////////////////////////////
	
	@Override
	public String toString() {
		return "Agv [id=" + id + ", descricao=" + descricao + ", mac=" + mac + ", tipoAgv=" + tipoAgv + ", atualStatus="
				+ atualStatus + ", ultimaAtualizacao=" + ultimaAtualizacao + ", atualBateria=" + atualBateria
				+ ", atualIntensidadeWifi=" + atualIntensidadeWifi + ", mensagemAlerta=" + mensagemAlerta
				+ ", sincronizando=" + sincronizando + ", sincronizacaoPendente=" + sincronizacaoPendente + "]";
	}
}
