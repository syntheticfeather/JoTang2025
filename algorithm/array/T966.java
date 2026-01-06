
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class T966 {
    public static void main(String[] args) {
        String[] wordlist = { "KiTe", "kite", "hare", "Hare" };
        String[] queries = { "kite", "Kite", "KiTe", "Hare", "HARE", "Hear", "hear", "keti", "keet", "keto" };
        Solution solution = new Solution();
        String[] result = solution.spellchecker(wordlist, queries);
        for (String res : result) {
            System.out.print(res + " ");
        }
    }
}

// 记录一个信息，优先级
// 第一层匹配，完全匹配，直接equals
// 第二层匹配，大小写不敏感，全部转成小写匹配，但是需要记录wordlist中转换前的匹配
// 第三层匹配，将所有元音转换为*，并记录wordlist中第一个匹配
// 无法匹配。

class Solution {
    public String[] spellchecker(String[] wordlist, String[] queries) {
        int wordlistSize = wordlist.length;
        int queriesSize = queries.length;
        Set<String> exactMatchSet = new HashSet<>();
        Map<String, String> lowerCaseMap = new java.util.HashMap<>();
        Map<String, String> vowelErrorMap = new java.util.HashMap<>();
        for (int i = 0; i < wordlistSize; i++) {
            exactMatchSet.add(wordlist[i]);
            String s = wordlist[i].toLowerCase();
            lowerCaseMap.putIfAbsent(s, wordlist[i]);
            s = s.replaceAll("[aeiou]", "*");
            vowelErrorMap.putIfAbsent(s, wordlist[i]);
        }
        String[] result = new String[queriesSize];
        for (int i = 0; i < queriesSize; i++) {
            String lowerCaseQuery = queries[i].toLowerCase();
            String vowelErrorQuery = lowerCaseQuery.replaceAll("[aeiou]", "*");
            if (exactMatchSet.contains(queries[i])) {
                result[i] = queries[i];
            } else if (lowerCaseMap.containsKey(lowerCaseQuery)) {
                result[i] = lowerCaseMap.get(lowerCaseQuery);
            } else if (vowelErrorMap.containsKey(vowelErrorQuery)) {
                result[i] = vowelErrorMap.get(vowelErrorQuery);
            } else {
                result[i] = "";
            }
        }
        return result;
    }
}