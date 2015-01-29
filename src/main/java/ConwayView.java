import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class ConwayView {
	private static final int SIZE_X = 20;
	private static final int SIZE_Y = 80;

	public static void main(String[] args) {
		File folder = new File(".");
		File[] listOfFiles = folder.listFiles();
		HashMap<String, Boolean[][]> hash = new HashMap<String, Boolean[][]>();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				continue;
			} else if (listOfFiles[i].isDirectory()) {
				String directoryName = listOfFiles[i].getName();
				if (directoryName.matches("depth_.*")) {
					Path path = Paths.get(directoryName + "/part-r-00000");

					Boolean[][] map = new Boolean[SIZE_X][SIZE_Y];
					for (int x = 0; x < map.length; x++) {
						for (int y = 0; y < map[0].length; y++) {
							map[x][y] = false;
						}
					}
					try {
						for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
							String[] coords = line.split(",");
							Integer x = Integer.parseInt(coords[0]);
							Integer y = Integer.parseInt(coords[1]);
							if (x < map.length && y < map[0].length) {
								map[x][y] = true;
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					hash.put(directoryName, map);

				}
			}
		}
		ArrayList<String> generations = new ArrayList<String>(hash.keySet());
		Collections.sort(generations, new Comparator<String>() {
			public int compare(String o1, String o2) {
				return extractInt(o1) - extractInt(o2);
			}

			int extractInt(String s) {
				String num = s.replaceAll("\\D", "");
				// return 0 if no digits found
				return num.isEmpty() ? 0 : Integer.parseInt(num);
			}
		});

		for (String key : generations) {
			System.out.println(key);
			displayMap(hash.get(key));
			try {
				Thread.sleep(500L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static void displayMap(Boolean[][] map) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (map[i][j]) {
					System.out.print("X");
				} else {
					System.out.print(".");
				}
			}
			System.out.println("");
		}
		System.out.println();
	}

}
