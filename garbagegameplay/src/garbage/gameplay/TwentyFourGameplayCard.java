package garbage.gameplay;

public enum TwentyFourGameplayCard {
	CLUBS_ACE (1, TwentyFourGameplaySuit .CLUBS, "1"),
	CLUBS_TWO (2, TwentyFourGameplaySuit .CLUBS, "2"),
	CLUBS_THREE (3, TwentyFourGameplaySuit .CLUBS, "3"),
	CLUBS_FOUR (4, TwentyFourGameplaySuit .CLUBS, "4"),
	CLUBS_FIVE (5, TwentyFourGameplaySuit .CLUBS, "5"),
	CLUBS_SIX (6, TwentyFourGameplaySuit .CLUBS, "6"),
	CLUBS_SEVEN (7, TwentyFourGameplaySuit .CLUBS, "7"),
	CLUBS_EIGHT (8, TwentyFourGameplaySuit .CLUBS, "8"),
	CLUBS_NINE (9, TwentyFourGameplaySuit .CLUBS, "9"),
	CLUBS_TEN (10, TwentyFourGameplaySuit .CLUBS, "10"),
	CLUBS_JACK (11, TwentyFourGameplaySuit .CLUBS, "J"),
	CLUBS_QUEEN (12, TwentyFourGameplaySuit .CLUBS, "Q"),
	CLUBS_KING (13, TwentyFourGameplaySuit .CLUBS, "K"),
	
	DIAMONDS_ACE (1, TwentyFourGameplaySuit .DIAMONDS, "1"),
	DIAMONDS_TWO (2, TwentyFourGameplaySuit .DIAMONDS, "2"),
	DIAMONDS_THREE (3, TwentyFourGameplaySuit .DIAMONDS, "3"),
	DIAMONDS_FOUR (4, TwentyFourGameplaySuit .DIAMONDS, "4"),
	DIAMONDS_FIVE (5, TwentyFourGameplaySuit .DIAMONDS, "5"),
	DIAMONDS_SIX (6, TwentyFourGameplaySuit .DIAMONDS, "6"),
	DIAMONDS_SEVEN (7, TwentyFourGameplaySuit .DIAMONDS, "7"),
	DIAMONDS_EIGHT (8, TwentyFourGameplaySuit .DIAMONDS, "8"),
	DIAMONDS_NINE (9, TwentyFourGameplaySuit .DIAMONDS, "9"),
	DIAMONDS_TEN (10, TwentyFourGameplaySuit .DIAMONDS, "10"),
	DIAMONDS_JACK (11, TwentyFourGameplaySuit .DIAMONDS, "J"),
	DIAMONDS_QUEEN (12, TwentyFourGameplaySuit .DIAMONDS, "Q"),
	DIAMONDS_KING (13, TwentyFourGameplaySuit .DIAMONDS, "K"),

	HEARTS_ACE (1, TwentyFourGameplaySuit .HEARTS, "1"),
	HEARTS_TWO (2, TwentyFourGameplaySuit .HEARTS, "2"),
	HEARTS_THREE (3, TwentyFourGameplaySuit .HEARTS, "3"),
	HEARTS_FOUR (4, TwentyFourGameplaySuit .HEARTS, "4"),
	HEARTS_FIVE (5, TwentyFourGameplaySuit .HEARTS, "5"),
	HEARTS_SIX (6, TwentyFourGameplaySuit .HEARTS, "6"),
	HEARTS_SEVEN (7, TwentyFourGameplaySuit .HEARTS, "7"),
	HEARTS_EIGHT (8, TwentyFourGameplaySuit .HEARTS, "8"),
	HEARTS_NINE (9, TwentyFourGameplaySuit .HEARTS, "9"),
	HEARTS_TEN (10, TwentyFourGameplaySuit .HEARTS, "10"),
	HEARTS_JACK (11, TwentyFourGameplaySuit .HEARTS, "J"),
	HEARTS_QUEEN (12, TwentyFourGameplaySuit .HEARTS, "Q"),
	HEARTS_KING (13, TwentyFourGameplaySuit .HEARTS, "K"),

	SPADES_ACE (1, TwentyFourGameplaySuit .SPADES, "1"),
	SPADES_TWO (2, TwentyFourGameplaySuit .SPADES, "2"),
	SPADES_THREE (3, TwentyFourGameplaySuit .SPADES, "3"),
	SPADES_FOUR (4, TwentyFourGameplaySuit .SPADES, "4"),
	SPADES_FIVE (5, TwentyFourGameplaySuit .SPADES, "5"),
	SPADES_SIX (6, TwentyFourGameplaySuit .SPADES, "6"),
	SPADES_SEVEN (7, TwentyFourGameplaySuit .SPADES, "7"),
	SPADES_EIGHT (8, TwentyFourGameplaySuit .SPADES, "8"),
	SPADES_NINE (9, TwentyFourGameplaySuit .SPADES, "9"),
	SPADES_TEN (10, TwentyFourGameplaySuit .SPADES, "10"),
	SPADES_JACK (11, TwentyFourGameplaySuit .SPADES, "J"),
	SPADES_QUEEN (12, TwentyFourGameplaySuit .SPADES, "Q"),
	SPADES_KING (13, TwentyFourGameplaySuit .SPADES, "K");

	
	int value;
	TwentyFourGameplaySuit suit;
	String name;
	
	TwentyFourGameplayCard (int value, TwentyFourGameplaySuit suit, String name) {
		this .value = value;
		this .suit = suit;
		this .name = name; } }
