import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {

    public Point[] readFileByLines(String fileName) {

        File file = new File(fileName);

        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            int line = 0;
            reader.mark((int) file.length() + 1);//在首行做个标记
            //统计行数
            while (reader.readLine() != null) {
                line++;
            }
            Point[] point = new Point[line];
            // 一次读入一行，直到读入null为文件结束
            reader.reset();        //从mark的那一行开始读
            int i = 0;
            while ((tempString = reader.readLine()) != null) {
                String[] array = tempString.split("\\s+");//按空格划分字符串
                double d1 = Double.parseDouble(array[0]);
                double d2 = Double.parseDouble(array[1]);
                //给x,y坐标赋值
                point[i] = new Point(d1, d2);
                //System.out.println("(" + point[i].getX() + "," + point[i].getY() + ")");//测试成果
                i++;
                //System.out.println(i);
            }
            reader.close();
            System.out.println("读取成功");
            return point;
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
        System.out.println("读取成功");
        return null;
    }

}
