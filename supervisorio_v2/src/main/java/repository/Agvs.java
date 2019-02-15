package repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import model.Agv;

public class Agvs implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Agv porId(Long id) {
		return manager.find(Agv.class, id);
	}
	
	public List<Agv> todos(){
		System.out.println("PEGANDO TODOS");
		return manager.createQuery("from Agv", Agv.class).getResultList();
	}
	
	public Agv salvar(Agv agv) {
		return manager.merge(agv);
	}
	
	public void remover(Agv agv) {
		agv = porId(agv.getId());
		manager.remove(agv);
	}
	
	public void teste() {
		System.out.println("TESTES INJCECTION");
	}
}
