package garbage;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Utils {
	public static String labelText (String text) {
		return "<html><pre>" + text + "</pre></html>"; }
	public static <T> List <T> cons (T head, Collection <T> tail) {
		return Stream .concat (List .of (head) .stream (), tail .stream ()) .collect (Collectors .toList ()); }
	public static <T> List <T> concat (Collection <T> a, Collection <T> b) {
		return Stream .concat (a .stream (), b .stream ()) .collect (Collectors .toList ()); }
	public static <T> List <T> remove (T needle, List <T> haystack) {
		return haystack .stream () .filter (item -> item != needle) .collect (Collectors .toList ()); }
	public static <T, U> List <U> map (Function <T, U> fn, T ... list) {
		return Utils .map (fn, Arrays .asList (list)); }
	public static <T, U> List <U> map (Function <T, U> fn, List <T> list) {
		return list .stream () .map (fn) .collect (Collectors .toList ()); }
	public static <T> List <T> filter (Predicate <T> fn, List <T> list) {
		return list .stream () .filter (fn) .collect (Collectors .toList ()); }
	public static <T, U> Map <U, T> mapCons (U key, T value, Map <U, T> tail) {
		return Stream .concat (List .of (Map .entry (key, value)) .stream (), tail .entrySet () .stream ())
			.collect (Collectors .toMap (Entry ::getKey, Entry ::getValue)); }
	public static <T, U, V> Map <V, U> mapMap (Function <T, U> fn, Map <V, T> map) {
		return map .entrySet ()
			.stream ()
			.map (entry -> Map .entry (entry .getKey (), fn .apply (entry .getValue ())))
			.collect (Collectors .toMap (Entry ::getKey, Entry ::getValue)); }
	public static <T, U> Map <U, T> mapFilter (Predicate <T> cond, Map <U, T> map) {
		return map .entrySet ()
			.stream ()
			.filter (entry -> cond .test (entry .getValue ()))
			.collect (Collectors .toMap (Entry ::getKey, Entry ::getValue)); }
	public static <T> List <T> unpick (List <Integer> indices, List <T> haystack) {
		return IntStream. range (0, haystack .size ()) .boxed ()
			.filter (i -> ! indices .contains (i))
			.map (i -> haystack .get (i))
			.collect (Collectors .toList ()); }
	public static Stream <List <Integer>> indexPairs (int n) {
		if (n == 0) {
			return Stream .empty (); }
		else {
			return Stream .concat
					( IntStream .range (0, n - 1) .boxed ()
						.flatMap (i -> 
							List .of
								( List .of (Integer .valueOf (n - 1), i)
								, List .of (i, Integer .valueOf (n - 1))) .stream () )
					, indexPairs (n - 1) ); } }
	}
