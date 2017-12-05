package br.ufms.facom.lpoo.rpg.ui;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.ufms.facom.lpoo.rpg.controle.Controle;
import br.ufms.facom.lpoo.rpg.personagem.Personagem;
import br.ufms.facom.lpoo.rpg.personagem.Posicao;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;

public class RolePlayingGame extends Application {

	private Canvas canvas;

	private ObservableList<Pair<String, Boolean>> mensagens;

	private Map<String, Image> icons;

	private List<Personagem> personagens;

	private Controle controle;

	private Thread threadControle;

	private SelecaoTabuleiro selecao;

	private static final int W = 768;

	private static final int H = 768;

	public static final int MAX_X = 8;

	public static final int MAX_Y = 8;

	private static final int CELL_W = W / MAX_X;

	private static final int CELL_H = H / MAX_Y;

	public static void main(String[] args) {
		launch(args);
	}

	public RolePlayingGame() {
		personagens = new LinkedList<>();
		icons = new HashMap<>();
		controle = new Controle(this);
		mensagens = FXCollections.observableArrayList();
		selecao = new SelecaoTabuleiro();
	}

	public void addPersonagem(Personagem p) {
		personagens.add(p);
	}

	public boolean removePersonagem(Personagem p) {
		return personagens.remove(p);
	}

	public void info(String msg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				mensagens.add(0, new Pair<>(msg, false));
			}
		});
	}

	public void erro(String msg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				mensagens.add(0, new Pair<>(msg, true));
			}
		});
	}

	public void atualizaTabuleiro() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
				desenhaCanvas();
			}
		});
	}

	public Personagem selecionaPersonagem() throws InterruptedException {
		synchronized (selecao) {
			while (selecao.estado != EstadoSelecao.DESOCUPADO)
				selecao.wait();

			selecao.personagem = null;
			selecao.estado = EstadoSelecao.PERSONAGEM;

			while (selecao.personagem == null)
				selecao.wait();

			Personagem p = selecao.personagem;
			selecao.personagem = null;
			selecao.estado = EstadoSelecao.DESOCUPADO;

			return p;
		}
	}


	public Posicao selecionaPosicao() throws InterruptedException {
		synchronized (selecao) {
			while (selecao.estado != EstadoSelecao.DESOCUPADO)
				selecao.wait();

			selecao.pos = null;
			selecao.estado = EstadoSelecao.POSICAO;

			while (selecao.pos == null)
				selecao.wait();

			Posicao pos = selecao.pos;
			selecao.pos = null;
			selecao.estado = EstadoSelecao.DESOCUPADO;

			return pos;
		}
	}

	private void desenhaCanvas() {
		// Desenha tabuleiro.
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setStroke(Color.GREY);
		for (int x = 1; x <= MAX_X; ++x)
			gc.strokeLine(x * CELL_W, 0, x * CELL_W, H);
		for (int y = 1; y <= MAX_Y; ++y)
			gc.strokeLine(0, y * CELL_H, W, y * CELL_H);

		// Desenha personagens.
		desenhaPersonagens(gc);
	}

	private void desenhaPersonagens(GraphicsContext gc) {
		for (Personagem p : personagens) {
			// Converte posiÁ„o em cÈlulas para pixels.
			double x = p.getX() * CELL_W;
			double y = p.getY() * CELL_H;
			// Desenha Ìcone do personagem.
			gc.drawImage(getIcon(p.getNome()), x, y);
			// Desenha Ìcone da arma ‡ dist‚ncia
			gc.drawImage(getIcon(p.getArma().getNome()), x + 45, y + 72);
			//Desenha Ìcone da arma corpo a corpo
			gc.drawImage(getIcon("Pen drive com vÌrus"), x + 70, y + 72);
			// Desenha nome do personagem.
			gc.setStroke(Color.BLUE);
			gc.strokeText(p.getNome(), x, y + 90);
			// Desenha barra de vida.
			gc.setFill(Color.GREY);
			gc.fillRect(x + 76, y + 4, 16, 64);
			int vida = p.getVida();
			if (vida < 0)
				vida = 0;
			else if (vida > 5)
				vida = 5;
			if (vida <= 1)
				gc.setFill(Color.RED);
			else if (vida <= 3)
				gc.setFill(Color.YELLOW);
			else
				gc.setFill(Color.GREEN);
			gc.fillRect(x + 76, y + 4 + (5 - vida) * 12.8, 16, 64 - (5 - vida) * 12.8);
		}
	}


	private Image getIcon(String nome) {
		Image icon = icons.get(nome);
		if (icon == null) {
			icon = new Image("/icons/" + nome + ".png");
			icons.put(nome, icon);
		}
		return icon;
	}


	private void onCanvasClick(int x, int y) {
		synchronized (selecao) {
			switch (selecao.estado) {
			case PERSONAGEM:
				if (selecao.personagem != null) {
					selecao.notifyAll();
					return;
				}

				// Busca personagem que est· na cÈlula selecionada.
				for (Personagem p : personagens) {
					if (p.getX() == x && p.getY() == y) {
						selecao.personagem = p;
						selecao.notifyAll();
						return;
					}
				}

				return;

			case POSICAO:
				if (selecao.pos != null) {
					selecao.notifyAll();
					return;
				}

				selecao.pos = new Posicao(x, y);
				selecao.notifyAll();
				return;

			default:

				System.err.println("Estado de seleÁ„o inv·lido!");
				selecao.notifyAll();
				return;

			}
		}
	}

	@Override
	public void start(Stage primaryStage) {
		// Cria painel grid.
		GridPane grid = new GridPane();

		// Canvas que exibe o tabuleiro.
		canvas = new Canvas(768, 768);
		desenhaCanvas();
		grid.add(canvas, 0, 0, 1, 2);
		canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				onCanvasClick((int) (event.getX() / CELL_W), (int) (event.getY() / CELL_H));
			}
		});

		// Lista de mensagens.
		ListView<Pair<String, Boolean>> viewMensagens = new ListView<>();
		grid.add(viewMensagens, 1, 0);
		viewMensagens.setPrefSize(256, 576);
		viewMensagens.setCellFactory((ListView<Pair<String, Boolean>> l) -> new MensagemCell());
		viewMensagens.setItems(mensagens);

		// Painel de bot√µes.
		FlowPane paneBotoes = new FlowPane();
		paneBotoes.setAlignment(Pos.CENTER);
		grid.add(paneBotoes, 1, 1);
		Button btnSair = new Button("Sair");
		btnSair.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Platform.exit();
			}
		});
		paneBotoes.getChildren().add(btnSair);

		// Configura Stage e o exibe.
		primaryStage.setTitle("Guerra Civil, o jogo de TI");
		Scene scene = new Scene(grid, 1024, 768);
		primaryStage.setScene(scene);
		primaryStage.show();

		threadControle = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						controle.executaTurno();
						synchronized (this) {
							Thread.sleep(100);
						}
					}
				} catch (InterruptedException e) {
				} finally {
					System.out.println("Thread de controle interrompiada com sucesso!");
				}
			}
		});

		// Inicia thread de controle.
		threadControle.start();
	}

	@Override
	public void stop() throws Exception {
		super.stop();

		// Interrompe thread de controle.
		threadControle.interrupt();
	}

}
