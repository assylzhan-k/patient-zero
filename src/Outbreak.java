import java.util.*;

public class Outbreak {

    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        long[][] dp = new long[n][2];
        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        dfsCost(0, -1, nb, cost, dp, parent);

        boolean[] tested = new boolean[n];

        boolean rootTested = dp[0][1] <= dp[0][0];
        rebuild(0, -1, rootTested, nb, dp, tested);

        return tested;
    }

    private static void dfsCost(
            int v,
            int p,
            int[][] nb,
            int[] cost,
            long[][] dp,
            int[] parent
    ) {
        parent[v] = p;

        // dp[v][1] = v is tested
        dp[v][1] = cost[v];

        // dp[v][0] = v is not tested
        dp[v][0] = 0;

        for (int child : nb[v]) {
            if (child == p) {
                continue;
            }

            dfsCost(child, v, nb, cost, dp, parent);

            // if v is tested, child may be tested or not
            dp[v][1] += Math.min(
                    dp[child][0],
                    dp[child][1]
            );

            // if v is not tested, child MUST be tested
            dp[v][0] += dp[child][1];
        }
    }

    private static void rebuild(
            int v,
            int p,
            boolean isTested,
            int[][] nb,
            long[][] dp,
            boolean[] tested
    ) {
        tested[v] = isTested;

        for (int child : nb[v]) {
            if (child == p) {
                continue;
            }

            if (!isTested) {
                // parent is not tested -> child must be tested
                rebuild(child, v, true, nb, dp, tested);
            } else {
                // parent is tested -> choose cheaper state for child
                boolean childTested =
                        dp[child][1] <= dp[child][0];

                rebuild(child, v, childTested, nb, dp, tested);
            }
        }
    }

    public static int longestChain(int n, int[][] nb) {
        int[] answer = new int[1];

        dfsLongest(0, -1, nb, answer);

        return answer[0];
    }

    private static int dfsLongest(
            int v,
            int p,
            int[][] nb,
            int[] answer
    ) {
        int best1 = 0;
        int best2 = 0;

        for (int child : nb[v]) {
            if (child == p) {
                continue;
            }

            int down = dfsLongest(child, v, nb, answer) + 1;

            if (down > best1) {
                best2 = best1;
                best1 = down;
            } else if (down > best2) {
                best2 = down;
            }
        }

        answer[0] = Math.max(
                answer[0],
                best1 + best2
        );

        return best1;
    }
}