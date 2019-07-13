package com.db.app.service.dataService;


import com.db.app.model.MyArrayList;

import java.util.ArrayList;

public class AccelProcess {
    private ArrayList<Integer> accelArray;

    public AccelProcess(ArrayList<Integer> accelArray) {
        this.accelArray = accelArray;
    }

    public void accelProcess() {
        accelArray.clear();
    }


    public static void main(String args[]) {
        ArrayList test = new ArrayList<>();
        test.add(11);
        test.add(12);
        test.add(13);
        test.add(14);
        test.add(15);
        test.add(16);
        System.out.println(test);

        test.subList(2, 4).clear();

        System.out.println(test);

    }

}


