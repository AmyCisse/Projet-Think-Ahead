package modele;

import java.util.ArrayList;
import java.util.List;

public class AlignementCases {
    private final List<Case> cases;
    private final Orientation orientation;
    private final int index; // numéro de ligne/colonne

    public AlignementCases(List<Case> cases, Orientation orientation, int index) {
        this.cases = new ArrayList<>(cases);
        this.orientation = orientation;
        this.index = index;
    }

    public List<Case> getCases() { return cases; }
    public Orientation getOrientation() { return orientation; }
    public int getIndex() { return index; }

    public List<Integer> positionsPossibles() {
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < cases.size(); i++)
            if (!cases.get(i).isPrise()) res.add(i);
        return res;
    }
}
