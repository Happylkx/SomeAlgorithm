import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.io.*;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.Arrays;

import static java.lang.Math.abs;

public class EarthMoverDistance {
    private static float[] x;
    private static float[] y;
    private static float[][] p;

    private static void earthMoverDistance() {
        int len = x.length;
        p = new float[len][len];

        float difference0;
        float difference1;
        for (int i = 0; i < len; i++) {
            int j = i + 1;
            //如果x[i]==y[i]就继续循环
            if (x[i] < y[i]) {
                difference0 = y[i] - x[i];//y[i]比x[i]多处的部分

                while ((j < len) && !(abs(x[i] - y[i]) < 1E-6)) {
                    if (x[j] > y[j]) {
                        difference1 = x[j] - y[j];
                        //后面的earth之差大于等于这儿所缺的earth之差
                        if (difference1 >= difference0) {
                            x[i] = x[i] + difference0;
                            x[j] = x[j] - difference0;
                            p[j][i] = p[j][i] + difference0;
                        } else if (difference1 < difference0) {
                            x[i] = x[i] + difference1;
                            x[j] = x[j] - difference1;
                            p[j][i] = p[j][i] + difference1;
                            difference0 = difference0 - difference1;
                            j++;
                        }
                    } else if (x[j] <= y[j]) {
                        j++;
                    }
                }
            } else if (x[i] > y[i]) {
                difference0 = x[i] - y[i];
                while ((j < len) && !(abs(x[i] - y[i]) < 1E-6)) {
                    if (x[j] < y[j]) {
                        difference1 = y[j] - x[j];
                        if (difference1 < difference0) {
                            x[i] = x[i] - difference1;
                            x[j] = x[j] + difference1;
                            p[i][j] = p[i][j] + difference1;
                            difference0 = difference0 - difference1;
                            j++;
                        } else if (difference1 >= difference0) {
                            x[i] = x[i] - difference0;
                            x[j] = x[j] + difference0;
                            p[i][j] = p[i][j] + difference0;
                        }
                    } else if (x[j] >= y[j]) {
                        j++;
                    }

                }
            }
        }
    }

    private static void readFile(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String[] arrayX = reader.readLine().split("\\s+");//按空格划分字符串
            String[] arrayY = reader.readLine().split("\\s+");//按空格划分字符串
            int len = arrayX.length;
            x = new float[len];
            y = new float[len];
            for (int i = 0; i < len; i++) {
                x[i] = Float.parseFloat(arrayX[i]);
            }
            for (int j = 0; j < len; j++) {
                y[j] = Float.parseFloat(arrayY[j]);
            }
            reader.close();
            System.out.println("读取成功");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
        }
    }

    public static void printArray(float[] array) {
        int len = array.length;
        for (float v : array) {
            System.out.println(v);
        }
        System.out.println("\n\t\t" + "length=" + len + "\n");
    }

    private static void printArray(float[][] array, int xLength, int yLength) {
        //使用NumberFormat来保留小数。
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(5);
        // 四舍五入
        nf.setRoundingMode(RoundingMode.UP);
        for (int i = 0; i < xLength; i++) {
            for (int j = 0; j < yLength; j++) {
                System.out.print(nf.format(array[i][j]) + "\t");
            }
            System.out.println();
        }
    }

    private static void saveToTxtFile(float[][] array) {

        File file = new File("EMD_result.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(Arrays.deepToString(array).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveToXlsFile(float[][] array, int xLength, int yLength) {
        try {
            // 打开文件
            WritableWorkbook writableWorkbook = Workbook.createWorkbook(new File("EMD_result.xls"));
            // 生成名为“sheet1”的工作表，参数0表示这是第一页
            WritableSheet writableSheet = writableWorkbook.createSheet("EMD_result", 0);

            //使用NumberFormat来保留小数。
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMaximumFractionDigits(5);
            // 四舍五入
            nf.setRoundingMode(RoundingMode.UP);
            for (int i = 0; i < xLength; i++) {
                for (int j = 0; j < yLength; j++) {
                    // 在Label对象的构造子中指名单元格位置是第i+1行,第j+1列,单元格内容为string
                    Label label = new Label(j, i, nf.format(array[i][j]));
                    writableSheet.addCell(label);
                }
            }
            // 写入数据并关闭文件
            writableWorkbook.write();
            writableWorkbook.close();
        } catch (WriteException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void printEMD(float[][] array) {
        int len = x.length;
        float sum = 0;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                if (array[i][j] != 0) {
                    sum += array[i][j] * abs(i - j);
                }
            }
        }
        System.out.println(sum);
    }

    public static void main(String[] args) {
        String fileName = "EMD.txt";
        readFile(fileName);
        earthMoverDistance();
        printArray(p, x.length, y.length);
        saveToTxtFile(p);
        saveToXlsFile(p, x.length, y.length);
        printEMD(p);
    }
}
