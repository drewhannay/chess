Feature: PawnMoves

  Scenario: Move one space ahead
    Given A normal chessboard initial setup
    And I am the first player
    When I look for moves available for pawn
    Then The result contains a space one ahead
