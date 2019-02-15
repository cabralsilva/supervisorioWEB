package controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import model.Agv;
import service.Start;

@Named
@ViewScoped
public class AgvBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public void buscarTodosAgvs() {
		for (Agv agv : Start.lstAGVS) {
			System.out.println(agv);
		}
	}

}