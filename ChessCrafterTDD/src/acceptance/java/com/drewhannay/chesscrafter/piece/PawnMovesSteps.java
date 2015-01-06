package com.drewhannay.chesscrafter.piece;

import com.drewhannay.chesscrafter.board.Board;
import com.drewhannay.chesscrafter.board.BoardCoordinate;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class PawnMovesSteps {

    Board mBoard;
    List<BoardCoordinate> mMoves;

    public static void setupStandardPawns(Board target, int row, int teamId) {
        for (int x = 1; x <= Board.DEFAULT_BOARD_SIZE; x++)
            target.addPiece(new Pawn(teamId), BoardCoordinate.at(x, row));
    }

    public static void setStandardPieces(Board target, int row, int teamId) {
        target.addPiece(new Rook(teamId), BoardCoordinate.at(1, row));
        target.addPiece(new Rook(teamId), BoardCoordinate.at(8, row));

        target.addPiece(new Knight(teamId), BoardCoordinate.at(2, row));
        target.addPiece(new Knight(teamId), BoardCoordinate.at(7, row));

        target.addPiece(new Bishop(teamId), BoardCoordinate.at(3, row));
        target.addPiece(new Bishop(teamId), BoardCoordinate.at(6, row));

        target.addPiece(new Queen(teamId), BoardCoordinate.at(4, row));
        target.addPiece(new King(teamId), BoardCoordinate.at(5, row));
    }

    @Given("^A normal chessboard initial setup$")
    public void A_normal_chessboard_initial_setup() throws Throwable {
        mBoard = new Board();

        setStandardPieces(mBoard, 1, Board.FIRST_TEAM_ID);
        setupStandardPawns(mBoard, 2, Board.FIRST_TEAM_ID);

        setupStandardPawns(mBoard, 7, Board.SECOND_TEAM_ID);
        setStandardPieces(mBoard, 8, Board.SECOND_TEAM_ID);
    }

    @And("^I am the first player$")
    public void I_am_the_first_player() throws Throwable {
    }

    @When("^I look for moves available for pawn$")
    public void I_look_for_moves_available_for_pawn() throws Throwable {
        mMoves = mBoard.getMovesFrom(BoardCoordinate.at(1, 2));
    }

    @Then("^The result contains a space one ahead$")
    public void The_result_contains_a_space_one_ahead() throws Throwable {
        assertTrue(mMoves.contains(BoardCoordinate.at(1, 3)));
    }
}
