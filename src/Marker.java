import java.util.*;

/**
 * Missions 3 and 4. Find a word in a long text.
 */
public class Marker {

    /**
     * Mission 3a. The prefix function of p. pi[i] = the length of the longest proper prefix of p[0..i] that is
     * also a suffix of p[0..i] ("proper" means shorter than p[0..i] itself).
     * Example: p = "ACGAC" gives [0, 0, 0, 1, 2]. The last number is the "Border length" line.
     */
    public static int[] prefixFunction(String p) {
        int[] lps = new int[p.length()];

        for (int i = 1; i < p.length(); i++) {
            int j = lps[i - 1];

            while (j > 0 && p.charAt(i) != p.charAt(j)) {
                j = lps[j - 1];
            }

            if (p.charAt(i) == p.charAt(j)) {
                j++;
            }

            lps[i] = j;
        }

        return lps;
    }

    /**
     * Mission 3b. KMP. All start positions (counting from 0, in increasing order) where p occurs in text.
     * Overlapping occurrences count: "ACG" occurs in "ACGACG" at 0 and 3, and "AA" in "AAA" at 0 and 1.
     * Use the prefix function: the text pointer never goes back. The number of positions is "Marker hits",
     * the first one is "First hit" (-1 when there is none).
     */
    public static int[] findAll(String text, String p) {
        int[] lps = prefixFunction(p);

        ArrayList<Integer> positions = new ArrayList<>();
        int j = 0;

        for (int i = 0; i < text.length(); i++) {

            while (j > 0 && text.charAt(i) != p.charAt(j)) {
                j = lps[j - 1];
            }

            if (text.charAt(i) == p.charAt(j)) {
                j++;
            }

            if (j == p.length()) {
                int position = i - p.length() + 1;
                positions.add(position);

                // allow overlapping matches
                j = lps[j - 1];
            }
        }
        int[] result = new int[positions.size()];

        for (int i = 0; i < positions.size(); i++) {
            result[i] = positions.get(i);
        }
        return result;
    }

    /**
     * Mission 4. Rabin-Karp. The number of positions i in a such that the word of k letters starting at i
     * (a[i..i+k-1]) also occurs somewhere in b. Count positions, not different words. This is the "Shared words" line.
     * The idea: give every window of k letters a number (a rolling hash, updated in O(1) when the window moves by one),
     * look the number up among the windows of b, and when it is found COMPARE THE LETTERS, because two different words
     * can have the same hash.
     * Example: a = "ACGTAC", b = "TTACGT", k = 4: the words of a are ACGT, CGTA, GTAC; only ACGT is in b, so the answer is 1.
     */
    public static int sharedWords(String a, String b, int k) {
        if (a.length() < k || b.length() < k) {
            return 0;
        }
        long base = 257;
        long mod = 1_000_000_007L;
        long power = 1;
        for (int i = 1; i < k; i++) {
            power = (power * base) % mod;
        }
        Map<Long, List<Integer>> hashesB = new HashMap<>();
        long hashB = 0;
        for (int i = 0; i < k; i++) {
            hashB = (hashB * base + b.charAt(i)) % mod;
        }
        hashesB.computeIfAbsent(hashB, x -> new ArrayList<>()).add(0);
        for (int i = 1; i <= b.length() - k; i++) {
            hashB = (hashB - b.charAt(i - 1) * power % mod + mod) % mod;
            hashB = (hashB * base + b.charAt(i + k - 1)) % mod;
            hashesB.computeIfAbsent(hashB, x -> new ArrayList<>()).add(i);
        }
        int count = 0;
        long hashA = 0;
        for (int i = 0; i < k; i++) {
            hashA = (hashA * base + a.charAt(i)) % mod;
        }
        for (int i = 0; i <= a.length() - k; i++) {

            if (i > 0) {
                hashA = (hashA - a.charAt(i - 1) * power % mod + mod) % mod;
                hashA = (hashA * base + a.charAt(i + k - 1)) % mod;
            }
            List<Integer> positions = hashesB.get(hashA);
            if (positions != null) {
                boolean found = false;
                for (int pos : positions) {
                    boolean same = true;
                    for (int j = 0; j < k; j++) {
                        if (a.charAt(i + j) != b.charAt(pos + j)) {
                            same = false;
                            break;
                        }
                    }
                    if (same) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    count++;
                }
            }
        }
        return count;
    }}