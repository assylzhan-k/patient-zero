# Assignment 4. Patient Zero

Student ID: 253941  
Name: Assylzhan Kishibayeva
Group: SE-2512

## Program output

The 9 lines your program prints on your data (`java -cp out Main`):

    Mutations: 72
    Shared core: 895
    Marker hits: 6
    First hit: 1061
    Border length: 3
    Shared words: 383
    Tests needed: 28
    Cheapest testing: 123
    Longest chain: 24

My outbreak report (a screenshot of `output/outbreak.html`):

![outbreak](output/outbreak.png)

## Questions

### Mission 1. What does the cell d[i][j] of your table mean, and how is it computed from other cells?

`d[i][j]` is the minimum number of edit operations needed to change the first `i` characters of strain A into the first `j` characters of strain B. If the current characters are equal, I take the diagonal value; otherwise I take the minimum of insertion, deletion and replacement and add 1.

### Mission 2. How is the longest common subsequence different from the longest common substring? My Shared core is bigger than the number of letters that are the same at the same place: why?

A substring must contain consecutive characters, while a subsequence only has to keep the same order and may skip characters. Therefore, the LCS can match letters that are located at different positions in the two strains, so Shared core can be larger than the number of letters that are equal at exactly the same index.

### Mission 3. What does the prefix function tell you, and why does the pointer in the text never go back in KMP? What is the Border length of my marker and what does it mean?

The prefix function tells me the length of the longest proper prefix that is also a suffix for every prefix of the pattern. KMP uses this information after a mismatch, so it changes the position in the pattern instead of moving the text pointer backwards. My Border length is `3`, which means the longest proper prefix of my marker that is also its suffix has length 3.

### Mission 4. Why must you compare the letters after the hashes are equal? What happens to the cost of Rabin-Karp if many windows have the same hash?

Two different strings can have the same hash because of a hash collision, so I compare the actual characters after the hashes match. If many windows have the same hash, the algorithm performs many extra character comparisons and can become much slower.

### Mission 5. What are the two states of a person in your DP, and how do you get the answer for a person from its children? Why does a greedy "test the person with most links first" not always give the cheapest answer?

For every person I keep two states: the minimum cost when the person is tested and the minimum cost when the person is not tested. If the person is tested, each child can be tested or not tested, so I take the cheaper state; if the person is not tested, every child must be tested. A greedy choice can fail because a locally good choice can force expensive choices later in other parts of the tree.

## AI use

I used ChatGPT to help me understand the algorithms, debug errors, and review my Java code. I asked for explanations of edit distance, LCS, KMP, Rabin-Karp and dynamic programming on trees, and I used it to help identify and correct implementation mistakes.

## Timing (Mission 7)

Milliseconds, average of 3 runs, from `java -cp out Bench`.

| test | size | time |
|---|---|---|
| naive search | 20 000 / 40 000 / 80 000 | 21.1 ms / 54.6 ms / 114.5 ms |
| KMP | 20 000 / 40 000 / 80 000 | 1.4 ms / 1.0 ms / 0.3 ms |
| edit distance | 500 / 1000 / 2000 / 4000 | 4.0 ms / 3.7 ms / 7.8 ms / 35.0 ms |

The naive search grows significantly when the text size doubles: approximately 2.6x and then 2.1x. KMP is much faster, and its measured times are very small, so JVM warm-up and measurement noise affect the results; theoretically KMP grows linearly with the input size. Edit distance has quadratic complexity, so doubling both string lengths should approach about 4x more work, although the smaller measurements are also affected by timing noise.

## Complexity

n is the length of the text (or of a strain), m the length of the pattern, k the length of the window, v the number of people.

| algorithm | time | space | assumptions |
|---|---|---|---|
| Edit distance | O(n × m) | O(n × m) | Uses a full 2D DP table. |
| LCS | O(n × m) | O(n × m) | Uses a full 2D DP table. |
| Prefix function and KMP | O(n + m) | O(m) excluding output positions | Prefix function takes O(m), then KMP scans the text once. |
| Rabin-Karp (shared words) | Expected O(n + m), with extra work for hash matches | O(m) | Rolling hash update is O(1). Equal hashes are checked by comparing up to k characters. In this assignment k = 12. |
| Tree DP | O(v) | O(v) | The input is a tree, so every person and edge is processed a constant number of times. |

