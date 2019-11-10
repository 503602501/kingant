import java.util.HashMap;


public class TestHashMap {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HashMap<Integer,Integer> map = new HashMap<>(2);
		map.put(2, 2);
		System.out.println(map.size());
		map.put(7, 7);
		System.out.println(map.size());
		map.put(9, 9);
		map.put(13, 13);
		map.put(11, 11);
		System.out.println(map.size());
		
		
	}

}
