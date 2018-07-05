import java.util.ArrayList;
import java.util.List;

public class Matrix {

    private final int rows;
    private final int columns;
    private int padding = 0;
    private List<Integer> elements = new ArrayList<>();
    private List<List<Integer>> matrix = new ArrayList<>();

    public Matrix(List<Integer> list, int rows, int columns) {
        assert (list.size() == rows * columns) : "Incompatible sizes!";

        this.rows = rows;
        this.columns = columns;
        copyList(elements, list);

        int absMax = 0;

        for (int i = 0; i < rows; i++) {
            List<Integer> temp = new ArrayList<>();
            for (int j = 0; j < columns; j++) {
                int index = (i * columns) + j;
                Integer toAdd = elements.get(index);
                int absCurr = MyMath.getSignificants(toAdd);
                absMax = absCurr > absMax ? absCurr : absMax;
                temp.add(toAdd);
            }
            matrix.add(i, temp);
        }

        padding = absMax + 1; // extra space
    }

    public Integer getElem(int row, int column){
        return matrix.get(row).get(column);
    }

    private void copyList(List<Integer> dst, List<Integer> srce){
        for (Integer aInteger : srce) {
            dst.add(aInteger);
        }
    }

    @Override
    public String toString(){
        String ret = "   " + String.format("%" + (MyMath.getSignificants(rows) + 1) + "s", "") + "  ";
        for (int i = 1; i < columns + 1; i++) {
            ret += String.format("%" + padding + "d ", i);
        }

        ret += "\n\n";
        for (int i = 0; i < matrix.size(); i++) {
            ret += " " + String.format("%" + (MyMath.getSignificants(rows) + 1) + "s", (i+1)) + "  [ ";
            for (int j = 0; j < matrix.get(0).size(); j++) {
                ret += String.format("%" + padding + "d ", matrix.get(i).get(j));
            }
            ret += " ] "+ String.format("%" + (MyMath.getSignificants(rows) + 1) + "s", (i+1)) +"\n";
        }

        ret += "\n   " + String.format("%" + (MyMath.getSignificants(rows) + 1) + "s", "") + "  ";

        for (int i = 1; i < columns + 1; i++) {
            ret += String.format("%" + padding + "d ", i);
        }

        ret += "\n";
        return ret;
    }

    public Integer getDeterminant(){

        assert (columns == rows) :
                "Trying to get a determinant of a non-square matrix";
        assert (columns > 0) :
                "Trying to get a determinant of an emtpy matrix";

        if (rows == 1){
            return this.getOneByOneDeterminant();
        } else if (rows == 2){
            return this.getTwoByTwoDeterminant();
        } else { // n > 2
            Integer ret = 0;
            for (int i = 0; i < columns; i++) {
                ret += ((Integer) (int) Math.pow(-1, i) * matrix.get(0).get(i)) * (getSmallerMatrix(0, i).getDeterminant());
            }
            return ret;
        }

    }

    private Integer getOneByOneDeterminant(){
        assert (rows == 1 && columns == 1) : "" +
                "Failed to get a determinant for a matrix with dimensions 1x1";
        return matrix.get(0).get(0);
    }

    private Integer getTwoByTwoDeterminant(){
        assert (rows == 2 && columns == 2) : "" +
                "Failed to get a determinant for a matrix with dimensions 2x2";
        return (matrix.get(0).get(0) * matrix.get(1).get(1)) - (matrix.get(0).get(1) * matrix.get(1).get(0));
    }

    private Matrix getSmallerMatrix(int theRow, int theColumn){

        int smallerRow = rows - 1;
        int smallerColumn = smallerRow;

        List<Integer> smallerList = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (i != theRow && j != theColumn){
                    smallerList.add(matrix.get(i).get(j));
                }
            }
        }
        return new Matrix(smallerList, smallerRow, smallerColumn);
    }
}















