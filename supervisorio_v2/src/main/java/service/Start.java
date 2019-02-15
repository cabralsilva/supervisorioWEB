package service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import model.Agv;
import model.Area;
import model.Ponto;
import model.Sentido;

public class Start extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public static List<Agv> lstAGVS;
	public static List<Ponto> lstPercurso;
	public static BufferedImage imagemMapa;

	private List<Ponto> listPontoBloquados = new ArrayList<>();

	public void init() throws ServletException {
		try {
			System.out.println("INICIANDO supervisoriov20");
			resetPercurso();
			carregarMapa();
			resetAgvList();
		} catch (Exception e) {
			System.out.println("FALHA AO INICIAR");
			e.printStackTrace();
		}
		
	}
	
	private void resetPercurso() {
		lstPercurso = new ArrayList<>();
	}

	private List<Agv> resetAgvList() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("supervisorio_v2");

		EntityManager manager = factory.createEntityManager();
		lstAGVS = manager.createQuery("from Agv", Agv.class).getResultList();
		factory.close();

		for (Agv agv : lstAGVS) {
			agv.setPosicao(0);
		}

		return lstAGVS;
	}

	private void carregarMapa() throws Exception {
		try {
			BufferedImage imgAux = ImageIO.read(getClass().getResourceAsStream("/background.png"));
			imagemMapa = new BufferedImage(imgAux.getWidth(), imgAux.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);

			for (int x = 0; x < imgAux.getWidth(); x++) {
				for (int y = 0; y < imgAux.getHeight(); y++) {
					imagemMapa.setRGB(x, y, imgAux.getRGB(x, y));
				}
			}
			
			for (int y = 0; y < imagemMapa.getHeight(); y += 2) {
				for (int x = 0; x < imagemMapa.getWidth(); x += 2) {
					
					Ponto ponto = new Ponto(x, y);
					if (!listPontoBloquados.contains(ponto)) {
						Color c = new Color(imagemMapa.getRGB(x, y));
						if (c.getRed() < 160 && c.getGreen() < 160 && c.getBlue() < 160) {
								
								Area areaNova = encontrarArea(new Ponto(x, y), null);
								if (areaNova != null) {
									while ((areaNova = encontrarArea(new Ponto(areaNova.getPontoFinal().getX(),
											areaNova.getPontoFinal().getY()), areaNova)) != null) {
									}
									System.out.println("FINAL");
								}
						}else {
//							imagemMapa.setRGB(x, y, new Color(200, 200, 0).getRGB());
						}
					}
				}
			}
			
//			for (Ponto p : Start.lstPercurso) {
//				imagemMapa.setRGB(p.getX(), p.getY(), new Color(0, 0, 200).getRGB());
//			}

		} catch (IOException exc) {
			exc.printStackTrace();
		}
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
				areaReturn.getListPonto().add(new Ponto(contX, contY));
			}
		}

		for (Ponto ponto : areaReturn.getListPonto()) {

			if (ponto.getY() == areaReturn.getyMin() && areaReturn.getPontoAtual() != Sentido.TOP) {
				// AVALIANDO O TOP
				if (ponto.getX() < imagemMapa.getWidth() && ponto.getX() > 10
						&& ponto.getY() < imagemMapa.getHeight() && ponto.getY() > 10) {
					Color c = new Color(imagemMapa.getRGB(ponto.getX(), ponto.getY()));
					if (c.getRed() < 160 && c.getGreen() < 160 && c.getBlue() < 160) {
						areaReturn.setPesoTop(areaReturn.getPesoTop() + 1);
					} else {
//						imagemMapa.setRGB(ponto.getX(), ponto.getY(), new Color(200, 0, 0).getRGB());
					}
				}

			}

			if (ponto.getX() == areaReturn.getxMax() && areaReturn.getPontoAtual() != Sentido.RIGHT) {
				// AVALIANDO O RIGHT
				if (ponto.getX() < imagemMapa.getWidth() && ponto.getX() > 10
						&& ponto.getY() < imagemMapa.getHeight() && ponto.getY() > 10) {
					Color c = new Color(imagemMapa.getRGB(ponto.getX(), ponto.getY()));
					if (c.getRed() < 160 && c.getGreen() < 160 && c.getBlue() < 160) {
						areaReturn.setPesoRight(areaReturn.getPesoRight() + 1);
					} else {
//						imagemMapa.setRGB(ponto.getX(), ponto.getY(), new Color(200, 200, 0).getRGB());
					}
				}
			}

			if (ponto.getY() == areaReturn.getyMax() && areaReturn.getPontoAtual() != Sentido.BOTTOM) {
				// AVALIANDO O BOTTOM
				if (ponto.getX() < imagemMapa.getWidth() && ponto.getX() > 10
						&& ponto.getY() < imagemMapa.getHeight() && ponto.getY() > 10) {
					Color c = new Color(imagemMapa.getRGB(ponto.getX(), ponto.getY()));

					if (c.getRed() < 160 && c.getGreen() < 160 && c.getBlue() < 160) {
						areaReturn.setPesoBottom(areaReturn.getPesoBottom() + 1);
					} else {
//						imagemMapa.setRGB(ponto.getX(), ponto.getY(), new Color(0, 200, 0).getRGB());
					}
				}
			}

			if (ponto.getX() == areaReturn.getxMin() && areaReturn.getPontoAtual() != Sentido.LEFT) {
				// AVALIANDO O LEFT
				if (ponto.getX() < imagemMapa.getWidth() && ponto.getX() > 10
						&& ponto.getY() < imagemMapa.getHeight() && ponto.getY() > 10) {
					Color c = new Color(imagemMapa.getRGB(ponto.getX(), ponto.getY()));
					if (c.getRed() < 160 && c.getGreen() < 160 && c.getBlue() < 160) {
						areaReturn.setPesoLeft(areaReturn.getPesoLeft() + 1);
					} else {
//						imagemMapa.setRGB(ponto.getX(), ponto.getY(), new Color(0, 0, 200).getRGB());
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

			if (areaAnterior.getSentido() == Sentido.LEFT || areaAnterior.getSentido() == Sentido.FRONT)
				areaReturn.setPontoFuturo(Sentido.LEFT);
			else
				areaReturn.setPontoFuturo(Sentido.TOP);
		} else if (areaReturn.getPesoLeft() == areaReturn.getPesoBottom() && areaReturn.getPesoLeft() > 0) {

			if (areaAnterior.getSentido() == Sentido.LEFT || areaAnterior.getSentido() == Sentido.FRONT)
				areaReturn.setPontoFuturo(Sentido.LEFT);
			else
				areaReturn.setPontoFuturo(Sentido.BOTTOM);
		} else if (areaReturn.getPesoRight() == areaReturn.getPesoBottom() && areaReturn.getPesoRight() > 0) {

			if (areaAnterior.getSentido() == Sentido.RIGHT || areaAnterior.getSentido() == Sentido.FRONT)
				areaReturn.setPontoFuturo(Sentido.RIGHT);
			else
				areaReturn.setPontoFuturo(Sentido.BOTTOM);
		} else if (areaReturn.getPesoRight() == areaReturn.getPesoTop() && areaReturn.getPesoRight() > 0) {

			if (areaAnterior.getSentido() == Sentido.RIGHT || areaAnterior.getSentido() == Sentido.FRONT)
				areaReturn.setPontoFuturo(Sentido.RIGHT);
			else
				areaReturn.setPontoFuturo(Sentido.TOP);
		}

		// DETERMINAR O PONTO FINAL
		int pontoFinal = 0;
		if (areaReturn.getPontoFuturo() == null) {
//			for (Ponto ponto : areaReturn.getListPonto()) {
//				System.out.println(ponto);
//			}

			return null;
		}

		if (areaReturn.getPontoAtual() == Sentido.LEFT && areaReturn.getPontoFuturo() == Sentido.BOTTOM)
			areaReturn.setSentido(Sentido.BOTTOM);
		if (areaReturn.getPontoAtual() == Sentido.LEFT && areaReturn.getPontoFuturo() == Sentido.RIGHT)
			areaReturn.setSentido(Sentido.FRONT);
		if (areaReturn.getPontoAtual() == Sentido.LEFT && areaReturn.getPontoFuturo() == Sentido.TOP)
			areaReturn.setSentido(Sentido.TOP);

		if (areaReturn.getPontoAtual() == Sentido.RIGHT && areaReturn.getPontoFuturo() == Sentido.BOTTOM)
			areaReturn.setSentido(Sentido.BOTTOM);
		if (areaReturn.getPontoAtual() == Sentido.RIGHT && areaReturn.getPontoFuturo() == Sentido.LEFT)
			areaReturn.setSentido(Sentido.FRONT);
		if (areaReturn.getPontoAtual() == Sentido.RIGHT && areaReturn.getPontoFuturo() == Sentido.TOP)
			areaReturn.setSentido(Sentido.TOP);

		if (areaReturn.getPontoAtual() == Sentido.TOP && areaReturn.getPontoFuturo() == Sentido.BOTTOM)
			areaReturn.setSentido(Sentido.FRONT);
		if (areaReturn.getPontoAtual() == Sentido.TOP && areaReturn.getPontoFuturo() == Sentido.RIGHT)
			areaReturn.setSentido(Sentido.RIGHT);
		if (areaReturn.getPontoAtual() == Sentido.TOP && areaReturn.getPontoFuturo() == Sentido.LEFT)
			areaReturn.setSentido(Sentido.LEFT);

		if (areaReturn.getPontoAtual() == Sentido.BOTTOM && areaReturn.getPontoFuturo() == Sentido.TOP)
			areaReturn.setSentido(Sentido.FRONT);
		if (areaReturn.getPontoAtual() == Sentido.BOTTOM && areaReturn.getPontoFuturo() == Sentido.RIGHT)
			areaReturn.setSentido(Sentido.RIGHT);
		if (areaReturn.getPontoAtual() == Sentido.BOTTOM && areaReturn.getPontoFuturo() == Sentido.LEFT)
			areaReturn.setSentido(Sentido.LEFT);

		switch (areaReturn.getPontoFuturo()) {
		case LEFT:
			for (Ponto ponto : areaReturn.getListPonto()) {
				if (ponto.getX() == areaReturn.getxMin()) {
					Color c = new Color(imagemMapa.getRGB(ponto.getX(), ponto.getY()));
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
					Color c = new Color(imagemMapa.getRGB(ponto.getX(), ponto.getY()));
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
					Color c = new Color(imagemMapa.getRGB(ponto.getX(), ponto.getY()));
					if (c.getRed() < 160 && c.getGreen() < 160 && c.getBlue() < 160) {
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
					Color c = new Color(imagemMapa.getRGB(ponto.getX(), ponto.getY()));
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
		if (pontoInicial.getY() + maximaLarguraLinha > imagemMapa.getHeight())
			maximaLarguraLinha = 0;
		////////////////////////////////////////////

		int Yy = pontoInicial.getY();
		for (; Yy < pontoInicial.getY() + maximaLarguraLinha; Yy++) {
			Color c2 = new Color(imagemMapa.getRGB(pontoInicial.getX(), Yy));
			if (c2.getRed() < 160 && c2.getGreen() < 160 && c2.getBlue() < 160) {
//			imagemMapa.setRGB(pontoInicial.getX(), Yy, new Color(0, 0, 100).getRGB());
			} else {
				return new Ponto(pontoInicial.getX(), (pontoInicial.getY() + ((int) (Yy - pontoInicial.getY()) / 2)));
			}
		}

		return null;
	}

}
