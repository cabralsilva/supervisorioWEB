package controller;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.imageio.ImageIO;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import model.Area;
import model.Ponto;
import model.Sentido;
import service.Start;

@Named
@ApplicationScoped
public class MapaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static boolean ok = false;

	List<Ponto> listPontoBloquados = new ArrayList<>();



	/*
	 * RETURN TRUE se o ponto inicial representa uma faixa(bordas brancas e faixa
	 * preta no meio) RETURN FALSE se o ponto não encontra uma faixa preta no meio
	 */
	private Ponto validarPrimeiroPonto(Ponto pontoInicial) {
		// REALIZAR ANALISE NO EIXO X CASO O Y RETORNO PONTO INVÁLIDO

		// 1º VERIFICAR SE A LARGURA VAI ULTRAPASSAR AS DIMENSÕES DA IMAGEM
		// NÃO FINALIZADO: ATUALMENTE SE A LARGURA ULTRAPASSAR ELE ZERA A LARGURA A SER
		// ANALISADA E NÃO REALIZA O MAPEADMENTO NESSES Ponto
		int maximaLarguraLinha = Area.maxLargura;
		if (pontoInicial.getY() + maximaLarguraLinha > Start.imagemMapa.getHeight())
			maximaLarguraLinha = 0;
		////////////////////////////////////////////

		int Yy = pontoInicial.getY();
		for (; Yy < pontoInicial.getY() + maximaLarguraLinha; Yy++) {
			Color c2 = new Color(Start.imagemMapa.getRGB(pontoInicial.getX(), Yy));
			if (c2.getRed() < 160 && c2.getGreen() < 160 && c2.getBlue() < 160) {
//				Start.imagemMapa.setRGB(pontoInicial.getX(), Yy, new Color(0, 0, 100).getRGB());
			} else {
				return new Ponto(pontoInicial.getX(), (pontoInicial.getY() + ((int) (Yy - pontoInicial.getY()) / 2)));
			}
		}

		return null;
	}

	private Area encontrarArea(Ponto pontoInicial, Area areaAnterior) throws Exception {

		
		
		Area areaReturn = new Area();
		

		if (areaAnterior == null) {
			areaReturn.setPontoInicial(validarPrimeiroPonto(pontoInicial));
			if (areaReturn.getPontoInicial() == null) {
				throw new Exception("PONTO INICIAL INVÁLIDO");
			}

			areaReturn.setxMin(pontoInicial.getX());
			areaReturn.setxMax(pontoInicial.getX() + (2 * Area.raio) - 1);
			areaReturn.setyMin(areaReturn.getPontoInicial().getY() - Area.raio);
			areaReturn.setyMax(areaReturn.getPontoInicial().getY() + Area.raio - 1);
			areaReturn.setPontoAtual(Sentido.LEFT);// VALIDAR CONFORME O LADO DO PONTO INICIAL ENCONTRADO, PODE SER
														// QUE SEJA O TOP
		} else {
			areaReturn.setPontoInicial(pontoInicial);
			switch (areaAnterior.getPontoFuturo()) {
			case TOP:
				areaReturn.setxMin(pontoInicial.getX() - Area.raio);
				areaReturn.setxMax(pontoInicial.getX() + Area.raio);
				areaReturn.setyMin(pontoInicial.getY() - (2 * Area.raio));
				areaReturn.setyMax(pontoInicial.getY());
				areaReturn.setPontoAtual(Sentido.BOTTOM);
				break;
			case RIGHT:
				areaReturn.setxMin(pontoInicial.getX());
				areaReturn.setxMax(pontoInicial.getX() + (2 * Area.raio));
				areaReturn.setyMin(pontoInicial.getY() - Area.raio);
				areaReturn.setyMax(pontoInicial.getY() + Area.raio);

				areaReturn.setPontoAtual(Sentido.LEFT);
				break;
			case BOTTOM:
				areaReturn.setxMin(pontoInicial.getX() - Area.raio);
				areaReturn.setxMax(pontoInicial.getX() + Area.raio);
				areaReturn.setyMin(pontoInicial.getY());
				areaReturn.setyMax(pontoInicial.getY() + (2 * Area.raio));

				areaReturn.setPontoAtual(Sentido.TOP);
				break;
			case LEFT:
				areaReturn.setxMin(pontoInicial.getX() - (2 * Area.raio));
				areaReturn.setxMax(pontoInicial.getX());
				areaReturn.setyMin(pontoInicial.getY() - Area.raio);
				areaReturn.setyMax(pontoInicial.getY() + Area.raio);

				areaReturn.setPontoAtual(Sentido.RIGHT);
				break;

			default:
				break;
			}
		}
		
		for (int contY = areaReturn.getyMin(); contY <= areaReturn.getyMax(); contY++) {
			for (int contX = areaReturn.getxMin(); contX <= areaReturn.getxMax(); contX++) {
//					Color c = new Color(Start.imagemMapa.getRGB(contX, contY));
//					Start.imagemMapa.setRGB(contX,contY, new Color(200, 0, 0).getRGB());
//				listPontoBloquados.add(new Ponto(contX, contY));
				areaReturn.getListPonto().add(new Ponto(contX, contY));
			}
		}
		
		
		for (Ponto ponto : areaReturn.getListPonto()) {
			
			if (ponto.getY() == areaReturn.getyMin() && areaReturn.getPontoAtual() != Sentido.TOP) {
				// AVALIANDO O TOP
//				System.out.println("TOP: " + ponto.getX() + "     " + ponto.getY() );
				if (ponto.getX() < Start.imagemMapa.getWidth() && ponto.getX()  > 10
						&& ponto.getY() < Start.imagemMapa.getHeight()&& ponto.getY() > 10) {
					Color c = new Color(Start.imagemMapa.getRGB(ponto.getX(), ponto.getY()));
					if (c.getRed() < 160 && c.getGreen() < 160 && c.getBlue() < 160) {
//						System.out.println("incremento top");
						areaReturn.setPesoTop(areaReturn.getPesoTop() + 1);
					} else {
//						Start.imagemMapa.setRGB(ponto.getX(), ponto.getY(), new Color(200, 0, 0).getRGB());
					}
				}

			}

			if (ponto.getX() == areaReturn.getxMax() && areaReturn.getPontoAtual() != Sentido.RIGHT) {
				// AVALIANDO O RIGHT
				if (ponto.getX() < Start.imagemMapa.getWidth() && ponto.getX()  > 10
						&& ponto.getY() < Start.imagemMapa.getHeight()&& ponto.getY() > 10) {
					Color c = new Color(Start.imagemMapa.getRGB(ponto.getX(), ponto.getY()));
					if (c.getRed() < 160 && c.getGreen() < 160 && c.getBlue() < 160) {
						areaReturn.setPesoRight(areaReturn.getPesoRight() + 1);
					} else {
//						Start.imagemMapa.setRGB(ponto.getX(), ponto.getY(), new Color(200, 200, 0).getRGB());
					}
				}
			}

			if (ponto.getY() == areaReturn.getyMax() && areaReturn.getPontoAtual() != Sentido.BOTTOM) {
				// AVALIANDO O BOTTOM
				if (ponto.getX() < Start.imagemMapa.getWidth() && ponto.getX()  > 10
						&& ponto.getY() < Start.imagemMapa.getHeight()&& ponto.getY() > 10) {
					Color c = new Color(Start.imagemMapa.getRGB(ponto.getX(), ponto.getY()));

					if (c.getRed() < 160 && c.getGreen() < 160 && c.getBlue() < 160) {
						areaReturn.setPesoBottom(areaReturn.getPesoBottom() + 1);
					} else {
//						Start.imagemMapa.setRGB(ponto.getX(), ponto.getY(), new Color(0, 200, 0).getRGB());
					}
				}
			}
//			System.out.println(ponto);
			
			if (ponto.getX() == areaReturn.getxMin() && areaReturn.getPontoAtual() != Sentido.LEFT) {
				// AVALIANDO O LEFT
//				System.out.println("LEFT: " + ponto.getX() + "     " + ponto.getY() );
				if (ponto.getX() < Start.imagemMapa.getWidth() && ponto.getX()  > 10
						&& ponto.getY() < Start.imagemMapa.getHeight()&& ponto.getY() > 10) {
					Color c = new Color(Start.imagemMapa.getRGB(ponto.getX(), ponto.getY()));
					if (c.getRed() < 160 && c.getGreen() < 160 && c.getBlue() < 160) {
//						System.out.println("incremento LEFT");
						areaReturn.setPesoLeft(areaReturn.getPesoLeft() + 1);
					} else {
//						Start.imagemMapa.setRGB(ponto.getX(), ponto.getY(), new Color(0, 0, 200).getRGB());
					}
				}
			}

		}
		
		if (areaReturn.getPesoTop() > areaReturn.getPesoRight() && areaReturn.getPesoTop() > areaReturn.getPesoBottom()
				&& areaReturn.getPesoTop() > areaReturn.getPesoLeft())
			areaReturn.setPontoFuturo(Sentido.TOP);
		else if (areaReturn.getPesoRight() > areaReturn.getPesoTop()
				&& areaReturn.getPesoRight() > areaReturn.getPesoBottom()
				&& areaReturn.getPesoRight() > areaReturn.getPesoLeft())
			areaReturn.setPontoFuturo(Sentido.RIGHT);
		else if (areaReturn.getPesoBottom() > areaReturn.getPesoTop()
				&& areaReturn.getPesoBottom() > areaReturn.getPesoRight()
				&& areaReturn.getPesoBottom() > areaReturn.getPesoLeft())
			areaReturn.setPontoFuturo(Sentido.BOTTOM);
		else if (areaReturn.getPesoLeft() > areaReturn.getPesoTop()
				&& areaReturn.getPesoLeft() > areaReturn.getPesoRight()
				&& areaReturn.getPesoLeft() > areaReturn.getPesoBottom())
			areaReturn.setPontoFuturo(Sentido.LEFT);
		else if (areaReturn.getPesoLeft() == areaReturn.getPesoTop() && areaReturn.getPesoLeft() > 0) {
			
			if (areaAnterior.getSentido() == Sentido.LEFT || areaAnterior.getSentido() == Sentido.FRONT) areaReturn.setPontoFuturo(Sentido.LEFT);
			else areaReturn.setPontoFuturo(Sentido.TOP);
		} else if (areaReturn.getPesoLeft() == areaReturn.getPesoBottom() && areaReturn.getPesoLeft() > 0) {
			
			if (areaAnterior.getSentido() == Sentido.LEFT || areaAnterior.getSentido() == Sentido.FRONT) areaReturn.setPontoFuturo(Sentido.LEFT);
			else areaReturn.setPontoFuturo(Sentido.BOTTOM);
		} else if (areaReturn.getPesoRight() == areaReturn.getPesoBottom() && areaReturn.getPesoRight() > 0) {
			
			if (areaAnterior.getSentido() == Sentido.RIGHT || areaAnterior.getSentido() == Sentido.FRONT) areaReturn.setPontoFuturo(Sentido.RIGHT);
			else areaReturn.setPontoFuturo(Sentido.BOTTOM);
		} else if (areaReturn.getPesoRight() == areaReturn.getPesoTop() && areaReturn.getPesoRight() > 0) {
			
			if (areaAnterior.getSentido() == Sentido.RIGHT || areaAnterior.getSentido() == Sentido.FRONT) areaReturn.setPontoFuturo(Sentido.RIGHT);
			else areaReturn.setPontoFuturo(Sentido.TOP);
		}

		
//		System.out.println(areaReturn);
		// DETERMINAR O PONTO FINAL
		int pontoFinal = 0;
//		System.out.println("sentido futuro: "+areaReturn.getPontoFuturo());
		if (areaReturn.getPontoFuturo() == null) {
//			System.out.println(areaReturn);
//			System.out.println(areaReturn.getListPonto());
			for(Ponto ponto:areaReturn.getListPonto()) {
//				System.out.println(ponto);
			}
			
			
			return null;
		}
		
		if (areaReturn.getPontoAtual() == Sentido.LEFT && areaReturn.getPontoFuturo() == Sentido.BOTTOM ) areaReturn.setSentido(Sentido.BOTTOM);
		if (areaReturn.getPontoAtual() == Sentido.LEFT && areaReturn.getPontoFuturo() == Sentido.RIGHT ) areaReturn.setSentido(Sentido.FRONT);
		if (areaReturn.getPontoAtual() == Sentido.LEFT && areaReturn.getPontoFuturo() == Sentido.TOP ) areaReturn.setSentido(Sentido.TOP);
		
		if (areaReturn.getPontoAtual() == Sentido.RIGHT && areaReturn.getPontoFuturo() == Sentido.BOTTOM ) areaReturn.setSentido(Sentido.BOTTOM);
		if (areaReturn.getPontoAtual() == Sentido.RIGHT && areaReturn.getPontoFuturo() == Sentido.LEFT ) areaReturn.setSentido(Sentido.FRONT);
		if (areaReturn.getPontoAtual() == Sentido.RIGHT && areaReturn.getPontoFuturo() == Sentido.TOP ) areaReturn.setSentido(Sentido.TOP);

		if (areaReturn.getPontoAtual() == Sentido.TOP && areaReturn.getPontoFuturo() == Sentido.BOTTOM ) areaReturn.setSentido(Sentido.FRONT);
		if (areaReturn.getPontoAtual() == Sentido.TOP && areaReturn.getPontoFuturo() == Sentido.RIGHT ) areaReturn.setSentido(Sentido.RIGHT);
		if (areaReturn.getPontoAtual() == Sentido.TOP && areaReturn.getPontoFuturo() == Sentido.LEFT ) areaReturn.setSentido(Sentido.LEFT);

		if (areaReturn.getPontoAtual() == Sentido.BOTTOM && areaReturn.getPontoFuturo() == Sentido.TOP ) areaReturn.setSentido(Sentido.FRONT);
		if (areaReturn.getPontoAtual() == Sentido.BOTTOM && areaReturn.getPontoFuturo() == Sentido.RIGHT ) areaReturn.setSentido(Sentido.RIGHT);
		if (areaReturn.getPontoAtual() == Sentido.BOTTOM && areaReturn.getPontoFuturo() == Sentido.LEFT ) areaReturn.setSentido(Sentido.LEFT);
		
		switch (areaReturn.getPontoFuturo()) {
		case LEFT:
			for (Ponto ponto : areaReturn.getListPonto()) {
				if (ponto.getX() == areaReturn.getxMin()) {
					Color c = new Color(Start.imagemMapa.getRGB(ponto.getX(), ponto.getY()));
					if (c.getRed() < 160 && c.getGreen() < 160 && c.getBlue() < 160) {
						pontoFinal += ponto.getY();
					}
				}
			}
			pontoFinal = pontoFinal / areaReturn.getPesoLeft();
			areaReturn.setPontoFinal(new Ponto(areaReturn.getxMin(), pontoFinal));
//					System.out.println("PONTO FINAL: " + areaReturn.getPontoFinal());
			break;
		case TOP:
			for (Ponto ponto : areaReturn.getListPonto()) {
				if (ponto.getY() == areaReturn.getyMin()) {
					Color c = new Color(Start.imagemMapa.getRGB(ponto.getX(), ponto.getY()));
					if (c.getRed() < 160 && c.getGreen() < 160 && c.getBlue() < 160) {
						pontoFinal += ponto.getX();
					}
				}
			}

			pontoFinal = pontoFinal / areaReturn.getPesoTop();
			areaReturn.setPontoFinal(new Ponto(pontoFinal, areaReturn.getyMin()));
//					System.out.println("PONTO FINAL: " + areaReturn.getPontoFinal());
			break;
		case RIGHT:
			for (Ponto ponto : areaReturn.getListPonto()) {
				if (ponto.getX() == areaReturn.getxMax()) {
//					System.out.println("PONTO PERCORRIDO:" + ponto);
					Color c = new Color(Start.imagemMapa.getRGB(ponto.getX(), ponto.getY()));
					if (c.getRed() < 160 && c.getGreen() < 160 && c.getBlue() < 160) {
//						System.out.println("###### PONTO preto:" + ponto);
						pontoFinal += ponto.getY();
					}
				}
			}

			pontoFinal = pontoFinal / areaReturn.getPesoRight();
			areaReturn.setPontoFinal(new Ponto(areaReturn.getxMax(), pontoFinal));
			break;
		case BOTTOM:

			for (Ponto ponto : areaReturn.getListPonto()) {
				if (ponto.getY() == areaReturn.getyMax()) {
					Color c = new Color(Start.imagemMapa.getRGB(ponto.getX(), ponto.getY()));
					if (c.getRed() < 160 && c.getGreen() < 160 && c.getBlue() < 160) {
						pontoFinal += ponto.getX();
					}
				}
			}

			pontoFinal = pontoFinal / areaReturn.getPesoBottom();
			areaReturn.setPontoFinal(new Ponto(pontoFinal, areaReturn.getyMax()));
			break;
		default:
			break;
		}
		
		if (listPontoBloquados.contains(areaReturn.getPontoFinal())) 
			return null;
		
		for (int contY = areaReturn.getyMin(); contY <= areaReturn.getyMax(); contY++) {
			for (int contX = areaReturn.getxMin(); contX <= areaReturn.getxMax(); contX++) {
				listPontoBloquados.add(new Ponto(contX, contY));
			}
		}
		
		areaReturn.getPontoInicial().setAngulo(areaReturn.getPontoFinal());
		
		
		Start.lstPercurso.add(areaReturn.getPontoInicial());
		return areaReturn;
	}

	public StreamedContent getImage() throws Exception {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(Start.imagemMapa, "png", os);
			return new DefaultStreamedContent(new ByteArrayInputStream(os.toByteArray()), "image/png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
