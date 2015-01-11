package com.drewhannay.chesscrafter.rules.legaldestinationcropper;

public final class MustCaptureLegalDestinationCropper extends LegalDestinationCropper {
    @Override
    public void cropLegalDestinations() {
        // TODO Auto-generated method stub
        // private void mustCapture(List<PieceController> team)
        // {
        // boolean foundCapture = false;
        // for (int i = 0; i < team.size(); i++)
        // {
        // PieceController current = team.get(i);
        // for (SquareController s : current.getLegalDests())
        // {
        // if (s.isOccupied())
        // {
        // foundCapture = true;
        // break;
        // }
        // }
        //
        // if (foundCapture)
        // break;
        // }
        // if (foundCapture)
        // {
        // for (int i = 0; i < team.size(); i++)
        // {
        // PieceController current = team.get(i);
        // List<SquareController> adjusted = Lists.newArrayList();
        // for (SquareController s : current.getLegalDests())
        // {
        // if (s.isOccupied())
        // adjusted.add(s);
        // }
        // current.setLegalDests(adjusted);
        // }
        // }
        // }
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof MustCaptureLegalDestinationCropper;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
