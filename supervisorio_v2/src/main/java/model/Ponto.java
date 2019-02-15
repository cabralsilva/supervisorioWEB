package model;

public class Ponto {
	private int x;
	private int y;
	
	private double angulo;
	
	public Ponto(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	
	
	public double getAngulo() {
		return angulo;
	}
	
	public void setAngulo(double angulo) {
		this.angulo = angulo;
	}
	
	public void setAngulo(Ponto pontoFuturo) {
		double catetoOposto = Math.abs(this.y - pontoFuturo.getY());
		double catetoAdjacente = Math.abs(this.x - pontoFuturo.getX());
		
		this.angulo = (double) Math.toDegrees(Math.atan(catetoOposto/catetoAdjacente));	
		
		
		if(pontoFuturo.getX() >= this.x && pontoFuturo.getY() <= this.y) {
//			1º QUADRANTE
			if (pontoFuturo.getX() == this.x && pontoFuturo.getY() != this.y) this.angulo = 90;
			else if (pontoFuturo.getY() == this.y && pontoFuturo.getX() != this.x) this.angulo = 0;
			
			this.angulo = - this.angulo;
			
		}
		
		if(pontoFuturo.getX() <= this.x && pontoFuturo.getY() <= this.y) {
//			2º QUADRANTE
			if (pontoFuturo.getX() == this.x && pontoFuturo.getY() != this.y) this.angulo = 90;
			else if (pontoFuturo.getY() == this.y && pontoFuturo.getX() != this.x) this.angulo = 0;
			
			this.angulo = - (180 - this.angulo);
		}
		
		if(pontoFuturo.getX() <= this.x && pontoFuturo.getY() >= this.y) {
//			3º QUADRANTE
			if (pontoFuturo.getX() == this.x && pontoFuturo.getY() != this.y) this.angulo = 90;
			else if (pontoFuturo.getY() == this.y && pontoFuturo.getX() != this.x) this.angulo = 0;
			this.angulo = - (180 + this.angulo);
		}
		
		if(pontoFuturo.getX() > this.x && pontoFuturo.getY() > this.y) {
//			4º QUADRANTE
			if (pontoFuturo.getX() == this.x && pontoFuturo.getY() != this.y) this.angulo = 90;
			else if (pontoFuturo.getY() == this.y && pontoFuturo.getX() != this.x) this.angulo = 0;
			this.angulo = - (360 - this.angulo);
		}
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		Ponto other = (Ponto) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return "Ponto [x=" + x + ", y=" + y + ", angulo=" + angulo + "]";
	}
	
	
}
