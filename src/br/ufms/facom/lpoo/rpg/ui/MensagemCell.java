package br.ufms.facom.lpoo.rpg.ui;

import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.util.Pair;

class MensagemCell extends ListCell<Pair<String, Boolean>> {
	@Override
	public void updateItem(Pair<String, Boolean> item, boolean empty) {
		super.updateItem(item, empty);

		// Configura quebra de linha.
		setPrefWidth(250);
		setWrapText(true);

		if (!empty && item != null) {
			// Configura o conte�do e a cor do texto.
			setText(item.getKey());
			if (item.getValue().booleanValue())
				setTextFill(Color.RED);
			else
				setTextFill(Color.BLACK);
		} else {
			// C�lulas vazias.
			setText(null);
			setGraphic(null);
		}
	}
}