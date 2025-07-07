Feature: The user generates a hash code on the evalart page for their respective comparative response with the challenge proposed by evalart.

  How user in the portal web evelart.
  I want to generate a hash code on the evalart page
  So I can use it to validate if my hash code is correct.


  @testQAOrange
  Scenario: The user interacts with the Evelart web portal to generate the hash code correctly by selecting the correct button.
    Given enter the Evelart web portal
      |strUser       |strPassword                                                    |
      |1091129       |10df2f32286b7120My0zLTkyMTE5MDE=30e0c83e6c29f1c3               |
    When  select the correct button to obtain the hash code.
    Then  verify that the hash code is correct