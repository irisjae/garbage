package garbage.gameplay;

import java.util.HashMap;
import java.util.Map;

public class TwentyFourGameplayPlayer {
	static Map <String, TwentyFourGameplayPlayer> _cache = new HashMap ();
	
	String id;

    @Override
    public boolean equals (Object obj) {
    	return (obj != null)
    		&& (TwentyFourGameplayPlayer .class .isAssignableFrom (obj .getClass ()))
    		&& (((TwentyFourGameplayPlayer) obj) .id == this .id); }
    
    public static TwentyFourGameplayPlayer of (String id) {
    	if (! TwentyFourGameplayPlayer ._cache .containsKey (id)) {
	    	TwentyFourGameplayPlayer player = new TwentyFourGameplayPlayer ();
	    	player .id = id;
	    	TwentyFourGameplayPlayer ._cache .put (id, player); }
    	return TwentyFourGameplayPlayer ._cache .get (id); } }