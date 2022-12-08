package DailyCode.Day7;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Day7Part2 {
    public static Integer findFileSizeToDelete() throws FileNotFoundException {
        File inputFile = new File("src\\DailyCode\\Day7\\Day7Input.txt");

        // Hashmap made up of: Filepath as String, FolderContents custom class (see bottom of .java file)
        HashMap<String, FolderContents> allFolders = findAllFolders(inputFile);
        allFolders = calculateDataForAllFolders(allFolders);

        // Finds the smallest folder that allows us to download the update
        // 40000000 is the maximum allowed space (70000000 - 30000000), so any space over that must be freed, making our deletion floor.
        int dataFloor = allFolders.get("\\/").getDataSize() - 40000000;
        int minimumSufficientDataDeletion = 0;
        for (FolderContents folder : allFolders.values()) {
            if (folder.getDataSize() >= dataFloor && (folder.getDataSize() < minimumSufficientDataDeletion || minimumSufficientDataDeletion == 0)) {
                minimumSufficientDataDeletion = folder.getDataSize();
            }
        }

        return minimumSufficientDataDeletion;
    }

    // Fill return array with every Folder filepath and their children from input, and assign each a data size equal only to direct children files, not folders.
    private static HashMap<String, FolderContents> findAllFolders (File inputFile) throws FileNotFoundException {
        Scanner in = new Scanner(inputFile);
        HashMap<String, FolderContents> allFolders = new HashMap<String, FolderContents>();
        String workingDirectory = "";

        boolean ignoreLine = true;
        String line = in.nextLine();
        while (in.hasNextLine()) {
            if (!ignoreLine)
                line = in.nextLine();
            else ignoreLine = false;

            // If command is "$ cd \something\" or "$ cd .." change the working directory accordingly.
            if (line.matches("^\\$\\scd\\s.+")) {
                if (line.charAt(5) == '.') { 
                    int folderNameSize = workingDirectory.length() - workingDirectory.lastIndexOf("\\");
                    workingDirectory = workingDirectory.substring(0, workingDirectory.length() - folderNameSize);
                    continue;
                } else {
                    workingDirectory = workingDirectory + "\\" + line.substring(5, line.length());
                }
                allFolders.put(workingDirectory, new FolderContents());
            }
            
            // If command is "$ ls", use folder contents to calculate data size of direct file children and add direct folder children to ArrayList using the current workingDirectory
            if (line.matches("^\\$\\sls$")) {
                boolean processingFolder = true;
                boolean noFolderChildren = true;
                while (processingFolder) {
                    if (!in.hasNextLine()) break;
                    line = in.nextLine();
                    // $ means a new command, and loop should break, while d means "dir", or another folder, and anything else (starts with digit) means a direct file child to add data total for.
                    switch(line.charAt(0)) {
                        case '$':
                            // Saves command for next outer while loop iteration
                            ignoreLine = true;
                            // Exits inner while loop
                            processingFolder = false;
                            continue;
                        case 'd':
                            // Adds the children folder filepath to the child of the current file
                            allFolders.get(workingDirectory).childrenFolders.add(workingDirectory + "\\" + line.substring(4));
                            noFolderChildren = false;
                            // Continues inner while loop
                            continue;
                    }
                    // Only happens if neither of the above two cases happen
                    String[] fileSizeAndName = line.split(" ");
                    allFolders.get(workingDirectory).addToDataSize(Integer.parseInt(fileSizeAndName[0]));
                }
                if (noFolderChildren) allFolders.get(workingDirectory).markCalculated();
            }
        }

        in.close();
        return allFolders;
    }

    // Indirectly recursive, calls calculateFolder()
    // Using a filled folder array, it finishes by using subfolder sizes to calculate data size on top of direct children (already filled in)
    private static HashMap<String, FolderContents> calculateDataForAllFolders (HashMap<String, FolderContents> allFolders) {
        String folder = "\\/"; // Use root level folder
        allFolders.get(folder).setDataSize(calculateFolder(folder, allFolders));
        allFolders.get(folder).markCalculated();
        return allFolders;
    }

    // Recursive, calls self
    private static Integer calculateFolder(String folder, HashMap<String, FolderContents> allFolders) {
        int dataSize = allFolders.get(folder).getDataSize();

        if (allFolders.get(folder).isCalculated()) return dataSize;

        for (String newFolder : allFolders.get(folder).childrenFolders) {
            dataSize += calculateFolder(newFolder, allFolders);
        }
        allFolders.get(folder).setDataSize(dataSize);
        allFolders.get(folder).markCalculated();

        return dataSize;
    }
}

class FolderContents {
    private int dataSize;
    public ArrayList<String> childrenFolders;
    private boolean isCalculated;
    
    public FolderContents() {
        dataSize = 0;
        childrenFolders = new ArrayList<String>();
        isCalculated = false;
    }

    public void markCalculated() {
        isCalculated = true;
    }

    public boolean isCalculated() {
        return isCalculated;
    }

    public void addToDataSize(int numberToAdd) {
        dataSize += numberToAdd;
    }
    
    public void setDataSize(int value) {
        dataSize = value;
    }
    
    public int getDataSize() {
        return dataSize;
    }
}