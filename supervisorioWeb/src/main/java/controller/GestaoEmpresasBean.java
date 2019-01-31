package controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Empresa;
import model.TipoEmpresa;
import repository.Empresas;
import service.CadastroEmpresaService;
import util.FacesMessages;

@Named
@ViewScoped
public class GestaoEmpresasBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Empresas empresas;
	
	@Inject
	private CadastroEmpresaService cadastroEmpresa;
	
	@Inject
	private FacesMessages messages;
	
	private List<Empresa> todasEmpresas;
	private Empresa empresaEdicao = new Empresa();
	
	public void prepararNovoCadastro() {
		empresaEdicao = new Empresa();
	}
	
	public void salvar() {
		cadastroEmpresa.salvar(empresaEdicao);
		consultar();
		
		messages.info("Empresa salva com sucesso!");
	}
	
	public void consultar() {
		todasEmpresas = empresas.todas();
	}

	public List<Empresa> getTodasEmpresas() {
		return todasEmpresas;
	}
	
	public TipoEmpresa[] getTiposEmpresas() {
		return TipoEmpresa.values();
	}

	public Empresa getEmpresaEdicao() {
		return empresaEdicao;
	}

	public void setEmpresaEdicao(Empresa empresaEdicao) {
		this.empresaEdicao = empresaEdicao;
	}
	
}