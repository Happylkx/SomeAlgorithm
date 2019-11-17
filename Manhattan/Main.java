public class Main {
    private static Point[][] A;//距离数据

    private static Point[][] getA() {
        A[0][0] = new Point(0, 0);
        A[0][1] = new Point(0, 1);
        A[0][2] = new Point(0, 4);
        A[0][3] = new Point(0, 4);
        A[0][4] = new Point(0, 4);

        A[1][0] = new Point(3, 0);
        A[1][1] = new Point(3, 0);
        A[1][2] = new Point(0, 6);
        A[1][3] = new Point(3, 4);
        A[1][4] = new Point(1, 6);

        A[2][0] = new Point(2, 0);
        A[2][1] = new Point(2, 2);
        A[2][2] = new Point(7, 5);
        A[2][3] = new Point(3, 5);
        A[2][4] = new Point(3, 8);

        A[3][0] = new Point(4, 0);
        A[3][1] = new Point(4, 4);
        A[3][2] = new Point(3, 2);
        A[3][3] = new Point(0, 2);
        A[3][4] = new Point(2, 5);

        A[4][0] = new Point(0, 0);
        A[4][1] = new Point(2, 3);
        A[4][2] = new Point(4, 1);
        A[4][3] = new Point(2, 1);
        A[4][4] = new Point(2, 3);
        return A;
    }

    private static int getI() {
        return 5;
    }

    private static int getJ() {
        return 5;
    }

    /**
     * @param points A[i][j],到该点距离对
     * @param i      列
     * @param j      行
     * @return B 箭头数组，用于构造解
     */
    private static int[][] constructMTP(Point[][] points, int i, int j) {
        //箭头数组
        int[][] b = new int[i][j];
        //结果数组
        int[][] c = new int[i][j];
        c[0][0] = 0;
        for (int x = 1; x < i; x++) {
            c[x][0] = c[x - 1][0] + points[x][0].getX();
            b[x][0] = 0;//向左的箭头
        }
        for (int y = 1; y < j; y++) {
            c[0][y] = c[0][y - 1] + points[0][y].getY();
            b[0][y] = 1;//向上的箭头
        }
        for (int x = 1; x < i; x++) {
            for (int y = 1; y < j; y++) {
                if ((c[x][y - 1] + points[x][y].getY()) >= (c[x - 1][y] + points[x][y].getX())) {
                    c[x][y] = c[x][y - 1] + points[x][y].getY();
                    b[x][y] = 1;//向上的箭头
                } else if ((c[x][y - 1] + points[x][y].getY()) < (c[x - 1][y] + points[x][y].getX())) {
                    c[x][y] = c[x - 1][y] + points[x][y].getX();
                    b[x][y] = 0;//向左的箭头
                }
            }
        }
        return b;
    }

    /**
     * @param array B箭头数组，用于构造解
     * @param i     列数
     * @param j     行数
     * @return 0:结束
     */
    private static int printMTP(int[][] array, int i, int j) {
        if (i == 0 && j == 0) {
            System.out.println("[" + i + "," + j + "]");
            return 0;
        } else if (array[i][j] == 0) {
            printMTP(array, i - 1, j);
            System.out.println("[" + i + "," + j + "]");
        } else if (array[i][j] == 1) {
            printMTP(array, i, j - 1);
            System.out.println("[" + i + "," + j + "]");
        }
        return 0;
    }

    public static void main(String[] args) {
        A = new Point[getI()][getJ()];
        int[][] i = constructMTP(getA(), getI(), getJ());
        System.out.println(printMTP(i, getI() - 1, getJ() - 1));
    }
}
