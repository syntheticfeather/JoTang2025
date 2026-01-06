public class T26 {
    public static void main(String[] args) {
        int[] nums = {0,0,1,2,2,2,2,3,6,8};
        Solution solution = new Solution();
        int length = solution.removeDuplicates(nums);
        System.out.println(length);
        for (int i = 0; i < length; i++){
            System.out.print(nums[i] + " ");
        }
    }
}

// 利用指针记录信息

class Solution {
    public int removeDuplicates(int[] nums) {
        int n = nums.length;
        // 原地修改，用一个指针指向不重复的位置
        int idx = 0;
        for (int i = 0; i < n; i++){
            // 遍历到不同的数字，把它放到 idx + 1 位置
            if (nums[i] != nums[idx]){
                idx++;
                nums[idx] = nums[i];
            }
        }
        return idx + 1;
    }
}