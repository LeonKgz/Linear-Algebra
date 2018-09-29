import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Matrix {

    private final int rows;
    private final int columns;
    private int padding = 0;
    private int decimalPlaces;
    private List<Double> elements = new ArrayList<>();
    private List<List<Double>> matrix = new ArrayList<>();

    public Matrix(List<Double> list, int rows, int columns, int decimals) {
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
        this.decimalPlaces = decimals;

        padding = absMax + 1 + (1 + decimals); // extra space
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
        //padding = 0;
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
        return new Matrix(smallerList, smallerRow, smallerColumn, decimalPlaces);
    }

    public Matrix copyThis(){
        Matrix copy = new Matrix(elements, rows, columns, decimalPlaces);
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
        int counter = pivotColumn;
        Double curr = matrix.get(counter).get(pivotColumn);

        for (int i = pivotColumn; i < rows; i++) {
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

    public Matrix add(Matrix another){
        assert (this.rows == another.rows && this.columns == another.columns) :
                "Trying to add incompatible matrices (rows&columns)!";
        List<Double> newList = new ArrayList<>();
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                newList.add(this.matrix.get(i).get(j) + another.matrix.get(i).get(j));
            }
        }
        return new Matrix(newList, this.rows, this.columns, this.decimalPlaces);
    }

    public Matrix coarseMultiply(Matrix another){
        assert (another.rows == this.columns) : "Trying to multiply incompatible matrices (rows&columns)!";
        List<Double> newList = new ArrayList<>();
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < another.columns; j++) {
                Double temp = 0.0;
                for (int k = 0; k < this.columns; k++) {
                    temp += this.matrix.get(i).get(k) * another.matrix.get(k).get(j);
                }
                newList.add(temp);
            }
        }
        return new Matrix(newList, this.rows, another.columns, this.decimalPlaces);
    }

/*
    public Matrix strassenMultiply(Matrix another){
        assert (another.rows == this.columns): "Trying to multiply incompatible matrices (rows&columns)!";
        assert (this.rows == this.columns): "Not square matrix!";
        if (this.rows == 2){

        } else (){

        }
    }*/
    public static Matrix composeFromList(List<Matrix> matrices){
        List<Double> newList = new ArrayList<>();
        int numOfMatrices = matrices.size();
        double check = Math.sqrt(numOfMatrices);
        assert (check - Math.floor(check) == 0);
        if (! (check - Math.floor(check) == 0)){
            System.exit(1);                                          //??????   АЛЛО
        }
        for (int i = 0; i < numOfMatrices; i++) {
           List<Double> currElements = matrices.get(i).elements;
           for (int j = 0; j < currElements.size(); j++) {
               int smallDimensions = matrices.get(i).columns;
               newList.set(getTheComposedIndex(i, j, smallDimensions), currElements.get(j));
           }
        }

        int dimensions = ((int) Math.sqrt(matrices.size())) * matrices.get(0).rows;
        return new Matrix (newList, dimensions, dimensions, matrices.get(0).decimalPlaces);
    }

    private static int getTheComposedIndex(int i, int j, int smallerDim){
        if (i == 0){

        } else {
            int offestOne = ((int) Math.pow(smallerDim, 2)) * (i - 1) + smallerDim ;
            int offsetTwo = (smallerDim * (j / smallerDim)) + j;
        }

        return -1;
    }

}


