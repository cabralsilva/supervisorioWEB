package model;

import java.util.ArrayList;
import java.util.List;

public class Area {
	private Ponto pontoInicial;
	private Ponto pontoFinal;
	
	private Sentido pontoAtual;
	private Sentido pontoFuturo;
	private Sentido sentido;
	
	List<Ponto> listPonto;
	
	private int xMin;
	private int xMax;
	private int yMin;
	private int yMax;
	
	private int pesoLeft;
	private int pesoTop;
	private int pesoBottom;
	private int pesoRight;
	
	public static final int maxLargura = 30;
	public static final int raio = 3;
	
	public Area() {
		super();
		listPonto = new ArrayList<>();

	}
	public Ponto getPontoInicial() {
		return pontoInicial;
	}
	public void setPontoInicial(Ponto pontoInicial) {
		this.pontoInicial = pontoInicial;
	}
	public Ponto getPontoFinal() {
		return pontoFinal;
	}
	public void setPontoFinal(Ponto pontoFinal) {
		this.pontoFinal = pontoFinal;
	}
	public Sentido getPontoAtual() {
		return pontoAtual;
	}
	public void setPontoAtual(Sentido pontoAtual) {
		this.pontoAtual = pontoAtual;
	}
	
	public List<Ponto> getListPonto() {
		return listPonto;
	}
	public void setListPonto(List<Ponto> listPonto) {
		this.listPonto = listPonto;
	}
	public int getxMin() {
		return xMin;
	}
	public void setxMin(int xMin) {
		this.xMin = xMin;
	}
	public int getxMax() {
		return xMax;
	}
	public void setxMax(int xMax) {
		this.xMax = xMax;
	}
	public int getyMin() {
		return yMin;
	}
	public void setyMin(int yMin) {
		this.yMin = yMin;
	}
	public int getyMax() {
		return yMax;
	}
	public void setyMax(int yMax) {
		this.yMax = yMax;
	}
	public Sentido getPontoFuturo() {
		return pontoFuturo;
	}
	public void setPontoFuturo(Sentido pontoFuturo) {
		this.pontoFuturo = pontoFuturo;
	}
	public int getPesoLeft() {
		return pesoLeft;
	}
	public void setPesoLeft(int pesoLeft) {
		this.pesoLeft = pesoLeft;
	}
	public int getPesoTop() {
		return pesoTop;
	}
	public void setPesoTop(int pesoTop) {
		this.pesoTop = pesoTop;
	}
	public int getPesoBottom() {
		return pesoBottom;
	}
	public void setPesoBottom(int pesoBottom) {
		this.pesoBottom = pesoBottom;
	}
	public int getPesoRight() {
		return pesoRight;
	}
	public void setPesoRight(int pesoRight) {
		this.pesoRight = pesoRight;
	}
	
	
	public Sentido getSentido() {
		return sentido;
	}
	public void setSentido(Sentido sentido) {
		this.sentido = sentido;
	}
	@Override
	public String toString() {
		return "Area [pontoInicial=" + pontoInicial + ", pontoFinal=" + pontoFinal + ", pontoAtual=" + pontoAtual
				+ ", pontoFuturo=" + pontoFuturo + ", sentido=" + sentido + ", xMin=" + xMin + ", xMax=" + xMax
				+ ", yMin=" + yMin + ", yMax=" + yMax + ", pesoLeft=" + pesoLeft + ", pesoTop=" + pesoTop
				+ ", pesoBottom=" + pesoBottom + ", pesoRight=" + pesoRight + "]";
	}
	
	
	
}
