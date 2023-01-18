import java.io.*;
import java.util.*;


public class Main {
    static String typeOfValues = null;
    static boolean isSortInAscendingOrder = true;
    public static class MergeFiles<Type> {
        boolean isSortInAscendingOrder;
        public MergeFiles(boolean isSortInAscendingOrder){
            this.isSortInAscendingOrder = isSortInAscendingOrder;
        }
        public List<Type> MergeArray(List<Type> array){
            if (array == null) {
                return null;
            }
            if (array.size() < 2) {
                return array;
            }

            List<Type> leftArray = array.subList(0, array.size()/2);
            List<Type> rightArray = array.subList(array.size()/2, array.size());

            leftArray = MergeArray(leftArray);
            rightArray = MergeArray(rightArray);

            return SortArray(leftArray, rightArray, isSortInAscendingOrder);
        }
        private List<Type> SortArray(List<Type> arrayL, List<Type> arrayR, boolean isSortInAscendingOrder) {

            List<Type> arrayC = new ArrayList<>();
            boolean isTypeString = !(arrayL.get(0) instanceof Integer);
            int posL = 0, posR = 0;

            int size = arrayL.size() + arrayR.size();
            for (int i = 0; i < size; i++) {
                if (posL < arrayL.size() && posR < arrayR.size()) {
                    if (isSortInAscendingOrder) {
                        if (isTypeString){
                            if (arrayL.get(posL).toString().compareTo(arrayR.get(posR).toString()) < 0) {
                                arrayC.add(arrayL.get(posL));
                                posL++;
                            } else {
                                arrayC.add(arrayR.get(posR));
                                posR++;
                            }
                        }else{
                            if ((Integer)arrayL.get(posL) < (Integer)arrayR.get(posR)) {
                                arrayC.add(arrayL.get(posL));
                                posL++;
                            } else {
                                arrayC.add(arrayR.get(posR));
                                posR++;
                            }
                        }

                    }else{
                        if (isTypeString){
                            if (arrayL.get(posL).toString().compareTo(arrayR.get(posR).toString()) > 0) {
                                arrayC.add(arrayL.get(posL));
                                posL++;
                            } else {
                                arrayC.add(arrayR.get(posR));
                                posR++;
                            }
                        }else{
                            if ((Integer)arrayL.get(posL) > (Integer)arrayR.get(posR)) {
                                arrayC.add(arrayL.get(posL));
                                posL++;
                            } else {
                                arrayC.add(arrayR.get(posR));
                                posR++;
                            }
                        }

                    }
                } else if (posL == arrayL.size() && posR < arrayR.size()) {
                    arrayC.add(arrayR.get(posR));
                    posR++;
                }
                else if (posL < arrayL.size() && posR == arrayR.size()) {
                    arrayC.add(arrayL.get(posL));
                    posL++;
                }
            }

            return arrayC;
        }
    }
    public static void CreateOrReplace(String name){
        try {
            File file = new File(name);
            if (!file.createNewFile()) {
                PrintWriter writer = new PrintWriter(name);
                writer.print("");
                writer.close();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public static void CheckFileExists(ArrayList<String> nameFiles) {
        int index = 1;
        while (nameFiles.size() > index) {
            File file = new File(nameFiles.get(index));
            if (!file.exists() || (file.length() == 0)){
                nameFiles.remove(index);
            }else{
                index++;
            }

        }
    }
    public static void WriteInToFile(List<?> list, String nameFiles, boolean isSortInAscendingOrder){
        String mainFile = null;
        PrintStream fileOut;

        try {
            if (!isSortInAscendingOrder){
                mainFile = nameFiles;
                nameFiles = "tempFile.txt";
                fileOut = new PrintStream(nameFiles);
            }else{
                fileOut = new PrintStream(new FileOutputStream(nameFiles, true));
            }

            int i = 0;
            while (i < list.size()) {
                fileOut.println(list.get(i));
                i++;
            }
            fileOut.close();
            if (!isSortInAscendingOrder){
                appendFiles(mainFile, nameFiles);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void appendFiles(String perm, String tmp){
        try {
            File permFile = new File(perm);
            PrintStream tmpOut = new PrintStream(new FileOutputStream(tmp, true));
            Scanner scannerPrem = new Scanner(permFile);
            while (scannerPrem.hasNext()){
                tmpOut.println(scannerPrem.nextLine());
            }
            scannerPrem.close();
            tmpOut.close();

            if(!permFile.delete()){
                System.out.println("File don't deleted");
            }
            if (!new File(tmp).renameTo(permFile)){
                System.out.println("File don't renamed");
            }

        } catch (IOException ignored) {}
    }
    public static boolean ReadArgs(String[] args,ArrayList<String> nameFiles){
        boolean isSetTypeOfValues = false;
        boolean isSetSortType = false;
        for (String arg : args) {
            if (!isSetTypeOfValues) {
                if (arg.equals("-s")) {
                    typeOfValues = "string";
                    isSetTypeOfValues = true;
                    continue;
                }
                if (arg.equals("-i")) {
                    typeOfValues = "integer";
                    isSetTypeOfValues = true;
                    continue;
                }
            }
            if (!isSetSortType) {
                if (arg.equals("-a")) {
                    isSetSortType = true;
                    continue;
                }
                if (arg.equals("-d")) {
                    isSortInAscendingOrder = false;
                    isSetSortType = true;
                    continue;
                }
            }
            nameFiles.add(arg);
        }
        return typeOfValues != null;
    }

    public static String StringScanners(ArrayList<String> nameFiles, List<Scanner> scanners, List<String> list){
        String val;
        String maxVal = "";
        for (int i = 1; i < nameFiles.size(); i++){
            try {
                scanners.add(new Scanner(new File(nameFiles.get(i))));
                if(scanners.get(i-1).hasNext()){
                    val = scanners.get(i-1).nextLine();
                    list.add(val);
                    if (val.compareTo(maxVal) > 0){
                        maxVal = val;
                    }
                }else{
                    scanners.remove(i-1);
                }

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return maxVal;
    }
    public static void StringProcessing(ArrayList<String> nameFiles, List<Scanner> scanners){
        MergeFiles<String> mergeFiles = new MergeFiles<>(isSortInAscendingOrder);
        List<String> list = new ArrayList<>();
        List<String> buffer = new ArrayList<>();

        String maxVal;
        String minVal = "";
        String val;

        int sizeScanners;
        boolean first = true;

        maxVal = StringScanners(nameFiles, scanners, list);

        do {
            for (Scanner scanner : scanners) {
                if (scanner.hasNext())
                    if (first) {
                        first = false;
                        for (int j = 0; j < 10; j++) {
                            if (scanner.hasNext()) {
                                try{
                                    val = scanner.nextLine();
                                    if (val.contains(" ")){
                                        continue;
                                    }
                                    if (val.compareTo(minVal) < 0) {
                                        continue;
                                    }
                                    list.add(val);
                                    if (val.compareTo(maxVal) > 0) {
                                        maxVal = val;
                                    }
                                }catch (Exception ignore){}
                            }
                        }
                    } else {
                        while (scanner.hasNext()) {
                            try{
                                val = scanner.nextLine();
                                if (val.compareTo(minVal) < 0) {
                                    continue;
                                }
                                if (val.compareTo(maxVal) > 0) {
                                    buffer.add(val);
                                    break;
                                } else {
                                    list.add(val);
                                }
                            }catch (Exception ignore){}

                        }
                    }

            }
            first = true;
            minVal = maxVal;

            WriteInToFile(
                    mergeFiles.MergeArray(list),
                    nameFiles.get(0),
                    isSortInAscendingOrder);

            list.clear();
            list.addAll(buffer);
            buffer.clear();

            for (String item : list) {
                if (item.compareTo(maxVal) > 0) {
                    maxVal = item;
                }
            }
            sizeScanners = 0;
            for (Scanner scanner : scanners) {
                if (scanner.hasNext()) {
                    sizeScanners++;
                }
            }
        } while (sizeScanners != 0);

        if (list.size() != 0){
            WriteInToFile(mergeFiles.MergeArray(list), nameFiles.get(0), isSortInAscendingOrder);
            list.clear();
        }

        for (Scanner scanner : scanners) {
            scanner.close();
        }
    }
    public static int IntegerScanners(ArrayList<String> nameFiles, List<Scanner> scanners, List<Integer> list){
        int maxVal = Integer.MIN_VALUE;
        int val;

        for (int i = 1; i < nameFiles.size(); i++){
            try {
                scanners.add(new Scanner(new File(nameFiles.get(i))));
                if(scanners.get(i-1).hasNext()){
                    val = scanners.get(i-1).nextInt();
                    list.add(val);
                    if (val > maxVal){
                        maxVal = val;
                    }
                }else{
                    scanners.remove(i-1);
                }

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return maxVal;
    }
    public static void IntegerProcessing(ArrayList<String> nameFiles, List<Scanner> scanners){
        MergeFiles<Integer> mergeFiles = new MergeFiles<>(isSortInAscendingOrder);
        List<Integer> list = new ArrayList<>();
        List<Integer> buffer = new ArrayList<>();

        int maxVal;
        int minVal = Integer.MIN_VALUE;
        int val;

        int sizeScanners;
        boolean first = true;

        maxVal = IntegerScanners(nameFiles, scanners, list);

        do {
            for (Scanner scanner : scanners) {
                if (scanner.hasNext())
                    if (first) {
                        first = false;
                        for (int j = 0; j < 10; j++) {
                            if (scanner.hasNext()) {
                                try {
                                    val = Integer.parseInt(scanner.nextLine());

                                    if (val < minVal) {
                                        continue;
                                    }

                                    list.add(val);
                                    if (val > maxVal) {
                                        maxVal = val;
                                    }
                                }catch (Exception ignored){}

                            }
                        }
                    } else {
                        while (scanner.hasNext()) {
                            try {
                                val = Integer.parseInt(scanner.nextLine());
                                if (val < minVal) {
                                    continue;
                                }
                                if (val > maxVal) {
                                    buffer.add(val);
                                    break;
                                } else {
                                    list.add(val);
                                }
                            }catch (Exception ignored){}
                        }
                    }

            }
            first = true;
            minVal = maxVal;

            WriteInToFile(
                    mergeFiles.MergeArray(list),
                    nameFiles.get(0),
                    isSortInAscendingOrder);

            list.clear();
            list.addAll(buffer);
            buffer.clear();

            for (Integer item : list) {
                if (item > maxVal) {
                    maxVal = item;
                }
            }
            sizeScanners = 0;
            for (Scanner scanner : scanners) {
                if (scanner.hasNext()) {
                    sizeScanners++;
                }
            }
        } while (sizeScanners != 0);

        if (list.size() != 0){
            WriteInToFile(mergeFiles.MergeArray(list), nameFiles.get(0), isSortInAscendingOrder);
            list.clear();
        }
        for (Scanner scanner : scanners) {
            scanner.close();
        }
    }
    public static void main(String[] args){

        ArrayList<String> nameFiles = new ArrayList<>();
        List<Scanner> scanners = new ArrayList<>();
        if (!ReadArgs(args, nameFiles)){
            System.out.println("Error: you did not enter a data type");
            return;
        }
        CreateOrReplace(nameFiles.get(0));
        CheckFileExists(nameFiles);

        if (typeOfValues.equals("string")){
            StringProcessing(nameFiles, scanners);
        }else{
            IntegerProcessing(nameFiles, scanners);
        }


    }

}


