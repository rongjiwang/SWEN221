package swen221.assignment4.cards.variations;
import java.util.List;
import swen221.assignment4.cards.core.Card;
import swen221.assignment4.cards.core.CardGame;
import swen221.assignment4.cards.core.Player;
import swen221.assignment4.cards.util.AbstractCardGame;

public class KnockOutWhist extends AbstractCardGame {
	private int hand = 13;

	public KnockOutWhist() {

	}

	public String getName() {
		return "Knock-Out Whist";
	}

	public boolean isGameFinished() {
		return hand == 0;
	}

	public void deal(List<Card> deck) {
		currentTrick = null;
		for (Player.Direction d : Player.Direction.values()) {
			players.get(d).getHand().clear();
		}
		Player.Direction d = Player.Direction.NORTH;
		for (int i = 0; i < hand * 4; ++i) {
			Card card = deck.get(i);
			players.get(d).getHand().add(card);
			d = d.next();
		}
	}

	public void endHand() {
		super.endHand();
		hand = hand - 1;
	}

	@Override
	public CardGame clone() {
		KnockOutWhist clone = new KnockOutWhist();
		clone.players.putAll(this.players);
		clone.scores.putAll(this.scores);
		clone.tricks.putAll(this.tricks);
		clone.trumps = this.trumps;
		if (currentTrick != null) {
			clone.currentTrick = currentTrick.clone();
		}
		return clone;
	}
}
