package swen221.assignment4.cards.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import swen221.assignment4.cards.core.Card;
import swen221.assignment4.cards.core.Player;
import swen221.assignment4.cards.core.Trick;

/**
 * Implements a simple computer player who plays the highest card available when
 * the trick can still be won, otherwise discards the lowest card available. In
 * the special case that the player must win the trick (i.e. this is the last
 * card in the trick), then the player conservatively plays the least card
 * needed to win.
 * 
 * @author David J. Pearce
 * 
 */
public class SimpleComputerPlayer extends AbstractComputerPlayer {

	public SimpleComputerPlayer(Player player) {
		super(player);
	}

	@Override
	public Card getNextCard(Trick trick) {
		Card.Suit trumps = trick.getTrumps();
		Card largestNext = null;
		Card smallestNext = null;
		Card WinningCard = null;
		Set<Card> leadingCardsSet = new HashSet<>();
		if (trick.getLeadSuit() != null) {
			leadingCardsSet = player.getHand().matches(trick.getLeadSuit());
		}
		Iterator<Card> playerCards = player.getHand().iterator();

		if (trick.getCardsPlayed() != null) {
			for (Card card : trick.getCardsPlayed()) {
				if (card.suit() == trumps || card.suit() == trick.getLeadSuit()) {
					if (WinningCard == null) {
						WinningCard = card;
						// compare with trumps to check winning chance
					} else if (card.compareWithTrumps(WinningCard, trumps) > 0) {
						WinningCard = card;
					}
				}
			}
		}
		if (leadingCardsSet != null && leadingCardsSet.size() > 0) {
			if (trick.getCardsPlayed().size() == 3) {
				largestNext = biggestWinCard(leadingCardsSet, WinningCard, trumps);
			} else {
				largestNext = playerBiggestCard(leadingCardsSet, trumps);
			}
			smallestNext = playerSmallestCard(leadingCardsSet, trumps);
		} else if (player.getHand().matches(trumps).size() > 0) {
			// play highest trump if no lead card
			if (trick.getCardsPlayed().size() == 3) {
				largestNext = biggestWinCard(player.getHand().matches(trumps), WinningCard, trumps);
			} else {
				largestNext = playerBiggestCard(player.getHand().matches(trumps), trumps);
			}
			smallestNext = playerSmallestCard(player.getHand().matches(trumps), trumps);
			// use the largest and smallest card
		} else {
			while (playerCards.hasNext()) {
				Card nextCard = playerCards.next();
				if (largestNext == null) {
					largestNext = nextCard;
				} else if (nextCard.compareTo(largestNext) > 0) {
					largestNext = nextCard;
				}
				if (smallestNext == null) {
					smallestNext = nextCard;
				} else if (nextCard.compareTo(smallestNext) < 0) {
					smallestNext = nextCard;
				}
			}
		}
		if (WinningCard == null || largestNext.compareWithTrumps(WinningCard, trumps) > 0) {
			if (largestNext.suit() == trumps || largestNext.suit() == trick.getLeadSuit()
					|| trick.getLeadSuit() == null) {
				return largestNext;
			}
		}
		return smallestNext;
	}

	/**
	 * 
	 * @param cards
	 * @param trumps
	 * @return lowest card of player
	 */
	private Card playerSmallestCard(Set<Card> cards, Card.Suit trumps) {
		Card lowest = null;
		for (Card card : cards) {
			if (lowest == null) {
				lowest = card;
			} else if (card.compareWithTrumps(lowest, trumps) < 0) {
				lowest = card;
			}
		}
		return lowest;
	}

	/**
	 * 
	 * @param cards
	 * @param trumps
	 * @return highest card of player
	 */
	private Card playerBiggestCard(Set<Card> cards, Card.Suit trumps) {
		Card highest = null;
		for (Card card : cards) {
			if (highest == null) {
				highest = card;
			} else if (card.compareWithTrumps(highest, trumps) > 0) {
				highest = card;
			}
		}
		return highest;
	}

	/**
	 * 
	 * @param cards
	 * @param winning
	 * @param trumps
	 * @return highest winning card
	 */
	private Card biggestWinCard(Set<Card> cards, Card winning, Card.Suit trumps) {
		Card highest = null;
		for (Card card : cards) {
			if (highest == null) {
				highest = card;
			} else if (card.compareWithTrumps(highest, trumps) > 0) {
				if (winning == null || highest.compareWithTrumps(winning, trumps) < 0) {
					highest = card;
				}
			} else {
				if (winning != null && card.compareWithTrumps(winning, trumps) > 0) {
					highest = card;
				}
			}
		}
		return highest;
	}
}
