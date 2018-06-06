package wiseasily.pair;


import java.io.Serializable;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 13/02/18.
 */

public class PairStateMachine<Pair, ArrayList> implements Serializable {

    private final Pair pairState;

    private final ArrayList pairStateArrayList;

    public PairStateMachine(Pair pairState, ArrayList pairStateArrayList) {
        this.pairState = pairState;
        this.pairStateArrayList = pairStateArrayList;
    }

    public Pair getPairState() {
        return pairState;
    }

    public ArrayList getPairStateArrayList() {
        return pairStateArrayList;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof wiseasily.pair.Pair)) return false;
        wiseasily.pair.Pair pairo = (wiseasily.pair.Pair) o;
        return this.pairState.equals(pairo.getSupplicantState()) &&
                this.pairStateArrayList.equals(pairo.getSsid());
    }



    @Override
    public String toString() {
        return "PairStateMachine{" +
                "pairState=" + pairState +
                ", pairStateArrayList=" + pairStateArrayList +
                '}';
    }
}
