
import java.util.ArrayDeque;
import java.util.Deque;

public class t {

    public static void main(String[] args) {
        int[] num = {1, 2, 3, 2};
        Solution solution = new Solution();
        boolean result = solution.find132pattern(num);
        System.out.println(result);
    }
}

class Solution {

    public boolean find132pattern(int[] nums) {
        int n = nums.length;
        int minLeft = Integer.MAX_VALUE;
        // 记录最小的
        int[] left = new int[n];
        // 记录小于的最大的
        int[] right = new int[n];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 1; i < n - 1; i++) {
            if (nums[i - 1] < minLeft) {
                minLeft = nums[i - 1];
            }
            left[i] = minLeft;
        }
        for (int i = n - 1; i > 0; i--) {
            while (!stack.isEmpty() && stack.peek() > nums[i]) {
                int idx = stack.pop();
                right[idx] = nums[i];
            }
            stack.push(i);
        }
        for (int i = 1; i < n - 1; i++) {
            if (left[i] < right[i] && right[i] < nums[i]) {
                return true;
            }
        }
        return false;

    }
}
