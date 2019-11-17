import java.util.ArrayList;

class MergeSort {
    private static ArrayList<Point> merge(ArrayList<Point> left, ArrayList<Point> right) {
        int i = 0, j = 0;
        ArrayList<Point> arrayList = new ArrayList<>();
        while (i < left.size() && j < right.size()) {
            if (left.get(i).getX() <= right.get(j).getX()) {
                arrayList.add(left.get(i));
                i++;
            } else {
                arrayList.add(right.get(j));
                j++;
            }
        }
        if (i == left.size()) {
            while (j < right.size()) {
                arrayList.add(right.get(j));
                j++;
            }
        }
        if (j == right.size()) {
            while (i < left.size()) {
                arrayList.add(left.get(i));
                i++;
            }
        }
        return arrayList;
    }

    static ArrayList<Point> mergeSort(ArrayList<Point> array) {
        if (array.size() == 1) {
            return array;
        }
        ArrayList<Point> left = new ArrayList<>();
        ArrayList<Point> right = new ArrayList<>();
        int mid = array.size() / 2;
        for (int i = 0; i < mid; i++)
            left.add(array.get(i));
        for (int j = mid; j < array.size(); j++)
            right.add(array.get(j));
        left = mergeSort(left);
        right = mergeSort(right);
        return merge(left, right);
    }

}
