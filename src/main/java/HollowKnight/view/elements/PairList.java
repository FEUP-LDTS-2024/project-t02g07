package HollowKnight.view.elements;

import java.util.ArrayList;
import java.util.List;

public class PairList<T>{
    private final List<T> firstList;
    private final List<T> secondList;

    public PairList() {
        this.firstList = new ArrayList<>();
        this.secondList = new ArrayList<>();
    }

    public List<T> getFirstList() {
        return firstList;
    }

    public List<T> getSecondList() {
        return secondList;
    }

}
