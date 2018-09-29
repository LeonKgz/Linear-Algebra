import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final String theLine = "\n--------------------------------------------------------------------\n\n";

    public static void main(String[] args) {


        /*for (int i = 0; i < 144; i++) {
            System.out.print((int) (Math.random() * 10000));
            System.out.print(" ");
        }
        System.out.println("\n");
        */

        Scanner sc = new Scanner(System.in);
        Matrix matrix = getMatrix();
        do {

            System.out.println("Pick from the menu: \n");
            System.out.println("1) Element\n" +
                               "2) Determinant\n" +
                               "3) Display\n" +
                               "4) New matrix\n" +
                               "5) Gaussian Elimination\n" +
                               "6) Add \n" +
                               "7) Multiply\n" +
                               "8) Compose\n" +
                               "9) Exit\n" );
            int option = sc.nextInt();
            switch (option){
                case 1:
                    getAnElement(sc, matrix);
                    break;
                case 2:
                    getADeterminant(matrix);
                    break;
                case 3:
                    System.out.println(matrix);;
                    break;
                case 4:
                    matrix = getMatrix();
                    break;
                case 5:
                    getAGaussianElimination(matrix);
                    break;
                case 6:
                    Matrix toAdd = getMatrix();
                    Matrix sum = addToAMatrix(matrix, toAdd);
                    System.out.println("\t Pick from the sub menu:\n" +
                                       "\t  1) Keep working with the original matrix\n" +
                                       "\t  2) Switch to the newly entered matrix\n" +
                                       "\t  3) Switch to the sum\n");
                    int caseSixChoice = sc.nextInt();
                    switch(caseSixChoice){
                        case 1:
                            break;
                        case 2:
                            matrix = toAdd;
                            break;
                        case 3:
                            matrix = sum;
                            break;
                    }
                    break;
                 case 7:
                    Matrix toMultiply = getMatrix();
                    Matrix product = coarseMultiplyTwoMatrices(matrix, toMultiply);
                    System.out.println("\t Pick from the sub menu:\n" +
                                       "\t  1) Keep working with the original matrix\n" +
                                       "\t  2) Switch to the newly entered matrix\n" +
                                       "\t  3) Switch to the product\n");
                    int caseSevenChoice = sc.nextInt();
                    switch(caseSevenChoice){
                        case 1:
                            break;
                        case 2:
                            matrix = toMultiply;
                            break;
                        case 3:
                            matrix = product;
                            break;
                    }
                    break;
                case 8:
                    Matrix m1 = getMatrix();
                    Matrix m2 = getMatrix();
                    Matrix m3 = getMatrix();
                    Matrix m4 = getMatrix();
                    List<Matrix> list = new ArrayList<>();
                    list.add(m1);
                    list.add(m2);
                    list.add(m3);
                    list.add(m4);
                    Matrix compose = Matrix.composeFromList(list);
                    System.out.println("Your composed matrix is:\n" + compose);
                    break;
                case 9:
                    System.exit(0);
                    break;
            }
        } while (true);

    }

    private static Matrix addToAMatrix(Matrix matrix, Matrix another){
        Matrix sum = another.add(matrix);
        System.out.println("Your sum is:\n\n" + sum);
        return sum;
    }

    private static Matrix coarseMultiplyTwoMatrices(Matrix matrix, Matrix another){
        Matrix product = matrix.coarseMultiply(another);
        System.out.println("Your product is:\n\n" + product);
        return product;
    }

    private static Matrix getMatrix(){
        Scanner scDecimal = new Scanner(System.in);
        System.out.println("What is your preferred precision (number of decimal places) ?");
        int decimalPlaces = scDecimal.nextInt();

        System.out.print("\nEnter the number of rows and columns, followed by all the elements in order: \n");
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        Scanner lineSc = new Scanner(line);

        int rows = lineSc.nextInt();
        int cols = lineSc.nextInt();
        List<Double> list = new ArrayList<>();
        while (lineSc.hasNextInt()){
            list.add(lineSc.nextDouble());
        }

        Matrix retMatrix =  new Matrix(list, rows, cols, decimalPlaces);
       // retMatrix.setDecimalPLaces(decimalPlaces);
        System.out.println("\nYour matrix: \n\n" + retMatrix);

        return retMatrix;
    }

    private static void getAnElement(Scanner sc, Matrix matrix){
        System.out.print("\nEnter the row and the column: \n");
        int row = sc.nextInt();
        int column = sc.nextInt();
        System.out.println("The element at row " + row + " and column " + column + " is " + matrix.getElem(row - 1, column - 1));
    }

    private static void getADeterminant(Matrix matrix){
        System.out.println("The determinant of your matrix is: " + matrix.getDeterminant());
    }

    private static void getAGaussianElimination(Matrix matrix){
        System.out.println("Performing Gaussian Elimination: \n\n\n" + matrix.gaussElimination());
    }

    private static void leChoix(Scanner sc, Matrix matrix){
        System.out.print("Do you want to continue with the same matrix?: \n");
        if (!sc.next().equals("y")){
            matrix =  getMatrix();
        }
    }

}
















