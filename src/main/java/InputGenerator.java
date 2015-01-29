import java.util.Random;

/**
 * Generator of random input files for Conway's Game of Life
 *
 */
public class InputGenerator {
	public static void main(String[] args) {
		Random rand = new Random();
		for (int i = 0; i < 30; i++) {
			for (int j = 0; j < 30; j++) {
				if (rand.nextDouble() < 0.3) {
					System.out.println(String.format("%d,%d", i, j));
				}
			}
		}
	}
}
