import java.util.ArrayList;
import java.util.List;

public class Matrix {

    private final int rows;
    private final int columns;
    private int padding = 0;
    private int decimalPlaces = 5;
    private List<Double> elements = new ArrayList<>();
    private List<List<Double>> matrix = new ArrayList<>();

    public Matrix(List<Double> list, int rows, int columns) {
        assert (list.size() == rows * columns) : "Incompatible sizes!";

        this.rows = rows;
        this.columns = columns;
        copyList(elements, list);

        int absMax = 0;

        for (int i = 0; i < rows; i++) {
            List<Double> temp = new ArrayList<>();
            for (int j = 0; j < columns; j++) {
                int index = (i * columns) + j;
                Double toAdd = elements.get(index);
                int absCurr = MyMath.getSignificants((int) (double) toAdd);
                absMax = absCurr > absMax ? absCurr : absMax;
                temp.add(toAdd);
            }
            matrix.add(i, temp);
        }

        padding = absMax + 1 + (1 + decimalPlaces); // extra space
    }

    public Double getElem(int row, int column){
        return matrix.get(row).get(column);
    }

    private void copyList(List<Double> dst, List<Double> srce){
        for (Double aDouble : srce) {
            dst.add(aDouble);
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
                ret += String.format("%" + padding + "." + decimalPlaces + "f ", matrix.get(i).get(j));
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

    public Double getDeterminant(){

        assert (columns == rows) :
                "Trying to get a determinant of a non-square matrix";
        assert (columns > 0) :
                "Trying to get a determinant of an emtpy matrix";

        if (rows == 1){
            return this.getOneByOneDeterminant();
        } else if (rows == 2){
            return this.getTwoByTwoDeterminant();
        } else { // n > 2
            Double ret = 0.0;
            for (int i = 0; i < columns; i++) {
                ret += (Math.pow(-1, i) * matrix.get(0).get(i)) * (getSmallerMatrix(0, i).getDeterminant());
            }
            return ret;
        }

    }

    private Double getOneByOneDeterminant(){
        assert (rows == 1 && columns == 1) : "" +
                "Failed to get a determinant for a matrix with dimensions 1x1";
        return matrix.get(0).get(0);
    }

    private Double getTwoByTwoDeterminant(){
        assert (rows == 2 && columns == 2) : "" +
                "Failed to get a determinant for a matrix with dimensions 2x2";
        return (matrix.get(0).get(0) * matrix.get(1).get(1)) - (matrix.get(0).get(1) * matrix.get(1).get(0));
    }

    private Matrix getSmallerMatrix(int theRow, int theColumn){

        int smallerRow = rows - 1;
        int smallerColumn = smallerRow;

        List<Double> smallerList = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (i != theRow && j != theColumn){
                    smallerList.add(matrix.get(i).get(j));
                }
            }
        }
        return new Matrix(smallerList, smallerRow, smallerColumn);
    }

    public Matrix copyThis(){
        Matrix copy = new Matrix(elements, rows, columns);
        return copy;
    }

    public Matrix gaussElimination(){
        Matrix ret = this.copyThis();
        for (int pivot = 0; pivot < rows; pivot++) {
            int nextLargest = ret.findNextRowWithLargestAbsoluteValueInCol(pivot);
            ret.swapRows(pivot, nextLargest);

            if (MyMath.almostZero(ret.matrix.get(pivot).get(pivot))){
                return null;
            }

            ret.reduceRow(pivot);
        }

        return ret;
    }

    private void reduceRow(int pivot){
        for (int i = pivot + 1; i < rows; i++) {
            double coefficient = matrix.get(i).get(pivot)/matrix.get(pivot).get(pivot);
            for (int j = pivot; j < columns; j++) {
                Double val = matrix.get(i).get(j);
                val -= coefficient * matrix.get(pivot).get(j);

                if (MyMath.almostZero(val)){
                    val = 0.0;
                }

                List<Double> temp = matrix.get(i);
                temp.set(j, val);
                matrix.set(i, temp);
            }
        }
    }

    private int findNextRowWithLargestAbsoluteValueInCol(int pivotColumn){
        int counter = 0;
        Double curr = matrix.get(counter).get(pivotColumn);

        for (int i = 1; i < rows; i++) {
            if (matrix.get(i).get(pivotColumn) > curr){
                counter = i;
            }
        }

        return counter;
    }

    private void swapRows(int n1, int n2){
        List<Double> copyOfn1 = matrix.get(n1);
        List<Double> copyOfn2 = matrix.get(n2);
        matrix.set(n1, copyOfn2);
        matrix.set(n2, copyOfn1);
    }

}















