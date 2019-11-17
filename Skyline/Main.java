import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    private static void skyline1(Point[] points) {
        int length = points.length;
        Point p, q;
        for (int i = 0; i < length; i++) {
            p = new Point(points[i].getX(), points[i].getY());
            for (int j = 0; j < length; j++) {
                if ((i != j) && (points[i] != null) && (points[j] != null)) {
                    q = new Point(points[j].getX(), points[j].getY());
                    if ((p.getX() < q.getX()) && (p.getY() < q.getY())) {
                        points[i] = null;
                    }
                    if ((p.getX() == q.getX()) && (p.getY() < q.getY())) {
                        points[i] = null;
                    }
                    if ((p.getX() < q.getX()) && (p.getY() == q.getY())) {
                        points[i] = null;
                    }
                }
            }
        }
        printSkyline1(points);
    }

    /**
     * 调用排序quickSortByXAxis函数，skylineDivide划分函数，printSkyline打印函数
     *
     * @param points 所有点对
     */
    private static void skyline2(ArrayList<Point> points) {
        points = MergeSort.mergeSort(points);//先按x轴进行排序
        points = skylineDivide(points);//然后再递归求解skyline
        printSkyline2(points);//打印skyline点对
    }

    /**
     * 二分所有点对，并调用skylineMerge对点对进行合并
     *
     * @param a 要划分的点对
     * @return 二分递归求解后的点对
     */
    private static ArrayList<Point> skylineDivide(ArrayList<Point> a) {
        if (a.size() > 0) {
            ArrayList<Point> left = new ArrayList<>();//储存左边待求解的数组
            ArrayList<Point> right = new ArrayList<>();//储存右边待求解的数组
            if (a.size() == 1) {
                return a;
            } else {
                int q = a.size() / 2;    //q:中点
                for (int i = 0; i < q - 1; i++) left.add(a.get(i));//将左边待求解的数组加到left数组中
                for (int i = q; i < a.size(); i++) right.add(a.get(i));//将右边待求解的数组加到right数组中
                left = skylineDivide(left);//递归划分左边的数组
                right = skylineDivide(right);//递归划分右边的数组
                a = skylineConquerMerge(left, right);//求解并且合并左边和右边的数组
            }
        }
        return a;//返回求解过后的数组
    }

    /**
     * 求解并且合并
     *
     * @param left  左边待合并的数组
     * @param right 右边待合并的数组
     * @return 返回求解并合并的数组
     */
    private static ArrayList<Point> skylineConquerMerge(ArrayList<Point> left, ArrayList<Point> right) {
        //先将右边的集合筛选一遍
        for (int i = 1; i < right.size(); i++) {
            Point p1 = right.get(i - 1);
            Point p2 = right.get(i);
            if (p1.getY() >= p2.getY())
                right.remove(p2);
            else if ((p1.getX() >= p2.getX()) && (p1.getY() >= p2.getY()))
                right.remove(p2);
        }
        Point ryMax = right.get(0);//先定义右边Y值最大的点
        for (Point point : right) {//求出右边Y值最大的点
            if (ryMax.getY() <= point.getY())
                ryMax = point;
        }
        for (int j = 0; j < left.size(); j++) {
            Point p = left.get(j);
            //将左边的点的Y值依次与右边Y最大值比较，如果比右边Y最大值小的话，则删去左边的那个点
            if ((p.getY() <= ryMax.getY())) {
                left.remove(p);
                j--;
            }
        }
        right.addAll(left);//将左边的点全部加到右边的集合中
        return right;//返回右边的点集
    }


    /**
     * 快速排序
     *
     * @param points 待排序的点对
     * @param p      排序的起始位置
     * @param r      排序的结束位置
     */
    private static void quickSortByXAxis(ArrayList<Point> points, int p, int r) {
        if (r > p) {
            int q = partition(points, p, r);
            quickSortByXAxis(points, p, q - 1);
            quickSortByXAxis(points, q + 1, r);
        }
    }

    /**
     * 划分函数
     *
     * @param points 待划分的点对
     * @param p      划分开始的位置
     * @param r      划分结束的位置
     * @return 划分的位置
     */
    private static int partition(ArrayList<Point> points, int p, int r) {
        Point x = points.get(r);
        int i = p - 1;
        for (int j = p; j <= r - 1; j++) {
            if (points.get(j).getX() <= x.getX()) {
                i++;
                //exchange
                Point temp;
                temp = points.get(i);
                points.set(i, points.get(j));
                points.set(j, temp);
            }
            //exchange
            Point temp;
            temp = points.get(i + 1);
            points.set(i + 1, points.get(r));
            points.set(r, temp);
        }
        return i + 1;
    }

    private static void printSkyline2(ArrayList<Point> points) {
        int count = 0;
        for (Point point : points) {
            if (point != null) {
                count++;
                System.out.println("NO." + count + ":" + point.toString());
            }
        }
        System.out.println("Total:" + count);
    }

    private static void printSkyline1(Point[] points) {
        int num = 0;
        for (Point point : points) {
            if (point != null) {
                num++;
                System.out.println("NO." + num + ":" + point.toString());
            }
        }
        System.out.println("Total:" + num);
    }

    public static void main(String[] args) {
        String str = "分治B数据.txt";
        ReadFile readFile = new ReadFile();
        //skyline1
        Point[] points1 = readFile.readFileByLines(str);//获取点对
        long startTime = System.currentTimeMillis();
        skyline1(points1);//执行方法
        long endTime = System.currentTimeMillis();
        float excTime = (float) (endTime - startTime);
        System.out.println("执行时间：" + excTime + "ms");
        //skyline2
        Point[] points2 = readFile.readFileByLines(str);//获取点对
        ArrayList<Point> points = new ArrayList<>(Arrays.asList(points2));
        long startTime2 = System.currentTimeMillis();
        skyline2(points);//执行方法
        long endTime2 = System.currentTimeMillis();
        float excTime2 = (float) (endTime2 - startTime2);
        System.out.println("执行时间：" + excTime2 + "ms");
    }
}
