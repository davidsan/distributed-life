import java.util.Random;

public class InputGenerator {
	public static void main(String[] args) {
		Random rand = new Random();
		for (int i = 0; i < 1000; i++) {
			for (int j = 0; j < 1000; j++) {
				if (rand.nextDouble() < 0.3) {
					System.out.println(String.format("%d,%d", i, j));
				}
			}
		}
	}
}
