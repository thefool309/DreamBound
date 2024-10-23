package com.example.dreambound;

import java.util.ArrayList;
import java.util.List;

public class FaeDex {
    private List<Fae> faeList;

    public FaeDex() {
        this.faeList = new ArrayList<>();
    }

    public void addFae(Fae fae) {
        faeList.add(fae);
    }

    public List<Fae> getFaeList() {
        return faeList;
    }


}
