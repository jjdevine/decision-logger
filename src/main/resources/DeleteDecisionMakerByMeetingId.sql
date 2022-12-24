DELETE FROM Decision_DecisionMaker
WHERE DecisionId IN (
    SELECT Id
    FROM Decision
    WHERE LinkedMeeting = ?
    )