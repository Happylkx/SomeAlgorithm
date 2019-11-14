import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

// 分支限界法实现01背包问题
public class Knapsack01 {

    private static int[] weight;//存储物品质量的数组
    private static int[] value;//存储物品价值的数组
    private static int maxC; // 背包的最大承重量
    private static int num;//物品的数量,25
    private static int[] result;//储存物品选择结果的数组，1或者0

    /**
     * constructor,构造函数,从文件中读取数据,
     * 为weight，value，result分配了num+1的内存空间，
     * weight，value最后一个元素为0，result第一个元素为0，这些都是不可用的元素
     */
    private Knapsack01() {
        String filename = "01Knapsack.txt";
        File file = new File(filename);
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String[] array = bufferedReader.readLine().split("\\|");
            maxC = Integer.parseInt(array[0]);//第一个是C
            int len = array.length;
            num = len - 1;//物品的数量应该等于读到的元素减一
            // 为weight，value，result分配了num+1的内存空间，
            // weight，value最后一个元素为0，result第一个元素为0，这些都是不可用的元素
            weight = new int[len];
            value = new int[len];
            result = new int[len];
            for (int i = 1; i < len; i++) {
                String[] temp = array[i].split(",");
                weight[i - 1] = Integer.parseInt(temp[0]);
                value[i - 1] = Integer.parseInt(temp[1]);
            }
            //最后一个元素为0，用来控制边界
            weight[len - 1] = 0;
            value[len - 1] = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Knapsack01 knapsack = new Knapsack01();
        sortByValueWeight();
        long startTime = System.currentTimeMillis();
        knapsack.runSearchTree();
        long endTime = System.currentTimeMillis();
        float excTime = endTime - startTime;
        System.out.println("执行时间：" + String.format("%.2f", excTime) + "ms");

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        long startTime2 = System.currentTimeMillis();
        constructKnapsack(dpKnapsack(), num, maxC);
        long endTime2 = System.currentTimeMillis();
        float excTime2 = endTime2 - startTime2;
        System.out.println("执行时间：" + String.format("%.2f", excTime2) + "ms");
    }

    /**
     * 按价值重量比进行快排
     */
    private static void sortByValueWeight() {
        int length = weight.length - 1;
        float[] wv = new float[length];
        for (int i = 0; i < length; i++) {
            float x = Float.parseFloat(String.valueOf(value[i]));
            float y = Float.parseFloat(String.valueOf(weight[i]));
            wv[i] = x / y;
        }
        quickSortByVW(wv, 0, length - 1);
    }

    /**
     * 快速排序
     *
     * @param valueWeight 待排序的数组，在排序时顺带排序value和weight数组
     * @param p           排序的起始位置
     * @param r           排序的结束位置
     */
    private static void quickSortByVW(float[] valueWeight, int p, int r) {
        if (r > p) {
            int q = partition(valueWeight, p, r);
            quickSortByVW(valueWeight, p, q - 1);
            quickSortByVW(valueWeight, q + 1, r);
        }
    }

    /**
     * 划分函数
     *
     * @param valueWeight 待划分的点对
     * @param p           划分开始的位置
     * @param r           划分结束的位置
     * @return 划分的位置
     */
    private static int partition(float[] valueWeight, int p, int r) {
        float x = valueWeight[r];
        int i = p - 1;
        for (int j = p; j <= r - 1; j++) {
            if (valueWeight[j] >= x) {
                i++;
                //exchange
                float[] temp = exchange(valueWeight[i], valueWeight[j]);
                valueWeight[i] = temp[0];
                valueWeight[j] = temp[1];
                int[] ints = exchange(weight[i], weight[j]);
                weight[i] = ints[0];
                weight[j] = ints[1];
                ints = exchange(value[i], value[j]);
                value[i] = ints[0];
                value[j] = ints[1];
            }
        }
        //exchange
        float[] temp = exchange(valueWeight[i + 1], valueWeight[r]);
        valueWeight[i + 1] = temp[0];
        valueWeight[r] = temp[1];
        int[] ints = exchange(weight[i + 1], weight[r]);
        weight[i + 1] = ints[0];
        weight[r] = ints[1];
        ints = exchange(value[i + 1], value[r]);
        value[i + 1] = ints[0];
        value[r] = ints[1];

        return i + 1;
    }

    /**
     * 直接交换的话再partition函数中无法真实交换，必须返回一下才可以。
     *
     * @param a 待交换的第一个元素
     * @param b 待交换的第二个元素
     * @return 交换结束的数组
     */
    private static float[] exchange(float a, float b) {
        float[] i = new float[2];
        float c = a;
        a = b;
        b = c;
        i[0] = a;
        i[1] = b;
        return i;
    }

    private static int[] exchange(int a, int b) {
        int[] i = new int[2];
        int c = a;
        a = b;
        b = c;
        i[0] = a;
        i[1] = b;
        return i;
    }

    //计算当前优化解下界
    private static int getBestValue() {
        int valueSum = 0;
        for (int j = 1; j <= num; j++)
            valueSum += (result[j] * value[j - 1]);
        return valueSum;
    }

    //打印出结果
    private static void printResult() {
        int valueSum = 0;
        int weightSum = 0;
        for (int j = 1; j <= num; j++) {
            System.out.println("NO." + j + "knapsack:" + result[j]);
            valueSum += (result[j] * value[j - 1]);
            weightSum += (result[j] * weight[j - 1]);
        }
        for (int j = 1; j <= num; j++)
            System.out.print(result[j] + "\t\t");
        System.out.println();
        for (int j = 0; j < num; j++)
            System.out.print(weight[j] + "\t\t");
        System.out.println();
        for (int j = 0; j < num; j++)
            System.out.print(value[j] + "\t\t");
        System.out.println();
        System.out.println("Total value:" + valueSum);
        System.out.println("Total weight:" + weightSum);
    }

    /**
     * 动态规划求解0/1背包问题。
     *
     * @return 构造好的解
     */
    private static int[][] dpKnapsack() {
        int n = num;
        //m记录优化解
        int[][] m = new int[n][maxC + 1];
        //边界条件
        for (int j = 0; j <= min(weight[n - 1] - 1, maxC); j++)
            m[n - 1][j] = 0;
        for (int j = weight[n - 1]; j <= maxC; j++)
            m[n - 1][j] = value[n - 1];
        //求解
        for (int i = n - 2; i >= 1; i--) {
            for (int j = 0; j <= min(weight[i] - 1, maxC); j++)
                m[i][j] = m[i + 1][j];
            for (int j = weight[i]; j <= maxC; j++)
                m[i][j] = max(m[i + 1][j], m[i + 1][j - weight[i]] + value[i]);
        }
        if (maxC < weight[0])
            m[0][maxC] = m[1][maxC];
        else {
            m[0][maxC] = max(m[1][maxC], m[1][maxC - weight[0]] + value[0]);
        }
        return m;
    }

    /**
     * 动态规划求解并打印0/1背包问题
     *
     * @param m 存储构造解的信息的数组
     * @param n 物品的数量
     * @param c 背包所能装的物品的总价值
     */
    private static void constructKnapsack(int[][] m, int n, int c) {
        int[] result = new int[n];
        //边界条件，第一个结果
        if (m[0][c] == m[1][c])
            result[0] = 0;
        else result[0] = 1;
        for (int a = 1; a < n - 1; a++) {
            if (result[a - 1] == 0) {
                if (m[a][c] == m[a + 1][c])
                    result[a] = 0;
                else result[a] = 1;
            } else if (result[a - 1] == 1) {
                c = c - weight[a - 1];
                if (m[a][c] == m[a + 1][c])
                    result[a] = 0;
                else result[a] = 1;
            }
        }
        //边界条件，最后一个结果
        if (c >= weight[n - 1])
            result[n - 1] = 1;
        else result[n - 1] = 0;
        System.out.println("动态规划求解0/1背包问题的结果：");
        printResult();
    }

    /**
     * 求出a,b的最小值
     *
     * @param a 参数一
     * @param b 参数二
     * @return 最小值
     */
    private static int min(int a, int b) {
        int min = a;
        if (b < min)
            min = b;
        return min;
    }

    /**
     * 求出传入参数的最大值
     *
     * @param a 参数一
     * @param b 参数二
     * @return 最大值
     */
    private static int max(int a, int b) {
        int max = a;
        if (b > max)
            max = b;
        return max;
    }

    /**
     * 搜索二叉树，并进行剪枝
     *
     * @param r        待搜索的结点
     * @param i        搜索的层数
     * @param maxValue bestValue，目前优化解下界
     */
    private void searchTree(Node r, int i, float maxValue) {
        //控制层数
        if (i + 1 < num) {
            //分配内存
            r.left = new Node();
            r.right = new Node();
            //建立结点，并计算结点信息
            r.left.weight = r.weight + weight[i + 1];
            r.left.value = r.value + value[i + 1];
            r.left.upBound = r.left.value + (maxC - r.left.weight) * value[i + 2] / weight[i + 2];
            r.left.parent = r;
            r.right.weight = r.weight;
            r.right.value = r.value;
            r.right.upBound = r.right.value + (maxC - r.weight) * value[i + 2] / weight[i + 2];
            r.right.parent = r;
            if (r.left.weight > maxC) {
                result[i + 1] = 0;
                if (i == num - 2)
                    maxValue = getBestValue();
                searchTree(r.right, i + 1, maxValue);
            }
            //如果UB小于当前优化解的下界，则搜索另一子树
            else if (r.upBound < maxValue) {
                result[i] = 0;
                searchTree(r.parent.right, i, maxValue);
            }
            //否则，优先选择UB大的结点，继续爬山搜索。
            else {
                if (r.left.upBound >= r.right.upBound) {
                    result[i + 1] = 1;
                    if (i == num - 2)
                        maxValue = getBestValue();
                    searchTree(r.left, i + 1, maxValue);
                } else {
                    result[i + 1] = 0;
                    if (i == num - 2)
                        maxValue = getBestValue();
                    searchTree(r.right, i + 1, maxValue);
                }
            }
        }
    }

    //初始化根节点，并且运行搜索二叉树的函数
    private void runSearchTree() {
        Node root = new Node();
        //初始化根节点
        root.weight = weight[0];
        root.value = value[0];
        root.upBound = root.value + (maxC - root.weight) * value[1] / weight[1];
        //搜索的层数
        int i = 0;
        // 最优的背包价值
        float bestValue = 0f;
        searchTree(root, i, bestValue);
        System.out.println("树搜索算法求解0/1背包问题的结果：");
        printResult();
    }

}
