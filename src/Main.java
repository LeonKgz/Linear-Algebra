import com.sun.org.apache.xerces.internal.impl.xs.SchemaNamespaceSupport;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final String theLine = "\n--------------------------------------------------------------------\n\n";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Matrix matrix = getMatrix();

        do {
            System.out.println("Pick from the menu: \n");
            System.out.println("1) Get an element\n" +
                               "2) Calculate the determinant\n" +
                               "3) Display the matrix\n" +
                               "4) Switch to a new matrix\n" +
                               "5) Exit\n" );
            int option = sc.nextInt();

            switch (option){
                case 1:
                    getAnElement(sc, matrix);
                    break;
                case 2:
                    getADeterminant(matrix);
                    break;
                case 3:
                    System.out.println(theLine);
                    System.out.println(matrix);;
                    System.out.println(theLine);
                    break;
                case 4:
                    System.out.println("\nSwitching to another matrix...\n");
                    matrix = getMatrix();
                    break;
                case 5:
                    System.exit(0);
                    break;
            }
            try {
                System.out.println("\n\t\t\t\t\t\tLoading...\n");
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while (true);

    }

    private static Matrix getMatrix(){

        try {
                System.out.println("\n\t\t\tLoading...\n");
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        Scanner sc = new Scanner(System.in);
        System.out.print("\nEnter the number of rows and columns, followed by all the elements in order: \n");
        String line = sc.nextLine();
        Scanner lineSc = new Scanner(line);

        int rows = lineSc.nextInt();
        int cols = lineSc.nextInt();
        List<Integer> list = new ArrayList<>();
        while (lineSc.hasNextInt()){
            list.add(lineSc.nextInt());
        }

        Matrix retMatrix =  new Matrix(list, rows, cols);
        System.out.print(theLine);
        System.out.println("\nYour matrix: \n\n" + retMatrix);
        System.out.print(theLine);

        return retMatrix;
    }

    private static void getAnElement(Scanner sc, Matrix matrix){
        System.out.print("\nEnter the row and the column: \n");
        int row = sc.nextInt();
        int column = sc.nextInt();
        System.out.print(theLine);
        System.out.println("The element at row " + row + " and column " + column + " is " + matrix.getElem(row - 1, column - 1));
        System.out.print(theLine);
    }

    private static void getADeterminant(Matrix matrix){

        System.out.print(theLine);
        System.out.println("The determinant of your matrix is: " + matrix.getDeterminant());
        System.out.print(theLine);

    }

    private static void leChoix(Scanner sc, Matrix matrix){
        System.out.print("Do you want to continue with the same matrix?: \n");
        if (!sc.next().equals("y")){
            matrix =  getMatrix();
        }
    }

}
















